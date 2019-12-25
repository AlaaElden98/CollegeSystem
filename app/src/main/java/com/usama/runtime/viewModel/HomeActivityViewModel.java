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

    DatabaseReference desiresRef, studentRef, depReference;
    List<String> arrayOfDesires = new ArrayList<>();
    int studentTotal;
    Student studentData;
    String name;
    String currentDesires;
    int capacityOfDepartment, totalOfDepartment;
    boolean flag = false;


    public MutableLiveData<String> finalSelectedDesirsd = new MutableLiveData<>();

    public void getFinalDesires(final String id) {
        desiresRef = FirebaseDatabase.getInstance().getReference("student_desires").child(id);
        desiresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.child("desires").getChildren()) {
                    String s = childSnapshot.getValue(String.class);
//                    Log.d("TAG", s + "    111111111111111111");
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
                        for (int i = 0 ; i < arrayOfDesires.size();i++){
                            currentDesires = arrayOfDesires.get(i);

                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                String dep = snapshot.child("name").getValue().toString();
//                                Log.d("TAG",  "   currentDesires " +  currentDesires +"   "+ dep + "   department " );
                                if (dep.contains(currentDesires)){
//                                    Log.d("TAG",  "if ");
                                    Department department = snapshot.getValue(Department.class);
                                    capacityOfDepartment = Integer.parseInt(department.getCapacity());
                                    totalOfDepartment = Integer.parseInt(department.getMin_total());

//                                    Log.d("TAGStudentTotal", studentTotal + "");
//                                    Log.d("TAGTotalOfDepartment", totalOfDepartment + "");
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
//
//
//    // TODO : first get first desires FROM student_desires --> this is in a arrayList
//    // TODO : go to table of this department to fetch all data about it department , capacity , total ,
//    // TODO : go to table of this student to fetch all data about it student , total
//    // TODO : if student's desires of the first or second or ... OK  add it in student_department
