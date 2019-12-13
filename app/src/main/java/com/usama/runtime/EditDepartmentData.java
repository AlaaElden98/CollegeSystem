package com.usama.runtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditDepartmentData extends AppCompatActivity {

    // TODO: MAKE EDIT IN ALL INFO << Done<< Alaa
    Button buttonEditDepartment;
    EditText getDepartmentName,getDepartmentCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_department_data);
        getDepartmentName = findViewById(R.id.getDepartmentName);
        getDepartmentCapacity = findViewById(R.id.getDepartmentCapacity);
        buttonEditDepartment = findViewById(R.id.buttonAddDepartment);
        buttonEditDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDepartment();
            }
        });
    }

    private void editDepartment() {
        String departmentName = getDepartmentName.getText().toString().trim();
        String departmentCapacity = getDepartmentCapacity.getText().toString().trim();
        String departmentOldName =getIntent().getStringExtra("getDepartmentName");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();

        mDatabaseRef.child("Department").child(departmentOldName).child("departmentName").setValue(departmentName);
        mDatabaseRef.child("Department").child(departmentOldName).child("departmentCapacity").setValue(departmentCapacity);


    }
}
