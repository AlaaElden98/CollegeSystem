package com.usama.runtime;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.rey.material.widget.CheckBox;
import com.usama.runtime.Prevalent.Prevalent;
import com.usama.runtime.model.Doctors;

import java.util.Objects;

import io.paperdb.Paper;


public class AdminOrDoctorLoginFragment extends Fragment {
    private EditText login_nationalId_et, login_password_et;
    private Button LoginButton;
    private CheckBox chkBoxRememberMe;
    private ProgressDialog loadingBar;
    private TextView adminLink, doctorLink;
    private String parentDbNationalId = "Doctors", realName;
    private static Doctors doctorsData;
    // variable to use in shared preference
    private static final String DoctorNationalId = "DoctorNationalId";


    public AdminOrDoctorLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_or_doctor_login, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LoginButton = Objects.requireNonNull(getView()).findViewById(R.id.login_admin_or_doctor_btn);
        login_nationalId_et = getView().findViewById(R.id.login_nationalId_doctor_or_admin);
        login_password_et = getView().findViewById(R.id.login_password_et);
        adminLink = getView().findViewById(R.id.admin_link);
        doctorLink = getView().findViewById(R.id.doctor_link);

        loadingBar = new ProgressDialog(getContext());


        chkBoxRememberMe = getView().findViewById(R.id.remember_me_chkb);
        // paper library
        Paper.init(Objects.requireNonNull(getContext()));


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginDoctor();
            }
        });


        adminLink.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                doctorLink.setVisibility(View.VISIBLE);
                parentDbNationalId = "Admins";
            }
        });

        doctorLink.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Doctors");
                adminLink.setVisibility(View.VISIBLE);
                doctorLink.setVisibility(View.INVISIBLE);
                parentDbNationalId = "Doctors";
            }
        });


    }

    private void LoginDoctor() {
        String nationalId = login_nationalId_et.getText().toString();
        String password = login_password_et.getText().toString();

        if (TextUtils.isEmpty(nationalId)) {
            Toast.makeText(getActivity(), "Please write your NationalId", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please write your Password....", Toast.LENGTH_LONG).show();
        } else {
            // wait to check is phone number is available in database
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("please wait , while we are checking the credentials .");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(nationalId, password);

        }
    }

    private void AllowAccessToAccount(final String nationalID, final String password) {
        if (chkBoxRememberMe.isChecked()) {
            // check box return two values true or false :)
            // method write take two parameter is key and value
            Paper.book().write(Prevalent.DoctorOrAdminNationalIDKey, nationalID);
            Paper.book().write(Prevalent.DoctorOrAdminPasswordKey, password);

            Log.d("TAGFROMADMINOR", nationalID);
            Log.d("TAGFROMADMINOR", password);
            loadingBar.dismiss();
        }
        // make database by a Reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbNationalId).child(nationalID).exists()) {
                    doctorsData = dataSnapshot.child(parentDbNationalId).child(nationalID).getValue(Doctors.class);
                    if (Objects.requireNonNull(doctorsData).getNationalID().equals(nationalID)) {
                        if (doctorsData.getPassword().equals(password)) {
                            if (parentDbNationalId.equals("Doctors")) {
                                realName = doctorsData.getRealName();
                                Toast.makeText(getContext(), "Welcome Doctor " + nationalID, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Prevalent.CurrentOnlineAdminOrDoctor = doctorsData;
                                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(AdminOrDoctorLoginFragmentDirections.actionAdminOrDoctorLoginFragmentToDoctorHomeFragment(realName,nationalID));
                            } else if (parentDbNationalId.equals("Admins")) {
                                Toast.makeText(getContext(), "Welcome Admin " + nationalID, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Prevalent.CurrentOnlineAdminOrDoctor = doctorsData;
                                realName = doctorsData.getRealName();
                                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(AdminOrDoctorLoginFragmentDirections.actionAdminOrDoctorLoginFragmentToAdminHomeFragment(realName));
                            }
                        }
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(getContext(), "Password is incorrect.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Account with this " + nationalID + " do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
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
