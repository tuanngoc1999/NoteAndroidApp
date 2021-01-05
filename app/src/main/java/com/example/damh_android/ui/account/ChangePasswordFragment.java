package com.example.damh_android.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.damh_android.Object.User;
import com.example.damh_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;


public class ChangePasswordFragment extends Fragment {

    Button btnChange;
    TextView currentPwd, newPwd, confirmPwd;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_pwd, container, false);
        currentPwd = root.findViewById(R.id.txtCurrentPwd_changePwdFrag);
        newPwd = root.findViewById(R.id.txtPwd_changePwdFrag);
        confirmPwd = root.findViewById(R.id.txtConfirmPwd_changePwdFrag);
        btnChange = root.findViewById(R.id.btnChange_editPwdFrag);
        Paper.init(getContext());
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePwd();
            }
        });
        return root;
    }
    private void changePwd() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentPassword = currentPwd.getText().toString();
        String newPassword = newPwd.getText().toString();
        String confirmPassword = confirmPwd.getText().toString();
        if(currentPassword.isEmpty())
        {
            currentPwd.setError("Enter password");
            currentPwd.requestFocus();
            return;
        }
        if(newPassword.isEmpty())
        {
            newPwd.setError("Enter new password");
            newPwd.requestFocus();
            return;
        }
        if(confirmPassword.isEmpty())
        {
            confirmPwd.setError("Confirm your password");
            confirmPwd.requestFocus();
            return;
        }
        if(!newPassword.equals(confirmPassword))
        {
            confirmPwd.setError("Password does not match!");
            confirmPwd.requestFocus();
            return;
        }
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Paper.book().write(User.password,newPassword);
                    Toast.makeText(getContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}