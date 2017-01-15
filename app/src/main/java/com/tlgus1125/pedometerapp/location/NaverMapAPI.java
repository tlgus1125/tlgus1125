package com.tlgus1125.pedometerapp.location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tlgus1125 on 2017-01-13.
 */

//naver map API 사용 하여 현재 위치 get
public class NaverMapAPI {
    String clientId = "2DvgPUKo22YS_N7fxlw6";//애플리케이션 클라이언트 아이디값";
    String clientSecret = "7BjkVl9vib";//애플리케이션 클라이언트 시크릿값";

    public String getLocation(double x, double y) {
        try {
            String apiURL = "https://openapi.naver.com/v1/map/reversegeocode?encoding=utf-8&coordType=latlng&query=" + y +","+ x; //json
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            JSONObject jObject = new JSONObject(response.toString());
            String result = jObject.getString("result");
            JSONObject jObject_result = new JSONObject(result);
            JSONArray jArr_items = new JSONArray(jObject_result.getString("items"));
            String address = ((JSONObject) jArr_items.get(0)).getString("address");

            return address;

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
