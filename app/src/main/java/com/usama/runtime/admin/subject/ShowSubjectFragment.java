package com.usama.runtime.admin.subject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usama.runtime.R;
import com.usama.runtime.model.Subject;

import java.util.Objects;

public class ShowSubjectFragment extends Fragment {
    private DatabaseReference subjectRef;
    private String doctorName;

    public ShowSubjectFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(ShowSubjectFragmentDirections.actionShowSubjectFragmentToAdminHomeFragment(doctorName));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view =  inflater.inflate(R.layout.fragment_show_subject, container, false);

        return view ;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ShowSubjectFragmentArgs args = ShowSubjectFragmentArgs.fromBundle(getArguments());
        String level = args.getLevel();
        String department = args.getDepartment();
        doctorName = args.getRealName();
        subjectRef = FirebaseDatabase.getInstance().getReference().child("Subjects").child(level).child(department);

        RecyclerView subjectList = getView().findViewById(R.id.subject_list);
        subjectList.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerOptions<Subject> options
                = new FirebaseRecyclerOptions.Builder<Subject>()
                .setQuery(subjectRef, Subject.class)
                .build();

        FirebaseRecyclerAdapter<Subject, ShowSubjectFragment.SubjectViewHolder> adapter
                = new FirebaseRecyclerAdapter<Subject, SubjectViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SubjectViewHolder holder, final int position, @NonNull Subject model) {
                Log.d("TAG", model.getSubject_Name());
                holder.subjectName.setText("Subject name : " + model.getSubject_Name());
                holder.doctorName.setText("Doctor name : " + model.getDoctor_Name());

                holder.deleteSubjectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence option[] = new CharSequence[]{
                                // here you put the option you need to show
                                "Yes",
                                "No"

                        };
                        // alert dialog take a context
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Are you sure !! you will delete this Subject ");

                        // here you show the option in dialog yes or no with on click listener
                        // here you have a int as a position
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                // if i == 0 --> the user chick in yes button
                                if (i == 0) {
                                    String subjectName = getRef(position).getKey();
                                    RemoverSubject(subjectName);
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
            public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_one_item_layout, parent, false);
                return new SubjectViewHolder(view);
            }
        };
        subjectList.setAdapter(adapter);
        adapter.startListening();

    }


    private static class SubjectViewHolder extends RecyclerView.ViewHolder {

        TextView subjectName, doctorName;
        Button deleteSubjectBtn;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectName = itemView.findViewById(R.id.subject_name);
            doctorName = itemView.findViewById(R.id.doctor_name);
            deleteSubjectBtn = itemView.findViewById(R.id.delete_subject_btn);
        }
    }

    private void RemoverSubject(String subjectName) {
        subjectRef.child(subjectName).removeValue();
    }

}