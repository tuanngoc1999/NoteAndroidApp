package com.example.damh_android.ui.gallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.damh_android.Object.Gallery;
import com.example.damh_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GalleryFragment extends Fragment {

    ListView listView;
    DatabaseReference rff = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<Gallery> galleryList = new ArrayList<Gallery>();
    GalleryAdapter galleryAdapter = null;
    String uId;
    FloatingActionButton addGal;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        uId = mAuth.getUid();
        galleryAdapter = new GalleryAdapter();
        addGal = root.findViewById(R.id.addGal_fragment);
        addGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog_edit_save, null);
                final EditText etUsername = (EditText) alertLayout.findViewById(R.id.et_Username);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Add Category");
                alert.setView(alertLayout);
                alert.setCancelable(true);
                alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.setNegativeButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = etUsername.getText().toString();
                        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        Gallery g = new Gallery(user, mydate);
                        String path = user + mydate;
                        rff.child("Users").child(uId).child("gallery").child(path).setValue(g).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Toast.makeText(getContext(), "Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

            }
        });
        listView = root.findViewById(R.id.lvGalleryFrag);
        listView.setAdapter(galleryAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                final String[] choise = {"Edit", "Delete"};
                b.setItems(choise, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String choised = choise[which];
                        if(choised.equals("Edit")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            final EditText input = new EditText(getContext());
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            alertDialog.setView(input);
                            alertDialog.setTitle("Edit Category");
                            alertDialog.setNegativeButton("EDIT",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String newName = input.getText().toString().trim();
                                            if (newName.isEmpty()) {
                                                input.setError("Enter category name");
                                                input.requestFocus();
                                            }
                                            else{
                                                Gallery g = galleryList.get(position);
                                                String path = g.getName()+g.getCreatDate();
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(uId).child("gallery").child(path).removeValue();
                                                g = new Gallery(newName, g.getCreatDate());
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(uId).child("gallery").child(newName+g.getCreatDate()).setValue(g).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getContext(), "Successfully!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });

                            alertDialog.setPositiveButton("CANCEL",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            alertDialog.show();
                        }
                        else if(choised.equals("Delete"))
                        {
                            Gallery g = galleryList.get(position);
                            String path = g.getName() + g.getCreatDate();
                            rff.child("Users").child(uId).child("gallery").child(path).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext()," Deleted!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                AlertDialog alert = b.create();
                alert.show();
                return false;
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
                galleryList.clear();
                for(DataSnapshot dataSnapshot : snapshot.child("Users").child(uId).child("gallery").getChildren()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String cDate = dataSnapshot.child("creatDate").getValue().toString();
                    Gallery g = new Gallery(name, cDate);
                    galleryList.add(g);
                }
                galleryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    class GalleryAdapter extends ArrayAdapter {

        public GalleryAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        public GalleryAdapter() {
            super(getActivity(),android.R.layout.simple_list_item_1, galleryList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.row, null);
            Gallery g = galleryList.get(position);
            ((TextView)row.findViewById(R.id.name_row)).setText(g.getName());
            ((TextView)row.findViewById(R.id.creatDate_row)).setText(g.getCreatDate());
            return row;
        }
    }
}