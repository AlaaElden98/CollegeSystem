package com.usama.runtime.doctor.makeExam;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.usama.runtime.MainFragmentDirections;
import com.usama.runtime.R;
import com.usama.runtime.doctor.addQuestion.AddQuestionFragmentArgs;
import com.usama.runtime.doctor.addQuestion.AddQuestionFragmentDirections;

import java.util.ArrayList;
import java.util.HashMap;

public class MakeExamFragment extends Fragment {
    private String level, department, subject, cha1, cha2, cha3, cha4, cha5, cha6, numberOfQuestionString;
    private DatabaseReference databaseReference;
    private int numberOfQuestion, numOfQuestion;
    private ProgressDialog loadingBar;
    private MaterialSpinner spinnerNumberOfQuestion, spinnerDegreeOfExam;
    private ArrayList<Integer> arrayOfNumQuestion;
    private String degreeOfExam;
    private Button publish_exam_btn;

    public MakeExamFragment() {
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
        return inflater.inflate(R.layout.fragment_make_exam, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadingBar = new ProgressDialog(getContext());
        publish_exam_btn = getView().findViewById(R.id.publish_exam_btn);
        arrayOfNumQuestion = new ArrayList<>();
        MakeExamFragmentArgs args = MakeExamFragmentArgs.fromBundle(getArguments());
        level = args.getLevel();
        department = args.getDepartment();
        subject = args.getSubject();
        cha1 = args.getCha1();
        cha2 = args.getCha2();
        cha3 = args.getCha3();
        cha4 = args.getCha4();
        cha5 = args.getCha5();
        cha6 = args.getCha6();
        numberOfQuestion = args.getNumberOfQuestion();

        spinnerNumberOfQuestion = getView().findViewById(R.id.spinner_choose_number_of_question);
        spinnerDegreeOfExam = getView().findViewById(R.id.spinner_choose_degree_of_the_exam);
        for (int i = 1; i <= numberOfQuestion; i++) {
            arrayOfNumQuestion.add(i);
        }
        spinnerNumberOfQuestion.setItems(arrayOfNumQuestion);
        spinnerNumberOfQuestion.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                numOfQuestion = (int) item;
            }
        });
        spinnerDegreeOfExam.setItems("5", "10", "15", "20", "25", "30");
        spinnerDegreeOfExam.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                degreeOfExam = item.toString();
            }
        });

        publish_exam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishExam();
            }
        });
    }

    private void publishExam() {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();


        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> examDataMap = new HashMap<>();
                examDataMap.put("level", level);
                examDataMap.put("department", department);
                examDataMap.put("subject", subject);
                examDataMap.put("degree_of_exam", degreeOfExam);
                // TODO : MAKE SURE THAT IS TRUE
                numberOfQuestionString = numOfQuestion + "";
                examDataMap.put("num_of_question", numberOfQuestionString);

                if (cha1.equals("Chapter_one")) {
                    examDataMap.put("Chapter_one", cha1);
                }
                if (cha2.equals("Chapter_two")) {
                    examDataMap.put("Chapter_two", cha2);
                }
                if (cha3.equals("Chapter_three")) {
                    examDataMap.put("Chapter_three", cha3);
                }
                if (cha4.equals("Chapter_four")) {
                    examDataMap.put("Chapter_four", cha4);
                }
                if (cha5.equals("Chapter_five")) {
                    examDataMap.put("Chapter_five", cha5);
                }
                if (cha6.equals("Chapter_sex")) {
                    examDataMap.put("Chapter_sex", cha6);
                }
                rootRef.child("Exam").child(level).child(department).child(subject).updateChildren(examDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Add this Exam is success", Toast.LENGTH_LONG).show();
                                    loadingBar.dismiss();
                                    Navigation.findNavController(getView()).navigate(MakeExamFragmentDirections.actionMakeExamFragmentToTestExamFragment2());
                                } else {
                                    loadingBar.dismiss();
                                    Toast.makeText(getActivity(), "Network Error: please try again after some time..", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
