package com.example.virtualtravelapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.adapter.SOSAdapter;
import com.example.virtualtravelapp.google.GPSMyLocation;
import com.example.virtualtravelapp.google.MapActivity;
import com.example.virtualtravelapp.sos.JsonPlaceID;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ToolsActivity extends AppCompatActivity {
    ArrayList<String> listPlaceID;
    ArrayList<HashMap<String, String>> listSOSDetail;
    ArrayList<HashMap<String, String>> listATM;
    ArrayList<HashMap<String, String>> listGAS;
    @BindView(R.id.lvSOS) ListView lvSOS;
    @BindView(R.id.tvNoGPS) TextView tvNoGPS;
    @BindView(R.id.btGet) Button btGet;
    private SOSAdapter adapter;
    private GPSMyLocation gpsMyLocation;
    private double latitude = 0;
    private double longtitude = 0;
    private int TAG = 0;
//    private Context context = this.getApplicationContext();
    private ProgressDialog dialog;
    private static final int progress_type = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listPlaceID = new ArrayList<>();
        listSOSDetail = new ArrayList<>();
        listATM = new ArrayList<>();
        listGAS = new ArrayList<>();
        TAG = getIntent().getIntExtra("TAG",0);
        Log.d("--TAG", String.valueOf(TAG));


        switch (TAG) {
            case 2:
                getSupportActionBar().setTitle("ATM");
                btGet.setText("Tìm máy ATM");
                break;
            case 3:
                getSupportActionBar().setTitle("Trạm xăng");
                btGet.setText("Tìm trạm xăng");
                break;
        }

        getMylocation();

        if (latitude != 0) {
            tvNoGPS.setVisibility(View.GONE);
            switch (TAG) {
                case 2:
                    new DownloadATM().execute(latitude + "," + longtitude);
                    break;
                case 3:
                    new DownloadGAS().execute(latitude + "," + longtitude);
                    break;
            }
        }else {
            tvNoGPS.setVisibility(View.VISIBLE);
        }

        lvSOS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (TAG){
                    case 2:
                        Intent iATM = new Intent(ToolsActivity.this, MapActivity.class);
                        iATM.putExtra("latlng", listATM.get(position).get("lat") + ";" + listATM.get(position).get("lng"));
                        iATM.putExtra("name", listATM.get(position).get("name"));
                        iATM.putExtra("zoom", 15);
                        startActivity(iATM);
                        break;
                    case 3:
                        Intent iGAS = new Intent(ToolsActivity.this, MapActivity.class);
                        iGAS.putExtra("latlng", listGAS.get(position).get("lat") + ";" + listGAS.get(position).get("lng"));
                        iGAS.putExtra("name", listGAS.get(position).get("name"));
                        iGAS.putExtra("zoom", 15);
                        startActivity(iGAS);
                        break;
                }

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case progress_type:
                dialog = new ProgressDialog(this);
                dialog.setMessage(getString(R.string.wait));
                dialog.setIndeterminate(false);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(false);
                dialog.show();
                return dialog;
            default:
                return null;
        }
//        return super.onCreateDialog(id);
    }

    @OnClick(R.id.btGet)
    public void getPolice(){
        getMylocation();
        if (latitude != 0) {
            tvNoGPS.setVisibility(View.GONE);
            switch (TAG){
                case 2:
                    new DownloadATM().execute(latitude + "," + longtitude);
                    break;
                case 3:
                    new DownloadGAS().execute(latitude + "," + longtitude);
                    break;
            }
        }else {
//            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            tvNoGPS.setVisibility(View.VISIBLE);
        }
    }

    //Download ATM
    private class DownloadATM extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_type);
        }

        @Override
        protected String doInBackground(String... params) {
            String data = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            String link = "https://maps.googleapis.com/maps/api/place/search/json?location=" + params[0] + "&rankby=distance&types=atm&sensor=false&key=AIzaSyDalYDFL51JNbm4TyjcIby82Ud5iZUJXow";
            try {
                URL url = new URL(link);
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JsonPlaceID jsonPlaceID = new JsonPlaceID();

                listATM = jsonPlaceID.getATM(jsonObject);
                Log.d("--listSOSDetail", String.valueOf(listATM) + " size " + listATM.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setAdapterListView(listATM);
            btGet.setEnabled(false);
            dismissDialog(progress_type);

        }
    }

    //Download ATM detail
    private class DownloadGAS extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_type);
        }

        @Override
        protected String doInBackground(String... params) {
            String data = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            String link = "https://maps.googleapis.com/maps/api/place/search/json?location=" + params[0] + "&rankby=distance&types=gas_station&sensor=false&key=AIzaSyDalYDFL51JNbm4TyjcIby82Ud5iZUJXow";
            try {
                URL url = new URL(link);
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JsonPlaceID jsonPlaceID = new JsonPlaceID();

                listGAS = jsonPlaceID.gasStation(jsonObject);
                Log.d("--listSOSDetail", String.valueOf(listGAS) + " size " + listGAS.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setAdapterListView(listGAS);
            btGet.setEnabled(false);
            dismissDialog(progress_type);
        }
    }

    public void setAdapterListView(ArrayList<HashMap<String,String>> list) {
        adapter = new SOSAdapter(getApplicationContext(), list);
        lvSOS.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //check and get location

    public void getMylocation(){
        gpsMyLocation = new GPSMyLocation(ToolsActivity.this);

        if (gpsMyLocation.checkEnabled()){
            Log.d("TAG", "checkEnabled");
            latitude = gpsMyLocation.getLatitude();
            Log.d("TAGlatitude", String.valueOf(latitude));
            longtitude = gpsMyLocation.getLongitude();
            Log.d("TAGlongtitude", String.valueOf(longtitude));
        }else {
            Log.d("TAG", "OUTEnabled");
            gpsMyLocation.showSetting();
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getMylocation();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}
