package com.usama.runtime.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.usama.runtime.R;

import java.util.HashMap;

public class AddDepartmentFragment extends Fragment {
    private ProgressDialog loadingBar;
    Button buttonAddDepartment;
    EditText getDepartmentCapacity, getDepartmentName;

    public AddDepartmentFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_department, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDepartmentName = getView().findViewById(R.id.getDepartmentName);
        getDepartmentCapacity = getView().findViewById(R.id.getDepartmentCapacity);
        buttonAddDepartment = getView().findViewById(R.id.buttonAddDepartment);
        loadingBar = new ProgressDialog(getActivity());

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


            // wait to check is phone number is available in database
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("please wait , while we are checking the credentials .");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(departmentName, departmentCapacity);
            

    }

    private void ValidatePhoneNumber(final String departmentName, final String departmentCapacity) {

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

                    RootRef.child("Department").child(departmentName).updateChildren(studentDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Add this department is success", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                        Navigation.findNavController(getView()).navigate(AddDepartmentFragmentDirections.actionAddDepartmentFragmentToButtonAdminFragment());
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(getActivity(), "Network Error: please try again after some time..", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(getActivity(), "This " + departmentName + " already exist ", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Navigation.findNavController(getView()).navigate(AddDepartmentFragmentDirections.actionAddDepartmentFragmentToButtonAdminFragment());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
