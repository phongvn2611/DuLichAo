package com.example.virtualtravelapp.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.adapter.ViewPagerAdapter;

import butterknife.ButterKnife;

public class DiaDanhFragment extends Fragment {
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private MienBacFragment mienBacFragment;
	private MienTrungFragment mienTrungFragment;
	private MienNamFragment mienNamFragment;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_diadanh,container,false);
		ButterKnife.bind(this,view);
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		tabLayout = (TabLayout) view.findViewById(R.id.tabs);
		getActivity().setTitle("Địa danh Việt Nam");
		setViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);

		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				switch(tab.getPosition()) {
					case 0:
						mienBacFragment = new MienBacFragment();
						break;
					case 1:
						mienTrungFragment = new MienTrungFragment();
						break;
					case 2:
						mienNamFragment = new MienNamFragment();
						break;
				}
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		return view;
	}


	public void setViewPager(ViewPager viewPager){
		//getChild vì do fragment lồng nhau,ta phải gọi tới thằng con
		ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
		mienBacFragment = new MienBacFragment();
		mienTrungFragment = new MienTrungFragment();
		mienNamFragment = new MienNamFragment();
		adapter.addFragment(mienBacFragment, "Miền Bắc");
		adapter.addFragment(mienTrungFragment, "Miền Trung");
		adapter.addFragment(mienNamFragment, "Miền Nam");
		viewPager.setAdapter(adapter);

	}
}
