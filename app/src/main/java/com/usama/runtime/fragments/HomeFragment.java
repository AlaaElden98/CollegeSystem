package com.usama.runtime.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.usama.runtime.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import io.paperdb.Paper;

public class HomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = getView().findViewById(R.id.toolbar);
        toolbar.setTitle("Home");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        Paper.init(getActivity());

        DrawerLayout drawer = getView().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = getView().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // make sure that user who access to this fields .. not admin


        if (id == R.id.nav_department_desires) {
            Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToRecordingDesiresFragment());

        } else if (id == R.id.nav_barcode) {
//            Intent intent = new Intent(HomeActivity.this, BarCodeActivity.class);
//            startActivity(intent);
            Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToBarCodeFragment());
        } else if (id == R.id.nav_news) {

        } else if (id == R.id.nav_logout) {
            // this line of code to destroy the save current student info
            Paper.book().destroy();
            Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToMainFragment());
        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
