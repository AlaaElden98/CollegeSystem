package com.usama.runtime.doctor;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

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
import com.usama.runtime.model.Posts;
import com.usama.runtime.viewHolder.PostsViewHolder;

import io.paperdb.Paper;


public class DoctorHomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private String realName, nationalId;
    private DatabaseReference postsRef;
    private RecyclerView doctor_recycler_view;

    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    public DoctorHomeFragment() {

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.doctor_home_fragment, container, false);

        navigationView = view.findViewById(R.id.nav_view);

        drawer = view.findViewById(R.id.drawer_layout);
        toolbar = view.findViewById(R.id.toolbar_doctor);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DoctorHomeFragmentArgs args = DoctorHomeFragmentArgs.fromBundle(getArguments());
        realName = args.getRealName();
        nationalId = args.getNationalId();

        navigationView.setNavigationItemSelectedListener(this);

        // put header view
        final View headerView = navigationView.getHeaderView(0);
        final TextView user_name = headerView.findViewById(R.id.user_profile_name);
        user_name.setText(realName);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Home");

        toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        doctor_recycler_view = getView().findViewById(R.id.home_doctor_recycler_menu);
        doctor_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        doctor_recycler_view.setLayoutManager(linearLayoutManager);

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
        doctor_recycler_view.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // make sure that user who access to this fields .. not admin


        if (id == R.id.nav_add_post) {
            Navigation.findNavController(getView()).navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToAddNewPostFragment(realName, nationalId));
        } else if (id == R.id.nav_add_question) {
            Navigation.findNavController(getView()).navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToSpecificSubjectFragment(nationalId, realName));
        } else if (id == R.id.nav_show_subject) {
            Navigation.findNavController(getView()).navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToChooseLevelToShowSubjectToDoctorFragment(realName , nationalId));
        } else if (id == R.id.nav_make_exam) {
            Navigation.findNavController(getView()).navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToInfoOfExamFragment(nationalId, realName));
        } else if (id == R.id.nav_student_attendance) {
            Navigation.findNavController(getView()).navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToChooseUniqueNumberToShowAttendanceFragment());
        } else if (id == R.id.nav_logout) {
            // this line of code to destroy the save current student info
            Paper.book().destroy();
            Navigation.findNavController(getView()).navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToMainFragment());
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
