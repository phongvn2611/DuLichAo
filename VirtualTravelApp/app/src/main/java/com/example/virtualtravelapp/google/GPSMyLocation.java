package com.example.virtualtravelapp.google;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class GPSMyLocation extends Service implements LocationListener {

	private Context context;
	//Co cho GPS
	boolean isGPSEnabled = false;
	//co cho network
	boolean isNetworkEnabled = false;

	boolean checkEnabled = false;
	Location location;
	double latitude;
	double longitude;
	//khoang cach toi thieu de update location (met)
	private static final long MIN_DISTANCE_CHANGE = 10;
	//thoi gian toi thieu giua cac lan update (mili)
	private static final long MIN_TIME_UPDATE = 1000 * 60 * 1;

	protected LocationManager locationManager;

	public GPSMyLocation(Context context) {
		this.context = context;
		getLocation();
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

			//get GPS status
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			//get network status
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (!isGPSEnabled && !isNetworkEnabled) {
				//no network
			} else {
			this.checkEnabled = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							MIN_TIME_UPDATE,
							MIN_DISTANCE_CHANGE, this);
					Log.d("network", "network");
					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				//if GPS Enabled
				if (isGPSEnabled) {
					if (location == null) {
						Log.d("TAGisGPSEnabled", "vao");
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
								MIN_TIME_UPDATE,
								MIN_DISTANCE_CHANGE, this);
						Log.d("GPS Encabled", "GPS");
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return location;
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	//tra lai 0.00 neu k lay dc kinh do, vi do


	public double getLatitude() {
		if (location != null){
			latitude = location.getLatitude();
		}
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		if (location != null){
			longitude = location.getLongitude();
		}
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	//check xem bat GPS chua

	public boolean checkEnabled(){
		return this.checkEnabled;
	}
	//hien thi bat ket noi
	public void showSetting(){
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Cài đặt GPS");
		builder.setMessage("GPS không được bật. Bạn có muốn di chuyển tới menu cài đặt?");
		builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
			}
		});
		builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	//dung su dung GPS
	public void stopUsingGPS(){
		if (locationManager != null){
			locationManager.removeUpdates(GPSMyLocation.this);
		}
	}
}
