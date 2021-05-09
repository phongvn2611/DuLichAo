package com.example.virtualtravelapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.virtualtravelapp.model.DiaDanh;
import com.example.virtualtravelapp.model.Place;

import java.util.ArrayList;

public class DBSource {
    SQLiteDatabase db;
    DBManager manager;

    public DBSource(Context context) {
        manager = new DBManager(context);
        manager.openDataBase();
        db = manager.getWritableDatabase();
    }

    public ArrayList<DiaDanh> getDiaDanh(){
        ArrayList<DiaDanh> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            DiaDanh diaDanh = new DiaDanh();
            diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
            diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));

            list.add(diaDanh);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }

    public ArrayList<Place> getPlace(){
        ArrayList<Place> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_PLACE;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Place place = new Place();
            place.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            place.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            place.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            place.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            place.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            place.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

            list.add(place);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }

    public ArrayList<Place> getPlaceId(int id){
        ArrayList<Place> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_PLACE + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Place place = new Place();
            place.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            place.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            place.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            place.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            place.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            place.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

            list.add(place);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }

    public int editPlace (Place place){
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_IMAGE, place.getImage());
        return db.update(DBManager.TABLE_PLACE,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(place.getId())});
    }
}
