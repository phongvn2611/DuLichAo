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

import java.util.ArrayList;

public class InsertDiaDanhActivity extends AppCompatActivity {

    EditText edtName, edtLat, edtLng, edtImage, edtImageDetail1, edtImageDetail2,
            edtImageDetail3, edtImageDetail4, edtCity;
    Button btnThem, btnThoat;
    Spinner spnRegion;
    DBManager db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertdiadanh);
        this.setTitle("Thêm địa danh");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Mapping();
        ArrayList<String> regionList = new ArrayList<>();
        regionList.add("Miền Bắc");
        regionList.add("Miền Trung");
        regionList.add("Miền Nam");
        ArrayAdapter regionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, regionList);
        spnRegion.setAdapter(regionAdapter);
        db = new DBManager(this);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertDiaDanh();
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsertDiaDanhActivity.this, AdminDiaDanhActivity.class));
            }
        });
    }

    private void Mapping() {
        edtName = (EditText) findViewById(R.id.edtInsertDiaDanhName);
        edtLat = (EditText) findViewById(R.id.edtInsertDiaDanhLatitude);
        edtLng = (EditText) findViewById(R.id.edtInsertDiaDanhLongtitude);
        edtImage = (EditText) findViewById(R.id.edtInsertDiaDanhImage);
        edtImageDetail1 = (EditText) findViewById(R.id.edtInsertDiaDanhImageDetail1);
        edtImageDetail2 = (EditText) findViewById(R.id.edtInsertDiaDanhImageDetail2);
        edtImageDetail3 = (EditText) findViewById(R.id.edtInsertDiaDanhImageDetail3);
        edtImageDetail4 = (EditText) findViewById(R.id.edtInsertDiaDanhImageDetail4);
        edtCity = (EditText) findViewById(R.id.edtInsertDiaDanhCity);
        spnRegion = (Spinner) findViewById(R.id.spnInsertDiaDanhRegion);
        btnThem = (Button) findViewById(R.id.btnInsertDiaDanhThem);
        btnThoat = (Button) findViewById(R.id.btnInsertDiaDanhThoat);
    }

    private void InsertDiaDanh() {
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
        int regionValue = -1;
        if (name == "" || image == "" || imageDetail1 == "" || imageDetail2 == "" ||
                imageDetail3 == "" || imageDetail4 == "" || city == "" || lat =="" || lng == "") {
            Toast.makeText(InsertDiaDanhActivity.this, "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
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
            db.openDataBase();
            int result = db.addDiaDanh(name, img, latlng, regionValue, city, 0);
            if (result == 1) {
                Toast.makeText(InsertDiaDanhActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(InsertDiaDanhActivity.this, AdminDiaDanhActivity.class));
            }
            else {
                Toast.makeText(InsertDiaDanhActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
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
