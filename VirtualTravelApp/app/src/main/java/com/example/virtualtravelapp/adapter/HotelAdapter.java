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
import com.example.virtualtravelapp.model.Hotel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotelAdapter extends BaseAdapter{
	private ArrayList<Hotel> list;
	Context context;
	Hotel hotel;

	public HotelAdapter(Context context, ArrayList<Hotel> list) {
		this.context = context;
		this.list = list;
	}

//	public HotelAdapter(Context context, ArrayList<Hotel> list){
//		this.context = context;
//		this.list = list;
//	}
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
		HotelHolder holder;
		if (convertView == null){
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant,parent,false);
			holder = new HotelHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder = (HotelHolder) convertView.getTag();
		}
		holder.bind((Hotel) getItem(position));
		hotel = list.get(position);
		return convertView;
	}

	public class HotelHolder{
		@BindView(R.id.imgRestaurant) ImageView imgRestaurant;
		@BindView(R.id.tvNameR) TextView tvNameR;
		@BindView(R.id.tvPhoneR) TextView tvPhoneR;
		public HotelHolder(View view){
			ButterKnife.bind(this,view);
		}

		public void bind(Hotel hotel){
			tvNameR.setText(hotel.getName());
			tvPhoneR.setText(hotel.getPrice() + " VNĐ");
//			imgRestaurant.setImageResource(hotel.getImage());
			boolean a = list.get(0).getImage().startsWith("/9j/");
			if (a) {
				imgRestaurant.setImageBitmap(base64toBmp(hotel.getImage()));
			}else {
				String[] image = (hotel.getImage()).split(";");
//			new DownloadImageTask(holder.imgPlace).execute(list.get(position).getImage());
//				PicassoClient.downloadImage(context, image[0], imgRestaurant);
				Glide.with(context)
						.load(image[0])
						.error(R.drawable.im_thumbnail)
						.skipMemoryCache(true)
						.into(imgRestaurant);
			}
		}
	}

	public Bitmap base64toBmp(String encodedImage){
		byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		return decodedByte;
	}
}
