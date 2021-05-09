package com.example.virtualtravelapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.google.GPSMyLocation;
import com.example.virtualtravelapp.model.Tour;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TourActivity extends AppCompatActivity {
	public static final String ONE_DAY = "Đi 1 ngày";
	public static final String TWO_DAY = "Đi 2 ngày 1 đêm";
	public static final String THR_DAY = "Đi 3 ngày 2 đêm";
	@BindView(R.id.spTourType) Spinner spTourType;
	@BindView(R.id.tvTourInfor) TextView tvTourInfor;
	DBManager db;
	ArrayList<Tour> list;
	private int id;
	private String image;
	private String name;

	private String item;

	private GPSMyLocation gpsMyLocation;
	private double latitude = 0;
	private double longtitude = 0;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);
		setContentView(R.layout.activity_tour);
		ButterKnife.bind(this);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		 Add code to print out the key hash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.example.mxhung.demofb",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
		id = getIntent().getIntExtra("id_diadanh", 0);
		image = getIntent().getStringExtra("image");
		name = getIntent().getStringExtra("name");
		db = new DBManager(this);
		list = db.getTourId(id);
		if (list.size() == 0) {
			Toast.makeText(getApplicationContext(), "Chưa có dữ liệu", Toast.LENGTH_LONG).show();
			return;
		}
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//check and get City
		if (isOnline()) {
			getMylocation();
			if (latitude != 0) {
				Toast.makeText(getApplicationContext(), getCity(latitude, longtitude), Toast.LENGTH_LONG).show();
			} else {
			}
		} else {
//			showAlertDialog();
		}


		spTourType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				 item = parent.getItemAtPosition(position).toString();
//				Toast.makeText(getApplicationContext(), "Bạn đã chọn " + item, Toast.LENGTH_SHORT).show();

				switch (item) {
					case ONE_DAY:
						String oneDay = list.get(0).getDetail();
						if (oneDay.equals("0")) {
							tvTourInfor.setText("Chưa có dữ liệu");
						} else {
							tvTourInfor.setText(Html.fromHtml(oneDay));
						}
						break;
					case TWO_DAY:
						String twoDay = list.get(1).getDetail();
						if (twoDay.equals("0")) {
							tvTourInfor.setText("Chưa có dữ liệu");
						} else {
							tvTourInfor.setText(Html.fromHtml(twoDay));
						}
						break;
					case THR_DAY:
						String threeDay = list.get(2).getDetail();
						if (threeDay.equals("0")) {
							tvTourInfor.setText("Chưa có dữ liệu");
						} else {
							tvTourInfor.setText(Html.fromHtml(threeDay));
						}
						break;
				}

				//share facebook
				ShareLinkContent content = new ShareLinkContent.Builder()
						.setContentDescription("Lịch trình du lịch " + name + " " + item)
						.setContentTitle("Du lịch " + name)
						.setContentUrl(Uri.parse("http://dantri.com.vn/"))
						.setImageUrl(Uri.parse(image))
//				.setShareHashtag(new ShareHashtag.Builder()
//				.setHashtag("#Dichoithoi!!!").build())
						.build();
				ShareButton shareButton = (ShareButton) findViewById(R.id.btShare);
				shareButton.setShareContent(content);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});



	}

	public String getCity(double lat, double lng){
		Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = gcd.getFromLocation(lat, lng, 1);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

		return addresses.get(0).getLocality();
	}

	//check and get location

	public void getMylocation() {
		gpsMyLocation = new GPSMyLocation(TourActivity.this);

		if (gpsMyLocation.checkEnabled()) {
			Log.d("TAG", "checkEnabled");
			latitude = gpsMyLocation.getLatitude();
			Log.d("TAGlatitude", String.valueOf(latitude));
			longtitude = gpsMyLocation.getLongitude();
			Log.d("TAGlongtitude", String.valueOf(longtitude));
		} else {
			Log.d("TAG", "OUTEnabled");
			gpsMyLocation.showSetting();
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	public void showAlertDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
				.setIcon(R.drawable.ic_warning)
				.setTitle("No Internet")
				.setMessage("Bạn hãy kiểm tra lại mạng internet")
				.show();
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

