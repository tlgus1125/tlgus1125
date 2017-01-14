package com.tlgus1125.pedometerapp.ui;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import com.tlgus1125.pedometerapp.R;
import com.tlgus1125.pedometerapp.baseinfomation.StepCount;

public class MainActivity extends ActivityGroup {
    private TabHost mTabHost = null;
    private final static String TAB1 = "만보기 화면";
    private final static String TAB2 = "만보기 기록";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTabLayout();
        StepCount.Step = 0;
    }



    public void initTabLayout(){
        mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup(getLocalActivityManager());

        mTabHost.addTab(mTabHost.newTabSpec(TAB1).setIndicator(TAB1).setContent(new Intent(this, StepViewActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec(TAB2).setIndicator(TAB2).setContent(new Intent(this, StepRecordActivity.class)));
    }


}
