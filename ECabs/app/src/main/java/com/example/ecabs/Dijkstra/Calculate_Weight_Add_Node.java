package com.example.ecabs.Dijkstra;

import org.json.JSONArray;
import org.json.JSONException;
import android.location.Location;

public class Calculate_Weight_Add_Node{

    //A double variable to store the weight of the path being calculated.
    double weight_distance = 0;
    double weight_fare = 0;

    public void Calculate_Weight_Distance_Add_Node(int index, int limit, JSONArray jArrCoordinates) throws JSONException{

        // executed only once, if limit is 1, then 1-0 = 0
        // 0 == 0 (limit)
        if(index == limit){
            // get JSON coordinate
            JSONArray latlngs = jArrCoordinates.getJSONArray(index);

            double lat_0 = latlngs.getDouble(0);
            double lng_0 = latlngs.getDouble(1);

            Location startingNode = new Location("");
            startingNode.setLatitude(lat_0);
            startingNode.setLongitude(lng_0);

            // get coordinate again
            JSONArray latlngs1 = jArrCoordinates.getJSONArray(++index);

            double lat_1 = latlngs1.getDouble(0);
            double lng_1 = latlngs1.getDouble(1);

            Location middleNode = new Location("");
            middleNode.setLatitude(lat_1);
            middleNode.setLongitude(lng_1);

            // store distance
            weight_distance += startingNode.distanceTo(middleNode);
            weight_fare += startingNode.distanceTo(middleNode);

        }else{
            for(int i = 0; i < 1; i++){

                // get JSON coordinate
                JSONArray latlngs = jArrCoordinates.getJSONArray(index);

                double lat_0 = latlngs.getDouble(0);
                double lng_0 = latlngs.getDouble(1);

                Location startingNode = new Location("");
                startingNode.setLatitude(lat_0);
                startingNode.setLongitude(lng_0);

                // get coordinate again
                JSONArray latlngs1 = jArrCoordinates.getJSONArray(++index);

                double lat_1 = latlngs1.getDouble(0);
                double lng_1 = latlngs1.getDouble(1);

                Location middleNode = new Location("");
                middleNode.setLatitude(lat_1);
                middleNode.setLongitude(lng_1);

                // store distance
                weight_distance += startingNode.distanceTo(middleNode);
                weight_fare += startingNode.distanceTo(middleNode);

                if(index == limit){
                    break; // if it reaches the middle, break; for example 0-72
                } else {
                    --i;
                }
            }
        }
    }
}





