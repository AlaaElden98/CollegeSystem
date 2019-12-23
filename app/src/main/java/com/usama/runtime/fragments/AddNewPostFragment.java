package com.usama.runtime.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AddNewPostFragment extends Fragment {
    private String professorName, subject, description, timetostring;
    private Button addPostBtn;
    private EditText nameOfProfessor, nameOfSubject, descriptionOfTopic;
    private DatabaseReference postRef;
    private ProgressDialog loadingBar;


    public AddNewPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_post, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        addPostBtn = getView().findViewById(R.id.addPostBtn);
        nameOfProfessor = getView().findViewById(R.id.nameOfProfessor);
        nameOfSubject = getView().findViewById(R.id.nameOfSubject);
        descriptionOfTopic = getView().findViewById(R.id.descriptionOfTopic);
        loadingBar = new ProgressDialog(getActivity());


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
            Toast.makeText(getActivity(), "Please write Your Name sir...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(subject)) {
            Toast.makeText(getActivity(), "Please write Name of subject...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(getActivity(), "Please write Description...", Toast.LENGTH_SHORT).show();
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

        SavePostInfoToDatabase();
    }

    private void SavePostInfoToDatabase() {
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
//                            Intent intent = new Intent(getActivity(), AddNewPostActivity.class);
//                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(getActivity(), "Post is added successfully..", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
