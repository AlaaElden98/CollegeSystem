package com.usama.runtime.loginPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.usama.runtime.R;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button studentBtn, adminOrDoctorsBtn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentBtn = findViewById(R.id.main_student_login_btn);
        adminOrDoctorsBtn = findViewById(R.id.main_doctors_admins_btn);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        studentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudentLoginActivity.class);
                startActivity(intent);
            }
        });

        adminOrDoctorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminOrDoctorLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

