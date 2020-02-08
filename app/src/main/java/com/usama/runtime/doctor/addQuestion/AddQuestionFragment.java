package com.usama.runtime.doctor.addQuestion;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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
import com.usama.runtime.R;
import com.usama.runtime.admin.subject.AddSubjectFragmentDirections;
import com.usama.runtime.admin.subject.ShowSubjectFragmentDirections;

import java.util.HashMap;
import java.util.Objects;

public class AddQuestionFragment extends Fragment {
    private EditText question, option1, option2, option3, option4;
    private ProgressDialog loadingBar;
    private String correct, level, subject, chapter, department, doctorName, nationalId;
    private TextView subject_name;
    private Button buttonQuestion;
    private MaterialSpinner spinner_choose_Correct_answer;
    private View view;
    private long chapter_number;

    public AddQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(AddQuestionFragmentDirections.actionAddQuestionFragmentToSpecificSubjectFragment(nationalId, doctorName));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        view = inflater.inflate(R.layout.fragment_add_question, container, false);

        subject_name = view.findViewById(R.id.subject_name);
        question = view.findViewById(R.id.Question);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        option4 = view.findViewById(R.id.option4);

        spinner_choose_Correct_answer = view.findViewById(R.id.spinner_choose_Correct_answer);
        buttonQuestion = view.findViewById(R.id.add_question_btn);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AddQuestionFragmentArgs args = AddQuestionFragmentArgs.fromBundle(getArguments());
        level = args.getLevel();
        subject = args.getSubject();
        chapter = args.getChapter();
        department = args.getDepartment();
        nationalId = args.getNationalId();
        doctorName = args.getRealName();

        subject_name.setText(level + " --> Department" + department + "--> Subject " + subject + " --> chapter " + chapter);
        spinner_choose_Correct_answer.setItems("Correct answer", "optionA", "optionB", "optionC", "optionD");

        loadingBar = new ProgressDialog(getContext());


        spinner_choose_Correct_answer.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                correct = item.toString();
            }
        });


        buttonQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion();
            }
        });
    }

    private void addQuestion() {
        String questionS = question.getText().toString().trim();
        String opt1 = option1.getText().toString().trim();
        String opt2 = option2.getText().toString().trim();
        String opt3 = option3.getText().toString().trim();
        String opt4 = option4.getText().toString().trim();

        if (questionS == null) {
            Toast.makeText(getContext(), "please write your question", Toast.LENGTH_SHORT).show();
        } else if (opt1 == null) {
            Toast.makeText(getContext(), "please write option 1", Toast.LENGTH_SHORT).show();
        } else if (opt2 == null) {
            Toast.makeText(getContext(), "please write option 2", Toast.LENGTH_SHORT).show();
        } else if (opt3 == null) {
            Toast.makeText(getContext(), "please write option 3", Toast.LENGTH_SHORT).show();
        } else if (opt4 == null) {
            Toast.makeText(getContext(), "please write option 4", Toast.LENGTH_SHORT).show();
        } else if (correct == null) {
            Toast.makeText(getContext(), "please choose correct answer ", Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.setTitle("Add Question");
            loadingBar.setMessage("please wait , while we are checking the credentials .");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidateDepartment(questionS, opt1, opt2, opt3, opt4, correct);
        }
    }

    private void ValidateDepartment(final String questionS, final String opt1, final String opt2, final String opt3, final String opt4, String correct) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        if (correct.equals("optionA")) {
            correct = opt1;
        } else if (correct.equals("optionB")) {
            correct = opt2;
        } else if (correct.equals("optionC")) {
            correct = opt3;
        } else if (correct.equals("optionD")) {
            correct = opt4;
        }

        if (chapter.equals("Chapter_one")){
            chapter_number = 1;
        }else if (chapter.equals("Chapter_two")){
            chapter_number = 2;
        }else if (chapter.equals("Chapter_three")){
            chapter_number = 3;
        }else if (chapter.equals("Chapter_four")){
            chapter_number = 4;
        }else if (chapter.equals("Chapter_five")){
            chapter_number = 5;
        }else if (chapter.equals("Chapter_sex")){
            chapter_number = 6;
        }

        final String finalCorrect = correct;
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> questionDataMap = new HashMap<>();
                questionDataMap.put(chapter , chapter_number);
                questionDataMap.put("question", questionS);
                questionDataMap.put("optionA", opt1);
                questionDataMap.put("optionB", opt2);
                questionDataMap.put("optionC", opt3);
                questionDataMap.put("optionD", opt4);
                questionDataMap.put("correctANS", finalCorrect);

                rootRef.child("Questions").child(level).child(department).child(subject).child(chapter).child(questionS).updateChildren(questionDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Add this Question is success", Toast.LENGTH_LONG).show();
                                    loadingBar.dismiss();
                                    Navigation.findNavController(Objects.requireNonNull(getView())).navigate(AddQuestionFragmentDirections.actionAddQuestionFragmentSelf(chapter, level, subject, department, doctorName, nationalId));
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