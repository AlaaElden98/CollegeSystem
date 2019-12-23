package com.usama.runtime.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.usama.runtime.R;


public class ButtonAdminFragment extends Fragment {

    // TODO : MAKE VALIDATION OF LOGIN AND REGISTER
    Button addDepartmentBtn, showDepartmentBtn, showStudentBtn, logoutBtn;

    public ButtonAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_button_admin, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logoutBtn = getView().findViewById(R.id.logoutBtn);
        addDepartmentBtn = getView().findViewById(R.id.addDepartmentBtn);
        showDepartmentBtn = getView().findViewById(R.id.showDepartmentBtn);
        showStudentBtn = getView().findViewById(R.id.showStudentBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(ButtonAdminFragmentDirections.actionButtonAdminFragmentToMainFragment());
            }
        });


        addDepartmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(ButtonAdminFragmentDirections.actionButtonAdminFragmentToAddDepartmentFragment());

            }
        });
        showDepartmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(ButtonAdminFragmentDirections.actionButtonAdminFragmentToShowDepartmentFragment());
            }
        });

        showStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(ButtonAdminFragmentDirections.actionButtonAdminFragmentToShowAllStudentFragment());
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
