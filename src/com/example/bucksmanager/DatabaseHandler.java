package com.example.bucksmanager;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
  
    // Database Name
    private static final String DATABASE_NAME = "DBBANKING.db";
  
    // Labels table name
    private static final String TABLE_LABELS = "SMSDATABASE";
  
    // Labels Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "smsnumber";
  
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
  
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LABELS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " VARCHAR)";
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }
  
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    	// check tables created or not
       onCreate(db);
    	
    	
    }
     
    /**
     * Inserting new lable into lables table
     * */
    public void insertLabel(String label){
        SQLiteDatabase db = this.getWritableDatabase();
         
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, label);
          
        // Inserting Row
        db.insert(TABLE_LABELS, null, values);
       // db.close(); // Closing database connection
    }
     
    /**
     * Getting all labels
     * returns list of labels
     * */
    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();
         
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LABELS;
      
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
      
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
         
        // closing connection
        cursor.close();
        db.close();
         
        // returning lables
        return labels;
    }
}
