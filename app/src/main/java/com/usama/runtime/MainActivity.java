package com.usama.runtime;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.usama.runtime.fragments.department.AddDepartmentFragment;
import com.usama.runtime.fragments.AdminOrDoctorLoginFragment;
import com.usama.runtime.fragments.ButtonAdminFragment;
import com.usama.runtime.fragments.HomeFragment;
import com.usama.runtime.fragments.department.ShowDepartmentFragment;
import com.usama.runtime.fragments.StudentLoginFragment;


public class MainActivity extends AppCompatActivity implements StudentLoginFragment.OnFragmentInteractionListener,
        AdminOrDoctorLoginFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener
        , ButtonAdminFragment.OnFragmentInteractionListener
        , AddDepartmentFragment.OnFragmentInteractionListener
        , ShowDepartmentFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
