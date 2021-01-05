package com.example.damh_android.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.damh_android.Object.Note;
import com.example.damh_android.Object.Status;
import com.example.damh_android.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    DatabaseReference rff = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<Note> noteList = new ArrayList<Note>();
    List<Status> statusList = new ArrayList<Status>();
    String uId;
    PieChart pieChart;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        uId = mAuth.getUid();
        pieChart = root.findViewById(R.id.pieChart);
        pieChart.getDescription().setText("");
        pieChart.setUsePercentValues(true);
        pieChart.setHoleRadius(0f);//Draw center circle
        pieChart.setTransparentCircleAlpha(0);//Draw circle outside
        pieChart.setEntryLabelTextSize(15);
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
                    String status = dataSnapshot.child("statusId").getValue().toString();
                    Note note = new Note(name, status);
                    noteList.add(note);
                }
                statusList.clear();
                for(DataSnapshot dataSnapshot : snapshot.child("Users").child(uId).child("status").getChildren()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String key = dataSnapshot.child("creatDate").getValue().toString();
                    Status s = new Status(name, key);
                    statusList.add(s);
                }
                List<PieEntry> entries = new ArrayList<>();
                for(int i = 0; i<statusList.size(); i++){
                    float total =0;
                    for (int j =0; j<noteList.size(); j++){
                        if(noteList.get(j).getStatusId().equals((statusList.get(i).getName()+statusList.get(i).getCreatDate()))){
                            total++;
                        }
                    }
                    if(total!=0)
                        entries.add(new PieEntry(total, statusList.get(i).getName()));
                }
                dataSet(entries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void dataSet(List<PieEntry> entries) {
        PieDataSet pieDataSet = new PieDataSet(entries,"");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.MAGENTA);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);

        pieDataSet.setColors(colors);
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

}