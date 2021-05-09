package com.example.virtualtravelapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.google.MapActivity;
import com.example.virtualtravelapp.model.Restaurant;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRetaurantActivity extends AppCompatActivity implements OnMapReadyCallback {
	private GoogleMap mMap;
	private String name;
	private String image;
	private String detail;
	private String latlng;
	private String phone;
	@BindView(R.id.imDetail) ImageView imDetail;
	@BindView(R.id.tvDetail) TextView tvDetail;
	@BindView(R.id.tvPhone) TextView tvPhone;
	@BindView(R.id.lnMap) LinearLayout lnMap;

	private int id;
	private DBManager db;
	private Restaurant restaurant;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_place);
		ButterKnife.bind(this);
		id = getIntent().getIntExtra("id", 1);
		db = new DBManager(this);
		restaurant = db.getRestaurantDetail(id);


		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(restaurant.getName());
		tvDetail.setText(restaurant.getDetail());
		if (restaurant.getPhone().equals("0")){
			tvPhone.setVisibility(View.GONE);
		}else {
			tvPhone.setVisibility(View.VISIBLE);
			tvPhone.setText("SĐT: " + restaurant.getPhone());
		}
//		new DownloadImageTask().execute(restaurant.getImage());

		Glide.with(getApplicationContext())
				.load(restaurant.getImage())
				.error(R.drawable.im_thumbnail)
				.skipMemoryCache(true)
				.into(imDetail);
		if (restaurant.getLatlng().equals("0")){
			lnMap.setVisibility(View.GONE);
		}else {
			lnMap.setVisibility(View.VISIBLE);
			SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			mapFragment.getMapAsync(this);
		}

		tvPhone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri number = Uri.parse("tel:" + restaurant.getPhone());
				Intent intent = new Intent(Intent.ACTION_VIEW, number);
				startActivity(intent);
			}
		});
	}

//	public void getData(){
//		Intent intent = getIntent();
//		Bundle bundle = intent.getExtras();
//
//		name = bundle.getString("name");
//		image = bundle.getString("image");
//		detail = bundle.getString("detail");
//		latlng = bundle.getString("latlng");
//		phone = bundle.getString("phone");
//	}
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);

		mMap.getMyLocation();
		String[] lat = restaurant.getLatlng().split(";");
		LatLng sydney = new LatLng(Double.parseDouble(lat[0]), Double.parseDouble(lat[1]));
		MarkerOptions location = new MarkerOptions();
		location.draggable(true);
		location.position(sydney);
		location.title(restaurant.getName());
//		location.snippet("Day la tru so MXH");
		Marker marker = mMap.addMarker(location);
		marker.showInfoWindow();

		CameraUpdate moveCamera = CameraUpdateFactory.newLatLngZoom(sydney,17);
		mMap.animateCamera(moveCamera,3000,null);
		//su kien click
		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				Intent intent = new Intent(DetailRetaurantActivity.this, MapActivity.class);
				intent.putExtra("latlng", restaurant.getLatlng());
				intent.putExtra("zoom", 10);
				startActivity(intent);
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home){
			finish();
		}
		return true;
	}

	//ham download image
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap bitmap = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				bitmap = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap result) {
			imDetail.setImageBitmap(result);
		}
	}
}

