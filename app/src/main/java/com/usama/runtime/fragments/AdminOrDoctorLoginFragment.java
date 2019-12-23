package com.usama.runtime.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.rey.material.widget.CheckBox;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usama.runtime.Prevalent.Prevalent;
import com.usama.runtime.R;
import com.usama.runtime.model.Admin;
import com.usama.runtime.model.Student;

import io.paperdb.Paper;

import static android.content.Context.MODE_PRIVATE;


public class AdminOrDoctorLoginFragment extends Fragment {

    public static Admin adminData;

    private EditText login_name_et, login_password_et;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;
    private String parentDbName = "Doctors";
    private CheckBox chkBoxRememberMe;

    // variable to use in shared preference
    public static final String MT_NATIONAL_ID = "MyNationalId";


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

        LoginButton = getView().findViewById(R.id.login_btn);
        login_name_et = getView().findViewById(R.id.login_name_et);
        login_password_et = getView().findViewById(R.id.login_password_et);
        AdminLink = getView().findViewById(R.id.admin_panel_link);
        NotAdminLink = getView().findViewById(R.id.not_admin_panel_link);

        loadingBar = new ProgressDialog(getContext());


        chkBoxRememberMe = getView().findViewById(R.id.remember_me_chkb);
        // paper library
        Paper.init(getContext());






        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginDoctor();
            }
        });


        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Doctors");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Doctors";
            }
        });


        // check if student already login
        String name = Paper.book().read(Prevalent.NameKey);
        String password = Paper.book().read(Prevalent.PasswordKey);

        if (name != "" && password != "") {
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                AllowAccess(name, password);

                loadingBar.setTitle("Already Login");
                loadingBar.setMessage("please wait .. ");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }


    }
    private void AllowAccess(final String name, final String password) {

        // make database by a Reference
        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // here child phone is a unique object
                if (dataSnapshot.child(parentDbName).child(name).exists()) {
                    Admin usersData = dataSnapshot.child(parentDbName).child(password).getValue(Admin.class);

                    // retrieve the Admin data
                    if (usersData.getName().equals(name)) {
                        if (usersData.getPassword().equals(password)) {
                            Toast.makeText(getActivity(), "you are already login  ... ", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();

//                            Intent intent = new Intent(getActivity(), AdminOrDoctorLoginFragment.class);


                            // this line to make sure the app doesn't crash when cloth it and open again
                            // because we use paper library
                            Prevalent.CurrentOnlineAdminOrDoctor = adminData;
//                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getActivity(), "Password is incorrect ", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "Account with this " + name + " do not exist ", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void LoginDoctor() {
        String name = login_name_et.getText().toString();
        String password = login_password_et.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Please write your Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please write your Password....", Toast.LENGTH_LONG).show();
        } else {
            // wait to check is phone number is available in database
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("please wait , while we are checking the credentials .");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(name, password);

        }
    }

    private void AllowAccessToAccount(final String name, final String password) {
        if (chkBoxRememberMe.isChecked()) {
            // check box return two values true or false :)
            // method write take two parameter is key and value
            Paper.book().write(Prevalent.NameKey, name);
            Paper.book().write(Prevalent.PasswordKey, password);
        }


        // make database by a Reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(name).exists()) {


                    adminData = dataSnapshot.child(parentDbName).child(name).getValue(Admin.class);
                    Log.d("TAG", "Data" + adminData);
                    if (adminData.getName().equals(name)) {
                        if (adminData.getPassword().equals(password)) {
                            if (parentDbName.equals("Admins")) {
                                Toast.makeText(getActivity(), "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();

                                Navigation.findNavController(getView()).navigate(AdminOrDoctorLoginFragmentDirections.actionAdminOrDoctorLoginFragmentToButtonAdminFragment());
                            } else if (parentDbName.equals("Doctors")) {
                                Toast.makeText(getActivity(), "logged in Successfully...", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();



                                // MY_PREFS_NAME - a static String variable like:
                                //public static final String MY_PREFS_NAME = "MyPrefsFile";
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MT_NATIONAL_ID, MODE_PRIVATE).edit();
                                editor.putString("nationalId", name);
                                editor.apply();

                                //make this to make the user data public in all classes to use it
                                Prevalent.CurrentOnlineAdminOrDoctor = adminData;
                                Navigation.findNavController(getView()).navigate(AdminOrDoctorLoginFragmentDirections.actionAdminOrDoctorLoginFragmentToAddNewPostFragment());
//                                startActivity(intent);
                            }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getActivity(), "Password is incorrect.", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "Account with this " + name + " number do not exists.", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
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
