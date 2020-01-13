package com.usama.runtime.fragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usama.runtime.R;
import com.usama.runtime.model.Posts;
import com.usama.runtime.model.Student;
import com.usama.runtime.viewHolder.PostsViewHolder;
import com.usama.runtime.viewModel.HomeActivityViewModel;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import io.paperdb.Paper;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {


    private DatabaseReference postsRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    // TODO : PROCESS TO MAKE FINAL DESIRES
    private HomeActivityViewModel homeActivityViewModel;
    private SharedPreferences prefs;
    private DatabaseReference desiresReference, studentRefrence;
    private String nationalId, finalDesiers, studentName;
    private static final String MY_NATIONAL_ID = "MyNationalId";
    private Student studentData;

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

        // put header view
        final View headerView = navigationView.getHeaderView(0);
        final TextView user_name = headerView.findViewById(R.id.user_profile_name);
        recyclerView = getView().findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        // TODO : VIEW MODEL TO MAKE FINAL DESIRES
        homeActivityViewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);

        prefs = getActivity().getSharedPreferences(MY_NATIONAL_ID, MODE_PRIVATE);
        nationalId = prefs.getString("nationalId", "2");//"No name defined" is the default value.
        Log.d("SharedPrefrence",nationalId);


        studentRefrence = FirebaseDatabase.getInstance().getReference().getRef().child("students");
        studentRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentData = dataSnapshot.child(nationalId).getValue(Student.class);
                studentName = studentData.getName();
                user_name.setText(studentData.getName());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        homeActivityViewModel.getFinalDesires(nationalId);
        homeActivityViewModel.finalSelectedDesirsd.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                finalDesiers = s;
                if (finalDesiers != null) {
                    desiresReference = FirebaseDatabase.getInstance().getReference().child("student_department");
                    HashMap<String, Object> desiresMap = new HashMap<>();
                    desiresMap.put(nationalId, studentName);
                    desiresReference.child(finalDesiers).updateChildren(desiresMap);

                    // update student child in firebase to add his final desires
                    HashMap<String, Object> departmentMap = new HashMap<>();
                    departmentMap.put("department", finalDesiers);
                    studentRefrence.child(nationalId).updateChildren(departmentMap);

                }
            }
        });
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
            if (finalDesiers == null) {
                Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToRecordingDesiresFragment());
            } else {
                Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToDesiresAcceptedFragment(finalDesiers));

            }
        } else if (id == R.id.nav_barcode) {
//            Intent intent = new Intent(HomeActivity.this, BarCodeActivity.class);
//            startActivity(intent);
            Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToBarCodeFragment());
        } else if (id == R.id.attendance_counter) {
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
