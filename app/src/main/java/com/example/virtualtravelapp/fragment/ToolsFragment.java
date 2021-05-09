package com.example.virtualtravelapp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

public class ToolsFragment extends Fragment {
    @BindView(R.id.tvATM)
    TextView tvATM;
    @BindView(R.id.tvGAS)
    TextView tvGAS;
    @BindView(R.id.tvNoGPS)
    TextView tvNoGPS;
    @BindView(R.id.btGet)
    Button btGet;
    private int type = 0;
    private GPSMyLocation gpsMyLocation;
    private double latitude = 0;
    private double longtitude = 0;

    ArrayList<HashMap<String, String>> listATM;
    ArrayList<HashMap<String, String>> listGAS;
    @BindView(R.id.lvSOS)
    ListView lvSOS;
    private ProgressDialog dialog;
    private SOSAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tools, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Tiện ích khác");
        listATM = new ArrayList<>();
        listGAS = new ArrayList<>();

        //Khởi tạo type khi mới vào
        type = 1;
        btGet.setText("Tìm máy ATM");
        tvGAS.setBackgroundColor(getResources().getColor(R.color.gray));
        tvATM.setBackgroundColor(getResources().getColor(R.color.white));
        tvATM.setTextColor(getResources().getColor(R.color.blue));

        getTools();

        lvSOS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type){
                    case 1:
                        Intent iATM = new Intent(getActivity(), MapActivity.class);
                        iATM.putExtra("latlng", listATM.get(position).get("lat") + ";" + listATM.get(position).get("lng"));
                        iATM.putExtra("name", listATM.get(position).get("name"));
                        iATM.putExtra("zoom", 15);
                        startActivity(iATM);
                        break;
                    case 2:
                        Intent iGAS = new Intent(getActivity(), MapActivity.class);
                        iGAS.putExtra("latlng", listGAS.get(position).get("lat") + ";" + listGAS.get(position).get("lng"));
                        iGAS.putExtra("name", listGAS.get(position).get("name"));
                        iGAS.putExtra("zoom", 15);
                        startActivity(iGAS);
                        break;
                }

            }
        });
        return view;

    }

    public void getTools(){
        if (isOnline()){
            getMylocation();

            if (latitude != 0) {
                tvNoGPS.setVisibility(View.GONE);
                switch (type) {
                    case 1:
                        new DownloadATM().execute(latitude + "," + longtitude);
                        break;
                    case 2:
                        new DownloadGAS().execute(latitude + "," + longtitude);
                        break;
                }
            }else {
                tvNoGPS.setVisibility(View.VISIBLE);
            }
        }else {
            showAlertDialog();
        }
    }
    @OnClick(R.id.tvATM)
    public void atm() {
        searchNear(1);

    }

    @OnClick(R.id.tvGAS)
    public void gas_station() {
        searchNear(2);

    }
    //su khien khi nhan button tim ATM hoac tram xang
    @OnClick(R.id.btGet)
    public void getPolice() {
        getTools();
    }

    public ProgressDialog optionDialog() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.wait));
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        return dialog;
    }

    public void getMylocation() {
        gpsMyLocation = new GPSMyLocation(getActivity());

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

    //Download ATM
    private class DownloadATM extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            optionDialog().show();
        }

        @Override
        protected String doInBackground(String... params) {
            String data = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            String link = "https://maps.googleapis.com/maps/api/place/search/json?location=" + params[0] + "&rankby=distance&types=atm&sensor=false&key=AIzaSyCgMD65ti0BBFkhMAEmHZjoQpY4_TXn7uA";
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
            dialog.dismiss();

        }
    }

    //Download ATM detail
    private class DownloadGAS extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            optionDialog().show();
        }

        @Override
        protected String doInBackground(String... params) {
            String data = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            String link = "https://maps.googleapis.com/maps/api/place/search/json?location=" + params[0] + "&rankby=distance&types=gas_station&sensor=false&key=AIzaSyCgMD65ti0BBFkhMAEmHZjoQpY4_TXn7uA";
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
            dialog.dismiss();
        }
    }

    public void setAdapterListView(ArrayList<HashMap<String, String>> list) {
        adapter = new SOSAdapter(getContext(), list);
        lvSOS.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void startFragment(Fragment fragment) {
        String fragmentName = fragment.getClass().getName();
        String fragmentTag = fragmentName;

        FragmentManager manager = getFragmentManager();
        boolean fragmentPopp = manager.popBackStackImmediate(fragmentName, 0);

        if (!fragmentPopp && manager.findFragmentByTag(fragmentTag) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.nav_frame, fragment, fragmentTag);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
        }
    }

    public void searchNear(int type) {
        if (type == 1) {
            this.type = 1;
            btGet.setText("Tìm máy ATM");
            tvGAS.setBackgroundColor(getResources().getColor(R.color.gray));
            tvATM.setBackgroundColor(getResources().getColor(R.color.white));
            tvATM.setTextColor(getResources().getColor(R.color.blue));
            tvGAS.setTextColor(getResources().getColor(R.color.black));
            Log.d("TAG--type", String.valueOf(this.type));

        } else {
            this.type = 2;
            btGet.setText("Tìm trạm xăng");
            tvATM.setBackgroundColor(getResources().getColor(R.color.gray));
            tvGAS.setBackgroundColor(getResources().getColor(R.color.white));
            tvGAS.setTextColor(getResources().getColor(R.color.blue));
            tvATM.setTextColor(getResources().getColor(R.color.black));
            Log.d("TAG--type", String.valueOf(this.type));

        }

        //sau khi set type tương ứng và set cac textview tương ứng.
        //check internet và GPS
        getTools();

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
