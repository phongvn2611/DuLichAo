package com.example.virtualtravelapp.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.activity.DiaDanhActivity;
import com.example.virtualtravelapp.adapter.DiaDanhAdapter;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.model.DiaDanh;
import com.example.virtualtravelapp.widget.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MienNamFragment extends Fragment {
	private RecyclerView recyclerView;
	DBManager db;
	ArrayList<DiaDanh> listDiaDanh;

	public EditText etSearch;
	@BindView(R.id.tvEmpty) TextView tvEmpty;
	private final int REQ_CODE_SPEECH_INPUT = 100;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_miennam, container, false);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
		etSearch = (EditText) view.findViewById(R.id.etSearch);
		ButterKnife.bind(this,view);
		db = new DBManager(getContext());
		textChange();
		listDiaDanh = db.getDiaDanhID(3);
		initViews();
		recyclerView.addOnItemTouchListener(
				new RecyclerTouchListener(getContext(), recyclerView ,new RecyclerTouchListener.OnItemClickListener() {
					@Override public void onItemClick(View view, int position) {
						Intent intent = new Intent(getActivity(), DiaDanhActivity.class);
						intent.putExtra("id_diadanh", listDiaDanh.get(position).getIdDiaDanh());
						startActivity(intent);
					}

					@Override public void onLongItemClick(View view, int position) {
						// do whatever
					}
				})
		);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}

	public void initViews(){
		recyclerView.setHasFixedSize(true);
		RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
		recyclerView.setLayoutManager(layoutManager);
		setAdapterListView(listDiaDanh);
	}
	public void setAdapterListView(ArrayList<DiaDanh> list){
		DiaDanhAdapter adapter = new DiaDanhAdapter(getActivity(), list);
		recyclerView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	@OnClick(R.id.ibtSpeech)
	public void speech(){
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_somthing));
		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);;
		}catch (ActivityNotFoundException a){
			Toast.makeText(getActivity(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
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
				listDiaDanh = db.searchDiaDanh(String.valueOf(s),3);
				if (listDiaDanh.size() == 0){
					tvEmpty.setVisibility(View.VISIBLE);
				} else {
					tvEmpty.setVisibility(View.GONE);
				}
				setAdapterListView(listDiaDanh);
				//adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
//		etSearch.setText("");
	}
}
