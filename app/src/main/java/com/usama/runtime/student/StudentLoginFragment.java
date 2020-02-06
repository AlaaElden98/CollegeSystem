package com.usama.runtime.student;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.rey.material.widget.CheckBox;
import com.usama.runtime.Prevalent.Prevalent;
import com.usama.runtime.R;
import com.usama.runtime.model.Student;

import java.util.Objects;

import io.paperdb.Paper;

import static android.content.Context.MODE_PRIVATE;

public class StudentLoginFragment extends Fragment {
    // get reference from student pojo class
    private static Student studentData;
    private EditText login_nationalId, login_sitting_number;
    private ProgressDialog loadingBar;
    private CheckBox chkBoxRememberMe;

    // variable to use in shared preference
    // to show student name in Home fragment
    private static final String MT_NATIONAL_ID = "MyNationalId";


    public StudentLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // handel back press .. when admin click back he will go to home fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getView()).navigate(StudentLoginFragmentDirections.actionStudentLoginFragmentToMainFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_login, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button loginButton = Objects.requireNonNull(getView()).findViewById(R.id.login_btn);
        login_nationalId = getView().findViewById(R.id.login_nationalId);
        login_sitting_number = getView().findViewById(R.id.login_sitting_number);

        loadingBar = new ProgressDialog(getContext());
        chkBoxRememberMe = getView().findViewById(R.id.remember_me_chkb);
        // paper library
        Paper.init(Objects.requireNonNull(getContext()));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginStudent();
            }
        });

    }

    private void LoginStudent() {
        String nationalID = login_nationalId.getText().toString();
        String sittingNumber = login_sitting_number.getText().toString();

        if (TextUtils.isEmpty(nationalID)) {
            Toast.makeText(getContext(), "Please write your National ID....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sittingNumber)) {
            Toast.makeText(getContext(), "Please write your sitting Number....", Toast.LENGTH_SHORT).show();
        } else {
            // wait to check is phone number is available in database
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("please wait , while we are checking the credentials .");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(nationalID, sittingNumber);

        }
    }

    private void AllowAccessToAccount(final String national_id, final String nt_ID) {
        if (chkBoxRememberMe.isChecked()) {
            // check box return two values true or false :)
            // method write take two parameter is key and value
            Paper.book().write(Prevalent.StudentNationalIdKey, national_id);
            Paper.book().write(Prevalent.StudentSittingNumberKey, nt_ID);
        }


        // get reference from firebase
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("students").child(national_id).exists()) {


                    studentData = dataSnapshot.child("students").child(national_id).getValue(Student.class);
                    if (Objects.requireNonNull(studentData).getNational_id().equals(national_id)) {
                        if (studentData.getNt_ID().equals(nt_ID)) {

                            Toast.makeText(getActivity(), "Welcome " + studentData.getName() + " your logged is Successfully...", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences(MT_NATIONAL_ID, MODE_PRIVATE).edit();
                            editor.putString("nationalId", national_id);
                            editor.apply();

                            //make this to make the user data public in all classes to use it
                            Prevalent.CurrentOnlineStudent = studentData;
                            // if login is valid --> go to Home fragment
                            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(StudentLoginFragmentDirections.actionStudentLoginFragmentToHomeFragment());

                        } else {
                            Toast.makeText(getActivity(), "Account with this " + nt_ID + " number do not exists.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "Account with this " + national_id + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
