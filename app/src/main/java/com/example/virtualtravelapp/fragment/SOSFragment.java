package com.example.virtualtravelapp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SOSFragment extends Fragment {
    ArrayList<String> listPlaceID;
    ArrayList<HashMap<String, String>> listSOSDetail;
    ArrayList<HashMap<String, String>> listATM;
    ArrayList<HashMap<String, String>> listGAS;
    @BindView(R.id.lvSOS)
    ListView lvSOS;
    @BindView(R.id.tvNoGPS)
    TextView tvNoGPS;
    @BindView(R.id.btGetPolice)
    Button btGetPolice;
    private SOSAdapter adapter;
    private GPSMyLocation gpsMyLocation;
    private double latitude = 0;
    private double longtitude = 0;
//    private int TAG = 0;

    private ProgressDialog dialog;
    private static final int progress_type = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sos, container, false);
        ButterKnife.bind(this, view);

        listPlaceID = new ArrayList<>();
        listSOSDetail = new ArrayList<>();
        listATM = new ArrayList<>();
        listGAS = new ArrayList<>();

        getActivity().setTitle("KHẨN CẤP");
        btGetPolice.setText("Tìm đồn công an");

        getSOS();

        lvSOS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listSOSDetail.get(position).get("formatted_phone_number") != null) {
                    Uri number = Uri.parse("tel:" + listSOSDetail.get(position).get("formatted_phone_number"));
                    Intent intent = new Intent(Intent.ACTION_VIEW, number);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra("latlng", listSOSDetail.get(position).get("lat") + ";" + listSOSDetail.get(position).get("lng"));
                    intent.putExtra("name", listSOSDetail.get(position).get("name"));
                    intent.putExtra("zoom", 15);
                    startActivity(intent);
                }

            }
        });
        return view;
    }

    @OnClick(R.id.btGetPolice)
    public void getPolice() {
        getSOS();

    }

    public void getSOS(){
        if (isOnline()) {
            getMylocation();
            if (latitude != 0) {
                tvNoGPS.setVisibility(View.GONE);
                new DownloadPlaceID().execute(latitude + "," + longtitude);
            } else {
                tvNoGPS.setVisibility(View.VISIBLE);
            }
        } else {
            showAlertDialog();
        }
    }


    //download get placeID
    private class DownloadPlaceID extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getString(R.string.wait));
            dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String data = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
//            String link = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + params[0] + "&radius=" + params[1] + "&type=police&key=AIzaSyDalYDFL51JNbm4TyjcIby82Ud5iZUJXow";
            String link = "https://maps.googleapis.com/maps/api/place/search/json?location=" + params[0] + "&rankby=distance&types=police&sensor=false&key=AIzaSyCgMD65ti0BBFkhMAEmHZjoQpY4_TXn7uA";
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
                listPlaceID = jsonPlaceID.placeID(jsonObject);
                Log.d("--listPlaceID", String.valueOf(listPlaceID) + " size " + listPlaceID.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (listPlaceID.size() != 0) {
                for (int i = 0; i < listPlaceID.size(); i++) {
                    new DownloadSOSDetail().execute(listPlaceID.get(i));
                }
                btGetPolice.setEnabled(false);
            }
        }
    }

    //Download sos detail
    private class DownloadSOSDetail extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String data = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            String linkDetail = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + params[0] + "&key=AIzaSyCgMD65ti0BBFkhMAEmHZjoQpY4_TXn7uA";
            https:
//maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=YOUR_API_KEY

            try {
                URL url = new URL(linkDetail);
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
            dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JsonPlaceID jsonPlaceID = new JsonPlaceID();

                listSOSDetail.add(jsonPlaceID.sosDetail(jsonObject));
                Log.d("--listSOSDetail", String.valueOf(listSOSDetail) + " size " + listSOSDetail.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setAdapterListView(listSOSDetail);
        }
    }


    public void setAdapterListView(ArrayList<HashMap<String, String>> list) {
        adapter = new SOSAdapter(getContext(), list);
        lvSOS.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //check and get location

    public void getMylocation() {
        gpsMyLocation = new GPSMyLocation(getContext());

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

    @Override
    public void onResume() {
        super.onResume();
//        getMylocation();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void showAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
