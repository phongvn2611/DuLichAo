package com.example.virtualtravelapp.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.adapter.PlaceAdapter;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.model.Place;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceActivity extends AppCompatActivity {
    @BindView(R.id.lvPlace)
    ListView lvPlace;
    PlaceAdapter adapter;
    DBManager db;
    ArrayList<Place> listPlace;
    String imString = "";
    Bitmap bitmap;
	static int i = 1;
	int id;
    //search
    @BindView(R.id.etSearch) EditText etSearch;
    @BindView(R.id.tvEmpty) TextView tvEmpty;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = getIntent().getIntExtra("id_diadanh", 0);

        db = new DBManager(this);
        textChange();
//        db.openDataBase();
        listPlace = db.getPlaceId(id);
        if (listPlace.size() == 0) {
            Toast.makeText(getApplicationContext(), "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            setAdapterListView(listPlace);
//	        ArrayList<Place> listPlace11 = db.getPlace();
//	        for (int i = 0; i < listPlace11.size(); i++) {
//                new LoadImage().execute(listPlace11.get(i).getImage());
//            }

        }

    }

    public void setAdapterListView(ArrayList<Place> list) {
        adapter = new PlaceAdapter(getApplicationContext(), list);
        lvPlace.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    //ham download image
    private class LoadImage extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                imString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
	            publishProgress(imString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imString;
        }

	    @Override
	    protected void onProgressUpdate(String... values) {
		    super.onProgressUpdate(values);
			    Place place = new Place();
			    place.setId(i++);
			    String image = values[0];
			    place.setImage(image);
			    db.editPlace(place);

	    }

	    protected void onPostExecute(String imString) {
//            Log.d("imString", imString);
        }

        public String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            image.compress(compressFormat, quality, byteArrayOS);
            return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        }
    }

    //----------------------------------------------------------searchh
    //--
    @OnClick(R.id.ibtSpeech)
    public void speech(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_somthing));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);;
        }catch (ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if ((result.get(0).equals("xóa"))){
                        etSearch.setText("");
                    }else {
                        etSearch.setText(result.get(0));
                    }
                }
                break;
            }
        }
    }

    @OnClick(R.id.btClean)
    public void cleanText(){
        etSearch.setText("");
    }

    public void textChange(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etSearch.setSelection(etSearch.getText().length());
                listPlace = db.searchPlace(String.valueOf(s), id);
                if (listPlace.size() == 0){
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                }
                setAdapterListView(listPlace);
                //adapter.notifyDataSetChanged();
            }
        });
    }
}
