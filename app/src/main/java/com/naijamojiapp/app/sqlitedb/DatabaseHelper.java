package com.naijamojiapp.app.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String Database_Name = "Naijamoji";

    // Database Version
    private static final int Database_Version = 1;

    // Table Name
    private static final String Table_Name = "Recent_stickers";

    // Column Id Primary Key
    private static final String Column_Id = "id";

    // Table fields
    private static final String Image = "image";
    private static final String image_id = "image_id";
    private static final String character_limit = "character_limit";
    private static final String is_studiomode = "is_studiomode";

    // Create table query
  /*  private static final String Create_Table = "Create table " + Table_Name
            + " ( " + Column_Id + " integer primary key autoincrement, "
            + Image
            + " text not null, "
            + image_id
            + " text not null);";*/


    private static final String Create_Table = " CREATE TABLE " + Table_Name
            + " (" + Column_Id + " integer primary key autoincrement, " +
            Image + " TEXT NOT NULL, " +
            image_id + " TEXT NOT NULL, " +
            character_limit + " TEXT NOT NULL, " +
            is_studiomode + " TEXT NOT NULL);";

    // Drop/delete table query
    private static final String Drop_Table = "Drop table if exists " + Table_Name;

    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // executing the created table query
        db.execSQL(Create_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int olderVersion, int newVersion) {

        // executing the drop table query if database version is changed
        db.execSQL(Drop_Table);
        onCreate(db);

    }


    // Insert data into database method
    public void insertData(DataModel data) {

        // Accessing SQL database to write data
        SQLiteDatabase db = this.getWritableDatabase();

        // Content values used for editing/writing data into database
        ContentValues values = new ContentValues();

        Cursor cursor = null;
        String sql = "SELECT image_id FROM " + Table_Name + " WHERE image_id=" + data.Id;
        cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            //image_id Found

            Log.i("FondNotFound","yes");

        } else {
            //image_id Not Found
            Log.i("FondNotFound","no");
            values.put(Image, data.getImage());
            values.put(image_id, data.getId());
            values.put(character_limit, data.getCharacter_limit());
            values.put(is_studiomode, data.getIs_studiomode());

            // Now inserting content values data into table
            db.insert(Table_Name, null, values);
            db.close();
            cursor.close();
        }
    }


    public void addData(DataModel model) {

        if (tableRow() > 6) {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
//                int i = db.delete(Table_Name, "id = (select id from "+ Table_Name +" where image_id =  5)", null);
                int i = db.delete(Table_Name, "id = (select id from " + Table_Name + " order by id ASC limit 1)", null);
                if (i > 0) {
                    insertData(model);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            insertData(model);
        }
    }


    private int tableRow() {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement s = db.compileStatement("SELECT count(id) FROM " + Table_Name);
        String count = s.simpleQueryForString();

        return Integer.parseInt(count);
    }

    // Getting all saved data
    public List<DataModel> getAllData() {

        // Data model list in which we have to return the data
        List<DataModel> data = new ArrayList<DataModel>();

        // Accessing database for reading data
        SQLiteDatabase db = this.getReadableDatabase();

        // Select query for selecting whole table data
        //String select_query = "Select * from " + Table_Name;
        Cursor cursor = db.query(Table_Name, null, null,
                null, null, null, Column_Id + " DESC", null);
        // Cursor for traversing whole data into database
//		Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {
                    DataModel data_model = new DataModel(
                                    cursor.getString(1),
                                    cursor.getString(2),
                                    cursor.getString(3),
                                    cursor.getString(4));
                    data.add(data_model);
                } while (cursor.moveToNext());
            }
        } finally {
            // After using cursor we have to close it
            cursor.close();
        }
        // Closing database
        db.close();
        // returning list
        return data;
    }

    // Deleting table from database
    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Deleting table
        db.delete(Table_Name, null, null);
        // Closing database
        db.close();
    }
}
