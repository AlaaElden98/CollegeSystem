package com.usama.runtime.doctor.subject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.usama.runtime.R;
import com.usama.runtime.admin.department.AddDepartmentFragmentDirections;
import com.usama.runtime.admin.subject.ChooseLevelToShowSubjectFragmentDirections;
import com.usama.runtime.model.Department;

import java.util.ArrayList;
import java.util.Objects;

public class ChooseLevelToShowSubjectToDoctorFragment extends Fragment {
    private String levelChoose, departmentChoose, doctorName, nationalID;
    private MaterialSpinner spinner_choose_level, spinner_choose_department;
    private Button buttonShowSubject;
    private ArrayList<String> arrayListOfDepartment;
    private ProgressDialog loadingBar;


    public ChooseLevelToShowSubjectToDoctorFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // handel back press .. when admin click back he will go to home fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(ChooseLevelToShowSubjectToDoctorFragmentDirections.actionChooseLevelToShowSubjectToDoctorFragmentToDoctorHomeFragment(doctorName, nationalID));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_level_to_show_subject_to_doctor, container, false);
        spinner_choose_level = view.findViewById(R.id.spinner_choose_Level_to_show_subject_to_doctor);
        spinner_choose_department = view.findViewById(R.id.spinner_choose_department_to_show_subject_to_doctor);
        buttonShowSubject = view.findViewById(R.id.show_subject_to_doctor_btn);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ChooseLevelToShowSubjectToDoctorFragmentArgs args = ChooseLevelToShowSubjectToDoctorFragmentArgs.fromBundle(getArguments());
        doctorName = args.getDoctorName();
        nationalID = args.getNationalId();
        loadingBar = new ProgressDialog(getContext());
        arrayListOfDepartment = new ArrayList<>();


        spinner_choose_level.setItems("Choose Level", "Level_One", "Level_Two", "Level_Three", "Level_Four");
        spinner_choose_level.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item == "Choose Level") {
                    Toast.makeText(getContext(), "Please choose Level", Toast.LENGTH_SHORT).show();
                } else {
                    levelChoose = item.toString();
                    loadingBar.setTitle("Showing Department");
                    loadingBar.setMessage("please wait , while we are checking the credentials .");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    getDepartment();
                }
                Log.d("TAG", item.toString());
            }
        });
        spinner_choose_department.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item == null) {
                    Toast.makeText(getContext(), "please choose department", Toast.LENGTH_SHORT).show();
                } else {
                    departmentChoose = item.toString();
                }
            }
        });

        buttonShowSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levelChoose == null) {
                    Toast.makeText(getContext(), "please make sure all field is valid", Toast.LENGTH_SHORT).show();
                } else {
                    Navigation.findNavController(getView()).navigate(ChooseLevelToShowSubjectToDoctorFragmentDirections.actionChooseLevelToShowSubjectToDoctorFragmentToShowSubjectToDoctorFragment(departmentChoose, levelChoose, doctorName ,nationalID));
                }
            }
        });
    }

    private void getDepartment() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("departments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAGDepartment", dataSnapshot.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String departmentName = snapshot.getValue(Department.class).getDepartmentName();
                    arrayListOfDepartment.add(departmentName);
                }
                loadingBar.dismiss();
                spinner_choose_department.setItems(arrayListOfDepartment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }
}
