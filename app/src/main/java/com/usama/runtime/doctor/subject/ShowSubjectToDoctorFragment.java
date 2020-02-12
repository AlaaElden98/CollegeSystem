package com.usama.runtime.doctor.subject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.usama.runtime.student.E_Exam.ExamMainForStudentFragmentDirections;


public class ShowSubjectToDoctorFragment extends Fragment {
    private DatabaseReference subjectRef;
    private RecyclerView subjectList;

    private String nationaId, doctorName;

    public ShowSubjectToDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getView()).navigate(ShowSubjectToDoctorFragmentDirections.actionShowSubjectToDoctorFragmentToChooseLevelToShowSubjectToDoctorFragment(doctorName, nationaId));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_subject_to_doctor, container, false);
        subjectList = view.findViewById(R.id.subject_list_to_doctor);
        subjectList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ShowSubjectToDoctorFragmentArgs args = ShowSubjectToDoctorFragmentArgs.fromBundle(getArguments());
        final String level = args.getLevel();
        String department = args.getDepartment();
        doctorName = args.getDoctorName();
        nationaId = args.getNationalId();

        subjectRef = FirebaseDatabase.getInstance().getReference().child("Subjects").child(level).child(department);

        FirebaseRecyclerOptions<Subject> options
                = new FirebaseRecyclerOptions.Builder<Subject>()
                .setQuery(subjectRef, Subject.class)
                .build();

        FirebaseRecyclerAdapter<Subject, ShowSubjectToDoctorFragment.SubjectViewHolder> adapter
                = new FirebaseRecyclerAdapter<Subject, ShowSubjectToDoctorFragment.SubjectViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ShowSubjectToDoctorFragment.SubjectViewHolder holder, final int position, @NonNull Subject model) {
                holder.subjectName.setText("Subject name : " + model.getSubject_Name());
                holder.levelNumber.setText("Level : " + level);

            }

            @NonNull
            @Override
            public ShowSubjectToDoctorFragment.SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_to_doctor_one_item_layout, parent, false);
                return new SubjectViewHolder(view);
            }
        };
        subjectList.setAdapter(adapter);
        adapter.startListening();

    }


    private static class SubjectViewHolder extends RecyclerView.ViewHolder {

        TextView subjectName, levelNumber;

        SubjectViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectName = itemView.findViewById(R.id.subject_name_to_doctor);
            levelNumber = itemView.findViewById(R.id.level_number);
        }
    }


}