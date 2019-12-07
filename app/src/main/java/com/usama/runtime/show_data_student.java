package com.usama.runtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class show_data_student extends AppCompatActivity {
    TextView StudentName, Studenttnationalid, Studentpassword, Studentseatnumber, Studentspecialize, Studenttotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data_student);
        StudentName = findViewById(R.id.StudentName);
        Studenttnationalid = findViewById(R.id.Studenttnationalid);
        Studentpassword = findViewById(R.id.Studentpassword);
        Studentseatnumber = findViewById(R.id.Studentseatnumber);
        Studentspecialize = findViewById(R.id.Studentspecialize);
        Studenttotal = findViewById(R.id.Studenttotal);
        StudentName.setText("User Name:" + getIntent().getStringExtra("getUserName"));
        Studenttnationalid.setText("National Id:" + getIntent().getStringExtra("getNationalId"));
        Studentpassword.setText("Password:" + getIntent().getStringExtra("getPassword"));
        Studentseatnumber.setText("Seat Number:" + getIntent().getStringExtra("getSeatNumber"));
        Studentspecialize.setText("Specialize:" + getIntent().getStringExtra("getSpecialize"));
        Studenttotal.setText("Total:" + getIntent().getStringExtra("getTotal"));
    }
}
