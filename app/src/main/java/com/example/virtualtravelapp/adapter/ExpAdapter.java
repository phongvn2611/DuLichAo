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
import com.example.virtualtravelapp.model.Experience;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpAdapter extends BaseAdapter{
	private ArrayList<Experience> list;
	Context context;
	Experience experience;
	public ExpAdapter(Context context, ArrayList<Experience> list){
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
	public View getView(int position, View view, ViewGroup parent) {
		ExpHolder holder;
		if (view == null){
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_experience, parent, false);
			holder = new ExpHolder(view);
			view.setTag(holder);
		}else {
			holder = (ExpHolder) view.getTag();
		}
		holder.tvExp.setText(list.get(position).getName());
		boolean a = list.get(0).getImage().startsWith("/9j/");
		if (a) {
			holder.imExp.setImageBitmap(decodeBase64(list.get(position).getImage()));
		}else {
//			new DownloadImageTask(holder.imgPlace).execute(list.get(position).getImage());
//			PicassoClient.downloadImage(context, list.get(position).getImage(), holder.imExp);
			Glide.with(context)
					.load(list.get(position).getImage())
					.error(R.drawable.im_thumbnail)
					.skipMemoryCache(true)
					.into(holder.imExp);
		}
		return view;
	}

	public class ExpHolder{
		@BindView(R.id.tvExp) TextView tvExp;
		@BindView(R.id.imExp) ImageView imExp;

		public ExpHolder(View view){
			ButterKnife.bind(this,view);
		}
	}

	public static Bitmap decodeBase64(String input) {
		byte[] decodedBytes = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}
}
