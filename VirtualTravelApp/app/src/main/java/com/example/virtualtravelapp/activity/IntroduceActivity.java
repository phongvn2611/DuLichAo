package com.example.virtualtravelapp.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.model.DiaDanh;
import com.example.virtualtravelapp.model.Introduce;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroduceActivity extends AppCompatActivity{
	private int id_diadanh;
	DBManager db;
	private Introduce introduce;
	@BindView(R.id.tvIntro) TextView tvIntro;
	@BindView(R.id.tvTitleIntro) TextView tvTitleIntro;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduce);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ButterKnife.bind(this);
		id_diadanh = getIntent().getIntExtra("id_diadanh", 0);
		db = new DBManager(this);
		DiaDanh diaDanh = db.getDiaDanhDetail(id_diadanh);
		introduce = db.getIntroID(id_diadanh);
		tvTitleIntro.setText(diaDanh.getNameDiaDanh());
		if (introduce.getIntro() != null) {
			tvIntro.setText(Html.fromHtml(introduce.getIntro()));
		}
		else {
			Toast.makeText(IntroduceActivity.this, "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
		}
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
