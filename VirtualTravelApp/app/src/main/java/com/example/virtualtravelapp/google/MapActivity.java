package com.example.virtualtravelapp.google;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.virtualtravelapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String latlng;
    String name;
    ArrayList<LatLng> markerPoints;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.btMyLocation)
    Button btMyLocation;
    GPSMyLocation gpsMyLocation;
    private double latitude = 0;
    private double longtitude = 0;
    private int zoom = 0;

    //show progressdialog
    private ProgressDialog dialog;
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        markerPoints = new ArrayList<LatLng>();
        //lay vi tri
        gpsMyLocation = new GPSMyLocation(MapActivity.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latlng = getIntent().getStringExtra("latlng");
        name = getIntent().getStringExtra("name");
        zoom = getIntent().getIntExtra("zoom", 10);
        getSupportActionBar().setTitle(name);
        Log.d("---", " latlng " + latlng + " name " + name);
        getMap();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                dialog = new ProgressDialog(MapActivity.this);
                dialog.setMessage(getString(R.string.wait));
                dialog.setIndeterminate(false);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(true);
                dialog.setCancelable(false);
                dialog.show();
                return dialog;
//				break;
            default:
                return null;
        }
    }

    //get vi tri hien tai
    @OnClick(R.id.btMyLocation)
    public void getMap() {
        if (isOnline()) {
            getMylocation();
            if (latitude != 0) {
                onMapReady(mMap);
            } else {
            }
        } else {
            showAlertDialog();
        }
    }

    //check and get location

    public void getMylocation() {
        gpsMyLocation = new GPSMyLocation(MapActivity.this);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.getMyLocation();
        MarkerOptions location = new MarkerOptions();
        String[] lat = latlng.split(";");

        //truong hop chua bat GPS va mang, k lay dc toa do

        if (latitude == 0) {
            LatLng diadanh = new LatLng(Double.parseDouble(lat[0]), Double.parseDouble(lat[1]));
            CameraUpdate moveCamera = CameraUpdateFactory.newLatLngZoom(diadanh, 10);
            mMap.animateCamera(moveCamera, 3000, null);
            location.draggable(true);
            location.position(diadanh);
            location.title(name);
            Marker marker = mMap.addMarker(location);
            marker.showInfoWindow();
        } else {
            //diem di la lay duoc tu GPS
            //diem den chinh la tu CSDL tach' ra

            LatLng origin = new LatLng(latitude, longtitude);
            LatLng diemden = new LatLng(Double.parseDouble(lat[0]), Double.parseDouble(lat[1]));
            markerPoints.add(diemden);
            markerPoints.add(origin);


            location.position(diemden);
            location.position(origin);

            location.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            Marker marker = mMap.addMarker(location);
            location.draggable(true);
            location.position(origin);
            location.title("Bắt đầu");
            marker.showInfoWindow();

            location.draggable(true);
            location.position(diemden);
            location.title(name);
            marker.showInfoWindow();

            String url = getDirectionUrl(origin, diemden);

            //thuc hien viec request, ve duong di
            new DowloadTas().execute(url);
            CameraUpdate moveCamera = CameraUpdateFactory.newLatLngZoom(origin, zoom);
            mMap.animateCamera(moveCamera, 3000, null);
        }
        Log.d("TAG", "onMapReady");


    }

    private String getDirectionUrl(LatLng origin, LatLng dest) {
        String strOrigin = "origin=" + origin.latitude + ","
                + origin.longitude;
        String strDest = "destination=" + dest.latitude
                + "," + dest.longitude;
        String parameters = strOrigin + "&" + strDest;

        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters + "&key=AIzaSyC1YIZjkANlWIbujR14e7UBHI-Ff__t_44";
        return url;
    }

    //download json data from url
    private String downloadUrl(String path) {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();

            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            data = buffer.toString();
            reader.close();
            inputStream.close();
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    //Download
    private class DowloadTas extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... params) {
            String data = "";
            data = downloadUrl(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(s);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... params) {
            JSONObject object;
            List<List<HashMap<String, String>>> routes = null;
            try {
                object = new JSONObject(params[0]);
                DirectionJSON json = new DirectionJSON();
                //start parsing data

                routes = json.parse(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            super.onPostExecute(result);
            ArrayList<LatLng> point = null;
            PolylineOptions polylineOptions = null;
//			MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";
            // neu mang gia tri tra ve nho hon 1
            if (result.size() < 1) {
                dismissDialog(progress_bar_type);
                Toast.makeText(getApplicationContext(), "Hiện tại chưa có đường đi", Toast.LENGTH_LONG).show();
                return;
            }

            for (int i = 0; i < result.size(); i++) {
                point = new ArrayList<>();
                polylineOptions = new PolylineOptions();
                //route
                List<HashMap<String, String>> path = result.get(i);
                //point co kinh do va vi do
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> pointPath = path.get(j);

                    //get thoi gian , do dai
                    //do add distance va duration duoc add o vi tri 0 va 1 ,contine de tiep tuc di tiep
                    if (j == 0) {
                        distance = pointPath.get("distance");
                        continue;
                    } else if (j == 1) {
                        duration = pointPath.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(pointPath.get("lat"));
                    double lng = Double.parseDouble(pointPath.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    //them tung diem vao 1 mang point
                    point.add(position);


                }

                //them tat ca cac diem vao route
                // 1 mang Latlng
                polylineOptions.addAll(point);
                polylineOptions.width(10);
                polylineOptions.color(Color.BLUE);
            }
            tvTime.setText("Quãng đường: " + distance
                    + "\t\t" + "Thời gian: " + duration);

            //ve polyline
            mMap.addPolyline(polylineOptions);
            dismissDialog(progress_bar_type);

        }
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
