package com.usama.runtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddStudentActivity extends AppCompatActivity {
    EditText etUserNameAddStudent, etPasswordAddStudent, etSeatNumberAddStudent, etNationalIdAddStudent, etTotalAddStudent, etSpecializeAddStudent;
    Button buttonAddStudent;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        etUserNameAddStudent = findViewById(R.id.etUserNameAddStudent);
        etPasswordAddStudent = findViewById(R.id.etPasswordAddStudent);
        etSeatNumberAddStudent = findViewById(R.id.etSeatNumberAddStudent);
        etNationalIdAddStudent = findViewById(R.id.etNationalIdAddStudent);
        etTotalAddStudent = findViewById(R.id.etTotalAddStudent);
        etSpecializeAddStudent = findViewById(R.id.etSpecializeAddStudent);
        buttonAddStudent = findViewById(R.id.buttonAddStudent);
        loadingBar = new ProgressDialog(this);

        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String userName = etUserNameAddStudent.getText().toString().trim();
        String password = etPasswordAddStudent.getText().toString().trim();
        String seatNumber = etSeatNumberAddStudent.getText().toString().trim();
        String nationalId = etNationalIdAddStudent.getText().toString().trim();
        String total = etTotalAddStudent.getText().toString().trim();
        String specialize = etSpecializeAddStudent.getText().toString().trim();

//        // check if the user name or password or phone field empty
//        if (TextUtils.isEmpty(userName)) {
//            Toast.makeText(this, "Please write your name....", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(seatNumber)) {
//            Toast.makeText(this, "Please write your seatNumber....", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(nationalId)) {
//            Toast.makeText(this, "Please write your nationalId....", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(total)) {
//            Toast.makeText(this, "Please write here totalDegree....", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(specialize)) {
//            Toast.makeText(this, "Please write your specialize....", Toast.LENGTH_SHORT).show();
//        } else {
            // wait to check is phone number is available in database
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("please wait , while we are checking the credentials .");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(userName, password,seatNumber, nationalId, total, specialize);
        }
//    }

    private void ValidatePhoneNumber(final String userName,final String password , final String seatNumber, final String nationalId, final String total, final String specialize) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // if this national id  valid create a new account
                // i make a national id a unique field in database

                if (!(dataSnapshot.child("Student").child(nationalId).exists())) {
                    // use hashMap to store data into the database (firebase)
                    HashMap<String, Object> studentDataMap = new HashMap<>();
                    studentDataMap.put("nationalId", nationalId);
                    studentDataMap.put("password", password);
                    studentDataMap.put("userName", userName);
                    studentDataMap.put("seatNumber", seatNumber);
                    studentDataMap.put("total", total);
                    studentDataMap.put("specialize", specialize);

                    RootRef.child("Student").child(nationalId).updateChildren(studentDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddStudentActivity.this, "Add this student success", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(AddStudentActivity.this, ButtonAdminActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(AddStudentActivity.this, "Network Error: please try again after some time..", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(AddStudentActivity.this, "This " + nationalId + " already exist ", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Intent intent = new Intent(AddStudentActivity.this, ButtonAdminActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}













