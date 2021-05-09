package com.example.virtualtravelapp.widget;

import com.example.virtualtravelapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class PareJSON {
    public static int temp;
    public static String city;
    public static String text;
    public static String country;
    public static int code;
    ArrayList<HashMap<String,String>> listWeather;
    public ArrayList<HashMap<String,String>> parse(JSONObject jsonObject){
        listWeather = new ArrayList<>();
        try {
            //dinh dang so thap phan
            DecimalFormat df = new DecimalFormat("#");

            JSONObject jQuery = jsonObject.getJSONObject("query");
            JSONObject jResult = jQuery.getJSONObject("results");
            JSONObject jChanel = jResult.getJSONObject("channel");

            JSONObject jLocation = jChanel.getJSONObject("location");
            city = jLocation.getString("city");
            country = jLocation.getString("country");
            JSONObject jItem = jChanel.getJSONObject("item");

            JSONObject jCondition = jItem.getJSONObject("condition");
            temp = Integer.parseInt(df.format((jCondition.getDouble("temp") - 32) / 1.8));
            text = jCondition.getString("text");
            code = jCondition.getInt("code");
            HashMap<String,String> firt = new HashMap<>();
            firt.put("city",city);
            firt.put("country", country);
            firt.put("temp", String.valueOf(temp));
            firt.put("text", text);
            firt.put("code", String.valueOf(code));
            listWeather.add(firt);
            JSONArray jForecast = jItem.getJSONArray("forecast");
            for (int i = 0; i < jForecast.length(); i++){
                JSONObject jDetail = jForecast.getJSONObject(i);
                int code = jDetail.getInt("code");
                String date = jDetail.getString("date");
                String day = jDetail.getString("day");
                int high = Integer.parseInt(df.format((jDetail.getDouble("high") - 32) / 1.8));
                int low = Integer.parseInt(df.format((jDetail.getDouble("low") - 32) / 1.8 ));
                String text = jDetail.getString("text");

                HashMap<String,String> weather = new HashMap<>();
                weather.put("code", String.valueOf(code));
                weather.put("date", date);
                weather.put("day", day);
                weather.put("high", String.valueOf(high));
                weather.put("low", String.valueOf(low));
                weather.put("text", text);

                //add weather
                listWeather.add(weather);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listWeather;
    }

    public static int getIconIdFromCode(int code) {
        switch (code) {
            case 0:
                return R.drawable.ic_0;
            case 1:
                return R.drawable.ic_1;
            case 2:
                return R.drawable.ic_2;
            case 3:
                return R.drawable.ic_3;
            case 4:
                return R.drawable.ic_4;
            case 5:
                return R.drawable.ic_5;
            case 6:
                return R.drawable.ic_6;
            case 7:
                return R.drawable.ic_7;
            case 8:
                return R.drawable.ic_8;
            case 9:
                return R.drawable.ic_9;
            case 10:
                return R.drawable.ic_10;
            case 11:
                return R.drawable.ic_11;
            case 12:
                return R.drawable.ic_12;
            case 13:
                return R.drawable.ic_13;
            case 14:
                return R.drawable.ic_14;
            case 15:
                return R.drawable.ic_15;
            case 16:
                return R.drawable.ic_16;
            case 17:
                return R.drawable.ic_17;
            case 18:
                return R.drawable.ic_18;
            case 19:
                return R.drawable.ic_19;
            case 20:
                return R.drawable.ic_20;
            case 21:
                return R.drawable.ic_21;
            case 22:
                return R.drawable.ic_22;
            case 23:
                return R.drawable.ic_23;
            case 24:
                return R.drawable.ic_24;
            case 25:
                return R.drawable.ic_25;
            case 26:
                return R.drawable.ic_26;
            case 27:
                return R.drawable.ic_27;
            case 28:
                return R.drawable.ic_28;
            case 29:
                return R.drawable.ic_29;
            case 30:
                return R.drawable.ic_30;
            case 31:
                return R.drawable.ic_31;
            case 32:
                return R.drawable.ic_32;
            case 33:
                return R.drawable.ic_33;
            case 34:
                return R.drawable.ic_34;
            case 35:
                return R.drawable.ic_35;
            case 36:
                return R.drawable.ic_36;
            case 37:
                return R.drawable.ic_37;
            case 38:
                return R.drawable.ic_38;
            case 39:
                return R.drawable.ic_39;
            case 40:
                return R.drawable.ic_40;
            case 41:
                return R.drawable.ic_41;
            case 42:
                return R.drawable.ic_42;
            case 43:
                return R.drawable.ic_43;
            case 44:
                return R.drawable.ic_44;
            case 45:
                return R.drawable.ic_45;
            case 46:
                return R.drawable.ic_46;
            case 47:
                return R.drawable.ic_47;
            default:
                return R.drawable.na;
        }
    }
}
