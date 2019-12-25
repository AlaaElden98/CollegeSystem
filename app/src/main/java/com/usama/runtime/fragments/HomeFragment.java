package com.usama.runtime.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usama.runtime.R;
import com.usama.runtime.model.Posts;
import com.usama.runtime.viewHolder.PostsViewHolder;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import io.paperdb.Paper;


public class HomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    // make data base reference to retrieve all data

    private DatabaseReference postsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


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

        // make an instance in database
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postsRef.keepSynced(true);

        Toolbar toolbar = getView().findViewById(R.id.toolbar);
        toolbar.setTitle("Home");

        Paper.init(getActivity());

        DrawerLayout drawer = getView().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = getView().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = getView().findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        //can retrieve all data
        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(postsRef, Posts.class)
                .build();

        FirebaseRecyclerAdapter<Posts, PostsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull Posts posts) {
                        holder.txtPostSubject.setText(posts.getSubject());
                        holder.txtPostDescription.setText(posts.getDescription());
                        holder.txtPostDoctorName.setText(posts.getName());
                        holder.dateAndTime.setText(posts.getDataAndTime());

                    }

                    @NonNull
                    @Override
                    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_layout, parent, false);
                        PostsViewHolder holder = new PostsViewHolder(view);
                        return holder;
                    }
                };
        // add adapter in recyclerView
        recyclerView.setAdapter(adapter);
        adapter.startListening();

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
        }
            else if (id == R.id.attendance_counter) {
//            Intent intent = new Intent(HomeActivity.this, BarCodeActivity.class);
//            startActivity(intent);
                Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToAttendanceCounterFragment());
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
