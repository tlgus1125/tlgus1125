package com.tlgus1125.pedometerapp.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.tlgus1125.pedometerapp.database.DataBaseUtil;
import com.tlgus1125.pedometerapp.database.DatabaseHelper;

/**
 * Created by tlgus1125 on 2017-01-15.
 */

//걸음정보 ContentProvider
public class PedometerContentProvider extends ContentProvider {
    private SQLiteDatabase mDB = null;
    private Uri contentUri = Uri.parse("com.tlgus1125.pedometerapp");
    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        mDB = dbHelper.getWritableDatabase();
        if(mDB == null)
            return false;
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = mDB.query(DataBaseUtil.CreateDB._TABLENAME, null, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri muri = null;
        long id = mDB.insert(DataBaseUtil.CreateDB._TABLENAME, null, values);
        if (id > 0) {
            muri = ContentUris.withAppendedId(contentUri, id);
            getContext().getContentResolver().notifyChange(muri, null);
        } else {
            Toast.makeText(getContext(),"DB insert fail !!",Toast.LENGTH_SHORT).show();
        }
        return muri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int id = mDB.delete(DataBaseUtil.CreateDB._TABLENAME, selection, selectionArgs);
        if (id > 0) {
            Uri muri = ContentUris.withAppendedId(contentUri, id);
            getContext().getContentResolver().notifyChange(muri, null);
        } else {
            Toast.makeText(getContext(),"DB delete fail !!",Toast.LENGTH_SHORT).show();
        }
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int id = mDB.update(DataBaseUtil.CreateDB._TABLENAME, values, selection, selectionArgs);
        if (id > 0) {
            Uri muri  = ContentUris.withAppendedId(contentUri, id);
            getContext().getContentResolver().notifyChange(muri, null);
        } else {
            Toast.makeText(getContext(),"DB update fail !!",Toast.LENGTH_SHORT).show();
        }
        return id;
    }
}
