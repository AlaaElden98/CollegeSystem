package com.usama.runtime.loginPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.CheckBox;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usama.runtime.ButtonAdminActivity;
import com.usama.runtime.Prevalent.Prevalent;
import com.usama.runtime.R;
import com.usama.runtime.RecordingDesiresDepartment.RecordingDesiresActivity;
import com.usama.runtime.addNewPosts.AddNewPostActivity;
import com.usama.runtime.makeNavigationBar.HomeActivity;
import com.usama.runtime.model.Admin;
import com.usama.runtime.model.Student;

import io.paperdb.Paper;


public class AdminOrDoctorLoginActivity extends AppCompatActivity {

    public static Admin adminData;

    private EditText login_name_et, login_password_et;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;
    private String parentDbName = "Doctors";
    private CheckBox chkBoxRememberMe;

    // variable to use in shared preference
    public static final String MT_NATIONAL_ID = "MyNationalId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_or_doctor_login);

        LoginButton = findViewById(R.id.login_btn);
        login_name_et = findViewById(R.id.login_name_et);
        login_password_et = findViewById(R.id.login_password_et);
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);

        loadingBar = new ProgressDialog(this);


        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        // paper library
        Paper.init(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginDoctor();
            }
        });


        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Doctors");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Doctors";
            }
        });


        // check if student already login
        String StudentNationalIdKey = Paper.book().read(Prevalent.StudentNationalIdKey);
        String StudentSittingNumberKey = Paper.book().read(Prevalent.StudentSittingNumberKey);

        if (StudentNationalIdKey != "" && StudentSittingNumberKey != "") {
            if (!TextUtils.isEmpty(StudentNationalIdKey) && !TextUtils.isEmpty(StudentSittingNumberKey)) {
                AllowAccess(StudentNationalIdKey, StudentSittingNumberKey);

                loadingBar.setTitle("Already Login");
                loadingBar.setMessage("please wait .. ");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        }


    }

    private void AllowAccess(final String studentNationalIdKey, final String studentSittingNumberKey) {

        // make database by a Reference
        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // here child phone is a unique object
                if (dataSnapshot.child(parentDbName).child(studentNationalIdKey).exists()) {
                    Admin adminData = dataSnapshot.child(parentDbName).child(studentNationalIdKey).getValue(Admin.class);

                    // retrieve the user data
                    if (adminData.getName().equals(studentNationalIdKey)) {
                        if (adminData.getPassword().equals(studentSittingNumberKey)) {
                            Toast.makeText(AdminOrDoctorLoginActivity.this, "you are already login  ... ", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(AdminOrDoctorLoginActivity.this, AdminOrDoctorLoginActivity.class);


                            // this line to make sure the app doesn't crash when cloth it and open again
                            // because we use paper library
                            Prevalent.CurrentOnlineAdmin = adminData;
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(AdminOrDoctorLoginActivity.this, "Password is incorrect ", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(AdminOrDoctorLoginActivity.this, "Account with this " + studentNationalIdKey + " number do not exist ", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void LoginDoctor() {
        String name = login_name_et.getText().toString();
        String password = login_password_et.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write your National ID....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your sitting Number....", Toast.LENGTH_SHORT).show();
        } else {
            // wait to check is phone number is available in database
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("please wait , while we are checking the credentials .");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(name, password);

        }
    }

    private void AllowAccessToAccount(final String name, final String password) {
        if (chkBoxRememberMe.isChecked()) {
            // check box return two values true or false :)
            // method write take two parameter is key and value
            Paper.book().write(Prevalent.AdminNameKey, name);
            Paper.book().write(Prevalent.AdminPasswordKey, password);
        }


        // make database by a Reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(name).exists()) {


                    adminData = dataSnapshot.child(parentDbName).child(name).getValue(Admin.class);
                    Log.d("TAG", "Data" + adminData);
                    if (adminData.getName().equals(name)) {
                        if (adminData.getPassword().equals(password)) {
                            if (parentDbName.equals("Admins")) {
                                Toast.makeText(AdminOrDoctorLoginActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(AdminOrDoctorLoginActivity.this, ButtonAdminActivity.class);
                                startActivity(intent);
                            } else if (parentDbName.equals("Doctors")) {
                                Toast.makeText(AdminOrDoctorLoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(AdminOrDoctorLoginActivity.this, AddNewPostActivity.class);


                                // MY_PREFS_NAME - a static String variable like:
                                //public static final String MY_PREFS_NAME = "MyPrefsFile";
                                SharedPreferences.Editor editor = getSharedPreferences(MT_NATIONAL_ID, MODE_PRIVATE).edit();
                                editor.putString("nationalId", name);
                                editor.apply();

                                //make this to make the user data public in all classes to use it
                                Prevalent.CurrentOnlineAdmin = adminData;
                                startActivity(intent);
                            }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(AdminOrDoctorLoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(AdminOrDoctorLoginActivity.this, "Account with this " + name + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}