package com.tlgus1125.pedometerapp.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import com.tlgus1125.pedometerapp.R;
import com.tlgus1125.pedometerapp.baseinfomation.StepInfo;
import com.tlgus1125.pedometerapp.database.DbOpenHelper;
import com.tlgus1125.pedometerapp.listadapter.CustomAdapter;

import java.util.ArrayList;

/**
 * Created by tlgus1125 on 2017-01-13.
 */
public class StepRecordActivity extends Activity{

    private ArrayList<StepInfo> mList;
    private CustomAdapter mAdapter;
    private ListView mListView;
    private StepInfo mWalkInfo;

    //DataBases
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
    }

    @Override
    protected void onResume() {
        mDbOpenHelper = DbOpenHelper.getDBHelper(this);
        mDbOpenHelper.open();
        mList = new ArrayList<StepInfo>();
        doWhileCursorToArray();
        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new CustomAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        super.onResume();
    }

    private void doWhileCursorToArray(){

        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();

        if(mCursor != null) {
            while (mCursor.moveToNext()) {

                mWalkInfo = new StepInfo(
                        mCursor.getString(mCursor.getColumnIndex("day")),
                        mCursor.getString(mCursor.getColumnIndex("walkcount")),
                        mCursor.getString(mCursor.getColumnIndex("distance"))
                );

                mList.add(mWalkInfo);
            }

            mCursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        if(mDbOpenHelper != null)
            mDbOpenHelper.close();
        super.onDestroy();
    }
}
