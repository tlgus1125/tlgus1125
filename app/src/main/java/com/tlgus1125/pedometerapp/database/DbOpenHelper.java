package com.tlgus1125.pedometerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tlgus1125 on 2017-01-13.
 */

public class DbOpenHelper {

    private static String DATABASE_NAME = "walkcount.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;
    private static DbOpenHelper mDBOpenHelper;

    static public DbOpenHelper getDBHelper(Context context){
        if (mDBOpenHelper == null){
            mDBOpenHelper = new DbOpenHelper(context);
        }
        return mDBOpenHelper;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB._CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(mDB != null)
            mDB.close();
    }

    public long insertColumn(String day, String walkcount, String distance){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.DAY, day);
        values.put(DataBases.CreateDB.WALKCOUNT, walkcount);
        values.put(DataBases.CreateDB.DISTANCE, distance);
        return mDB.insert(DataBases.CreateDB._TABLENAME, null, values);
    }

    public boolean updateColumn(String day, String walkcount, String distance){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.DAY, day);
        values.put(DataBases.CreateDB.WALKCOUNT, walkcount);
        values.put(DataBases.CreateDB.DISTANCE, distance);
        return mDB.update(DataBases.CreateDB._TABLENAME, values, DataBases.CreateDB.DAY + "='" + day + "'", null) > 0;
    }

    public Cursor getAllColumns(){
        if(mDB != null)
            return mDB.query(DataBases.CreateDB._TABLENAME, null, null, null, null, null, null);
        else
            return null;
    }

    public Cursor getMatchDay(String day){
        Cursor c = mDB.rawQuery( "select * from mytable where day=" + "'" + day + "'" , null);
        return c;
    }
}
