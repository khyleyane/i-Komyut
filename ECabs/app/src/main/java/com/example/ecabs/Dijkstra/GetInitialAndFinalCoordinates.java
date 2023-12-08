package com.example.ecabs.Dijkstra;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.widget.Toast;

import com.example.ecabs.Utils.SQLHelper;

public class GetInitialAndFinalCoordinates extends Activity {

    // DB
    Cursor cursor;
    // An integer variable to store a fixed start node.
    int fixed_start_node = 0;
    // A string variable to store latitude information
    String exploded_lat_only = "";
    //An object of the Location class to store user position information.
    Location userPosition = new Location("");
    //An ArrayList to store temporary graph data.
    ArrayList<String> tmp_graph = new ArrayList<String>();

    // Return JSON
    JSONObject result_json = new JSONObject();

    // GET NODES FROM START AND DESTINATION FIELDS IN THE GRAPH TABLE, THEN COMBINE THEM; example 1,0 AND PUT INTO AN ARRAY
    //A list to store duplicate rows of nodes.
    List<String> duplicateRows = new ArrayList<String>();
    //A list to store worked row indexes.
    List<String> workedRowIndex = new ArrayList<String>();

    /*
     * @function
     * Select the nodes to be worked on
     * if there are nodes 1-0 and 0-1 then only 1-0 is worked on (because the coordinates of 1-0 are the same as 0-1 (coordinates are just reversed))
     * @parameters
     *   latx : user or SMK latitude
     *   lngx : user or destination longitude
     *   context : MainActivity context
     * @return
     *   JSON (index coordinates, nodes0, nodes1)
     */
    public JSONObject GetNodes(double latx, double lngx, Context context) throws JSONException {
        // TODO Auto-generated constructor stub

        // your coordinate
        userPosition.setLatitude(latx);
        userPosition.setLongitude(lngx);

        // HOLD NODES FROM START AND DESTINATION FIELDS IN THE GRAPH TABLE, THEN COMBINE THEM; example 1,0 AND PUT INTO AN ARRAY
        List<String> duplicateRows = new ArrayList<String>();
        List<String> workedRowIndex = new ArrayList<String>();

        SQLHelper dbHelper = new SQLHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // filter nodes to be worked on
        //cursor = db.rawQuery("SELECT * FROM graph where starting != '' and ending != '' and route != '' and weight != '' ", null);
        cursor = db.rawQuery("SELECT * FROM graph where starting != '' and ending != '' and route != '' and weight_distance != '' and weight_fare != ''", null);
        cursor.moveToFirst();

        // GET NODES FROM starting AND ending FIELDS IN THE GRAPH TABLE, THEN COMBINE THEM; example 1,0 AND PUT INTO AN ARRAY
        // looping below to CHECK DUPLICATE ROWS {doubled nodes} 1,0 -> 0,1 {1,0 counted but 0,1 not counted}
        for (int i = 0; i < cursor.getCount(); i++) {

            cursor.moveToPosition(i);

            // node from start_node field
            String startNodeField = cursor.getString(1).toString();

            // node from destination_node field
            String destinationNodeField = cursor.getString(2).toString();

            String combinedNodes = startNodeField + "," + destinationNodeField;
            String reverseCombinedNodes = destinationNodeField + "," + startNodeField;

            // select duplicated routes; example: 1,0 == 0,1
            // choose one, for example: 1,0
            if (duplicateRows.isEmpty()) {

                duplicateRows.add(reverseCombinedNodes);

                // field id in the graph table
                workedRowIndex.add(cursor.getString(0).toString());
            } else {

                if (!duplicateRows.contains(combinedNodes)) {
                    duplicateRows.add(reverseCombinedNodes);

                    // field id in the graph table
                    workedRowIndex.add(cursor.getString(0).toString());
                }
            }
        }

        // list of nodes being worked on
        StringBuilder workedRowIndex1 = new StringBuilder();
        for (int j = 0; j < workedRowIndex.size(); j++) {

            if (workedRowIndex1.length() == 0) {

                // field id again in the graph table (specifically for stringbuilder)
                workedRowIndex1.append(workedRowIndex.get(j));
            } else {
                workedRowIndex1.append("," + workedRowIndex.get(j)); // for where in ('0,1')
            }
        }

        // Query for non-duplicate rows
        cursor = db.rawQuery("SELECT * FROM graph where id in(" + workedRowIndex1 + ")", null);
        cursor.moveToFirst();

        JSONObject obj = new JSONObject();

        // @@@@@@=========== Find DISTANCE
        // looping all records
        for (int k = 0; k < cursor.getCount(); k++) {

            // VARIABLE TO FIND 1 DISTANCE IN 1 RECORD (1 record contains many coordinates)
            // store distance from user position to coordinates around the NODE (in meters)
            List<Double> distanceFromUserToNodeCoordinates = new ArrayList<Double>();

            cursor.moveToPosition(k);

            // get Lat, Lng coordinates from the coordinate field (3)
            String json = cursor.getString(3).toString();

            // manipulating JSON
            JSONObject jObject = new JSONObject(json);
            JSONArray jArrCoordinates = jObject.getJSONArray("coordinates");
            JSONArray jArrNodes = jObject.getJSONArray("nodes");

            // get coordinate
            for (int w = 0; w < jArrCoordinates.length(); w++) {
                JSONArray latlngs = jArrCoordinates.getJSONArray(w);
                Double lats = latlngs.getDouble(0);
                Double lngs = latlngs.getDouble(1);

                // SET LAT,LNG
                Location nodeCoordinates = new Location("");
                nodeCoordinates.setLatitude(lats);
                nodeCoordinates.setLongitude(lngs);

                // FIND DISTANCE FROM USER POSITION TO coordinates around the NODE (in meters)
                double distance = userPosition.distanceTo(nodeCoordinates);

                distanceFromUserToNodeCoordinates.add(distance);
            }

            // FIND the smallest weight
            int index_nodeCoordinates = 0;
            for (int m = 0; m < distanceFromUserToNodeCoordinates.size(); m++) {

                if (distanceFromUserToNodeCoordinates.get(m) <= distanceFromUserToNodeCoordinates.get(0)) {
                    distanceFromUserToNodeCoordinates.set(0, distanceFromUserToNodeCoordinates.get(m));

                    // index array of the smallest value
                    index_nodeCoordinates = m;
                }
            }

            // field id in the graph table
            int row_id = cursor.getInt(0);

            JSONObject list = new JSONObject();

            // put index of coordinate array, smallest weight and number of coordinates into JSON
            list.put("row_id", row_id);
            list.put("index", index_nodeCoordinates);
            list.put("weight", distanceFromUserToNodeCoordinates.get(0));
            list.put("nodes", jArrNodes.getString(0));
            list.put("coordinate_count", (jArrCoordinates.length() - 1));

            JSONArray ja = new JSONArray();
            ja.put(list);

            // Create json
            // example output:
            // {"0" : [{"row_id":17, "index":"7", "weight":"427.66", "coordinate_count":"15", "nodes":"0-1"}]}
            obj.put("" + k, ja);

        }//end looping rows from DB

        double x = 0;
        double y = 0;
        int rowId_json = 0;
        int indexCoordinate_json = 0;
        int coordinateCount_json = 0;
        String nodes_json = "";

        // find the smallest weight from JSON
        for (int s = 0; s < obj.length(); s++) {

            if (s == 0) {
                // first
                JSONArray a = obj.getJSONArray("0");
                JSONObject b = a.getJSONObject(0);
                x = Double.parseDouble(b.getString("weight"));

                // ==========
                // row id field
                rowId_json = Integer.parseInt(b.getString("row_id"));
                // index coordinate around the NODE
                indexCoordinate_json = Integer.parseInt(b.getString("index"));
                // number of coordinates
                coordinateCount_json = Integer.parseInt(b.getString("coordinate_count"));
                // nodes
                nodes_json = b.getString("nodes").toString();
                // ==========

            } else {
                // second, etc
                JSONArray c = obj.getJSONArray("" + s);
                JSONObject d = c.getJSONObject(0);
                y = Double.parseDouble(d.getString("weight"));

                // get the smallest value (weight)
                if (y <= x) {
                    // weight
                    x = y;

                    // ==========
                    // row id field
                    rowId_json = Integer.parseInt(d.getString("row_id"));
                    // index coordinate around the NODE
                    indexCoordinate_json = Integer.parseInt(d.getString("index"));
                    // number of coordinates
                    coordinateCount_json = Integer.parseInt(d.getString("coordinate_count"));
                    // nodes
                    nodes_json = d.getString("nodes").toString();
                    // ==========

                }
            }

        }

        // nodes: 0-1
        String[] exp_nodes = nodes_json.split("-");

        int start_node_field = 0;
        int destination_node_field = 0;

        if (exp_nodes.length == 2) {
            String startNodeStr = exp_nodes[0].trim();
            String destinationNodeStr = exp_nodes[1].trim();

            if (!startNodeStr.isEmpty() && !destinationNodeStr.isEmpty()) {
                start_node_field = Integer.parseInt(startNodeStr);
                destination_node_field = Integer.parseInt(destinationNodeStr);
            }
        }

        // Coordinates obtained at the beginning or at the end, then there is no need to add a node
        if (indexCoordinate_json == 0 || indexCoordinate_json == coordinateCount_json) {

            // determine the nearest starting or ending node to the user's position
            if (indexCoordinate_json == 0) {

                // nodes in the start_node field
                fixed_start_node = start_node_field;
            } else if (indexCoordinate_json == coordinateCount_json) {

                // nodes in the destination_node field
                fixed_start_node = destination_node_field;
            }

            result_json.put("status", "no_path");

        }
        // Coordinates obtained are in the middle of nodes 0 - 1 (for example)
        else {
            // find duplicated nodes, reverse the nodes
            cursor = db.rawQuery("SELECT id FROM graph where starting = " + destination_node_field + " and ending = " + start_node_field, null);
            cursor.moveToFirst();
            cursor.moveToPosition(0);

            int duplicates = cursor.getCount();

            // there are duplicated nodes (1,0) and (0,1)
            if (duplicates == 1) {

                result_json.put("status", "double_path");

            }
            // not duplicated, only (1,0)
            else if (duplicates == 0) {

                result_json.put("status", "single_path");

            }
        }

        // JSON
        result_json.put("node_start0", start_node_field);
        result_json.put("node_start1", destination_node_field);
        result_json.put("index_coordinate_json", indexCoordinate_json);
        // result_json.put("destination_node", destination_node);
        result_json.put("exploded_lat_only", exploded_lat_only);

        return result_json;

    }

}