package com.example.damh_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damh_android.Object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
Button btnSignIn, btnExit;
CheckBox rememberMe;
TextView email, pwd;
private FirebaseAuth mAuth;
FloatingActionButton btnSignUp;
ProgressDialog loadingBar;
CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.txtMail_loginForm);
        pwd = findViewById(R.id.txtPwd_loginForm);
        rememberMe = findViewById(R.id.checkBox);
        btnSignIn = findViewById(R.id.btnSignIn_loginForm);
        btnExit = findViewById(R.id.btnExit_loginForm);
        btnSignUp = findViewById(R.id.floatingActionButton);
        btnSignIn.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        loadingBar = new ProgressDialog(this);
        checkBox = findViewById(R.id.checkBox);
        Paper.init(this);
        String userName = Paper.book().read(User.userName);
        String userPassword = Paper.book().read(User.password);
        if (userName != "" && userPassword != "")
        {
            if (!TextUtils.isEmpty(userName)  &&  !TextUtils.isEmpty(userPassword))
            {
                loadingBar.setTitle("Login in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                logIn(userName, userPassword);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignIn_loginForm:
                signIn();
                break;
            case R.id.floatingActionButton:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case  R.id.btnExit_loginForm:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
        }
    }
    private void signIn(){

        String mail = email.getText().toString().trim();
        String pass = pwd.getText().toString().trim();
        if(mail.isEmpty())
        {
            email.setError("Vui lòng nhập email!");
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
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Vui lòng nhập email hợp lệ!");
            email.requestFocus();
            return;
        }
        if(checkBox.isChecked())
        {
            Paper.book().write(User.userName, mail);
            Paper.book().write(User.password,pass);
        }

        loadingBar.setTitle("Login Account");
        loadingBar.setMessage("Please wait!");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        logIn(mail, pass);
    }
    public void logIn(String mail, String pass){
        mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loadingBar.dismiss();
                    startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                }
                else{
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}