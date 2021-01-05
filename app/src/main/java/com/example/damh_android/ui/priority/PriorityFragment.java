package com.example.damh_android.ui.priority;

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

import com.example.damh_android.Object.Priority;
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

public class PriorityFragment extends Fragment {
    ListView listView;
    DatabaseReference rff = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<Priority> priorityList = new ArrayList<Priority>();
    PriorityAdapter priorityAdapter = null;
    String uId;
    FloatingActionButton addPrio;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_priority, container, false);
        uId = mAuth.getUid();
        priorityAdapter = new PriorityAdapter();
        addPrio = root.findViewById(R.id.addPrio_fragment);
        addPrio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog_edit_save, null);
                final EditText etUsername = (EditText) alertLayout.findViewById(R.id.et_Username);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Add Priority");
                alert.setView(alertLayout);
                alert.setCancelable(true);
                alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = etUsername.getText().toString();
                        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        Priority p = new Priority(user, mydate);
                        String path = user + mydate;
                        rff.child("Users").child(uId).child("priority").child(path).setValue(p);
                        Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
        listView = root.findViewById(R.id.lvPriorityFrag);
        listView.setAdapter(priorityAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                final String[] choise = {"Edit", "Delete"};
                b.setItems(choise, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String choised = choise[which];
                        if(choised.equals("Edit")){
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            final EditText input = new EditText(getContext());
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            alertDialog.setView(input);
                            alertDialog.setTitle("Edit Priority");
                            alertDialog.setNegativeButton("EDIT",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String newName = input.getText().toString();
                                            if (newName.isEmpty()) {
                                                input.setError("Enter priority name");
                                                input.requestFocus();
                                            }
                                            else{
                                                Priority priority = priorityList.get(position);
                                                String path = priority.getName()+priority.getCreatDate();
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(uId).child("priority").child(path).removeValue();
                                                priority = new Priority(newName, priority.getCreatDate());
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(uId).child("priority").child(newName+priority.getCreatDate()).setValue(priority);

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
                            Priority p = priorityList.get(position);
                            String path = p.getName() + p.getCreatDate();
                            rff.child("Users").child(uId).child("priority").child(path).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext()," Deleted!",
                                            Toast.LENGTH_SHORT).show();

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
                priorityList.clear();
                for(DataSnapshot dataSnapshot : snapshot.child("Users").child(uId).child("priority").getChildren()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String cDate = dataSnapshot.child("creatDate").getValue().toString();
                    Priority priority = new Priority(name, cDate);
                    priorityList.add(priority);
                }
                priorityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    class PriorityAdapter extends ArrayAdapter {

        public PriorityAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        public PriorityAdapter() {
            super(getActivity(),android.R.layout.simple_list_item_1, priorityList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.row, null);
            Priority priority = priorityList.get(position);
            ((TextView)row.findViewById(R.id.name_row)).setText(priority.getName());
            ((TextView)row.findViewById(R.id.creatDate_row)).setText(priority.getCreatDate());

            return row;
        }
    }
}
