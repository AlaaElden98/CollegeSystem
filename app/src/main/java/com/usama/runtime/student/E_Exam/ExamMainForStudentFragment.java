package com.usama.runtime.student.E_Exam;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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


    private Exam examData;
    private MaterialSpinner spinner_subject;
    private ArrayList<String> arrayListOfSubjects;
    private String subjectChoose;

    public ExamMainForStudentFragment() {
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
        return inflater.inflate(R.layout.fragment_exam_main_for_student, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        arrayListOfSubjects = new ArrayList<>();
        spinner_subject = getView().findViewById(R.id.spinner_choose_subject_exam);
        ExamMainForStudentFragmentArgs args = ExamMainForStudentFragmentArgs.fromBundle(getArguments());
        final String department = args.getDepartment();


        getSubject(department);

        spinner_subject.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                subjectChoose = item.toString();
            }
        });

        // TODo  : delete this
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Exam").child("Level_One");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // TODO : replace it with department
                if (dataSnapshot.child("English language and Literature").exists()) {
                    Log.d("TAG_EXAM", dataSnapshot.child("English language and Literature").getChildren() + "");
                } else {
                    Toast.makeText(getContext(), department + " don't have exam ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Button startQuiz = getView().findViewById(R.id.start_quiz_btn);
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subjectChoose != null) {
                    Navigation.findNavController(getView()).navigate(ExamMainForStudentFragmentDirections.actionExamMainForStudentFragmentToQuestionsFragment(subjectChoose, department));
                } else {
                    Toast.makeText(getContext(), "Make sure you choose subject", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getSubject(final String department) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Subjects").child("Level_One").child(department);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", dataSnapshot.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String subject = snapshot.getKey();
                    arrayListOfSubjects.add(subject);
                }
                spinner_subject.setItems(arrayListOfSubjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
