package com.example.ecabs.Dijkstra;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.example.ecabs.Utils.SQLHelper;

public class AddNode {

    protected Cursor cursor;
    SQLHelper dbHelper;
    public String[][] modified_graph = new String[100][100];
    public String old_node;
    public int new_node;

    String target_node = "";

    /*
     * @function
     *   insert a new node
     *   e.g., insert node 5-4 as 5-6-4
     *   and node 4-5 as 4-6-5
     * @parameters
     *   nodes0 : e.g., {"nodes": "5-4"} then nodes0 = 5
     *   nodes1 : e.g., {"nodes": "5-4"} then nodes1 = 4
     *   coordinate_json_index : index of coordinate array in JSON
     *   context : MainActivity.context
     *   graph[][] : array to store the graph from the DB
     *         example output: graph[5][0] = 4->439.281
     *                        graph[6][0] = 1->216.281
     *   increase_row_id : new DB row id
     * @return
     *   old_node = nodes0 + "-" + nodes1
     *   new_node = initial node
     *   graph[][]
     */
    public void doubleNode(int nodes0, int nodes1,
                           int coordinate_json_index, Context context,
                           String[][] graph, int increase_row_id
    ) throws JSONException{

        // read DB
        SQLHelper dbHelper = new SQLHelper(context);
        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();

        // create an insert to the DB
        SQLiteDatabase dbInsert = dbHelper.getWritableDatabase();


        // CALCULATE THE ORIGINAL NODE FIRST (5-4), NOT THE DOUBLE (4-5)
        //==============================================================
        String column_index_graph = "";

        // find the column index for nodes1 (4) from graph[row][column]
        for(int l = 0; l < 100; l++){

            if(graph[nodes0][l] != null){

                String startNode = graph[nodes0][l]; // [5][0] = 4->721.666

                // 4->721.666
                String [] explode = startNode.split("->");

                target_node = explode[0]; // 4

                // if 4 == 4 (node1)
                if(target_node.trim().equals(String.valueOf(nodes1).trim()) ){

                    // column index; example graph[row][column]
                    column_index_graph = String.valueOf(l);
                }

            }else break;

        }// for

        // index for the graph[row][column] to be edited
        int row = nodes0;
        int column = Integer.parseInt(column_index_graph);

        // get the coordinates from node 5-4
        cursor = dbInsert.rawQuery("SELECT route FROM graph where starting = "+ nodes0 +" and ending = "+ nodes1, null);
        cursor.moveToFirst();
        cursor.moveToPosition(0);

        // --
        // get coordinates from JSON
        String json_coordinates = cursor.getString(0).toString();
        JSONObject jObject = new JSONObject(json_coordinates);
        JSONArray jArrCoordinates = jObject.getJSONArray("coordinates");
        // --

        // find the maximum node, (for numbering the new node)
        cursor = dbRead.rawQuery("SELECT max(starting), max(ending) FROM graph", null);
        cursor.moveToFirst();
        int max_node_db       = 0;
        int max_start_node_db  = Integer.parseInt(cursor.getString(0).toString());
        int max_end_node_db = Integer.parseInt(cursor.getString(1).toString());
        if(max_start_node_db >= max_end_node_db){
            max_node_db = max_start_node_db;
        }else{
            max_node_db = max_end_node_db;
        }

        // split the coordinates from START->MIDDLE
        int limit = coordinate_json_index;
        Calculate_Weight_Add_Node cw = new Calculate_Weight_Add_Node();
        cw.Calculate_Weight_Distance_Add_Node(0, limit, jArrCoordinates); // 0, middle coordinate, JSON coordinates

        //replace array graph[5][0] = 6->888.6
        graph[row][column] = (max_node_db+1)+"->"+cw.weight_distance;

        int start_loop = 0;
        // create and save (new record) json coordinate to the DB
        createAndSave_NewJsonCoordinate(start_loop, limit, jArrCoordinates, increase_row_id, row, (max_node_db + 1), cw.weight_distance,
                dbInsert, dbRead); // 501 : new index record


        // reset weight
        cw.weight_distance = 0;


        // split the coordinates from MIDDLE->END
        int start_loop1 = coordinate_json_index;
        int limit1 = (jArrCoordinates.length() - 1); // - 1 because the array starts from 0
        cw.Calculate_Weight_Distance_Add_Node(coordinate_json_index, limit1, jArrCoordinates); // middle coordinate to end

        // new array graph[6][0] = 4->777.4
        graph[(max_node_db+1)][0] = nodes1 + "->" + cw.weight_distance; //defined [0] because it's a new index in graph[][]

        // create and save (new record) json coordinate to the DB
        createAndSave_NewJsonCoordinate(start_loop1, limit1, jArrCoordinates, ++increase_row_id, (max_node_db + 1), nodes1, cw.weight_distance,
                dbInsert, dbRead); // 502 : new index record


        // reset weight
        cw.weight_distance = 0;


        // CALCULATE THE DOUBLE NODE (4-5), NOT THE ORIGINAL (5-4)
        //==============================================================

        String column_index_graph1 = "";
        String nodes_inside_column = "";

        // reverse, nodes0 becomes nodes1; example (5-4) becomes (4-5)
        int temp_nodes0 = nodes1; // 4
        int temp_nodes1 = nodes0; // 5

        // find the column index from graph[4][its index]
        for(int l = 0; l < 100; l++){

            if(graph[temp_nodes0][l] != null){

                // == get the destination node, example: 5->9585.340
                String startNode = graph[temp_nodes0][l];
                String [] explode1 = startNode.split("->");

                nodes_inside_column = explode1[0];

                if(nodes_inside_column.trim().equals(String.valueOf(temp_nodes1)) ){
                    column_index_graph1 = String.valueOf(l);
                }

            }else break;
        }//for


        // index of graph[row1][column1] to be edited
        int row1 = temp_nodes0;
        int column1 = Integer.parseInt(column_index_graph1);

        // get the coordinates from node 4-5
        cursor = dbRead.rawQuery("SELECT route FROM graph where starting = "+temp_nodes0+" and ending = "+temp_nodes1, null);
        cursor.moveToFirst();
        cursor.moveToPosition(0);

        // --
        // get coordinates from DB
        String json1 = cursor.getString(0).toString();
        JSONObject jObject1 = new JSONObject(json1);
        JSONArray jArrCoordinates1 = jObject1.getJSONArray("coordinates");
        // --

        // split the coordinates from START->MIDDLE
        int index_double_coordinate_json = ( (jArrCoordinates1.length()-1) - coordinate_json_index );
        cw.Calculate_Weight_Distance_Add_Node(0, index_double_coordinate_json, jArrCoordinates1); // 0, initial coordinate to middle, JSONArray coordinate

        //replace array graph[4][0] = 6->777.4
        graph[row1][column1] = (max_node_db+1)+"->"+cw.weight_distance;

        // create and save (new record) json coordinate to the DB
        int start_loop2 = 0;
        createAndSave_NewJsonCoordinate(start_loop2, index_double_coordinate_json, jArrCoordinates1, ++increase_row_id, row1, (max_node_db + 1), cw.weight_distance,
                dbInsert, dbRead); // 503 : new index record


        // reset weight
        cw.weight_distance = 0;


        // split the coordinates from MIDDLE->END
        int limit2 = (jArrCoordinates1.length() - 1); // - 1 because the array starts from 0
        cw.Calculate_Weight_Distance_Add_Node(index_double_coordinate_json, limit2, jArrCoordinates1); // middle coordinate to end

        //replace array graph[6][1] = 5->888.6
        graph[(max_node_db+1)][1] = temp_nodes1+"->"+cw.weight_distance; // defined [1] because there is already index 0 in graph[][]

        // create and save (new record) json coordinate to the DB
        createAndSave_NewJsonCoordinate(index_double_coordinate_json, limit2, jArrCoordinates1, ++increase_row_id, (max_node_db + 1), temp_nodes1, cw.weight_distance,
                dbInsert, dbRead); // 503 : new index record



        // return
        old_node = nodes0 + "-" + nodes1;
        new_node = (max_node_db + 1);
        modified_graph = graph; // graph[][]

    }// public void doubleNode

