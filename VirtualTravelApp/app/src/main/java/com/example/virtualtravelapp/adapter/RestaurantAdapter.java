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
import com.example.virtualtravelapp.model.Restaurant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantAdapter extends BaseAdapter{
	private ArrayList<Restaurant> list;
	Context context;
	Restaurant restaurant;

	public RestaurantAdapter(Context context, ArrayList<Restaurant> list){
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
		RestaurantHolder holder;
		if (convertView == null){
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant,parent,false);
			holder = new RestaurantHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder = (RestaurantHolder) convertView.getTag();
		}
		holder.bind((Restaurant) getItem(position));
		restaurant = list.get(position);
		return convertView;
	}

		public class RestaurantHolder{
		@BindView(R.id.imgRestaurant) ImageView imgRestaurant;
		@BindView(R.id.tvNameR) TextView tvNameR;
		@BindView(R.id.tvPhoneR) TextView tvPhoneR;
		public RestaurantHolder(View view){
			ButterKnife.bind(this,view);
		}

		public void bind(Restaurant restaurant){
			tvNameR.setText(restaurant.getName());
			if (restaurant.getPhone().equals("0")){
				tvPhoneR.setVisibility(View.GONE);
			}else {
				tvPhoneR.setText("SĐT: " + restaurant.getPhone());
			}
//			imgRestaurant.setImageResource(restaurant.getImage());
			boolean a = list.get(0).getImage().startsWith("/9j/");
			if (a) {
				imgRestaurant.setImageBitmap(base64toBmp(restaurant.getImage()));
			}else {
				String[] image = (restaurant.getImage()).split(";");
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
