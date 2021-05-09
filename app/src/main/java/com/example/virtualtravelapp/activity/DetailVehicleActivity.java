package com.example.virtualtravelapp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.model.Vehicle;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailVehicleActivity extends AppCompatActivity {
	@BindView(R.id.imVehicel) ImageView imVehicel;
	@BindView(R.id.tvVehicles) TextView tvVehicles;
	private String name;
	private String image;
	private String detail;
	private  int id;
	private Vehicle vehicle;
	private DBManager db;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_vehicles);
		ButterKnife.bind(this);
		db = new DBManager(this);


		id = getIntent().getIntExtra("id", 1);
		vehicle = db.getVedicleDetail(id);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(vehicle.getName());

		tvVehicles.setText(Html.fromHtml(vehicle.getDetail()));
//		new DownloadImageTask().execute(vehicle.getImage());
//		PicassoClient.downloadImage(getApplicationContext(), anh[0], imVehicel);
		Glide.with(getApplicationContext())
				.load(vehicle.getImage())
				.error(R.drawable.im_thumbnail)
				.skipMemoryCache(true)
				.into(imVehicel);

	}

//	public void getData(){
//		Intent intent = getIntent();
//		Bundle bundle = intent.getExtras();
//
//		name = bundle.getString("name");
//		image = bundle.getString("image");
//		detail = bundle.getString("detail");
//	}

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
			imVehicel.setImageBitmap(result);
		}
	}
}