    /*
     * @function
     *   insert a new node
     *   e.g., insert node 5-4 as 5-6-4
     * @parameters
     *   nodes_start0 : e.g., {"nodes": "5-4"} then nodes_start0 = 5
     *   nodes_end1 : e.g., {"nodes": "5-4"} then nodes_end1 = 4
     *   coordinate_json_index : index of coordinate array in JSON
     *   context : MainActivity.context
     *   graph[][] : array to store the graph from the DB
     *         example output: graph[5][0] = 4->439.281
     *                        graph[6][0] = 1->216.281
     *   increase_row_id : new DB row id
     * @return
     *   new_node : final node
     *   graph[][]
     */
    public void singleNode(int nodes_start0, int nodes_end1, int coordinate_json_index,
                           Context context, String[][] graph, int increase_row_id) throws JSONException{

        // read DB
        SQLHelper dbHelper = new SQLHelper(context);
        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();

        // create an insert to the DB
        SQLiteDatabase dbInsert = dbHelper.getWritableDatabase();


        // CALCULATE THE ORIGINAL NODE (5-4)
        //==============================================================

        String column_index_graph = "";

        // find the column index for end_node1 (4) from graph[row][column]
        for(int l = 0; l < 100; l++){

            if(graph[nodes_start0][l] != null){

                String startNode = graph[nodes_start0][l]; // [5][0] = 4->721.666
                String [] explode = startNode.split("->");

                // 6->721.666
                String value_node_array = explode[0];

                // if 4 == 4 (end_node1)
                if( value_node_array.trim().equals(String.valueOf(nodes_end1).trim()) ){

                    // column index; example graph[row][column]
                    column_index_graph = String.valueOf(l);
                }

            }else break;
        }//for


        // index of graph[row][column] to be edited
        int row = nodes_start0;
        int column = Integer.parseInt(column_index_graph);


        // get the coordinates from node 3-6
        cursor = dbRead.rawQuery("SELECT route FROM graph where starting = "+nodes_start0+" and ending = "+nodes_end1, null);
        cursor.moveToFirst();
        cursor.moveToPosition(0);

        // --
        // get coordinates JSON
        String json_coordinates = cursor.getString(0).toString();
        JSONObject jObject = new JSONObject(json_coordinates);
        JSONArray jArrCoordinates = jObject.getJSONArray("coordinates");
        // --

        // find the maximum node, (for numbering the new node)
        cursor = dbRead.rawQuery("SELECT max(starting) FROM graph", null);
        cursor.moveToFirst();
        int max_node_db = Integer.parseInt(cursor.getString(0).toString());


        // split the coordinates from START->MIDDLE
        System.out.println("single start->middle");
        int limit = coordinate_json_index;
        Calculate_Weight_Add_Node cw = new Calculate_Weight_Add_Node();
        cw.Calculate_Weight_Distance_Add_Node(0, limit, jArrCoordinates); // 0, middle coordinate, JSON coordinates

        //replace array graph[5][0] = 6->888.6
        graph[row][column] = (max_node_db+1)+"->"+cw.weight_distance;


        int start_loop = 0;

        // create and save (new record) json coordinate to the DB
        createAndSave_NewJsonCoordinate(start_loop, limit, jArrCoordinates, increase_row_id, row, (max_node_db + 1), cw.weight_distance,
                dbInsert, dbRead); // 501 : new index record

        // reset weight
        cw.weight_distance = 0;


        // split the coordinates from MIDDLE->END
        int start_loop1 = coordinate_json_index; // - 1 because the array starts from 0
        int limit1 = (jArrCoordinates.length() - 1); // - 1 because the array starts from 0
        cw.Calculate_Weight_Distance_Add_Node(coordinate_json_index, limit1, jArrCoordinates); // coordinate middle to end

        // new array graph[6][0] = 4->777.4
        graph[(max_node_db+1)][0] = nodes_end1 + "->" + cw.weight_distance; //defined [0] because it's a new index in graph[][]

        // create and save (new record) json coordinate to the DB
        createAndSave_NewJsonCoordinate(start_loop1, limit1, jArrCoordinates, ++increase_row_id, (max_node_db + 1), nodes_end1, cw.weight_distance,
                dbInsert, dbRead); // 502 : new index record

        // return
        old_node = nodes_start0 + "-" + nodes_end1;
        new_node = (max_node_db + 1);
        modified_graph = graph; // graph[][]
    }


