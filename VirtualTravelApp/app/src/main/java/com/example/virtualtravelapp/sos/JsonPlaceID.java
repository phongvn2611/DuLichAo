package com.example.virtualtravelapp.sos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonPlaceID {
    String placeID = null;
    ArrayList<String> listPlaceID;
    HashMap<String, String> sosDetail;

    HashMap<String, String> atm;
    HashMap<String, String> gas_station;
    String name = null;
    String vicinity = null;
    String lat = null;
    String lng = null;

    String formatted_phone_number = "";
    public ArrayList<String> placeID (JSONObject jsonObject){
        listPlaceID = new ArrayList<>();
        try {
            JSONArray jResults = jsonObject.getJSONArray("results");
            for (int i = 0; i < jResults.length(); i++){
                JSONObject object = jResults.getJSONObject(i);
                placeID = object.getString("place_id");
                listPlaceID.add(placeID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listPlaceID;
    }

    public HashMap<String,String> sosDetail (JSONObject jsonObject){
        sosDetail = new HashMap<>();
        try {
            JSONObject result = jsonObject.getJSONObject("result");
            String formatted_address = result.optString("formatted_address", "");
            formatted_phone_number = result.optString("formatted_phone_number", "");
            String name = result.optString("name","");
            JSONObject geometry = result.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            String lat = location.optString("lat","");
            String lng = location.optString("lng","");

            sosDetail.put("formatted_address", formatted_address);
            if (!formatted_phone_number.equals("")) {
                sosDetail.put("formatted_phone_number", formatted_phone_number);
            }
            sosDetail.put("name", name);
            sosDetail.put("lat", lat);
            sosDetail.put("lng", lng);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sosDetail;
    }

    public ArrayList<HashMap<String,String>> getATM (JSONObject jsonObject){
        JSONArray jResults = null;
        ArrayList<HashMap<String,String>> listATM = new ArrayList<>();
        try {
            jResults = jsonObject.getJSONArray("results");
            for (int i = 0; i < jResults.length(); i++){
                atm = new HashMap<>();
                JSONObject object = jResults.getJSONObject(i);
                name = object.optString("name","");
                vicinity = object.optString("vicinity","");
                JSONObject geometry = object.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                lat = location.optString("lat","");
                lng = location.optString("lng","");

                atm.put("name", name);
                atm.put("vicinity", vicinity);
                atm.put("lat", lat);
                atm.put("lng", lng);
                listATM.add(atm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listATM;
    }

    public ArrayList<HashMap<String,String>> gasStation (JSONObject jsonObject){
        ArrayList<HashMap<String,String>> listGAS = new ArrayList<>();
        JSONArray jResults = null;
        try {
            jResults = jsonObject.getJSONArray("results");
            for (int i = 0; i < jResults.length(); i++){
                gas_station = new HashMap<>();
                JSONObject object = jResults.getJSONObject(i);
                name = object.optString("name","");
                vicinity = object.optString("vicinity","");
                JSONObject geometry = object.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                lat = location.optString("lat","");
                lng = location.optString("lng","");

                gas_station.put("name", name);
                gas_station.put("vicinity", vicinity);
                gas_station.put("lat", lat);
                gas_station.put("lng", lng);
                listGAS.add(gas_station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGAS;
    }
}
