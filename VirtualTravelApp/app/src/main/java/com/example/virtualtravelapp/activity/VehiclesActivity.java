package com.example.virtualtravelapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.adapter.VehiclesAdapter;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.model.Vehicle;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VehiclesActivity extends AppCompatActivity {
    @BindView(R.id.lvVehicles)
    ListView lvVehicles;
    VehiclesAdapter adapter;
    DBManager db;
    ArrayList<Vehicle> listVehicle;
    String imString = "";
    Bitmap bitmap;
    static int i = 1;
    int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = getIntent().getIntExtra("id_diadanh", 0);
        db = new DBManager(this);
        listVehicle = db.getVedicleID(id);
        if (listVehicle.size() == 0) {
            Toast.makeText(getApplicationContext(), "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            setAdapterListView(listVehicle);
        }
    }

    public void setAdapterListView(ArrayList<Vehicle> list) {
        adapter = new VehiclesAdapter(getApplicationContext(), list);
        lvVehicles.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

}
