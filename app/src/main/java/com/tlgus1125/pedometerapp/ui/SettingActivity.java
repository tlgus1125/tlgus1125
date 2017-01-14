package com.tlgus1125.pedometerapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tlgus1125.pedometerapp.R;
/**
 * Created by tlgus1125 on 2017-01-13.
 */

public class SettingActivity extends Activity{

    private Button mOkBtn = null;
    private EditText mStride = null;
    public static double mStrideValue = 0.6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mOkBtn = (Button) findViewById(R.id.ok);
        mStride = (EditText) findViewById(R.id.stride);
        if(mStride != null)
            mStride.setText(String.valueOf(mStrideValue));
        if(mOkBtn != null){
            mOkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishActivity();
                }
            });
        }
    }

    public void finishActivity(){
        Intent intent = new Intent();
        if(mStride != null) {
            intent.putExtra("STRIDE", mStride.getText().toString());
            mStrideValue = Double.parseDouble(mStride.getText().toString());
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
