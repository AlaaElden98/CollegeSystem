package com.usama.runtime.admin;

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

import java.util.Objects;

import io.paperdb.Paper;


public class AdminHomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private String realName ;
    public AdminHomeFragment() {
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
        return inflater.inflate(R.layout.admin_home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = Objects.requireNonNull(getView()).findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        AdminHomeFragmentArgs args = AdminHomeFragmentArgs.fromBundle(getArguments());
        realName = args.getRealName();

        NavigationView navigationView = getView().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // put header view
        final View headerView = navigationView.getHeaderView(0);
        final TextView user_name = headerView.findViewById(R.id.user_profile_name);
        user_name.setText(realName);


        DrawerLayout drawer = getView().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // make sure that user who access to this fields .. not admin


        if (id == R.id.nav_add_department) {
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToAddDepartmentFragment(realName));
        } else if (id == R.id.nav_add_subject) {
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToAddSubjectFragment());
        } else if (id == R.id.nav_show_subject) {
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToChooseLevelToShowSubjectFragment2());
        } else if (id == R.id.nav_show_department) {
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToShowDepartmentFragment());
        } else if (id == R.id.nav_show_student) {
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToShowAllStudentFragment());
        } else if (id == R.id.nav_logout) {
            // this line of code to destroy the save current student info
            Paper.book().destroy();
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToMainFragment());
        }
        DrawerLayout drawer = Objects.requireNonNull(getView()).findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
