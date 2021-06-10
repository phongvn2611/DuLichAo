package com.example.virtualtravelapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.model.DiaDanh;

import java.util.ArrayList;

public class UpdateDiaDanhActivity extends AppCompatActivity {

    EditText edtName, edtLat, edtLng, edtImage, edtImageDetail1, edtImageDetail2,
            edtImageDetail3, edtImageDetail4, edtCity;
    Button btnSua, btnThoat;
    Spinner spnRegion, spnFavorite;
    DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatediadanh);
        this.setTitle("Sửa địa danh");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Mapping();
        ArrayList<String> regionList = new ArrayList<>();
        regionList.add("Miền Bắc");
        regionList.add("Miền Trung");
        regionList.add("Miền Nam");
        ArrayAdapter regionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, regionList);
        spnRegion.setAdapter(regionAdapter);
        ArrayList<String> favoriteList = new ArrayList<>();
        favoriteList.add("Thích");
        favoriteList.add("Không thích");
        ArrayAdapter favoriteAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, favoriteList);
        spnFavorite.setAdapter(favoriteAdapter);
        db = new DBManager(this);
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("ID_DiaDanh");
        getDiaDanh(id);
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDiaDanh(id);
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateDiaDanhActivity.this, AdminDiaDanhActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Mapping() {
        edtName = (EditText) findViewById(R.id.edtUpdateDiaDanhName);
        edtLat = (EditText) findViewById(R.id.edtUpdateDiaDanhLatitude);
        edtLng = (EditText) findViewById(R.id.edtUpdateDiaDanhLongtitude);
        edtImage = (EditText) findViewById(R.id.edtUpdateDiaDanhImage);
        edtImageDetail1 = (EditText) findViewById(R.id.edtUpdateDiaDanhImageDetail1);
        edtImageDetail2 = (EditText) findViewById(R.id.edtUpdateDiaDanhImageDetail2);
        edtImageDetail3 = (EditText) findViewById(R.id.edtUpdateDiaDanhImageDetail3);
        edtImageDetail4 = (EditText) findViewById(R.id.edtUpdateDiaDanhImageDetail4);
        edtCity = (EditText) findViewById(R.id.edtUpdateDiaDanhCity);
        spnRegion = (Spinner) findViewById(R.id.spnUpdateDiaDanhRegion);
        spnFavorite = (Spinner) findViewById(R.id.spnUpdateDiaDanhFavorite);
        btnSua = (Button) findViewById(R.id.btnUpdateDiaDanhSua);
        btnThoat = (Button) findViewById(R.id.btnUpdateDiaDanhThoat);
    }

    private void getDiaDanh(int id) {
        DiaDanh diaDanh = db.getDiaDanhByID(id);
        String[] latlng = diaDanh.getLatlng().split(";");
        edtName.setText(diaDanh.getNameDiaDanh());
        edtLng.setText(latlng[0]);
        edtLat.setText(latlng[1]);
        String[] image = diaDanh.getImDiaDanh().split(";");
        edtImage.setText(image[0]);
        edtImageDetail1.setText(image[1]);
        edtImageDetail2.setText(image[2]);
        edtImageDetail3.setText(image[3]);
        edtImageDetail4.setText(image[4]);
        edtCity.setText(diaDanh.getCity());
        switch (diaDanh.getRegions()) {
            case 1:
                spnRegion.setSelection(0);
                break;
            case 2:
                spnRegion.setSelection(1);
                break;
            case 3:
                spnRegion.setSelection(2);
                break;
        }
        switch (diaDanh.getFavotite()) {
            case 0:
                spnFavorite.setSelection(1);
                break;
            case 1:
                spnFavorite.setSelection(0);
                break;
        }
    }

    private void UpdateDiaDanh(int id) {
        String name = edtName.getText().toString().trim();
        String image = edtImage.getText().toString().trim();
        String imageDetail1 = edtImageDetail1.getText().toString().trim();
        String imageDetail2 = edtImageDetail2.getText().toString().trim();
        String imageDetail3 = edtImageDetail3.getText().toString().trim();
        String imageDetail4 = edtImageDetail4.getText().toString().trim();
        String city = edtCity.getText().toString().trim();
        String lat = edtLat.getText().toString().trim();
        String lng = edtLng.getText().toString().trim();
        String region = spnRegion.getSelectedItem().toString().trim();
        String favorite = spnFavorite.getSelectedItem().toString().trim();
        int regionValue = -1, favoriteValue = -1;
        if (name == "" || image == "" || imageDetail1 == "" || imageDetail2 == "" ||
            imageDetail3 == "" || imageDetail4 == "" || city == "" || lat == "" || lng == "") {
            Toast.makeText(UpdateDiaDanhActivity.this, "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
        else {
            String latlng = String.join(";", lng, lat);
            String img = String.join(";", image, imageDetail1, imageDetail2, imageDetail3, imageDetail4);
            switch (region) {
                case "Miền Bắc":
                    regionValue = 1;
                    break;
                case "Miền Trung":
                    regionValue = 2;
                    break;
                case "Miền Nam":
                    regionValue = 3;
                    break;
            }
            switch (favorite) {
                case "Thích":
                    favoriteValue = 1;
                    break;
                case "Không thích":
                    favoriteValue = 0;
                    break;
            }
            db.openDataBase();
            int result = db.editDiaDanh(id, name, img, latlng, regionValue, city, favoriteValue);
            if (result == 1) {
                Toast.makeText(UpdateDiaDanhActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateDiaDanhActivity.this, AdminDiaDanhActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(UpdateDiaDanhActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return true;
    }
}
