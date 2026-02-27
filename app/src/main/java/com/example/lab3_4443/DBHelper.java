package com.example.lab3_4443;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

//Our SQLITE database manager
public class DBHelper extends SQLiteOpenHelper {
    //Name of the database file we are saving to
    private static final String DB_NAME = "lab3.db";
    private static final int DB_VERSION = 1;
//Initializing all database columns
    public static final String TABLE = "contacts";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_PHONE = "phone";
    public static final String COL_DATE = "dateAdded";
    public static final String COL_DESC = "description";

    //Calls the parent (SQLiteOpenHelper) constructor
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Runs only once to create the database file
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_PHONE + " TEXT NOT NULL, " +
                COL_DATE + " TEXT NOT NULL, " +
                COL_DESC + " TEXT" +
                ")";
        db.execSQL(sql);
    }
    //Happens if you change the database version, SQLite updates
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
    //Opens the database in write mode, adds values into it
    public long insertContact(Contact c) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, c.name);
        values.put(COL_PHONE, c.phone);
        values.put(COL_DATE, c.dateAdded);
        values.put(COL_DESC, c.description);
        return db.insert(TABLE, null, values);
    }
    //Function that loads all contacts onto the database
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE,
                null,              // all columns
                null,
                null,
                null,
                null,
                COL_ID + " DESC"   // newest first
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE));
                String date  = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
                String desc  = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC));

                contacts.add(new Contact(name, phone, date, desc));
            }
            cursor.close();
        }

        return contacts;
    }

    public int deleteByPhone(String phone) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE, COL_PHONE + "=?", new String[]{phone});
    }
}
