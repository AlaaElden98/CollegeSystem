package com.usama.runtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.usama.runtime.loginPackage.AdminOrDoctorLoginActivity;

public class ButtonAdminActivity extends AppCompatActivity {

    // TODO : MAKE VALIDATION OF LOGIN AND REGISTER
    Button addDepartmentBtn, showDepartmentBtn, showStudentBtn, addStudentBtn, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_admin);
        logoutBtn = findViewById(R.id.logoutBtn);
        addStudentBtn = findViewById(R.id.addStudentBtn);
        addDepartmentBtn = findViewById(R.id.addDepartmentBtn);
        showDepartmentBtn = findViewById(R.id.showDepartmentBtn);
        showStudentBtn = findViewById(R.id.showStudentBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ButtonAdminActivity.this, AdminOrDoctorLoginActivity.class);
                startActivity(intent);
            }
        });

        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ButtonAdminActivity.this, AddStudentActivity.class);
                startActivity(intent);
            }
        });
        addDepartmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ButtonAdminActivity.this, AddDepartmentActivity.class);
                startActivity(intent);

            }
        });
        showDepartmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ButtonAdminActivity.this, ShowDepartmentActivity.class);
                startActivity(intent);

            }
        });

        showStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ButtonAdminActivity.this, ShowAllStudentActivity.class);
                startActivity(intent);

            }
        });
    }

}
