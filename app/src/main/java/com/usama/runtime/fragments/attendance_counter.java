package com.usama.runtime.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usama.runtime.Attendance.AttendanceAdapter;
import com.usama.runtime.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class attendance_counter extends Fragment {

    AttendanceAdapter adapter;
    private RecyclerView recyclerView;

    private DatabaseReference studentAttendanceRef;
    List<String> arrayList=new ArrayList<>();
    public static final String MY_NATIONAL_ID = "MyNationalId";

    public attendance_counter() {
        // Required empty public constructor
    }

    public void onStart() {
        super.onStart();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AttendanceAdapter();
        recyclerView.setAdapter(adapter);
        getAttendance();
        //Log.d("Array", String.valueOf(arrayList.isEmpty()));



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.attendanceRecyclerView);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance_counter, container, false);
    }




    private void getAttendance() {
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_NATIONAL_ID, MODE_PRIVATE);
        final String nationalId = prefs.getString("nationalId", "1");
        studentAttendanceRef = FirebaseDatabase.getInstance().getReference().child("students_attendance").child(nationalId);


        studentAttendanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot lang:dataSnapshot.getChildren()){
                    arrayList.add(lang.getKey().toString());
                    arrayList.add(lang.getValue().toString());
                    Log.d("Subject", lang.getValue().toString());
                    Log.d("Attendance", lang.getKey().toString());

                }
                adapter.setList(arrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
