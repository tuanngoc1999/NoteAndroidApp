package com.example.damh_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
Button btnSignIn, btnExit;
CheckBox rememberMe;
TextView email, pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.txtMail_loginForm);
        pwd = findViewById(R.id.txtPwd_loginForm);
        rememberMe = findViewById(R.id.checkBox);
        btnSignIn = findViewById(R.id.btnSignIn_loginForm);
        btnExit = findViewById(R.id.btnExit_loginForm);
        btnSignIn.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignIn_loginForm:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            case  R.id.btnExit_loginForm:
        }
    }
}