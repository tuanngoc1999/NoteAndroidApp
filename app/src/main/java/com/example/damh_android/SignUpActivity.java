package com.example.damh_android;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSignIn, btnSignUp;
    EditText email, pwd, confirmPwd;
    DatabaseReference rff = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.txtMail_signUpForm);
        pwd = findViewById(R.id.txtPwd_signUpForm);
        confirmPwd = findViewById(R.id.txtConfirmPwd_signUpForm);
        btnSignIn = findViewById(R.id.btnSignin_signUpForm);
        btnSignUp = findViewById(R.id.btnSignUp_signUpForm);
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignin_signUpForm:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                break;
            case R.id.btnSignUp_signUpForm:
                regisUser();
                break;
        }
    }
    private void regisUser(){
        String mail = email.getText().toString().trim();
        String pass = pwd.getText().toString().trim();
        String pass2 = confirmPwd.getText().toString().trim();
        if(mail.isEmpty())
        {
            email.setError("Vui lòng nhập email!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Vui lòng nhập email hợp lệ!");
            email.requestFocus();
            return;
        }
        if(pass.isEmpty())
        {
            pwd.setError("Vui lòng nhập mật khẩu!");
            pwd.requestFocus();
            return;
        }
        if(pass.length() < 6)
        {
            pwd.setError("Mật khẩu phải trên 6 ký tự!");
            pwd.requestFocus();
            return;
        }
        if(!pass.equals(pass2))
        {
            confirmPwd.setError("Mật khẩu không trùng khớp!");
            confirmPwd.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    rff.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile").child("mail").setValue(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            }
                            else
                                Toast.makeText(SignUpActivity.this, "Đăng ký không thành công! Vui lòng thử lại!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                    Toast.makeText(SignUpActivity.this, "Đăng ký không thành công! Vui lòng thử lại!", Toast.LENGTH_LONG).show();

            }
        });
    }
}
