package com.usama.runtime.admin;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.usama.runtime.R;
import com.usama.runtime.admin.subject.ShowSubjectFragmentDirections;
import com.usama.runtime.model.Posts;
import com.usama.runtime.viewHolder.PostsViewHolder;

import java.util.Objects;

import io.paperdb.Paper;


public class AdminHomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference postsRef;
    private RecyclerView admin_recycler_view;
    private Toolbar toolbar;
    private String realName;
    private DrawerLayout drawer;
    private View view;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        view = inflater.inflate(R.layout.admin_home_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar_admin);
        toolbar.setTitle("Home");
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        drawer = view.findViewById(R.id.drawer_layout);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // make an instance in database
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postsRef.keepSynced(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = getView().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        admin_recycler_view = getView().findViewById(R.id.home_admin_recycler_menu);
        admin_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        admin_recycler_view.setLayoutManager(linearLayoutManager);


        AdminHomeFragmentArgs args = AdminHomeFragmentArgs.fromBundle(getArguments());
        if (args.getRealName() == null) {
            Toast.makeText(getContext(), "please check your internet", Toast.LENGTH_SHORT).show();
        } else {
            realName = args.getRealName();
            // put header view
            final View headerView = navigationView.getHeaderView(0);
            final TextView user_name = headerView.findViewById(R.id.user_profile_name);
            user_name.setText(realName);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postsRef.keepSynced(true);

        Query query = postsRef.orderByChild("counter");

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(query, Posts.class)
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
        admin_recycler_view.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // make sure that user who access to this fields .. not admin


        if (id == R.id.nav_add_department) {
            Navigation.findNavController(view).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToAddDepartmentFragment(realName));
        } else if (id == R.id.nav_add_subject) {
            Navigation.findNavController(view).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToAddSubjectFragment());
        } else if (id == R.id.nav_show_subject) {
            Navigation.findNavController(view).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToChooseLevelToShowSubjectFragment2(realName));
        } else if (id == R.id.nav_show_department) {
            Navigation.findNavController(view).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToShowDepartmentFragment(realName));
        } else if (id == R.id.nav_show_student) {
            Navigation.findNavController(view).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToShowAllStudentFragment());
        } else if (id == R.id.nav_add_doctor) {
            Navigation.findNavController((view)).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToAddDoctorFragment(realName));
        } else if (id == R.id.nav_show_doctor) {
            Navigation.findNavController(view).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToShowDoctorFragment());
        } else if (id == R.id.nav_logout) {
            // this line of code to destroy the save current student info
            Paper.book().destroy();
            Navigation.findNavController(view).navigate(AdminHomeFragmentDirections.actionAdminHomeFragmentToMainFragment());
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
