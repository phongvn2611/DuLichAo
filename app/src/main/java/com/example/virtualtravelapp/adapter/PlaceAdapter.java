package com.example.virtualtravelapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.model.Place;

import java.io.InputStream;
import java.util.ArrayList;

public class PlaceAdapter extends BaseAdapter{
	private ArrayList<Place> list;
	Context context;
	Place place;

	public PlaceAdapter(Context context, ArrayList<Place> list){
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
			holder.imgPlace.setImageBitmap(decodeBase64(list.get(position).getImage()));
		}else {
//			new DownloadImageTask(holder.imgPlace).execute(list.get(position).getImage());
//			PicassoClient.downloadImage(context, list.get(position).getImage(), holder.imgPlace);
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

	//ham download image
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap bitmap = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				bitmap = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	public static Bitmap decodeBase64(String input) {
		byte[] decodedBytes = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}
}
