package com.example.virtualtravelapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.virtualtravelapp.model.DiaDanh;
import com.example.virtualtravelapp.model.Experience;
import com.example.virtualtravelapp.model.Hotel;
import com.example.virtualtravelapp.model.Introduce;
import com.example.virtualtravelapp.model.Place;
import com.example.virtualtravelapp.model.Restaurant;
import com.example.virtualtravelapp.model.User;
import com.example.virtualtravelapp.model.Vehicle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dulich.sqlite";
    public static final int DATABASE_VERSION = 12;
    public static final String TABLE_DIADANH = "diadanh";

    public static final String COLUMN_ID_DIADANH = "ID_DIADANH";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_IMAGE = "IMAGE";
    public static final String COLUMN_IMAGE_INT = "IMAGE_INT";
    public static final String COLUMN_LATLNG = "LATLNG";
    public static final String COLUMN_REGIONS = "REGIONS";
    public static final String COLUMN_CITY = "CITY";
    public static final String COLUMN_FAVORITE = "FAVORITE";

    public static final String TABLE_PLACE = "diemdi";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DETAIL = "DETAIL";

    public static final String TABLE_INTRODUCE = "gioithieu";
    public static final String COLUMN_INTRODUCE = "INTRODUCE";

    public static final String TABLE_VEHICLE = "phuongtien";
    public static final String TABLE_HOTEL = "nhanghi";
    public static final String COLUMN_PHONE = "PHONE";
    public static final String COLUMN_PRICE = "PRICE";

    public static final String TABLE_RESTAURANT = "doan";
    public static final String TABLE_TOUR = "lichtrinh";
    public static final String TABLE_EXPERIENCE = "kinhnghiem";
    public static final String COLUMN_DAY = "DAY";

    public static final String TABLE_USER = "nguoidung";
    public static final String TABLE_GROUP = "nhom";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_ID_GROUP = "ID_GROUP";

    private static final String DB_PATH_SUFFIX = "/databases/";

    static Context context;
    //	String pathDatabase = "";
    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void CopyDataBaseFromAsset() throws IOException {

        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = getDatabasePath();

        // if the path doesn't exist first, create it
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    private static String getDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public ArrayList<User> getUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<User> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_USER;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = new User();
            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            user.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PASSWORD)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
            user.setIdGroup(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_GROUP))));

            list.add(user);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        User user = new User();
        String sql = "SELECT * FROM " + DBManager.TABLE_USER + " WHERE " +
                DBManager.COLUMN_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            user.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PASSWORD)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
            user.setIdGroup(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_GROUP))));
        }
        return user;
    }

    public int addUser(String name, String username, String password, String email, String phone, int idGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_NAME, name);
        values.put(DBManager.COLUMN_USERNAME, username);
        values.put(DBManager.COLUMN_PASSWORD, password);
        values.put(DBManager.COLUMN_EMAIL, email);
        values.put(DBManager.COLUMN_PHONE, phone);
        values.put(DBManager.COLUMN_ID_GROUP, idGroup);
        long result = db.insert(DBManager.TABLE_USER, null, values);
        if (result == -1) {
            return 0;
        }
        else {
            return 1;
        }
    }

    public int editUser(String name, String username, String password, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_NAME, name);
        values.put(DBManager.COLUMN_USERNAME, username);
        values.put(DBManager.COLUMN_PASSWORD, password);
        values.put(DBManager.COLUMN_EMAIL, email);
        values.put(DBManager.COLUMN_PHONE, phone);
        String sql = "SELECT * FROM " + DBManager.TABLE_USER + " WHERE " +
                DBManager.COLUMN_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            long result = db.update(DBManager.TABLE_USER, values,
                    DBManager.COLUMN_USERNAME + " = ?", new String[] {username});
            if (result == -1) {
                return 0;
            }
            else {
                return 1;
            }
        }
        else {
            return 0;
        }
    }

    public int checkUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + DBManager.TABLE_USER + " WHERE " + DBManager.COLUMN_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public int checkLogin(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + DBManager.TABLE_USER + " WHERE " + DBManager.COLUMN_USERNAME + " = '" +
                username + "' AND " + DBManager.COLUMN_PASSWORD + " = '" + password +"'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public ArrayList<DiaDanh> getDiaDanh(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DiaDanh> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH;
        Cursor cursor = db.rawQuery(sql,null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idDiaDanh = cursor.getInt(0);
            String nameDiaDanh = cursor.getString(1);
            String imDiaDanh = cursor.getString(2);
            String latlng = cursor.getString(3);
            int regions = cursor.getInt(4);
            String city = cursor.getString(5);
            int favorite = cursor.getInt(6);
            String imageInt = cursor.getString(7);
            list.add(new DiaDanh(idDiaDanh, nameDiaDanh, imDiaDanh, imageInt,
                    latlng, city, regions, favorite));
        }
        return list;
    }

    public DiaDanh getDiaDanhByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        DiaDanh diaDanh = new DiaDanh();
        String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
            diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));
            diaDanh.setCity(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_CITY)));
            diaDanh.setFavotite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_FAVORITE))));
            diaDanh.setImage_int(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE_INT)));
        }
        return diaDanh;
    }
    public long editDiadanh (DiaDanh diaDanh){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_IMAGE_INT, diaDanh.getImage_int());
