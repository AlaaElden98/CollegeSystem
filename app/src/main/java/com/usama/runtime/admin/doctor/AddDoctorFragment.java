package com.usama.runtime.admin.doctor;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
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
import com.usama.runtime.R;
import com.usama.runtime.admin.department.AddDepartmentFragmentArgs;

import java.util.HashMap;
import java.util.Objects;

public class AddDoctorFragment extends Fragment {
    private ProgressDialog loadingBar;
    private EditText doctor_name_et, doctor_nationalID_et, doctor_password_et;
    private Button add_doctor_btn;
    private String realName;

    public AddDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_doctor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        doctor_name_et = Objects.requireNonNull(getView()).findViewById(R.id.doctor_name_add_doctor);
        doctor_nationalID_et = getView().findViewById(R.id.doctor_nationalId__add_doctor);
        doctor_password_et = getView().findViewById(R.id.doctor_password__add_doctor);
        add_doctor_btn = getView().findViewById(R.id.add_doctor_btn_add_doctor);
        loadingBar = new ProgressDialog(getActivity());

        AddDepartmentFragmentArgs args = AddDepartmentFragmentArgs.fromBundle(getArguments());
        realName = args.getRealname();
        add_doctor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDoctor();
            }
        });

    }

    private void addDoctor() {
        String doctorName = doctor_name_et.getText().toString().trim();
        String doctorNationalId = doctor_nationalID_et.getText().toString().trim();
        String doctorPassword = doctor_password_et.getText().toString().trim();

        if (doctorName.equals("")) {
            Toast.makeText(getContext(), "please add doctor name ", Toast.LENGTH_SHORT).show();
        } else if (doctorNationalId.equals("")) {
            Toast.makeText(getContext(), "please add doctor national ID", Toast.LENGTH_SHORT).show();
        } else if (doctorPassword.equals("")) {
            Toast.makeText(getContext(), "please add doctor password", Toast.LENGTH_SHORT).show();
        } else {
            // wait until add doctor
            loadingBar.setTitle("Add Doctor");
            loadingBar.setMessage("please wait , while we are checking the credentials .");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateDepartment(doctorName, doctorNationalId, doctorPassword);
        }

    }

    private void ValidateDepartment(final String doctorName, final String doctorNationalId, final String doctorPassword) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Doctors").child(doctorNationalId).exists())) {
                    HashMap<String, Object> doctorDataMap = new HashMap<>();
                    doctorDataMap.put("realName", doctorName);
                    doctorDataMap.put("password", doctorNationalId);
                    doctorDataMap.put("nationalID", doctorPassword);
                    RootRef.child("Doctors").child(doctorNationalId).updateChildren(doctorDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Add this doctor is success", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                        Navigation.findNavController(getView()).navigate(AddDoctorFragmentDirections.actionAddDoctorFragmentToAdminHomeFragment(realName));
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(getActivity(), "Network Error: please try again after some time..", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(getActivity(), "This " + doctorNationalId + " already exist ", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Navigation.findNavController(getView()).navigate(AddDoctorFragmentDirections.actionAddDoctorFragmentToAdminHomeFragment(realName));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
