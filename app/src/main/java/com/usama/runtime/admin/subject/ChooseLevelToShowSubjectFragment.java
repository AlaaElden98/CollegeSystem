package com.usama.runtime.admin.subject;

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
import com.usama.runtime.doctor.AddNewPostFragmentDirections;
import com.usama.runtime.doctor.subject.ChooseLevelToShowSubjectToDoctorFragmentArgs;
import com.usama.runtime.model.Department;

import java.util.ArrayList;


public class ChooseLevelToShowSubjectFragment extends Fragment {
    private String levelChoose, departmentChoose, doctorName;
    private MaterialSpinner chooseLevel, chooseDepartment;
    private Button buttonShowSubject;
    private ArrayList<String> arrayListOfDepartment;
    private ProgressDialog loadingBar;

    public ChooseLevelToShowSubjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_level_to_show_subject, container, false);
        chooseLevel = view.findViewById(R.id.spinner_choose_Level_to_show_subject);
        chooseDepartment = view.findViewById(R.id.spinner_choose_department_to_show_subject);
        buttonShowSubject = view.findViewById(R.id.buttonShowSubject);


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ChooseLevelToShowSubjectToDoctorFragmentArgs args = ChooseLevelToShowSubjectToDoctorFragmentArgs.fromBundle(getArguments());
        doctorName = args.getDoctorName();

        loadingBar = new ProgressDialog(getContext());
        arrayListOfDepartment = new ArrayList<>();


        chooseLevel.setItems("Choose Level", "Level_One", "Level_Two", "Level_Three", "Level_Four");
        chooseLevel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
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
        chooseDepartment.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
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
                if (chooseLevel == null) {
                    Toast.makeText(getContext(), "please make sure all field is valid", Toast.LENGTH_SHORT).show();
                } else {
                    Navigation.findNavController(getView()).navigate(ChooseLevelToShowSubjectFragmentDirections.actionChooseLevelToShowSubjectFragmentToShowSubjectFragment(levelChoose, departmentChoose , doctorName));
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
                chooseDepartment.setItems(arrayListOfDepartment);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }
}
