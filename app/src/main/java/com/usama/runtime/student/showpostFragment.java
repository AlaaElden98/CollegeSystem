package com.usama.runtime.student;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.usama.runtime.R;
import com.usama.runtime.model.Posts;

import static android.content.Context.MODE_PRIVATE;

public class showpostFragment extends Fragment {

    private RecyclerView postList;
    private DatabaseReference postRef;
    private static final String DoctorName = "DoctorName";
    private LinearLayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        postList = getView().findViewById(R.id.Posts_list);
        postList.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         // Inflate the layout for this fragment

        return inflater.inflate( R.layout.fragment_showpost, container, false );
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = getActivity().getSharedPreferences(DoctorName, MODE_PRIVATE);
        //Query firebasesearch =postRef.orderByChild("id").startAt().endAt(+"\uf8ff");

        Query firebasesearch =postRef.orderByChild("id").startAt(prefs.getString("DoctorID", "")).endAt(prefs.getString("DoctorID", "")+"\uf8ff");

        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                        .setQuery(firebasesearch, Posts.class)
                        .build();
        FirebaseRecyclerAdapter<Posts,PostViewHolder> adapter
                = new FirebaseRecyclerAdapter<Posts,PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostViewHolder holder, final int position, @NonNull final Posts model) {
                    holder.DoctorName.setText(model.getName());
                    holder.Post_subject.setText( model.getSubject());
                    holder.Post_description.setText(model.getDescription());
                    holder.post_date.setText(model.getDataAndTime() );
                holder.Delete_student_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // display a message
                        // dialog
                        CharSequence option[] = new CharSequence[]{
                                // here you put the option you need to show
                                "Yes",
                                "No"

                        };
                        // alert dialog take a context
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Are you sure !! you will delete this department ");

                        // here you show the option in dialog yes or no with on click listener
                        // here you have a int as a position
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                // if i == 0 --> the user chick in yes button
                                if (i == 0) {
                                    String uID = getRef(position).getKey();

                                    // this method to remove have one parameter uID --> id of user order (phone)
                                    RemoverDepartment(uID);
                                    // else if the user chick in the no button
                                } else {
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_items_for_doctor, parent, false);

                return new PostViewHolder(view);
            }
        };
        //mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(mLayoutManager);

        postList.setAdapter(adapter);
        adapter.startListening();

    }

    private static class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView  DoctorName, Post_subject, Post_description, post_date;

        public Button Delete_student_btn;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
           // if(Post_description.equals( "2" )) {
            DoctorName = itemView.findViewById( R.id.doctor_name );
            Post_subject = itemView.findViewById( R.id.subject );
            Post_description = itemView.findViewById( R.id.post_description );
            post_date = itemView.findViewById( R.id.dateAndTime );
            Delete_student_btn = itemView.findViewById(R.id.Delete_post_btn);

       }
    }
    private void RemoverDepartment(String uID) {
        // first connect with database
        // orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        postRef.child(uID).removeValue();
    }

}
