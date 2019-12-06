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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText login_nationalId, login_password;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;
    private String parentDbName = "Student";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = findViewById(R.id.login_btn);
        login_nationalId = findViewById(R.id.login_nationalId);
        login_password = findViewById(R.id.login_password);
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);

        loadingBar = new ProgressDialog(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
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
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Student";
            }
        });
    }


    private void LoginUser() {
        String nationalID = login_nationalId.getText().toString();
        String password = login_password.getText().toString();

        if (TextUtils.isEmpty(nationalID)) {
            Toast.makeText(this, "Please write your National ID....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password....", Toast.LENGTH_SHORT).show();
        } else {
            // wait to check is phone number is available in database
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("please wait , while we are checking the credentials .");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(nationalID, password);

        }
    }

    private void AllowAccessToAccount(final String nationalid, final String password) {

        // make database by a Reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(nationalid).exists()) {
                    if (nationalid.equals(nationalid)) {
                        if (password.equals(password)) {
                            if (parentDbName.equals("Admins")) {
                                Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, ButtonAdminActivity.class);
                                startActivity(intent);
                            } else if (parentDbName.equals("Student")) {
                                Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, RecordingDesiresActivity.class);
                                //make this to make the user data public in all classes to use it
                                startActivity(intent);
                            }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Account with this " + nationalid + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}