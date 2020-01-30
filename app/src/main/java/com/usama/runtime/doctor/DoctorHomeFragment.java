package com.usama.runtime.doctor;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;
import com.usama.runtime.R;

import io.paperdb.Paper;


public class DoctorHomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawer;


    public DoctorHomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.doctor_home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar = getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        drawer = getView().findViewById(R.id.drawer_layout);

        DrawerLayout drawer = getView().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = getView().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // put header view
        final View headerView = navigationView.getHeaderView(0);
        // TODO : PUT NAME OF DOCTOR HERE
        final TextView user_name = headerView.findViewById(R.id.user_profile_name);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // make sure that user who access to this fields .. not admin


        if (id == R.id.nav_add_post) {
            Navigation.findNavController(getView()).navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToAddNewPostFragment());
        } else if (id == R.id.nav_add_question) {
            Navigation.findNavController(getView()).navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToSpecificSubjectFragment());
        } else if (id == R.id.nav_show_subject) {
            Navigation.findNavController(getView()).navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToChooseLevelToShowSubjectFragment());
        } else if (id == R.id.nav_make_exam) {
            Navigation.findNavController(getView()).navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToInfoOfExamFragment());
        } else if (id == R.id.nav_logout) {
            // this line of code to destroy the save current student info
            Paper.book().destroy();
            Navigation.findNavController(getView()).navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToMainFragment());
        }
        DrawerLayout drawer = getView().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
