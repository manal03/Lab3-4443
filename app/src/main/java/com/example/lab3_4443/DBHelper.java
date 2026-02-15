package com.example.lab3_4443;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "lab3.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE = "contacts";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_PHONE = "phone";
    public static final String COL_DATE = "dateAdded";
    public static final String COL_DESC = "description";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public long insertContact(Contact c) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, c.name);
        values.put(COL_PHONE, c.phone);
        values.put(COL_DATE, c.dateAdded);
        values.put(COL_DESC, c.description);
        return db.insert(TABLE, null, values);
    }
}
