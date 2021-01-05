package com.example.damh_android.ui.note;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.damh_android.Object.Gallery;
import com.example.damh_android.Object.Note;
import com.example.damh_android.Object.Priority;
import com.example.damh_android.Object.Status;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NoteFragment extends Fragment {
    ListView listView;
    DatabaseReference rff = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<Note> noteList = new ArrayList<Note>();
    List<Status> statusList = new ArrayList<Status>();
    List<Priority> priorityList = new ArrayList<Priority>();
    List<Gallery> galleryList = new ArrayList<Gallery>();
    NoteAdapter noteAdapter = null;
    String uId;
    FloatingActionButton addNote;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);
        uId = mAuth.getUid();
        noteAdapter = new NoteAdapter();
        addNote = root.findViewById(R.id.addNote_fragment);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.add_note_layout, null);
                final EditText etUsername = (EditText) alertLayout.findViewById(R.id.et_Username);
                Spinner gallerySpinner = (Spinner)alertLayout.findViewById(R.id.spinnerCategory);
                Spinner prioritySpinner = (Spinner)alertLayout.findViewById(R.id.spinnerPriority);
                Spinner statusSpinner = (Spinner)alertLayout.findViewById(R.id.spinnerStatus);
                Button pickDate = (Button)alertLayout.findViewById(R.id.pickDate);
                TextView planDate = (TextView)alertLayout.findViewById(R.id.planDate_noteForm);
                pickDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                planDate.setText(selectedDate);
                            }
                        }, year, month, day);
                        datePickerDialog.show();
                    }
                });
                String pickedStatus = "";
                String pickedPriority= "";
                String pickeGallery = "";
                List<String> statusNameList = new ArrayList<String>();
                statusNameList.add("Select status");
                for (Status s: statusList) {
                    statusNameList.add(s.getName());
                }
                List<String> priorityNameList = new ArrayList<String>();
                priorityNameList.add("Select priority");
                for (Priority priority: priorityList) {
                    priorityNameList.add(priority.getName());
                }
                List<String> galleryNameList = new ArrayList<String>();
                galleryNameList.add("Select category");
                for (Gallery gallery:galleryList) {
                    galleryNameList.add(gallery.getName());
                }
                ArrayAdapter<String> statusArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, statusNameList);
                statusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                statusSpinner.setAdapter(statusArrayAdapter);
                ArrayAdapter<String> galleryArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, galleryNameList);
                galleryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gallerySpinner.setAdapter(galleryArrayAdapter);
                ArrayAdapter<String> priorityArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, priorityNameList);
                priorityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                prioritySpinner.setAdapter(priorityArrayAdapter);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Add Note");
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
                        String priority="";
                        if(prioritySpinner.getSelectedItemPosition() > 0)
                            priority = priorityList.get(prioritySpinner.getSelectedItemPosition() - 1).getName()+priorityList.get(prioritySpinner.getSelectedItemPosition() - 1).getCreatDate();
                        String status ="";
                        if(statusSpinner.getSelectedItemPosition() > 0)
                            status = statusList.get(statusSpinner.getSelectedItemPosition() - 1).getName()+statusList.get(statusSpinner.getSelectedItemPosition() - 1).getCreatDate();
                        String gallery ="";
                        if(gallerySpinner.getSelectedItemPosition() > 0)
                            gallery =  galleryList.get(gallerySpinner.getSelectedItemPosition() - 1).getName()+galleryList.get(gallerySpinner.getSelectedItemPosition() - 1).getCreatDate();
                        String selectedPlanDate = "";
                        if(!planDate.getText().toString().equals("Select plan date"))
                            selectedPlanDate = planDate.getText().toString();
                        Note note = new Note(user, mydate, status, priority, gallery, selectedPlanDate);
                        String path = user + mydate;
                        rff.child("Users").child(uId).child("note").child(path).setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
        listView = root.findViewById(R.id.lvNoteFrag);
        listView.setAdapter(noteAdapter);
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
                            LayoutInflater inflater = getLayoutInflater();
                            View alertLayout = inflater.inflate(R.layout.add_note_layout, null);
                            final EditText etUsername = (EditText) alertLayout.findViewById(R.id.et_Username);
                            Spinner gallerySpinner = (Spinner)alertLayout.findViewById(R.id.spinnerCategory);
                            Spinner prioritySpinner = (Spinner)alertLayout.findViewById(R.id.spinnerPriority);
                            Spinner statusSpinner = (Spinner)alertLayout.findViewById(R.id.spinnerStatus);
                            Button pickDate = (Button)alertLayout.findViewById(R.id.pickDate);
                            TextView planDate = (TextView)alertLayout.findViewById(R.id.planDate_noteForm);
                            Note note = noteList.get(position);
                            etUsername.setText(note.getName());
                            planDate.setText(note.getPlanDate());
                            pickDate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Calendar calendar = Calendar.getInstance();
                                    int year = calendar.get(Calendar.YEAR);
                                    int month = calendar.get(Calendar.MONTH);
                                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                            planDate.setText(selectedDate);
                                            //Toast.makeText(NoteActivity.this, selectedDate[0], Toast.LENGTH_SHORT).show();
                                        }
                                    }, year, month, day);
                                    datePickerDialog.show();
                                }
                            });
                            String pickedStatus = "";
                            String pickedPriority= "";
                            String pickeGallery = "";
                            List<String> statusNameList = new ArrayList<String>();
                            statusNameList.add("Select status");
                            for (Status s: statusList) {
                                statusNameList.add(s.getName());
                            }
                            List<String> priorityNameList = new ArrayList<String>();
                            priorityNameList.add("Select priority");
                            for (Priority priority: priorityList) {
                                priorityNameList.add(priority.getName());
                            }
                            List<String> galleryNameList = new ArrayList<String>();
                            galleryNameList.add("Select category");
                            for (Gallery gallery:galleryList) {
                                galleryNameList.add(gallery.getName());
                            }
                            ArrayAdapter<String> statusArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, statusNameList);
                            statusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            statusSpinner.setAdapter(statusArrayAdapter);
                            ArrayAdapter<String> galleryArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, galleryNameList);
                            galleryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            gallerySpinner.setAdapter(galleryArrayAdapter);
                            ArrayAdapter<String> priorityArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, priorityNameList);
                            priorityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            prioritySpinner.setAdapter(priorityArrayAdapter);
                            for (int position = 0; position < statusList.size(); position++) {
                                if ((statusList.get(position).getName() + statusList.get(position).getCreatDate()).equals(note.getStatusId())) {
                                    statusSpinner.setSelection(position+1);
                                }
                            }
                            for (int position = 0; position < priorityList.size(); position++) {
                                if ((priorityList.get(position).getName() + priorityList.get(position).getCreatDate()).equals(note.getPriorityId())) {
                                    prioritySpinner.setSelection(position+1);
                                }
                            }
                            for (int position = 0; position < galleryList.size(); position++) {
                                if ((galleryList.get(position).getName() + galleryList.get(position).getCreatDate()).equals(note.getGalleryId())) {
                                    gallerySpinner.setSelection(position+1);
                                }
                            }
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("Edit Note");
                            alert.setView(alertLayout);
                            alert.setCancelable(true);
                            alert.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                                }
                            });
                            alert.setNegativeButton("EDIT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String user = etUsername.getText().toString();
                                    String mydate = note.getCreatDate();
                                    String priority="";
                                    if(prioritySpinner.getSelectedItemPosition() > 0)
                                        priority = priorityList.get(prioritySpinner.getSelectedItemPosition() - 1).getName()+priorityList.get(prioritySpinner.getSelectedItemPosition() - 1).getCreatDate();
                                    String status ="";
                                    if(statusSpinner.getSelectedItemPosition() > 0)
                                        status = statusList.get(statusSpinner.getSelectedItemPosition() - 1).getName()+statusList.get(statusSpinner.getSelectedItemPosition() - 1).getCreatDate();
                                    String gallery ="";
                                    if(gallerySpinner.getSelectedItemPosition() > 0)
                                        gallery =  galleryList.get(gallerySpinner.getSelectedItemPosition() - 1).getName()+galleryList.get(gallerySpinner.getSelectedItemPosition() - 1).getCreatDate();
                                    String selectedPlanDate = "";
                                    if(!planDate.getText().toString().equals("Select plan date"))
                                        selectedPlanDate = planDate.getText().toString();
                                    Note noteEdit = new Note(user, mydate, status, priority, gallery, selectedPlanDate);
                                    String path = user + mydate;
                                    rff.child("Users").child(uId).child("note").child(note.getName()+note.getCreatDate()).removeValue();
                                    rff.child("Users").child(uId).child("note").child(path).setValue(noteEdit).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                            AlertDialog editNoteDialog = alert.create();
                            editNoteDialog.show();
                        }
                        else if(choised.equals("Delete"))
                        {
                            Note note = noteList.get(position);
                            String path = note.getName() + note.getCreatDate();
                            rff.child("Users").child(uId).child("note").child(path).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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
                noteList.clear();
                for(DataSnapshot dataSnapshot : snapshot.child("Users").child(uId).child("note").getChildren()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String gallery = dataSnapshot.child("galleryId").getValue().toString();
                    String status = dataSnapshot.child("statusId").getValue().toString();
                    String priority = dataSnapshot.child("priorityId").getValue().toString();
                    String planDate = dataSnapshot.child("planDate").getValue().toString();
                    String cDate = dataSnapshot.child("creatDate").getValue().toString();
                    Note note = new Note(name, cDate,status,priority,gallery,planDate);
                    noteList.add(note);
                }
                galleryList.clear();
                for(DataSnapshot dataSnapshot : snapshot.child("Users").child(uId).child("gallery").getChildren()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String key = dataSnapshot.child("creatDate").getValue().toString();
                    Gallery g = new Gallery(name,key);
                    galleryList.add(g);
                }
                statusList.clear();
                for(DataSnapshot dataSnapshot : snapshot.child("Users").child(uId).child("status").getChildren()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String key = dataSnapshot.child("creatDate").getValue().toString();
                    Status s = new Status(name, key);
                    statusList.add(s);
                }
                priorityList.clear();
                for(DataSnapshot dataSnapshot : snapshot.child("Users").child(uId).child("priority").getChildren()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String key = dataSnapshot.child("creatDate").getValue().toString();
                    Priority p = new Priority(name, key);
                    priorityList.add(p);
                }
                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    class NoteAdapter extends ArrayAdapter {

        public NoteAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        public NoteAdapter() {
            super(getActivity(),android.R.layout.simple_list_item_1, noteList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.row_note, null);
            Note n = noteList.get(position);
            String status= "";
            String priority = "";
            String gallery = "";
            for (Status s: statusList){
                if(n.getStatusId().equals(s.getName()+s.getCreatDate()))
                    status = s.getName();
            }
            for (Priority priorityTemp: priorityList){
                if(n.getPriorityId().equals(priorityTemp.getName()+priorityTemp.getCreatDate()))
                    priority = priorityTemp.getName();
            }
            for (Gallery galleryTemp: galleryList){
                if(n.getGalleryId().equals(galleryTemp.getName()+galleryTemp.getCreatDate()))
                    gallery = galleryTemp.getName();
            }

            ((TextView)row.findViewById(R.id.name_rowNote)).setText(n.getName());
            ((TextView)row.findViewById(R.id.category_rowNote)).setText(gallery);
            ((TextView)row.findViewById(R.id.priority_rowNote)).setText(priority);
            ((TextView)row.findViewById(R.id.status_rowNote)).setText(status);
            ((TextView)row.findViewById(R.id.planDate_rowNote)).setText(n.getPlanDate());
            ((TextView)row.findViewById(R.id.creatDate_rowNote)).setText(n.getCreatDate());

            return row;
        }
    }
}

