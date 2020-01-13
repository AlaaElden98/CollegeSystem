package com.usama.runtime.fragments;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usama.runtime.R;


public class DesiresAcceptedFragment extends Fragment {

    private String finalDesires, departmentDescription;
    private TextView nameOfDepartment, desciptionOfDepartment;
    private DatabaseReference databaseReference;

    public DesiresAcceptedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_desires_accepted, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DesiresAcceptedFragmentArgs args = DesiresAcceptedFragmentArgs.fromBundle(getArguments());
        finalDesires = args.getFinalDesires();
        nameOfDepartment = getView().findViewById(R.id.nameOfDepartment);
        nameOfDepartment.setText(finalDesires);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        ValueEventListener departmentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                departmentDescription = dataSnapshot.child("departments").child(finalDesires).child("departmentDescription").getValue(String.class);
                Log.d("ss",departmentDescription);
                desciptionOfDepartment = getView().findViewById(R.id.departmentDescription);
                desciptionOfDepartment.setText(departmentDescription);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(departmentListener);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
