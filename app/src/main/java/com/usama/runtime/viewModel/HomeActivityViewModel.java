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
import com.usama.runtime.model.Department;
import com.usama.runtime.model.Student;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityViewModel extends ViewModel {

    private DatabaseReference desiresRef, studentRef, depReference;
    private List<String> arrayOfDesires = new ArrayList<>();
    private int studentTotal;
    private Student studentData;
    private String name;
    private String currentDesires;
    private int capacityOfDepartment, totalOfDepartment;
    private boolean flag = false;


    public MutableLiveData<String> finalSelectedDesirsd = new MutableLiveData<>();

    public void getFinalDesires(final String id) {
        desiresRef = FirebaseDatabase.getInstance().getReference("student_desires").child(id);
        desiresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.child("desires").getChildren()) {
                    String s = childSnapshot.getValue(String.class);
                    arrayOfDesires.add(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        studentRef = FirebaseDatabase.getInstance().getReference().getRef().child("students");
        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentData = dataSnapshot.child(id).getValue(Student.class);
                studentTotal = Integer.parseInt(studentData.getTotal().substring(0, 3));
                name = studentData.getName();

                depReference = FirebaseDatabase.getInstance().getReference().child("departments");
                depReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (int i = 0; i < arrayOfDesires.size(); i++) {
                            currentDesires = arrayOfDesires.get(i);

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String dep = snapshot.child("departmentName").getValue().toString();
                                if (dep.contains(currentDesires)) {
                                    Department department = snapshot.getValue(Department.class);
                                    capacityOfDepartment = Integer.parseInt(department.getDepartmentCapacity());
                                    totalOfDepartment = Integer.parseInt(department.getDepartmentMinTotal());

                                    if (studentTotal > totalOfDepartment && flag == false) {
                                        finalSelectedDesirsd.setValue(currentDesires);
                                        Log.d("HEY", finalSelectedDesirsd + " ");
                                        flag = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
