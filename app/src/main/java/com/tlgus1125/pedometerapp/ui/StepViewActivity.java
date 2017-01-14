package com.tlgus1125.pedometerapp.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tlgus1125.pedometerapp.R;
import com.tlgus1125.pedometerapp.baseinfomation.StepCount;
import com.tlgus1125.pedometerapp.database.DataBases;
import com.tlgus1125.pedometerapp.database.DbOpenHelper;
import com.tlgus1125.pedometerapp.location.MyLocation;
import com.tlgus1125.pedometerapp.service.SensorService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tlgus1125 on 2017-01-13.
 */
public class StepViewActivity extends Activity {
    private Button mBtnSetting = null;

    private MyLocation mLocation = null;
    private TextView mTvLocation = null;

    private Button mServiceBtn = null;
    private Intent mSensorServiceIntent = null;
    private BroadcastReceiver mReceiver = null;
    private boolean flag = true;
    private TextView mStepCountText = null;
    private TextView mDistanceText = null;
    private String mServiceData = null;
    //DB
    private DbOpenHelper mDbOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        mBtnSetting = (Button) findViewById(R.id.btn_setting);
        if(mBtnSetting != null) {
            mBtnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSetting();
                }
            });
        }
        mTvLocation = (TextView) findViewById(R.id.loaction);
        mLocation = new MyLocation();
        mLocation.initMyLocation(this, StepViewActivity.this, mTvLocation);

        mSensorServiceIntent = new Intent(this, SensorService.class);

        mReceiver = new SensorReceiver();
        mStepCountText = (TextView) findViewById(R.id.stepCount);
        mDistanceText = (TextView) findViewById(R.id.distance);

        mServiceBtn = (Button) findViewById(R.id.btn_start);
        if(mServiceBtn != null) {
            mServiceBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (flag) {
                        mServiceBtn.setText("STOP");

                        try {
                            IntentFilter mainFilter = new IntentFilter("com.tlgus1125.pedometerapp");
                            registerReceiver(mReceiver, mainFilter);
                            startService(mSensorServiceIntent);

                        } catch (Exception e) {
                        }
                    } else {
                        mServiceBtn.setText("START");

                        try {
                            unregisterReceiver(mReceiver);
                            stopService(mSensorServiceIntent);
                            //DB Update
                            updateDataBase();
                            mServiceData = "0";
                            mStepCountText.setText(mServiceData);

                            double distance = getDistanceValue(mServiceData);
                            String isMeter = distance < 1000 ? "m" : "Km";
                            mDistanceText.setText(String.valueOf(distance) + isMeter);

                            StepCount.Step = 0;

                        } catch (Exception e) {
                        }
                    }
                    flag = !flag;
                }
            });
        }
    }

    public void startSetting(){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void updateDataBase(){
        mDbOpenHelper = DbOpenHelper.getDBHelper(this);
        mDbOpenHelper.open();
        String cur_date = getCurrentDate();
        Cursor cursor = mDbOpenHelper.getMatchDay(cur_date);
        if(!mServiceData.equals("0")) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToNext();
                String day = cursor.getString(cursor.getColumnIndex(DataBases.CreateDB.DAY));
                String walkcount = cursor.getString(cursor.getColumnIndex(DataBases.CreateDB.STEPCOUNT));
                String distance = cursor.getString(cursor.getColumnIndex(DataBases.CreateDB.DISTANCE));
                double distanceValue = Double.parseDouble(String.format("%.2f",Double.parseDouble(distance) + Integer.parseInt(mServiceData) * SettingActivity.mStrideValue));
                mDbOpenHelper.updateColumn(day, String.valueOf(Integer.parseInt(walkcount) + Integer.parseInt(mServiceData)),
                        String.valueOf(distanceValue));
            } else {
                double distanceValue = Double.parseDouble(String.format("%.2f",Integer.parseInt(mServiceData) * SettingActivity.mStrideValue));
                mDbOpenHelper.insertColumn(cur_date, mServiceData, String.valueOf(distanceValue));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(mDbOpenHelper != null)
            mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public String getCurrentDate(){
        SimpleDateFormat CurDate = new SimpleDateFormat("yyyy-MM-DD", Locale.KOREA);
        return CurDate.format(new Date());
    }

    public double getDistanceValue(String strCount){
        int stepCount = Integer.parseInt(strCount);
        double distance = SettingActivity.mStrideValue * stepCount;
        if(distance >= 1000.0){
            distance /= 1000;
        }
        return Double.parseDouble(String.format("%.1f", distance));
    }

    class SensorReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            mServiceData = intent.getStringExtra("serviceData");

            mStepCountText.setText(mServiceData);
            double distance = getDistanceValue(mServiceData);
            String isMeter = distance < 1000 ? "m" : "Km";
            mDistanceText.setText(String.valueOf(distance) + isMeter);
        }
    }
}