//		Log.d("image", diaDanh.getImDiaDanh());
        Log.d("editD", String.valueOf(db.update(DBManager.TABLE_DIADANH,values,DBManager.COLUMN_ID_DIADANH + " =?", new String[]{String.valueOf(diaDanh.getIdDiaDanh())})));
        long ketqua = db.update(DBManager.TABLE_DIADANH,values,DBManager.COLUMN_ID_DIADANH + " =?", new String[]{String.valueOf(diaDanh.getIdDiaDanh())});

        Log.v("InsertDatabase","Ket qua :" + ketqua);

        return ketqua;
    }

    public int editDiaDanh(int id, String name, String image, String latlng, int region, String city, int favorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_ID_DIADANH, id);
        values.put(DBManager.COLUMN_NAME, name);
        values.put(DBManager.COLUMN_IMAGE, image);
        values.put(DBManager.COLUMN_LATLNG, latlng);
        values.put(DBManager.COLUMN_REGIONS, region);
        values.put(DBManager.COLUMN_CITY, city);
        values.put(DBManager.COLUMN_FAVORITE, favorite);
        String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            long result = db.update(DBManager.TABLE_DIADANH, values,
                    DBManager.COLUMN_ID_DIADANH + " = ?", new String[] {String.valueOf(id)});
            if (result == -1) {
                return 0;
            }
            else {
                return 1;
            }
        }
        else {
            return 0;
        }
    }

    public int addDiaDanh(String name, String image, String latlng, int region, String city, int favorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_NAME, name);
        values.put(DBManager.COLUMN_IMAGE, image);
        values.put(DBManager.COLUMN_LATLNG, latlng);
        values.put(DBManager.COLUMN_REGIONS, region);
        values.put(DBManager.COLUMN_CITY, city);
        values.put(DBManager.COLUMN_FAVORITE, favorite);
        long result = db.insert(DBManager.TABLE_DIADANH, null, values);
        if (result == -1) {
            return 0;
        }
        else {
            return 1;
        }
    }

    public int deleteDiaDanh(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(DBManager.TABLE_DIADANH, DBManager.COLUMN_ID_DIADANH + " = " + id, null);
        if (result == 0) {
            return 0;
        }
        else {
            return 1;
        }
    }

    //chuc nang favorite dia danh
    public int editFavorite (DiaDanh diaDanh){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_FAVORITE, diaDanh.getFavotite());
