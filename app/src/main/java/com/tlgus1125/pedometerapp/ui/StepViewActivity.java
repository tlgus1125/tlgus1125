package com.tlgus1125.pedometerapp.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tlgus1125.pedometerapp.R;
import com.tlgus1125.pedometerapp.baseinfomation.StepCount;
import com.tlgus1125.pedometerapp.baseinfomation.Utils;
import com.tlgus1125.pedometerapp.database.DataBaseUtil;
import com.tlgus1125.pedometerapp.database.PedometerDataBase;
import com.tlgus1125.pedometerapp.location.MyLocation;
import com.tlgus1125.pedometerapp.service.SensorService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tlgus1125 on 2017-01-13.
 */

//만보기 정보 (걸음 수, 거리, 주소 등) 를 사용자에게 표시
public class StepViewActivity extends Activity {

    private Button mOkBtn = null;
    private EditText mStride = null;

    private MyLocation mLocation = null;
    private TextView mTvLocation = null;

    private Button mServiceBtn = null;
    private Intent mSensorServiceIntent = null;
    private BroadcastReceiver mReceiver = null;
    private TextView mStepCountText = null;
    private TextView mDistanceText = null;
    private String mServiceData = null;

    //DB
    private PedometerDataBase mPedometerDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        mOkBtn = (Button) findViewById(R.id.btn_setting);
        if(mOkBtn != null) {
            mOkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSetting();
                }
            });
        }
        mStride = (EditText) findViewById(R.id.stride);
        if(mStride != null)
            mStride.setText(String.valueOf(StepCount.StrideValue));

        mTvLocation = (TextView) findViewById(R.id.loaction);

        mLocation = MyLocation.getInstance();
        mLocation.setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 0:
                        String text = String.valueOf(msg.obj);
                        mTvLocation.setText(text);
                }
            }
        });

        mSensorServiceIntent = new Intent(this, SensorService.class);

        mReceiver = new SensorReceiver();
        mStepCountText = (TextView) findViewById(R.id.stepCount);
        mDistanceText = (TextView) findViewById(R.id.distance);

        mServiceBtn = (Button) findViewById(R.id.btn_start);
        if(mServiceBtn != null) {
            mServiceBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (StepCount.isSensorServiceRun == false) {
                        startSensor();
                    } else {
                        stopSensor();
                    }
                }
            });
        }
        SharedPreferences pref = getSharedPreferences("pedometer", MODE_PRIVATE);

        if(mStepCountText != null)
            mStepCountText.setText(String.valueOf(pref.getInt(Utils.SP_STEPCOUNT,0)));

        if(mDistanceText != null)
            mDistanceText.setText(String.valueOf(pref.getFloat(Utils.SP_DISTANCE,0)));

        if(pref.getBoolean(Utils.SP_SENSORSERVICE, false) == true)
            startSensor();
        else
            stopSensor();
    }

    public void startSetting(){
        StepCount.StrideValue = Double.parseDouble(mStride.getText().toString());
    }

    public void updateDataBase(){
        mPedometerDataBase = PedometerDataBase.getDBHelper(this);
        mPedometerDataBase.open();
        String cur_date = getCurrentDate();
        Cursor cursor = mPedometerDataBase.getMatchDay(cur_date);
        if(!mServiceData.equals("0")) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToNext();
                String day = cursor.getString(cursor.getColumnIndex(DataBaseUtil.CreateDB.DAY));
                String walkcount = cursor.getString(cursor.getColumnIndex(DataBaseUtil.CreateDB.STEPCOUNT));
                String distance = cursor.getString(cursor.getColumnIndex(DataBaseUtil.CreateDB.DISTANCE));
                double distanceValue = Double.parseDouble(String.format("%.2f",Double.parseDouble(distance) + Integer.parseInt(mServiceData) * StepCount.StrideValue));
                mPedometerDataBase.updateColumn(day, String.valueOf(Integer.parseInt(walkcount) + Integer.parseInt(mServiceData)),
                        String.valueOf(distanceValue));
            } else {
                double distanceValue = Double.parseDouble(String.format("%.2f",Integer.parseInt(mServiceData) * StepCount.StrideValue));
                mPedometerDataBase.insertColumn(cur_date, mServiceData, String.valueOf(distanceValue));
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(mPedometerDataBase != null)
            mPedometerDataBase.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void startSensor(){
        mServiceBtn.setText("STOP");
        mServiceBtn.setBackgroundColor(Color.GRAY);
        try {
            StepCount.isSensorServiceRun = true;
            IntentFilter mainFilter = new IntentFilter("com.tlgus1125.pedometerapp");
            registerReceiver(mReceiver, mainFilter);
            startService(mSensorServiceIntent);

        } catch (Exception e) {
        }
    }

    public void stopSensor(){
        mServiceBtn.setText("START");
        mServiceBtn.setBackgroundColor(Color.RED);
        try {
            StepCount.isSensorServiceRun = false;
            unregisterReceiver(mReceiver);
            stopService(mSensorServiceIntent);
            updateDataBase();

            mServiceData = "0";
            mStepCountText.setText(mServiceData);
            mDistanceText.setText("0m");

            StepCount.Distance = 0.0;
            StepCount.Step = 0;

        } catch (Exception e) {
        }
    }

    public String getCurrentDate(){
        SimpleDateFormat CurDate = new SimpleDateFormat("yyyy-MM-DD", Locale.KOREA);
        return CurDate.format(new Date());
    }

    class SensorReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            mServiceData = intent.getStringExtra("serviceData");
            mStepCountText.setText(mServiceData);
            double distance = Utils.getDistanceValue(mServiceData);
            StepCount.Distance = distance;
            String isMeter = distance < 1000 ? "m" : "Km";
            mDistanceText.setText(String.valueOf(distance) + isMeter);
        }
    }
}
