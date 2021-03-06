package com.usama.runtime;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.usama.runtime.Prevalent.Prevalent;
import com.usama.runtime.admin.department.AddDepartmentFragmentDirections;
import com.usama.runtime.model.Doctors;
import com.usama.runtime.model.Student;

import java.util.Objects;

import io.paperdb.Paper;


public class MainFragment extends Fragment {
    private ProgressDialog loadingBar;
    private Button studentBtn, adminOrDoctorBtn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // handel back press .. when admin click back he will go to home fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        studentBtn = view.findViewById(R.id.main_student_login_btn);
        adminOrDoctorBtn = view.findViewById(R.id.main_doctors_admins_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Paper.init(getContext());
        loadingBar = new ProgressDialog(getContext());

        studentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StudentNationalId = Paper.book().read(Prevalent.StudentNationalIdKey);
                String StudentSittingNumber = Paper.book().read(Prevalent.StudentSittingNumberKey);
                if (StudentNationalId != "" && StudentSittingNumber != "") {
                    if (!TextUtils.isEmpty(StudentNationalId) && !TextUtils.isEmpty(StudentSittingNumber)) {
                        loadingBar.setTitle("Already Login");
                        loadingBar.setMessage("please wait .. ");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        AllowAccessStudent(StudentNationalId, StudentSittingNumber);
                    } else {
                        Navigation.findNavController(v).navigate(MainFragmentDirections.actionMainGoToStudentFragment());
                    }
                } else {
                    Navigation.findNavController(v).navigate(MainFragmentDirections.actionMainGoToStudentFragment());
                }
            }
        });

        adminOrDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DoctorOrAdminNationalId = Paper.book().read(Prevalent.DoctorOrAdminNationalIDKey);
                String DoctorOrAdminPassword = Paper.book().read(Prevalent.DoctorOrAdminPasswordKey);
                Log.d("TAG", DoctorOrAdminNationalId + DoctorOrAdminPassword);
                if (DoctorOrAdminNationalId != null && DoctorOrAdminPassword != null) {
                    if (!TextUtils.isEmpty(DoctorOrAdminNationalId) && !TextUtils.isEmpty(DoctorOrAdminPassword)) {
                        loadingBar.setTitle("Already Login");
                        loadingBar.setMessage("please wait .. ");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        AllowAccessDoctor(DoctorOrAdminNationalId, DoctorOrAdminPassword);
                    } else {
                        Navigation.findNavController(v).navigate(MainFragmentDirections.actionMainFragmentToAdminOrDoctorLoginFragment());
                    }
                } else {
                    Navigation.findNavController(v).navigate(MainFragmentDirections.actionMainFragmentToAdminOrDoctorLoginFragment());

                }

            }
        });


    }

    private void AllowAccessDoctor(final String national, final String password) {
        // make database by a Reference
        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // here child phone is a unique object
                if (dataSnapshot.child("Admins").child(national).exists()) {
                    Doctors doctorData = dataSnapshot.child("Admins").child(national).getValue(Doctors.class);

                    // retrieve the user data
                    if (doctorData.getNationalID().equals(national)) {
                        if (doctorData.getPassword().equals(password)) {
                            String realname = doctorData.getRealName();
                            Toast.makeText(getContext(), "you are already login  ... ", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            // this line to make sure the app doesn't crash when cloth it and open again
                            // because we use paper library
                            Prevalent.CurrentOnlineAdminOrDoctor = doctorData;
                            Navigation.findNavController(getView()).navigate(MainFragmentDirections.actionMainFragmentToAdminHomeFragment(realname));
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getContext(), "Password is incorrect ", Toast.LENGTH_SHORT).show();
                            Paper.book().destroy();
                        }
                    }
                } else if (dataSnapshot.child("Doctors").child(national).exists()) {
                    Doctors doctorData = dataSnapshot.child("Doctors").child(national).getValue(Doctors.class);
                    if (doctorData.getNationalID().equals(national)) {
                        if (doctorData.getPassword().equals(password)) {
                            Toast.makeText(getContext(), "you are already login ...", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            Prevalent.CurrentOnlineAdminOrDoctor = doctorData;
                            String realName = doctorData.getRealName();
                            Navigation.findNavController(getView()).navigate(MainFragmentDirections.actionMainFragmentToDoctorHomeFragment(realName, national));
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getContext(), "Password is incorrect ", Toast.LENGTH_SHORT).show();
                            Paper.book().destroy();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Account with this " + national + " number do not exist ", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AllowAccessStudent(final String nationalId, final String sittingNumber) {
        // make database by a Reference
        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // here child phone is a unique object
                if (dataSnapshot.child("students").child(nationalId).exists()) {
                    Student studentData = dataSnapshot.child("students").child(nationalId).getValue(Student.class);

                    // retrieve the user data
                    if (studentData.getNational_id().equals(nationalId)) {
                        if (studentData.getNt_ID().equals(sittingNumber)) {
                            Toast.makeText(getContext(), "you are already login  ... ", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            // this line to make sure the app doesn't crash when cloth it and open again
                            // because we use paper library
                            Prevalent.CurrentOnlineStudent = studentData;
                            Navigation.findNavController(getView()).navigate(MainFragmentDirections.actionMainFragmentToHomeFragment());
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getContext(), "Password is incorrect ", Toast.LENGTH_SHORT).show();
                            Paper.book().destroy();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Account with this " + nationalId + " number do not exist ", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
