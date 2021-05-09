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
import com.example.virtualtravelapp.activity.DetailExp;
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
				Intent iExp = new Intent(getActivity(), DetailExp.class);
				iExp.putExtra("id", position + 1);
				startActivity(iExp);
			}
		});
		return view;
	}

	public void loadListExp(){
		listExp = db.getExperience();
//		Experience experience;
//
//		String[] doAn = {"Gà đồi hấp", "Sườn xào chua ngọt", "Bún chả", "Phở Bò", "Cháo lòng", "Chả đa nem", "Phở cuốn"};
//		for (int i = 0; i < doAn.length; i++) {
//			experience = new Experience(doAn[i], R.drawable.im_thitbo, "Một quán cafe nhỏ nằm nhô hẳn ra mặt đường nhựa và ngay trên các vách núi với tầm nhìn vô cùng đẹp cũng như không gian thoáng mát. Quán không có mái che nhưng cho dù ngồi ngay dưới trời nắng cũng không quá khó chịu bởi không khí mát mẻ từ dưới vách núi thổi lên. Một điểm trừ nữa là quán hơi ít bàn và nhân viên cũng không chủ động sắp xếp bàn cho bạn thế nên các bạn nếu muốn có chỗ đẹp thì nên bố trí lên sớm để “giành” chỗ nhé.");
//			list.add(experience);
//		}
		setAdapterListView(listExp);
		adapter.notifyDataSetChanged();
	}

	public void setAdapterListView(ArrayList<Experience> list) {
		adapter = new ExpAdapter(getActivity(), list);
		lvExp.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}
