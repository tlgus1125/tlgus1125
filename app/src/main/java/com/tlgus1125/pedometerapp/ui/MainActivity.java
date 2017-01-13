package com.tlgus1125.pedometerapp.ui;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.tlgus1125.pedometerapp.R;

public class MainActivity extends ActivityGroup {
    private TabHost mTabHost = null;
    private final static String TAB1 = "만보기 화면";
    private final static String TAB2 = "만보기 기록";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabLayout();
    }

    public void initTabLayout(){
        mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup(getLocalActivityManager());

        mTabHost.addTab(mTabHost.newTabSpec(TAB1).setIndicator(TAB1).setContent(new Intent(this, ViewActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec(TAB2).setIndicator(TAB2).setContent(new Intent(this, RecordActivity.class)));
    }
}
