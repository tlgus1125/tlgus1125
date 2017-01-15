package com.tlgus1125.pedometerapp.ui;

import android.Manifest;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TabHost;

import com.tlgus1125.pedometerapp.R;
import com.tlgus1125.pedometerapp.baseinfomation.StepCount;
import com.tlgus1125.pedometerapp.baseinfomation.Utils;
import com.tlgus1125.pedometerapp.location.MyLocation;
import com.tlgus1125.pedometerapp.service.MiniModeService;

public class MainActivity extends ActivityGroup {
    private TabHost mTabHost = null;
    private final static String TAB1 = "만보기 화면";
    private final static String TAB2 = "만보기 기록";
    private MyLocation mLocation = null;
    private final static int GPSPERMISSION = 100;
    private static boolean mbUsePermission = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbUsePermission = true;
        initTabLayout();
        SharedPreferences pref = getSharedPreferences("pedometer", MODE_PRIVATE);
        StepCount.Step = pref.getInt(Utils.SP_STEPCOUNT, 0);
        StepCount.Distance = pref.getFloat(Utils.SP_DISTANCE, 0);
        StepCount.isSensorServiceRun = pref.getBoolean(Utils.SP_SENSORSERVICE, false);
        mLocation = MyLocation.getInstance();
        mLocation.initMyLocation(this);
    }

    @Override
    protected void onPause() {
        if(StepCount.isSensorServiceRun == true)
            startService(new Intent(this, MiniModeService.class));
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        stopService(new Intent(this, MiniModeService.class));
        if(mbUsePermission != false)
            checkPermission();
        super.onResume();
    }

    public void initTabLayout(){
        mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup(getLocalActivityManager());

        mTabHost.addTab(mTabHost.newTabSpec(TAB1).setIndicator(TAB1).setContent(new Intent(this, StepViewActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec(TAB2).setIndicator(TAB2).setContent(new Intent(this, StepRecordActivity.class)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        SharedPreferences pref = getSharedPreferences("pedometer", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Utils.SP_STEPCOUNT, StepCount.Step);
        editor.putFloat(Utils.SP_DISTANCE, Float.parseFloat(String.valueOf(StepCount.Distance)));
        editor.putBoolean(Utils.SP_SENSORSERVICE, StepCount.isSensorServiceRun);
        editor.commit();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        GPSPERMISSION);
            } else {
                if(mLocation != null)
                    mLocation.setRequestLocation();
            }
        } else {
            if(mLocation != null)
                mLocation.setRequestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            switch (requestCode) {
                case GPSPERMISSION:
                    if(grantResults.length > 0) {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                            if(mLocation != null) {
                                mLocation.setRequestLocation();
                            }
                        }
                        else{
                            mbUsePermission = false;
                        }
                    }
            }
        }
    }

}
