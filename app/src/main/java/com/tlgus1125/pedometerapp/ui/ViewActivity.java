package com.tlgus1125.pedometerapp.ui;



import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.tlgus1125.pedometerapp.R;
import com.tlgus1125.pedometerapp.location.MyLocation;


/**
 * Created by tlgus1125 on 2017-01-13.
 */
public class ViewActivity extends Activity {

    private MyLocation mLocation = null;
    private TextView mTvLocation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mTvLocation = (TextView) findViewById(R.id.loaction);
        mLocation = new MyLocation();
        mLocation.initMyLocation(this, ViewActivity.this, mTvLocation);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
