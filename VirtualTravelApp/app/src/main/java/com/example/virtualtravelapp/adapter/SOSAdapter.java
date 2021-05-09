package com.example.virtualtravelapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.virtualtravelapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SOSAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> listSOSDetail;
    Context context;

    public SOSAdapter(Context context, ArrayList<HashMap<String, String>> listSOSDetail) {
        this.context = context;
        this.listSOSDetail = listSOSDetail;
    }

    @Override
    public int getCount() {
        return listSOSDetail != null ? listSOSDetail.size() : null;
    }

    @Override
    public Object getItem(int position) {
        return listSOSDetail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SOSHolder holder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sos, parent, false);
            holder = new SOSHolder();

            holder.tvNameSOS = (TextView) view.findViewById(R.id.tvNameSOS);
            holder.tvAddressSOS = (TextView) view.findViewById(R.id.tvAddressSOS);
            holder.tvPhoneSOS = (TextView) view.findViewById(R.id.tvPhoneSOS);
            holder.imSOS = (ImageView) view.findViewById(R.id.imSOSPhone);
            holder.imSOSMap = (ImageView) view.findViewById(R.id.imSOSMap);
            holder.rlSOS = (LinearLayout) view.findViewById(R.id.rlSOS);
            view.setTag(holder);
        } else {
            holder = (SOSHolder) view.getTag();
        }
        //chua hieu vi sao them cai dong nay vao lai load du het
        if (listSOSDetail.get(position).size() != 0) {
            holder.rlSOS.setVisibility(View.VISIBLE);
            holder.tvNameSOS.setText(listSOSDetail.get(position).get("name"));
            if (listSOSDetail.get(position).get("formatted_address") != null) {
                holder.tvAddressSOS.setText(listSOSDetail.get(position).get("formatted_address"));
            }else {
                holder.tvAddressSOS.setText(listSOSDetail.get(position).get("vicinity"));
            }
            if (listSOSDetail.get(position).get("formatted_phone_number") != null) {
                holder.tvPhoneSOS.setVisibility(View.VISIBLE);
                holder.tvPhoneSOS.setText("SĐT: " + listSOSDetail.get(position).get("formatted_phone_number"));
                holder.imSOS.setVisibility(View.VISIBLE);
                holder.imSOSMap.setVisibility(View.GONE);
            } else {
                holder.imSOSMap.setVisibility(View.VISIBLE);
                holder.tvPhoneSOS.setVisibility(View.GONE);
                holder.imSOS.setVisibility(View.GONE);
            }
        }else {
            holder.rlSOS.setVisibility(View.GONE);
        }
        return view;
    }

    public static class SOSHolder {
        TextView tvNameSOS;
        TextView tvAddressSOS;
        TextView tvPhoneSOS;
        ImageView imSOS;
        ImageView imSOSMap;
        LinearLayout rlSOS;
    }
}
