package com.tlgus1125.pedometerapp.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tlgus1125.pedometerapp.baseinfomation.StepInfo;

import java.util.ArrayList;
import com.tlgus1125.pedometerapp.R;
/**
 * Created by tlgus1125 on 2017-01-13.
 */

public class CustomAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<StepInfo> mList;
    private ViewHolder mHolder;

    public CustomAdapter(Context c, ArrayList<StepInfo> list){
        mInflater = LayoutInflater.from(c);
        mList = list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null){
            mHolder = new ViewHolder();
            v = mInflater.inflate(R.layout.list_item, null);
            mHolder.day = (TextView) v.findViewById(R.id.day);
            mHolder.stepcount = (TextView) v.findViewById(R.id.walkcount);
            mHolder.distance = (TextView) v.findViewById(R.id.distance);
            v.setTag(mHolder);
        }
        else{
            mHolder = (ViewHolder)v.getTag();
        }

        mHolder.day.setText(mList.get(position).day);
        mHolder.stepcount.setText(mList.get(position).stepcount);
        mHolder.distance.setText(mList.get(position).distance);

        return v;
    }

    class ViewHolder{
        TextView day;
        TextView stepcount;
        TextView distance;
    }
}
