package com.example.virtualtravelapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.google.MapActivity;
import com.example.virtualtravelapp.model.Place;
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

public class DetailPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {
	private GoogleMap mMap;
	private String name;
	private String image;
	private String detail;
	private String latlng;
	private int id;
	DBManager db;
	private Place place;
	@BindView(R.id.imDetail) ImageView imDetail;
	@BindView(R.id.tvDetail) TextView tvDetail;
	@BindView(R.id.tvPhone) TextView tvPhone;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_place);
		ButterKnife.bind(this);
		db = new DBManager(this);

		id = getIntent().getIntExtra("id", 1);
		place = db.getPlaceDetail(id);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(place.getName());
		Log.d("TAGPlace", String.valueOf(place));
		Log.d("TAGPlaceimage", String.valueOf(place.getImage()));
		tvPhone.setVisibility(View.GONE);
		tvDetail.setText(Html.fromHtml(place.getDetail()));
		boolean a = place.getImage().startsWith("/9j/");
		if (a) {
			imDetail.setImageBitmap(decodeBase64(place.getImage()));
		}else {
//			String[] image = (diaDanh.getImDiaDanh()).split(";");
////			new DownloadImageTask(holder.imgPlace).execute(list.get(position).getImage());
//			PicassoClient.downloadImage(context, image[0], holder.imImage);
//			new DownloadImageTask().execute(place.getImage());
			Glide.with(getApplicationContext())
					.load(place.getImage())
					.error(R.drawable.im_thumbnail)
					.skipMemoryCache(true)
					.into(imDetail);
		}
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

//	public void getData(){
//		Intent intent = getIntent();
//		Bundle bundle = intent.getExtras();
//
//		name = bundle.getString("name");
//		id = bundle.getInt("id");
//		image = bundle.getString("image");
//		detail = bundle.getString("detail");
//		latlng = bundle.getString("latlng");
//	}
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);

		latlng = place.getLatlng();
		mMap.getMyLocation();
		String[] lat = latlng.split(";");
		LatLng sydney = new LatLng(Double.parseDouble(lat[0]), Double.parseDouble(lat[1]));
		MarkerOptions location = new MarkerOptions();
		location.draggable(true);
		location.position(sydney);
		location.title(place.getName());
//		location.snippet("Day la tru so MXH");
		Marker marker = mMap.addMarker(location);
		marker.showInfoWindow();

		CameraUpdate moveCamera = CameraUpdateFactory.newLatLngZoom(sydney,17);
		mMap.animateCamera(moveCamera,3000,null);

		//su kien click
		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				Intent intent = new Intent(DetailPlaceActivity.this, MapActivity.class);
				intent.putExtra("latlng", place.getLatlng());
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

	public static Bitmap decodeBase64(String input) {
		byte[] decodedBytes = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}
}
