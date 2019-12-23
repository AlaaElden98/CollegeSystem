package com.usama.runtime.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.usama.runtime.R;

public class MainFragment extends Fragment {
    private Button studentBtn, adminOrDoctorsBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        studentBtn = getView().findViewById(R.id.main_student_login_btn);

        adminOrDoctorsBtn = getView().findViewById(R.id.main_doctors_admins_btn);


        studentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(MainFragmentDirections.actionMainGoToStudentFragment());
            }
        });

        adminOrDoctorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(MainFragmentDirections.actionMainFragmentToAdminOrDoctorLoginFragment());
            }
        });
    }

}
