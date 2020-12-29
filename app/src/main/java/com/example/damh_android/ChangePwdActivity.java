package com.example.damh_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChangePwdActivity extends AppCompatActivity implements View.OnClickListener {
Button btnChange, btnHome;
TextView currentPwd, newPwd, confirmPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        currentPwd = findViewById(R.id.txtCurrentPwd_changePwdForm);
        newPwd = findViewById(R.id.txtPwd_changePwdForm);
        confirmPwd = findViewById(R.id.txtConfirmPwd_changePwdForm);
        btnChange = findViewById(R.id.btnChange_changePwdForm);
        btnHome = findViewById(R.id.btnHome_changePwdForm);
        btnChange.setOnClickListener(this);
        btnHome.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnHome_changePwdForm:
                startActivity(new Intent(ChangePwdActivity.this, MainActivity.class));

            case  R.id.btnChange_changePwdForm:
        }
    }
}