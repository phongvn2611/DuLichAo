package com.example.virtualtravelapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.activity.UpdateDiaDanhActivity;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.model.DiaDanh;

import java.util.ArrayList;

public class AdminDiaDanhAdapter extends BaseAdapter {
    Context context;
    ArrayList<DiaDanh> list;

    public AdminDiaDanhAdapter(Context context, ArrayList<DiaDanh> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_admindiadanh, null);
        TextView tvDiaDanh = view.findViewById(R.id.tv_AdminDiaDanhLocation);
        Button btnSua = view.findViewById(R.id.btn_AdminDiaDanhEdit);
        Button btnXoa = view.findViewById(R.id.btn_AdminDiaDanhDelete);
        DiaDanh diaDanh = list.get(position);
        tvDiaDanh.setText(diaDanh.getNameDiaDanh() + "");
        DBManager db = new DBManager(context);
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateDiaDanhActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ID_DiaDanh", diaDanh.getIdDiaDanh());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn chắc chắn muốn xóa địa điểm này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int result = db.deleteDiaDanh(diaDanh.getIdDiaDanh());
                        list.clear();
                        if (result == 1) {
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                        }

                        list = db.getDiaDanh();
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }
}
