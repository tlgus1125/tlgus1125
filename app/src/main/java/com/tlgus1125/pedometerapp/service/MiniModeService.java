package com.tlgus1125.pedometerapp.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tlgus1125.pedometerapp.R;
import com.tlgus1125.pedometerapp.baseinfomation.StepCount;
import com.tlgus1125.pedometerapp.baseinfomation.Utils;

/**
 * Created by tlgus1125 on 2017-01-14.
 */

//미니모드 지원 하기 위한 service
public class MiniModeService extends Service{

    private TextView mMiniStepCount = null;
    private TextView mMiniDistance = null;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private View mMiniLayout = null;
    private BroadcastReceiver mMiniModeReceiver = null;

    private float START_X, START_Y;
    private int PREV_X, PREV_Y;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMiniLayout = mInflater.inflate(R.layout.minimode, null);
        mMiniLayout.setOnTouchListener(mViewTouchListener);
        mMiniLayout.setBackgroundColor(Color.WHITE);

        mMiniStepCount = (TextView) mMiniLayout.findViewById(R.id.mini_stepcount);
        mMiniDistance = (TextView) mMiniLayout.findViewById(R.id.mini_distance);

        if(mMiniStepCount != null)
            mMiniStepCount.setText(String.valueOf(StepCount.Step));
        if(mMiniDistance != null) {
            String isMeter = StepCount.Distance < 1000 ? "m" : "Km";
            mMiniDistance.setText(String.valueOf(StepCount.Distance)+isMeter);
        }

        mMiniModeReceiver = new MiniModeReceiver();

        mParams = new WindowManager.LayoutParams(
                300,
                200,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.CENTER;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mMiniLayout, mParams);

        startForeground(2, new Notification());
    }

    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    START_X = event.getRawX();
                    START_Y = event.getRawY();
                    PREV_X = mParams.x;
                    PREV_Y = mParams.y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x = (int)(event.getRawX() - START_X);
                    int y = (int)(event.getRawY() - START_Y);

                    mParams.x = PREV_X + x;
                    mParams.y = PREV_Y + y;

                    mWindowManager.updateViewLayout(mMiniLayout, mParams);
                    break;
            }

            return true;
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter mainFilter = new IntentFilter("com.tlgus1125.pedometerapp");
        registerReceiver(mMiniModeReceiver, mainFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if(mWindowManager != null) {
            if(mMiniLayout != null) mWindowManager.removeView(mMiniLayout);
        }
        unregisterReceiver(mMiniModeReceiver);
        super.onDestroy();
    }

    public class MiniModeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String stepCount = intent.getStringExtra("serviceData");
            mMiniStepCount.setText(stepCount);
            double distance = Utils.getDistanceValue(stepCount);
            String isMeter = distance < 1000 ? "m" : "Km";
            mMiniDistance.setText(String.valueOf(distance) + isMeter);
        }
    }
}
