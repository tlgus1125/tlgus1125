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
import android.widget.Toast;

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
    private static final int MSG_SETTING = 100;

    private MyLocation mLocation = null;
    private TextView mTvLocation = null;

    private Button mServiceBtn;
    private Intent mSensorServiceIntent;
    private BroadcastReceiver mReceiver;
    private boolean flag = true;
    private TextView mStepCountText;
    private String mServiceData;

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
        startActivityForResult(intent, MSG_SETTING);
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
                String walkcount = cursor.getString(cursor.getColumnIndex(DataBases.CreateDB.WALKCOUNT));
                String distance = cursor.getString(cursor.getColumnIndex(DataBases.CreateDB.DISTANCE));
                mDbOpenHelper.updateColumn(day, String.valueOf(Integer.parseInt(walkcount) + Integer.parseInt(mServiceData)),
                        String.valueOf(Double.parseDouble(distance) + Integer.parseInt(mServiceData) * SettingActivity.mStrideValue));
            } else {
                mDbOpenHelper.insertColumn(cur_date, mServiceData, String.valueOf(Integer.parseInt(mServiceData) * SettingActivity.mStrideValue));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MSG_SETTING){

        }
        super.onActivityResult(requestCode, resultCode, data);
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

        }
    }
}
