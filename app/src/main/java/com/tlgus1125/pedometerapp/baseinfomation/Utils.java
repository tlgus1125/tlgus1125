package com.tlgus1125.pedometerapp.baseinfomation;

/**
 * Created by tlgus1125 on 2017-01-14.
 */

public class Utils {

    public static double getDistanceValue(String strCount){
        int stepCount = Integer.parseInt(strCount);
        double distance = StepCount.StrideValue * stepCount;
        if(distance >= 1000.0){
            distance /= 1000;
        }
        return Double.parseDouble(String.format("%.1f", distance));
    }
}
