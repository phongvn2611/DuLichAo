package com.example.virtualtravelapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.model.User;

public class AccountFragment extends Fragment {

    EditText edtAccountName, edtAccountEmail, edtAccountPhone, edtAccountPassword;
    Button btnAccountChange;
    DBManager database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        getActivity().setTitle("Thông tin tài khoản");
        edtAccountName = (EditText) view.findViewById(R.id.edtAccountName);
        edtAccountEmail = (EditText) view.findViewById(R.id.edtAccountEmail);
        edtAccountPhone = (EditText) view.findViewById(R.id.edtAccountPhone);
        edtAccountPassword = (EditText) view.findViewById(R.id.edtAccountPassword);
        btnAccountChange = (Button) view.findViewById(R.id.btnAccountChange);
        database = new DBManager(getActivity());
        Bundle bundle = getActivity().getIntent().getExtras();
        String username = bundle.getString("Username");
        GetAccount(username);
        btnAccountChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAccount(username);
            }
        });
        return view;
    }

    private void GetAccount(String username) {
        User user = database.getUserByUsername(username);
        edtAccountName.setText(user.getName());
        edtAccountEmail.setText(user.getEmail());
        edtAccountPhone.setText(user.getPhone());
        edtAccountPassword.setText(user.getPassword());
    }

    private void UpdateAccount(String username) {
        String name = edtAccountName.getText().toString().trim();
        String email = edtAccountEmail.getText().toString().trim();
        String phone = edtAccountPhone.getText().toString().trim();
        String password = edtAccountPassword.getText().toString().trim();
        if (name == "" || email == "" || phone == "" || password == "") {
            Toast.makeText(getActivity(), "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
        else {
            database.openDataBase();
            int result = database.editUser(name, username, password, email, phone);
            if (result == 1) {
                Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