//		Log.d("image", diaDanh.getImDiaDanh());
        Log.d("--editFavotite", String.valueOf(db.update(DBManager.TABLE_DIADANH,values,DBManager.COLUMN_ID_DIADANH + " =?", new String[]{String.valueOf(diaDanh.getIdDiaDanh())})));
        return db.update(DBManager.TABLE_DIADANH,values,DBManager.COLUMN_ID_DIADANH + " =?", new String[]{String.valueOf(diaDanh.getIdDiaDanh())});
    }
    public ArrayList<DiaDanh> getDiaDanhID(int regions){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DiaDanh> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH + " WHERE " + DBManager.COLUMN_REGIONS + " = " + regions + " ORDER BY " + COLUMN_NAME;
        Cursor cursor = db.rawQuery(sql,null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idDiaDanh = cursor.getInt(0);
            String nameDiaDanh = cursor.getString(1);
            String imDiaDanh = cursor.getString(2);
            String latlng = cursor.getString(3);
            int region = cursor.getInt(4);
            String city = cursor.getString(5);
            int favorite = cursor.getInt(6);
            String imageInt = cursor.getString(7);
            list.add(new DiaDanh(idDiaDanh, nameDiaDanh, imDiaDanh, imageInt,
                    latlng, city, regions, favorite));
        }
        return list;
    }

    public ArrayList<DiaDanh> getFavorite(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DiaDanh> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH + " WHERE " + DBManager.COLUMN_FAVORITE + " = 1 "  + " ORDER BY " + COLUMN_NAME;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            DiaDanh diaDanh = new DiaDanh();
            diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
            diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            diaDanh.setImage_int(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE_INT)));
            diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            diaDanh.setCity(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_CITY)));
            diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));
            diaDanh.setFavotite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_FAVORITE))));

            list.add(diaDanh);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }

    public ArrayList<DiaDanh> searchFavorite(String s, int favorite){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DiaDanh> list = new ArrayList<>();
        String sql = "SELECT " + COLUMN_ID_DIADANH + ", " + COLUMN_NAME + ", " + COLUMN_IMAGE + ", " + COLUMN_LATLNG + ", " + COLUMN_REGIONS + ", " + COLUMN_CITY + ", " + COLUMN_FAVORITE
                + " FROM "+ TABLE_DIADANH + " WHERE " + COLUMN_NAME + " LIKE '%" + s + "%'" + " AND " + COLUMN_FAVORITE + " = " + favorite;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            DiaDanh diaDanh = new DiaDanh();
            diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
            diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            diaDanh.setImage_int(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE_INT)));
            diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));
            diaDanh.setFavotite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_FAVORITE))));
            diaDanh.setCity(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_CITY)));

            list.add(diaDanh);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }
    public DiaDanh getDiaDanhDetail(int id_diadanh){
        SQLiteDatabase db = this.getWritableDatabase();
        DiaDanh diaDanh = new DiaDanh();
        String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id_diadanh;
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
            diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            diaDanh.setImage_int(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE_INT)));
            diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            diaDanh.setCity(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_CITY)));
            diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));
            diaDanh.setFavotite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_FAVORITE))));

        }
        Log.d("ketqua", String.valueOf(diaDanh));
        return diaDanh;
    }


    public ArrayList<DiaDanh> searchDiaDanh(String s, int region){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DiaDanh> list = new ArrayList<>();
//		String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH;
        String sql = "SELECT " + COLUMN_ID_DIADANH + ", " + COLUMN_NAME + ", " + COLUMN_IMAGE + ", " + COLUMN_LATLNG + ", " + COLUMN_REGIONS + ", " + COLUMN_CITY + ", " + COLUMN_FAVORITE + ", " + COLUMN_IMAGE_INT
                + " FROM "+ TABLE_DIADANH + " WHERE " + COLUMN_NAME + " LIKE '%" + s + "%'" + " AND " + COLUMN_REGIONS + " = " + region;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            DiaDanh diaDanh = new DiaDanh();
            diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
            diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            diaDanh.setImage_int(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE_INT)));
            diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));
            diaDanh.setFavotite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_FAVORITE))));
            diaDanh.setCity(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_CITY)));

            list.add(diaDanh);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }
    public ArrayList<Place> getPlace(){
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Place> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_PLACE + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id  + " ORDER BY " + COLUMN_NAME;
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
    public Place getPlaceDetail(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + DBManager.TABLE_PLACE + " WHERE " + DBManager.COLUMN_ID + " = " + id;
        Place place = new Place();
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            place.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            place.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            place.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            place.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            place.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            place.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
        }
        Log.d("ketqua", String.valueOf(place));
        return place;
    }

    public ArrayList<Place> searchPlace(String s, int id_diadanh){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Place> list = new ArrayList<>();
        String sql = "SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_IMAGE + ", " + COLUMN_LATLNG + ", " + COLUMN_DETAIL + ", " + COLUMN_ID_DIADANH
                + " FROM "+ TABLE_PLACE + " WHERE " + COLUMN_NAME + " LIKE '%" + s + "%'"+ " AND " + COLUMN_ID_DIADANH + " = " + id_diadanh;
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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_IMAGE, place.getImage());
        Log.d("image", place.getImage());
        Log.d("editP", String.valueOf(db.update(DBManager.TABLE_PLACE,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(place.getId())})));
        return db.update(DBManager.TABLE_PLACE,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(place.getId())});
    }

    //vehicle


    public ArrayList<Vehicle> getVedicle(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_VEHICLE;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Vehicle vehicle = new Vehicle();
            vehicle.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            vehicle.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            vehicle.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            vehicle.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            vehicle.setId_diadanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

            list.add(vehicle);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }

    public ArrayList<Vehicle> getVedicleID(int id_diadanh){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_VEHICLE + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id_diadanh;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Vehicle vehicle = new Vehicle();
            vehicle.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            vehicle.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            vehicle.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            vehicle.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            vehicle.setId_diadanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

            list.add(vehicle);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }

    public Vehicle getVedicleDetail(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + DBManager.TABLE_VEHICLE + " WHERE " + DBManager.COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql,null);
        Vehicle vehicle = new Vehicle();
        if (cursor.moveToFirst()){
            vehicle.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            vehicle.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            vehicle.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            vehicle.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            vehicle.setId_diadanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

        }
        return vehicle;
    }


    public int editVehicle (Vehicle vehicle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_IMAGE, vehicle.getImage());
        Log.d("image", vehicle.getImage());
        Log.d("editV", String.valueOf(db.update(DBManager.TABLE_VEHICLE,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(vehicle.getId())})));
        return db.update(DBManager.TABLE_VEHICLE,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(vehicle.getId())});
    }

    //hotel

    public ArrayList<Hotel> getHotel(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Hotel> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_HOTEL;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Hotel hotel = new Hotel();
            hotel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            hotel.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            hotel.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            hotel.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            hotel.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
            hotel.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            hotel.setPrice(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PRICE)));
            hotel.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

            list.add(hotel);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }

    public ArrayList<Hotel> getHotelID(int id_diadanh){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Hotel> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_HOTEL + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id_diadanh;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Hotel hotel = new Hotel();
            hotel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            hotel.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            hotel.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            hotel.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            hotel.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
            hotel.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            hotel.setPrice(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PRICE)));
            hotel.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

            list.add(hotel);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }

    public Hotel getHotelDetail(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + DBManager.TABLE_HOTEL + " WHERE " + DBManager.COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql,null);
        Hotel hotel = new Hotel();
        if (cursor.moveToFirst()){
            hotel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            hotel.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            hotel.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            hotel.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            hotel.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
            hotel.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            hotel.setPrice(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PRICE)));
            hotel.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
        }
        return hotel;
    }
    public int editHotel (Hotel hotel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_IMAGE, hotel.getImage());
        Log.d("image", hotel.getImage());
        Log.d("editH", String.valueOf(db.update(DBManager.TABLE_HOTEL,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(hotel.getId())})));
        return db.update(DBManager.TABLE_HOTEL,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(hotel.getId())});
    }

    public ArrayList<Hotel> searchHotel(String s, int id_diadanh){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Hotel> list = new ArrayList<>();
        String sql = "SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_IMAGE + ", " + COLUMN_LATLNG + ", " + COLUMN_PHONE + ", " + COLUMN_DETAIL + ", " + COLUMN_ID_DIADANH + ", " + COLUMN_PRICE
                + " FROM "+ TABLE_HOTEL + " WHERE " + COLUMN_NAME + " LIKE '%" + s + "%'" + " AND " + COLUMN_ID_DIADANH + " = " + id_diadanh;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Hotel hotel = new Hotel();
            hotel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            hotel.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            hotel.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            hotel.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            hotel.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
            hotel.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            hotel.setPrice(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PRICE)));
            hotel.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

            list.add(hotel);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }
    //get resstaurant
    public ArrayList<Restaurant> getRestaurant(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Restaurant> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_RESTAURANT;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Restaurant restaurant = new Restaurant();
            restaurant.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            restaurant.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            restaurant.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            restaurant.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            restaurant.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
            restaurant.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            restaurant.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

            list.add(restaurant);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }
    public ArrayList<Restaurant> getRestaurantId(int id_diadanh){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Restaurant> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_RESTAURANT + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id_diadanh;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Restaurant restaurant = new Restaurant();
            restaurant.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            restaurant.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            restaurant.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            restaurant.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            restaurant.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
            restaurant.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            restaurant.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

            list.add(restaurant);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }

    public Restaurant getRestaurantDetail(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + DBManager.TABLE_RESTAURANT + " WHERE " + DBManager.COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql,null);
        Restaurant restaurant = new Restaurant();
        if (cursor.moveToFirst()){
            restaurant.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            restaurant.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            restaurant.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            restaurant.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            restaurant.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
            restaurant.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            restaurant.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

        }
        return restaurant;
    }

    public ArrayList<Restaurant> searchRestaurant(String s, int id_diadanh){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Restaurant> list = new ArrayList<>();
        String sql = "SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_IMAGE + ", " + COLUMN_LATLNG + ", " + COLUMN_PHONE + ", " + COLUMN_DETAIL + ", " + COLUMN_ID_DIADANH
                + " FROM "+ TABLE_RESTAURANT + " WHERE " + COLUMN_NAME + " LIKE '%" + s + "%'" + " AND " + COLUMN_ID_DIADANH + " = " + id_diadanh;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Restaurant restaurant = new Restaurant();
            restaurant.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            restaurant.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            restaurant.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            restaurant.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
            restaurant.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
            restaurant.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
            restaurant.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

            list.add(restaurant);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }
    public int editRestaurant (Restaurant restaurant){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBManager.COLUMN_IMAGE, restaurant.getImage());
        Log.d("image", restaurant.getImage());
        Log.d("editR", String.valueOf(db.update(DBManager.TABLE_RESTAURANT,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(restaurant.getId())})));
        return db.update(DBManager.TABLE_RESTAURANT,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(restaurant.getId())});
    }

    public Introduce getIntroID(int id_diadanh){
        SQLiteDatabase db = this.getWritableDatabase();
        Introduce introduce = new Introduce();
        String sql = "SELECT * FROM " + DBManager.TABLE_INTRODUCE + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id_diadanh;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            introduce.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            introduce.setIntro(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_INTRODUCE)));
            introduce.setId_diadanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
            cursor.moveToNext();
        }
        return introduce;
    }

    public ArrayList<Experience> getExperience(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Experience> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_EXPERIENCE;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Experience experience = new Experience();
            experience.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            experience.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            experience.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            experience.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));

            list.add(experience);
            cursor.moveToNext();
        }
        Log.d("ketqua", String.valueOf(list));
        return list;
    }

    public Experience getExperienceDetail(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Experience> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBManager.TABLE_EXPERIENCE + " WHERE " + COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql,null);
        Experience experience = new Experience();
        if (cursor.moveToFirst()){
            experience.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
            experience.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
            experience.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
            experience.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));

        }
        return experience;
    }
}
