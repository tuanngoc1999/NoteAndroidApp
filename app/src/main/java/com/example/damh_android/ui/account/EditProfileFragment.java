package com.example.damh_android.ui.account;


import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.damh_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditProfileFragment extends Fragment {

    DatabaseReference rff = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String uId;
    TextView firstName, name, email;
    Button btnChange;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        uId = mAuth.getUid();
        mAuth = FirebaseAuth.getInstance();
        firstName = root.findViewById(R.id.txtFirstName_editProfileFrag);
        name = root.findViewById(R.id.txtName_editProfileFrag);
        email = root.findViewById(R.id.txtEmail_editProfileFrag);
        btnChange = root.findViewById(R.id.btnChange_editProfileFrag);

        uId = mAuth.getCurrentUser().getUid();
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editName();
            }
        });
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        rff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profileFirstName = snapshot.child("Users").child(uId).child("profile").child("fname").getValue().toString();
                String profileName = snapshot.child("Users").child(uId).child("profile").child("lname").getValue().toString();
                String profilemail = snapshot.child("Users").child(uId).child("profile").child("mail").getValue().toString();
                email.setText(profilemail);
                firstName.setText(profileFirstName);
                name.setText(profileName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    private void editName() {
        String mail = email.getText().toString().trim();
        String fName = firstName.getText().toString().trim();
        String lastName = name.getText().toString().trim();
        if(fName.isEmpty())
        {
            firstName.setError("Enter your first name!");
            firstName.requestFocus();
            return;
        }
        if(lastName.isEmpty())
        {
            name.setError("Enter your name!");
            name.requestFocus();
            return;
        }
        if(mail.isEmpty())
        {
            email.setError("Enter your email!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Invalid email!");
            email.requestFocus();
            return;
        }

        rff.child("Users").child(uId).child("profile").child("fname").setValue(fName);
        rff.child("Users").child(uId).child("profile").child("lname").setValue(lastName);
        rff.child("Users").child(uId).child("profile").child("mail").setValue(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(getContext(), "Edited!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
