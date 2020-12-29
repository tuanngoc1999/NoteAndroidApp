package com.example.damh_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnChange, btnHome;
    TextView firstName, name, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firstName = findViewById(R.id.txtFirstName_editProfileForm);
        name = findViewById(R.id.txtName_editProfileForm);
        email = findViewById(R.id.txtEmail_editProfileForm);
        btnChange = findViewById(R.id.btnChange_editProfileForm);
        btnHome = findViewById(R.id.btnHome_editProfileForm);
        btnChange.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnHome_editProfileForm:
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
            case R.id.btnChange_editProfileForm:
        }

    }
}