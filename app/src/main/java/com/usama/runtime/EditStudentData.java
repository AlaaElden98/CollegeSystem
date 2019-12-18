package com.usama.runtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditStudentData extends AppCompatActivity {
    EditText studenttotal, studentpassword, studentspecialize, studentseatNumber, studentnationalId, studentuserName;
    Button updatedestudentdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_data);
        studentuserName = findViewById(R.id.studentuserName);
        studentpassword = findViewById(R.id.studentpassword);
        studentseatNumber = findViewById(R.id.studentseatNumber);
        studentnationalId = findViewById(R.id.studentnationalId);
        studenttotal = findViewById(R.id.studenttotal);
        studentspecialize = findViewById(R.id.studentspecialize);
        updatedestudentdata = findViewById(R.id.updatedestudentdata);
        studentuserName.append(getIntent().getStringExtra("getUserName"));
        studentpassword.append(getIntent().getStringExtra("getPassword"));
        studentseatNumber.append(getIntent().getStringExtra("getSeatNumber"));
        studentnationalId.append(getIntent().getStringExtra("getNationalId"));
        studenttotal.append(getIntent().getStringExtra("getTotal"));
        studentspecialize.append(getIntent().getStringExtra("getSpecialize"));

        updatedestudentdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Updateaccountstudent();
            }
            private void Updateaccountstudent() {
                String userName = studentuserName.getText().toString();
                String password = studentpassword.getText().toString();
                String seatNumber = studentseatNumber.getText().toString();
                String nationalId = studentnationalId.getText().toString();
                String total = studenttotal.getText().toString();
                String specialize = studentspecialize.getText().toString();

                ValidatePhoneNumber(userName,password,seatNumber,nationalId,total,specialize);
            }
            private void ValidatePhoneNumber(final String userName,final String password , final String seatNumber, final String nationalId, final String total, final String specialize) {

                final DatabaseReference RootRef;
                HashMap<String, Object> studentDataMap = new HashMap<>();
                studentDataMap.put("nationalId", nationalId);
                studentDataMap.put("password", password);
                studentDataMap.put("userName", userName);
                studentDataMap.put("seatNumber", seatNumber);
                studentDataMap.put("total", total);
                studentDataMap.put("specialize", specialize);

                FirebaseDatabase.getInstance().getReference("Student").child(getIntent().getStringExtra("uid")).updateChildren(studentDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EditStudentData.this, "update success", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditStudentData.this, ButtonAdminActivity.class);
                                startActivity(intent);

                            }
                        });


            }
        });

        /*StudentName = findViewById(R.id.StudentName);
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
        Studenttotal.setText("Total:" + getIntent().getStringExtra("getTotal"));*/
    }
}
