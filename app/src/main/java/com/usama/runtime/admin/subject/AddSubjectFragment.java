package com.usama.runtime.admin.subject;

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
import com.usama.runtime.model.Doctors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddSubjectFragment extends Fragment {

    private EditText subjectName;
    private String level, department, doctorName, subj;
    private ProgressDialog loadingBar;
    private ArrayList<String> arrayListOfDepartment, arrayListOfDoctorName;
    private MaterialSpinner departmentSpinner;
    private MaterialSpinner doctorNameSpinner;

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
        subjectName = Objects.requireNonNull(getView()).findViewById(R.id.subjectName);
        doctorNameSpinner = getView().findViewById(R.id.spinner_choose_doctor_name);
        MaterialSpinner levelSpinner = getView().findViewById(R.id.spinner_choose_subject);
        departmentSpinner = getView().findViewById(R.id.spinner_choose_department);
        Button buttonAddSubject = getView().findViewById(R.id.buttonAddSubject);
        arrayListOfDepartment = new ArrayList<>();
        arrayListOfDoctorName = new ArrayList<>();

        levelSpinner.setItems("Choose Level", "Level_One", "Level_Two", "Level_Three", "Level_Four");
        levelSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (level != item) {
                    arrayListOfDepartment.clear();
                    arrayListOfDoctorName.clear();
                }

                if (item == "Choose Level") {
                    Toast.makeText(getContext(), "Please choose Level", Toast.LENGTH_SHORT).show();
                    arrayListOfDepartment.clear();
                    arrayListOfDoctorName.clear();
                } else {
                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("please wait , while we are checking the credentials .");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    level = item.toString();
                    getDepartment();
                    getDoctorName();
                }
            }
        });


        departmentSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                department = item.toString();
            }
        });

        doctorNameSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                doctorName = item.toString();
            }
        });

        buttonAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subj = subjectName.getText().toString().trim();
                if (department.equals("")) {
                    Toast.makeText(getContext(), "please choose department ", Toast.LENGTH_SHORT).show();
                } else if (subj.equals("")) {
                    Toast.makeText(getContext(), "Please write subject name ", Toast.LENGTH_SHORT).show();
                } else if (level.equals("")) {
                    Toast.makeText(getContext(), "please choose level ", Toast.LENGTH_SHORT).show();
                } else if (doctorName.equals("")) {
                    Toast.makeText(getContext(), "please choose doctor name ", Toast.LENGTH_SHORT).show();
                } else {
                    addSubject(department, subj, level, doctorName);
                }
            }
        });
    }

    private void getDoctorName() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", dataSnapshot.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Doctors doctorData = snapshot.getValue(Doctors.class);
                    String name = doctorData.getRealName();
                    arrayListOfDoctorName.add(name);
                }
                loadingBar.dismiss();
                doctorNameSpinner.setItems(arrayListOfDoctorName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
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

    private void addSubject(final String department, final String subj, String level, final String doctorName) {
        final HashMap<String, Object> subjectDataMap = new HashMap<>();
        subjectDataMap.put("Subject_Name", subj);
        subjectDataMap.put("Doctor_Name", doctorName);
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Subjects").child(level);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child(department).child(subj).updateChildren(subjectDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Add this Subject is success", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(AddSubjectFragmentDirections.actionAddSubjectFragmentToAdminHomeFragment(doctorName));
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
