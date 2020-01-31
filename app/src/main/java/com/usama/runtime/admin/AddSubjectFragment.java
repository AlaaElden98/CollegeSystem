package com.usama.runtime.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.usama.runtime.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AddSubjectFragment extends Fragment {

    private EditText subjectName, doctorName;
    private String level , department;
    private ProgressDialog loadingBar;
    private ArrayList<String> arrayListOfDepartment;
    private MaterialSpinner departmentSpinner;

    public AddSubjectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_subject, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadingBar = new ProgressDialog(getContext());
        subjectName = getView().findViewById(R.id.subjectName);
        doctorName = getView().findViewById(R.id.doctorName);
        Button buttonAddSubject = getView().findViewById(R.id.buttonAddSubject);
        MaterialSpinner levelSpinner = getView().findViewById(R.id.spinner_choose_subject);
        departmentSpinner = getView().findViewById(R.id.spinner_choose_department);
        arrayListOfDepartment = new ArrayList<>();

        levelSpinner.setItems("Choose Level", "Level_One", "Level_Two", "Level_Three", "Level_Four");
        levelSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item == "Choose Level") {
                    Toast.makeText(getContext(), "Please choose Level", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("please wait , while we are checking the credentials .");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    level = item.toString();
                    getDepartment();
                }
            }
        });

        departmentSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                department = item.toString();
            }
        });
        buttonAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubject();
            }
        });
    }

    private void getDepartment() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("departments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", dataSnapshot.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String subject = snapshot.getKey();
                    arrayListOfDepartment.add(subject);
                }
                loadingBar.dismiss();
                departmentSpinner.setItems(arrayListOfDepartment);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }

    private void addSubject() {
        final String subj = subjectName.getText().toString().trim();
        final String docName = doctorName.getText().toString().trim();

        if (subj == null) {
            Toast.makeText(getContext(), "please write subject name", Toast.LENGTH_SHORT).show();
        } else if (docName == null) {
            Toast.makeText(getContext(), "Please write doctor name ", Toast.LENGTH_SHORT).show();
        } else {
            final HashMap<String, Object> subjectDataMap = new HashMap<>();
            subjectDataMap.put("Subject_Name", subj);
            subjectDataMap.put("Doctor_Name", docName);

            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Subjects").child(level);

            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    rootRef.child(department).child(subj).updateChildren(subjectDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Add this Subject is success", Toast.LENGTH_LONG).show();
                                Navigation.findNavController(getView()).navigate(AddSubjectFragmentDirections.actionAddSubjectFragmentToAdminHomeFragment(docName));
                            } else {
                                Toast.makeText(getActivity(), "Network Error: please try again after some time..", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}
