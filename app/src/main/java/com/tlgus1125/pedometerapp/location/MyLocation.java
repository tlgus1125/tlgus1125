package com.tlgus1125.pedometerapp.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

/**
 * Created by tlgus1125 on 2017-01-13.
 */

public class MyLocation {

    private NaverMapAPI mNaverMapAPIAPI = null;
    private LocationManager mLocationManager = null;
    private LocationListener mLocationListener = null;

    private String m_strLocation = null;
    private TextView mTextView = null;
    private Context context = null;

    private Handler mHandler = null;

    public static MyLocation mInstance = null;
    public static MyLocation getInstance(){
        if(mInstance == null)
            mInstance = new MyLocation();
        return mInstance;
    }

    public void initMyLocation(Context context) {
        this.context = context;
        mNaverMapAPIAPI = new NaverMapAPI();
        initLocationManager();
    }

    public void setRequestLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
    }

    public void initLocationManager(){
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {

                if (location != null)
                    setLocation(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    public void setLocation(final double latitude, final double longitude){

        new Thread(new Runnable() {
            @Override
            public void run() {
                m_strLocation = mNaverMapAPIAPI.getLocation(latitude, longitude);
                if(mHandler != null) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.obj = m_strLocation;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void setHandler(Handler handler){
        mHandler = handler;
    }
}
