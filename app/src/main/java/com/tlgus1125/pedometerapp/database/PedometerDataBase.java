package com.tlgus1125.pedometerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tlgus1125 on 2017-01-13.
 */

public class PedometerDataBase {

    public static String DATABASE_NAME = "walkcount.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;
    private static PedometerDataBase mPedometerDataBase;

    static public PedometerDataBase getDBHelper(Context context){
        if (mPedometerDataBase == null){
            mPedometerDataBase = new PedometerDataBase(context);
        }
        return mPedometerDataBase;
    }

    public PedometerDataBase(Context context){
        this.mCtx = context;
    }

    public PedometerDataBase open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(mDB != null)
            mDB.close();
    }

    public long insertColumn(String day, String stepcount, String distance){
        ContentValues values = new ContentValues();
        values.put(DataBaseUtil.CreateDB.DAY, day);
        values.put(DataBaseUtil.CreateDB.STEPCOUNT, stepcount);
        values.put(DataBaseUtil.CreateDB.DISTANCE, distance);
        return mDB.insert(DataBaseUtil.CreateDB._TABLENAME, null, values);
    }

    public boolean updateColumn(String day, String stepcount, String distance){
        ContentValues values = new ContentValues();
        values.put(DataBaseUtil.CreateDB.DAY, day);
        values.put(DataBaseUtil.CreateDB.STEPCOUNT, stepcount);
        values.put(DataBaseUtil.CreateDB.DISTANCE, distance);
        return mDB.update(DataBaseUtil.CreateDB._TABLENAME, values, DataBaseUtil.CreateDB.DAY + "='" + day + "'", null) > 0;
    }

    public Cursor getAllColumns(){
        if(mDB != null)
            return mDB.query(DataBaseUtil.CreateDB._TABLENAME, null, null, null, null, null, null);
        else
            return null;
    }

    public Cursor getMatchDay(String day){
        Cursor c = mDB.rawQuery( "select * from mytable where day=" + "'" + day + "'" , null);
        return c;
    }

    public SQLiteDatabase getSQLiteDatabase(){
        return mDB;
    }
}
