package com.example.ecabs.Utils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.ecabs.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SQLHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "routeplan_graph.db"; // Change to .db extension
    private static final int DATABASE_VERSION = 1;

    private final Context myContext;

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // This method will not be used, as we're executing SQL script instead
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method will not be used, as we're executing SQL script instead
    }

    public void createDB() throws IOException {
        if (!isDatabaseExist()) {
            getReadableDatabase(); // Ensure the database is created
            executeSQLScript();
        }
    }

    private boolean isDatabaseExist() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(
                    myContext.getDatabasePath(DATABASE_NAME).getPath(),
                    null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            Toast.makeText(myContext, "Please Try Again", Toast.LENGTH_LONG);
        } finally {
            if (checkDB != null) {
                checkDB.close();
            }
        }
        return checkDB != null;
    }

    private void executeSQLScript() {
        try {
            InputStream is = myContext.getResources().openRawResource(R.raw.routeplan_graph);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            StringBuilder statement = new StringBuilder();
            SQLiteDatabase db = this.getWritableDatabase();

            while ((line = reader.readLine()) != null) {
                statement.append(line);

                // Execute the SQL statement when you reach a semicolon
                if (line.endsWith(";")) {
                    try {
                        db.execSQL(statement.toString());
                    } catch (SQLException e) {
                        Log.e("SQLHelper", "Error executing SQL statement: " + e.getMessage());
                    }
                    statement = new StringBuilder();
                }
            }
            //while ((line = reader.readLine()) != null) {
            // statement.append(line);

            // Execute the SQL statement when you reach a semicolon
            //if (line.endsWith(";")) {
            //db.execSQL(statement.toString());
            //statement = new StringBuilder();
            //}
            // }

            // Close the database
            //db.close();

        } catch (IOException | SQLException e) {
            Log.e("SQLHelper", "Error executing SQL script: " + e.getMessage());
        }
    }
}
