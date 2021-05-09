package com.example.virtualtravelapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.database.DBManager;

public class SignupActivity extends AppCompatActivity {
    EditText edtSignupName, edtSignupPhone, edtSignupEmail, edtSignupUsername, edtSignupPassword, edtSignupConfirmPassword;
    Button btnSignupSignup, btnSignupExit;
    DBManager database;

    private void Mapping() {
        edtSignupName = (EditText) findViewById(R.id.edtSignupName);
        edtSignupPhone = (EditText) findViewById(R.id.edtSignupPhone);
        edtSignupEmail = (EditText) findViewById(R.id.edtSignupEmail);
        edtSignupUsername = (EditText) findViewById(R.id.edtSignupUsername);
        edtSignupPassword = (EditText) findViewById(R.id.edtSignupPassword);
        edtSignupConfirmPassword = (EditText) findViewById(R.id.edtSignupConfirmPassword);
        btnSignupSignup = (Button) findViewById(R.id.btnSignupSignup);
        btnSignupExit = (Button) findViewById(R.id.btnSignupExit);
    }

    private void Signup() {
        String username = edtSignupUsername.getText().toString().trim();
        String password = edtSignupPassword.getText().toString().trim();
        String name = edtSignupName.getText().toString().trim();
        String phone = edtSignupPhone.getText().toString().trim();
        String email = edtSignupEmail.getText().toString().trim();
        String confirmPassword = edtSignupConfirmPassword.getText().toString().trim();

        if (username == "" || password == "" ||
                name == "" || phone =="" || email == "") {
            Toast.makeText(SignupActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
        else {
            if (password.equals(confirmPassword)) {
                int check = database.checkUsername(username);
                if (check == 0) {
                    database.openDataBase();
                    int insert = database.addUser(name, username, password, email, phone, 2);
                    if (insert == 1) {
                        Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    }
                    else {
                        Toast.makeText(SignupActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SignupActivity.this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(SignupActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                edtSignupPassword.setText("");
                edtSignupConfirmPassword.setText("");
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Mapping();
        database = new DBManager(this);
        btnSignupSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup();
            }
        });
        btnSignupExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
