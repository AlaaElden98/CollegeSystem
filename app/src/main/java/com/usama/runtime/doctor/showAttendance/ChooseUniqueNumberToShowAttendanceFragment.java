package com.usama.runtime.doctor.showAttendance;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.usama.runtime.doctor.subject.ChooseLevelToShowSubjectToDoctorFragmentArgs;
import com.usama.runtime.doctor.subject.ChooseLevelToShowSubjectToDoctorFragmentDirections;
import com.usama.runtime.model.Department;
import com.usama.runtime.model.StudentsAttendance;

import java.util.ArrayList;

public class
ChooseUniqueNumberToShowAttendanceFragment extends Fragment {
    private String uniqueNumberChoose;
    private MaterialSpinner spinner_choose_unique_number;
    private Button buttonShowStudentAttendance;
    private ArrayList<String> arrayListOfUniqueNumber;
    private ProgressDialog loadingBar;

    public ChooseUniqueNumberToShowAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_unique_number_to_show_attendance, container, false);
        spinner_choose_unique_number = view.findViewById(R.id.spinner_choose_unique_number_to_show_students_attendance);
        buttonShowStudentAttendance = view.findViewById(R.id.show_students_attendance_btn);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadingBar = new ProgressDialog(getContext());
        arrayListOfUniqueNumber = new ArrayList<>();

        getUniqueNumber();

        spinner_choose_unique_number.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                uniqueNumberChoose = item.toString();
            }
        });
        buttonShowStudentAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uniqueNumberChoose == null) {
                    Toast.makeText(getContext(), "please make sure all field is valid", Toast.LENGTH_SHORT).show();
                } else {
                    Navigation.findNavController(getView()).navigate(ChooseUniqueNumberToShowAttendanceFragmentDirections.actionChooseUniqueNumberToShowAttendanceFragmentToShowStudentAttendanceFragment(uniqueNumberChoose));
                }
            }
        });
    }

    private void getUniqueNumber() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("students_attendance");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    arrayListOfUniqueNumber.add(snapshot.getKey());
                }
                spinner_choose_unique_number.setItems(arrayListOfUniqueNumber);
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }
}
