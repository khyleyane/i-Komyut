package com.example.ecabs.Dijkstra;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.ecabs.Utils.SQLHelper;

/*
 * CONVERT GRAPH FROM DB TO ARRAY
 */
public class GraphToArray {

    // DB
    //An object of the SQLHelper class, used for database operations.
    SQLHelper dbHelper;
    //An object of the SQLiteDatabase class, representing the SQLite database.
    SQLiteDatabase db;
    //An object of the Cursor class, used to traverse and retrieve data from the database.
    protected Cursor cursor;

    // Array Graph
    String[][] graph = new String[100][100];


    /* Convert Graph from DB to Array
     * parameter mainContext : context MainActivity
     * return array String[][]
     */
    public String[][] convertToArray(Context context){

        dbHelper = new SQLHelper(context);
        db = dbHelper.getReadableDatabase();

        // MOVE GRAPH FROM DB TO GRAPH ARRAY
        cursor = db.rawQuery("SELECT * FROM graph ORDER BY starting, ending ASC", null);
        cursor.moveToFirst();

        String temp_row_index = "";
        int column_index = 0;
        int row_count = cursor.getCount();

        for(int i = 0; i < row_count; i++) {

            // row
            cursor.moveToPosition(i);

            // Find column index
            int startNodeDB = Integer.parseInt(cursor.getString(1)); // destination_node

            if (temp_row_index == "") {
                temp_row_index = String.valueOf(startNodeDB);
            } else {
                // next start_node is different from the previous one, reset column_index = 0
                if (Integer.parseInt(temp_row_index) != startNodeDB) {
                    column_index = 0;
                    temp_row_index = String.valueOf(startNodeDB);
                }
            }

            // Put into the graph array
            String destinationAndWeight = "";
            if (cursor.getString(2).equals("") && cursor.getString(3).equals("") && cursor.getString(4).equals("")) { // no outgoing degree
                destinationAndWeight = ";";
            }
            // has outgoing degree
            else {

                // example output : 2->789.98
                destinationAndWeight = cursor.getString(2).toString() + "->" + cursor.getString(4).toString(); //destination_node and weight
            }

            graph[startNodeDB][column_index] = destinationAndWeight;
            column_index++;
        }


        return graph;

    }

}