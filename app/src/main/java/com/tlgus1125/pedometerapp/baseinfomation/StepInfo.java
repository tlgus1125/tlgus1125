package com.tlgus1125.pedometerapp.baseinfomation;

/**
 * Created by tlgus1125 on 2017-01-13.
 */

//리스트 뷰에 저장 할 걸음 정보 관련 class
public class StepInfo {
    public String day;
    public String stepcount;
    public String distance;

    public StepInfo(){}
    public StepInfo(String day, String stepcount, String distance){
        this.day = day;
        this.stepcount = stepcount;
        this.distance = distance;
    }
}
