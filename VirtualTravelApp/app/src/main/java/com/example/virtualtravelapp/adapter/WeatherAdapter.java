package com.example.virtualtravelapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.model.Weather;
import com.example.virtualtravelapp.widget.PareJSON;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
	private ArrayList<Weather> listWeater;
	private Context context;

	public WeatherAdapter (Context context, ArrayList<Weather> listWeater){
		this.context = context;
		this.listWeater = listWeater;
	}
	@Override
	public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather,parent,false);
		return new WeatherViewHolder(view);
	}

	@Override
	public void onBindViewHolder(WeatherViewHolder holder, int position) {
		Weather weather = listWeater.get(position);
		holder.tvNextName.setText(weather.getDay());
		holder.imNextIcon.setImageResource(PareJSON.getIconIdFromCode(weather.getCode()));
		holder.tvNextCencius.setText(weather.getCencius());
		holder.tvText.setText(weather.getText());
		holder.tvNextDate.setText(weather.getDate());
	}

	@Override
	public int getItemCount() {
		return listWeater.size();
	}

	public class WeatherViewHolder extends RecyclerView.ViewHolder {
		private TextView tvNextName;
		private ImageView imNextIcon;
		private TextView tvNextCencius;
		private TextView tvText;
		private TextView tvNextDate;
		public WeatherViewHolder(View itemView) {
			super(itemView);
			tvNextName = (TextView) itemView.findViewById(R.id.nextName);
			imNextIcon = (ImageView) itemView.findViewById(R.id.nextIcon);
			tvNextCencius = (TextView) itemView.findViewById(R.id.nextCencius);
			tvText = (TextView) itemView.findViewById(R.id.nextText);
			tvNextDate = (TextView) itemView.findViewById(R.id.nextDate);
		}
	}
}
