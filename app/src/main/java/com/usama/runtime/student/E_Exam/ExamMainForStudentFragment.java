package com.usama.runtime.student.E_Exam;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.usama.runtime.R;
import com.usama.runtime.model.Exam;

import java.util.ArrayList;


public class ExamMainForStudentFragment extends Fragment {


    private MaterialSpinner spinner_choose_subject;
    private ArrayList<String> arrayListOfSubjects;
    private String subjectChoose, department;
    private Button startQuiz;

    public ExamMainForStudentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getView()).navigate(ExamMainForStudentFragmentDirections.actionExamMainForStudentFragmentToHomeFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_main_for_student, container, false);
        // init view
        startQuiz = view.findViewById(R.id.start_quiz_btn);
        spinner_choose_subject = view.findViewById(R.id.spinner_choose_subject_exam);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        subjectChoose = "choose subject";

        arrayListOfSubjects = new ArrayList<>();
        ExamMainForStudentFragmentArgs args = ExamMainForStudentFragmentArgs.fromBundle(getArguments());
        department = args.getDepartment();


        getSubject(department);
        spinner_choose_subject.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                subjectChoose = item.toString();
                checkIfHaveExam(subjectChoose);
            }
        });


        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subjectChoose != null && !subjectChoose.equals("choose subject")) {
                    Navigation.findNavController(getView()).navigate(ExamMainForStudentFragmentDirections.examMainToQuestionFragment(subjectChoose, department));
                } else {
                    Toast.makeText(getContext(), "Make sure you choose subject", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // return all subject find in department
    private void getSubject(final String department) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Subjects").child("Level_One").child(department);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String subject = snapshot.getKey();
                    arrayListOfSubjects.add(subject);
                }
                spinner_choose_subject.setItems(arrayListOfSubjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfHaveExam(final String subjectChoose) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Exam").child("Level_One");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(department).child(subjectChoose).exists()) {
                    startQuiz.setEnabled(true);
                } else {
                    Toast.makeText(getContext(), department + " don't have exam ", Toast.LENGTH_SHORT).show();
                    startQuiz.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
