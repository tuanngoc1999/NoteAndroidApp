package com.example.damh_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSignIn, btnExit;
    TextView email, pwd, confirmPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.txtMail_loginForm);
        pwd = findViewById(R.id.txtPwd_loginForm);
        btnSignIn = findViewById(R.id.btnSignIn_loginForm);
        btnExit = findViewById(R.id.btnExit_loginForm);
        btnSignIn.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignin_signUpForm:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

            case  R.id.btnSignUp_signUpForm:
        }
    }
}