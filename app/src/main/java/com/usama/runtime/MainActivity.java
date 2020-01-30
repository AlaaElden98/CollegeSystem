package com.usama.runtime;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.usama.runtime.student.HomeFragment;
import com.usama.runtime.student.StudentLoginFragment;


public class MainActivity extends AppCompatActivity implements StudentLoginFragment.OnFragmentInteractionListener,
        AdminOrDoctorLoginFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
