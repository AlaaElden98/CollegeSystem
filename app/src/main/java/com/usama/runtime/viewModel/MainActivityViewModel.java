package com.usama.runtime.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private DatabaseReference eventIdRef;

    public MutableLiveData<List<String>> departmentNameData = new MutableLiveData<>();
    private ArrayList <String> arrayList;

    public void getArrayOfDepartmentName() {
        eventIdRef = FirebaseDatabase.getInstance().getReference().child("departments");
        eventIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //arrayList.clear();
                arrayList=new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String plate = snapshot.child("departmentName").getValue().toString();
                    arrayList.add(plate);

                    Log.d("test", plate);
                }
                departmentNameData.setValue(arrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
