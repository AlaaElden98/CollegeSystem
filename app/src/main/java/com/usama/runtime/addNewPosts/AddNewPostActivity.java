package com.usama.runtime.addNewPosts;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usama.runtime.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddNewPostActivity extends AppCompatActivity {
    private String professorName, subject, description, timetostring;
    private Button addPostBtn;
    private EditText nameOfProfessor, nameOfSubject, descriptionOfTopic;
    private DatabaseReference postRef;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_add_new_posts);

        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        addPostBtn = findViewById(R.id.addPostBtn);
        nameOfProfessor = findViewById(R.id.nameOfProfessor);
        nameOfSubject = findViewById(R.id.nameOfSubject);
        descriptionOfTopic = findViewById(R.id.descriptionOfTopic);
        loadingBar = new ProgressDialog(this);


        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidatePostData();
            }
        });
    }

    private void ValidatePostData() {
        professorName = nameOfProfessor.getText().toString();
        subject = nameOfSubject.getText().toString();
        description = descriptionOfTopic.getText().toString();

        if (TextUtils.isEmpty(professorName)) {
            Toast.makeText(this, "Please write Your Name sir...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(subject)) {
            Toast.makeText(this, "Please write Name of subject...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please write Description...", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }


    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Add New Post");
        loadingBar.setMessage("Dear Doctor, please wait while we are adding the new post.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Date currentTime = Calendar.getInstance().getTime();
        timetostring = currentTime.toString();

        SaveProductInfoToDatabase();
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("name", professorName);
        postMap.put("dataAndTime", timetostring);
        postMap.put("subject", subject);
        postMap.put("description", description);

        postRef.child(professorName).updateChildren(postMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(AddNewPostActivity.this, AddNewPostActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AddNewPostActivity.this, "Post is added successfully..", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddNewPostActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
