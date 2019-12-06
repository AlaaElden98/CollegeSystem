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

public class AddDepartmentActivity extends AppCompatActivity {
    private ProgressDialog loadingBar;
    Button buttonAddDepartment;
    EditText getDepartmentCapacity, getDepartmentName, getDepartmentMinValue, getDepartmentMinSpecial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);

        getDepartmentName = findViewById(R.id.getDepartmentName);
        getDepartmentCapacity = findViewById(R.id.getDepartmentCapacity);
        getDepartmentMinValue = findViewById(R.id.getDepartmentMinValue);
        getDepartmentMinSpecial = findViewById(R.id.getDepartmentMinSpecial);
        buttonAddDepartment = findViewById(R.id.buttonAddDepartment);
        loadingBar = new ProgressDialog(this);

        buttonAddDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDepartment();
            }
        });

    }

    private void addDepartment() {
        String departmentName = getDepartmentName.getText().toString().trim();
        String departmentCapacity = getDepartmentCapacity.getText().toString().trim();
        String departmentMinValue = getDepartmentMinValue.getText().toString().trim();
        String depMinSpecial = getDepartmentMinSpecial.getText().toString().trim();
        

            // wait to check is phone number is available in database
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("please wait , while we are checking the credentials .");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(departmentName, departmentCapacity, departmentMinValue, depMinSpecial);
            

    }

    private void ValidatePhoneNumber(final String departmentName, final String departmentCapacity, final String departmentMinValue, final String departmentMinSpecial) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // if this national id  valid create a new account
                // i make a national id a unique field in database

                if (!(dataSnapshot.child("Department").child(departmentName).exists())) {
                    // use hashMap to store data into the database (firebase)
                    HashMap<String, Object> studentDataMap = new HashMap<>();
                    studentDataMap.put("departmentName", departmentName);
                    studentDataMap.put("departmentCapacity", departmentCapacity);
                    studentDataMap.put("departmentMinValue", departmentMinValue);
                    studentDataMap.put("departmentMinSpecial", departmentMinSpecial);

                    RootRef.child("Department").child(departmentName).updateChildren(studentDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddDepartmentActivity.this, "Add this department is success", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(AddDepartmentActivity.this, ButtonAdminActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(AddDepartmentActivity.this, "Network Error: please try again after some time..", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(AddDepartmentActivity.this, "This " + departmentName + " already exist ", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Intent intent = new Intent(AddDepartmentActivity.this, ButtonAdminActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}













