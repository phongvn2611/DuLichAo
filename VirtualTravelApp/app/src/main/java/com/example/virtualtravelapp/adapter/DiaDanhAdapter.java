package com.example.virtualtravelapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.model.DiaDanh;

import java.util.ArrayList;

public class DiaDanhAdapter extends RecyclerView.Adapter<DiaDanhAdapter.DiaDanhViewHolder> {
	private ArrayList<DiaDanh> list;
	private Context context;

	public class DiaDanhViewHolder extends RecyclerView.ViewHolder{
		public TextView tvName;
		public ImageView imImage;
		public DiaDanhViewHolder(View view){
			super(view);
			tvName = (TextView) view.findViewById(R.id.tvDiaDanh);
			imImage = (ImageView) view.findViewById(R.id.imDiaDanh);
		}
	}
	public DiaDanhAdapter(Context context,ArrayList<DiaDanh> list){
		this.context = context;
		this.list = list;
	}
	@Override
	public DiaDanhViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_diadanh, parent, false);

		return new DiaDanhViewHolder(view);
	}

	@Override
	public void onBindViewHolder(DiaDanhViewHolder holder, int position) {
		DiaDanh diaDanh = list.get(position);
		holder.tvName.setText(diaDanh.getNameDiaDanh());
		boolean a = list.get(0).getImDiaDanh().startsWith("/9j/");
		if (a) {
			holder.imImage.setImageBitmap(decodeBase64(diaDanh.getImDiaDanh()));
		}else {
			String[] image = (diaDanh.getImDiaDanh()).split(";");
			Glide.with(context)
					.load(image[0])
					.error(R.drawable.im_thumbnail)
					.skipMemoryCache(true)
					.into(holder.imImage);
		}
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public static Bitmap decodeBase64(String input) {
		byte[] decodedBytes = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}
}
