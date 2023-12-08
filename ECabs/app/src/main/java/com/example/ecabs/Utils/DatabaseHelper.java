package com.example.ecabs.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_IMAGES = "images";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE = "image_data";

    public static final String databaseName = "SignLog.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "SignLog.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("CREATE TABLE users(email TEXT PRIMARY KEY , age INTEGER, contact_number INTEGER, address TEXT, password TEXT)");

        // Create the images table
        MyDatabase.execSQL("CREATE TABLE " + TABLE_NAME_IMAGES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_IMAGE + " BLOB" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
    }

    public boolean insertData(String email , String age, String contactNumber, String address, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("age", age);
        contentValues.put("contact_number", contactNumber);
        contentValues.put("address", address);
        contentValues.put("password", password);

        long result = MyDatabase.insert("users", null, contentValues);

        return result != -1;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }
    public boolean insertImage(Bitmap imageBitmap) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        byte[] imageData = getBytesFromBitmap(imageBitmap);
        values.put(COLUMN_IMAGE, imageData);
        long id = MyDatabase.insert(TABLE_NAME_IMAGES, null, values);
        return id != -1;
    }
    public Bitmap retrieveImage(int id) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.query(TABLE_NAME_IMAGES,
                new String[]{COLUMN_IMAGE},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") byte[] imageData = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
            cursor.close();
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        } else {
            cursor.close();
            return null;
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }


}