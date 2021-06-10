package com.example.virtualtravelapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.activity.DetailExpActivity;
import com.example.virtualtravelapp.adapter.ExpAdapter;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.model.Experience;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpFragment extends Fragment {
	@BindView(R.id.lvExperience) ListView lvExp;
	ArrayList<Experience> listExp;
	ExpAdapter adapter;
	DBManager db;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_experience,container,false);
		ButterKnife.bind(this,view);
		db = new DBManager(getContext());
		getActivity().setTitle("Kinh nghiệm du lịch");
		loadListExp();

		lvExp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent iExp = new Intent(getActivity(), DetailExpActivity.class);
				iExp.putExtra("id", position + 1);
				startActivity(iExp);
			}
		});
		return view;
	}

	public void loadListExp(){
		listExp = db.getExperience();
		setAdapterListView(listExp);
		adapter.notifyDataSetChanged();
	}

	public void setAdapterListView(ArrayList<Experience> list) {
		adapter = new ExpAdapter(getActivity(), list);
		lvExp.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}