    /* @function
     *   Create and save new coordinates in JSON format to the DB
     * @parameters
     *   start : starting looping, e.g., 0
     *   limit : index of coordinate array, e.g., i[7] then limit = 7
     *   jArrCoordinates : Coordinates from DB in JSONArray format
     *   new_id : new record id
     *   field_start_node : row of multidimensional array, e.g., i[row][column]
     *   field_end_node : column of multidimensional array, e.g., i[row][column]
     *   new_weight : new weight from coordinate splitting
     *   dbInsert : insert into the database
     *   dbRead : read database records
     * @return
     *   no return
     */
    public void createAndSave_NewJsonCoordinate(int start, int limit, JSONArray jArrCoordinates,
                                                int new_id, int field_start_node, int field_end_node, double new_weight_distance,
                                                SQLiteDatabase dbInsert, SQLiteDatabase dbRead) throws JSONException{

        // JSON for saving new coordinate
        JSONObject json_new = new JSONObject();
        JSONArray new_root_coordinates = new JSONArray();

        // looping from initial coordinate to middle coordinate
        // or
        // looping from middle coordinate to end coordinate
        // then, move old coordinate to new coordinate
        for(int ne = start; ne <= limit; ne++){

            JSONArray latlng = jArrCoordinates.getJSONArray(ne);
            double new_lat = latlng.getDouble(0);
            double new_lng = latlng.getDouble(1);

            JSONArray new_list_coordinates = new JSONArray();
            new_list_coordinates.put(new_lat);
            new_list_coordinates.put(new_lng);

            // coordinates
            new_root_coordinates.put(new_list_coordinates);
        }


        // nodes
        JSONArray nodes = new JSONArray();
        String combine_nodes = String.valueOf(field_start_node) + '-' + String.valueOf(field_end_node);
        nodes.put(combine_nodes);

        // distance_metres
        JSONArray distance_metres = new JSONArray();
        distance_metres.put(new_weight_distance);


        // create new JSON
        json_new.put("nodes", nodes);
        json_new.put("coordinates", new_root_coordinates);
        json_new.put("distance_metres", distance_metres);

        String new_path = json_new.toString();
        System.out.println(new_path);

        // INSERT NEW NODE
        ContentValues newCon = new ContentValues();
        newCon.put("id", new_id);
        newCon.put("starting", field_start_node);
        newCon.put("ending", field_end_node);
        newCon.put("route", new_path);
        //newCon.put("weight", new_weight_distance);
        newCon.put("weight_distance", new_weight_distance);
        newCon.put("weight_fare", (Double) null);
        dbInsert.insert("graph", null, newCon);

    }
}
