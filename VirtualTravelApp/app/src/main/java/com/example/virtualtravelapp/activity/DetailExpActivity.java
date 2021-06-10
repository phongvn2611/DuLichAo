package com.example.virtualtravelapp.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.model.Experience;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailExpActivity extends AppCompatActivity {
    @BindView(R.id.tvExp) TextView tvExp;
    @BindView(R.id.imExp) ImageView imExp;
    @BindView(R.id.tvDetail) TextView tvDetail;

    private int id;
    private DBManager db;
    private Experience experience;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_experience);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DBManager(this);
        id = getIntent().getIntExtra("id", 1);
        experience = db.getExperienceDetail(id);
        getSupportActionBar().setTitle(experience.getName());
//        tvExp.setText(experience.getName());
        tvDetail.setText(Html.fromHtml(experience.getDetail()));
//        PicassoClient.downloadImage(getApplicationContext(), experience.getImage(), imExp);
        Glide.with(getApplicationContext())
                .load(experience.getImage())
                .error(R.drawable.im_thumbnail)
                .skipMemoryCache(true)
                .into(imExp);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return true;
    }
}
