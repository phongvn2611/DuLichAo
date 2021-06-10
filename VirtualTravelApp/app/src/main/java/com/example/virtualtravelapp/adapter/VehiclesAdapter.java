package com.example.virtualtravelapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.model.Vehicle;

import java.util.ArrayList;

public class VehiclesAdapter extends BaseAdapter{
	private ArrayList<Vehicle> list;
	Context context;
	Vehicle vehicles;

	public VehiclesAdapter(Context context, ArrayList<Vehicle> list){
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		return list != null ? list.size() : null;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final PlaceHolder holder;
		View view = convertView;
		if (view == null){
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place,parent,false);
			holder = new PlaceHolder();
			holder.imgPlace = (ImageView) view.findViewById(R.id.imgPlace);
			holder.tvNamePlace = (TextView) view.findViewById(R.id.tvNamePlace);
			view.setTag(holder);
		}else {
			holder = (PlaceHolder) view.getTag();
		}
		holder.tvNamePlace.setText(list.get(position).getName());
		boolean a = list.get(0).getImage().startsWith("/9j/");
		if (a) {
			holder.imgPlace.setImageBitmap(base64toBmp(list.get(position).getImage()));
		}else {
			Glide.with(context)
					.load(list.get(position).getImage())
					.error(R.drawable.im_thumbnail)
					.skipMemoryCache(true)
					.into(holder.imgPlace);
		}
		return view;
	}

	public static class PlaceHolder{
		ImageView imgPlace;
		TextView tvNamePlace;
	}


	public Bitmap base64toBmp(String encodedImage){
		byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		return decodedByte;
	}
}
