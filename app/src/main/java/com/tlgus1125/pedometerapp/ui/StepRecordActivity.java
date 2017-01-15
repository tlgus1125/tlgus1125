package com.tlgus1125.pedometerapp.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import com.tlgus1125.pedometerapp.R;
import com.tlgus1125.pedometerapp.baseinfomation.StepInfo;
import com.tlgus1125.pedometerapp.database.DataBaseUtil;
import com.tlgus1125.pedometerapp.database.PedometerDataBase;
import com.tlgus1125.pedometerapp.listadapter.CustomAdapter;

import java.util.ArrayList;

/**
 * Created by tlgus1125 on 2017-01-13.
 */

//일별 기록 정보 (날짜, 걸음 수, 거리) 를 사용자에게 표시
public class StepRecordActivity extends Activity{

    private ArrayList<StepInfo> mList = null;
    private CustomAdapter mAdapter = null;
    private ListView mListView = null;
    private StepInfo mWalkInfo = null;

    //DataBaseUtil
    private PedometerDataBase mPedometerDataBase;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
    }

    @Override
    protected void onResume() {
        mPedometerDataBase = PedometerDataBase.getDBHelper(this);
        mPedometerDataBase.open();
        mList = new ArrayList<StepInfo>();
        doWhileCursorToArray();
        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new CustomAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        super.onResume();
    }

    private void doWhileCursorToArray(){

        mCursor = null;
        mCursor = mPedometerDataBase.getAllColumns();

        if(mCursor != null) {
            while (mCursor.moveToNext()) {

                mWalkInfo = new StepInfo(
                        mCursor.getString(mCursor.getColumnIndex(DataBaseUtil.CreateDB.DAY)),
                        mCursor.getString(mCursor.getColumnIndex(DataBaseUtil.CreateDB.STEPCOUNT)),
                        mCursor.getString(mCursor.getColumnIndex(DataBaseUtil.CreateDB.DISTANCE))
                );

                mList.add(mWalkInfo);
            }

            mCursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        if(mPedometerDataBase != null)
            mPedometerDataBase.close();
        super.onDestroy();
    }
}
