package com.tlgus1125.pedometerapp.database;

/**
 * Created by tlgus1125 on 2017-01-13.
 */

//DB 생성 쿼리 및 column 값 선언
public class DataBaseUtil {
    public static final class CreateDB{
        public static final String DAY = "day";
        public static final String STEPCOUNT = "stepcount";
        public static final String DISTANCE = "distance";
        public static final String _TABLENAME = "mytable";
        public static final String _CREATE =
                "create table "+_TABLENAME+"("
                        +"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        +DAY+" text not null, "
                        +STEPCOUNT+" text not null ,"
                        +DISTANCE+" text not null );";
    }
}
