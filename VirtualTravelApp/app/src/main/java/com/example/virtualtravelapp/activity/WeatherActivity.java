package com.example.virtualtravelapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.adapter.WeatherAdapter;
import com.example.virtualtravelapp.model.Weather;
import com.example.virtualtravelapp.widget.PareJSON;

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

public class WeatherActivity extends AppCompatActivity {
    @BindView(R.id.tvCity) TextView tvCity;
    @BindView(R.id.tvCencius) TextView tvCencius;
    @BindView(R.id.tvTemp) TextView tvtemp;
    @BindView(R.id.tvText) TextView tvText;
    @BindView(R.id.imStatus) ImageView imStatus;
    private RecyclerView recyclerView;
    private String city;
    private String cityW;
    private ArrayList<HashMap<String, String>> listWeather;

    private ProgressDialog dialog;
    private static final int progress_type = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        listWeather = new ArrayList<>();
        initViews();
        city = getIntent().getStringExtra("city");
        Log.d("--city", city);
        if (isOnline()) {
            new DownloadJson().execute(city);
        }else {
            showAlertDialog();
        }

    }

    @OnClick(R.id.imStatus)
    public void imClick(){
        if (isOnline()) {
            new DownloadJson().execute(city);
        }else {
            showAlertDialog();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
//        return super.onCreateDialog(id);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void initViews() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


    }

    //set cac thuoc tinh sau khi decode json
    private ArrayList<Weather> getData() {
        ArrayList<Weather> list = new ArrayList<>();
        for (int i = 2; i < listWeather.size(); i++) {
            Weather weather = new Weather();
            weather.setCode(Integer.parseInt(listWeather.get(i).get("code")));
            weather.setCencius(listWeather.get(i).get("low") + "°C - " + listWeather.get(i).get("high") + "°C");
            weather.setDay(listWeather.get(i).get("day"));
            weather.setText(listWeather.get(i).get("text"));
            weather.setDate(listWeather.get(i).get("date"));
            list.add(weather);
        }
        return list;
    }

    //download json data
    private class DownloadJson extends AsyncTask<String, String, String> {
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

            String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", params[0]);
            String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
            try {
                URL url = new URL(endpoint);
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
            Log.d("--json", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                PareJSON pareJSON = new PareJSON();
                listWeather = pareJSON.parse(jsonObject);
                if (listWeather.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Kiểm tra mạng internet", Toast.LENGTH_LONG).show();
                } else {
                    tvCity.setText(listWeather.get(0).get("city") + ", " + listWeather.get(0).get("country"));
                    tvtemp.setText(listWeather.get(0).get("temp") + "°C");
                    tvCencius.setText(listWeather.get(1).get("low") + "°C - " + listWeather.get(1).get("high") + "°C");
                    tvText.setText(listWeather.get(0).get("text"));
                    imStatus.setImageResource(PareJSON.getIconIdFromCode(Integer.parseInt(listWeather.get(0).get("code"))));
                    Log.d("--listWeather", String.valueOf(listWeather));
                }

                //getData la lay listWeather dc parse tu chuoi json
                ArrayList<Weather> listWeather = getData();
                WeatherAdapter adapter = new WeatherAdapter(getApplicationContext(), listWeather);
                recyclerView.setAdapter(adapter);

                dismissDialog(progress_type);
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
}

