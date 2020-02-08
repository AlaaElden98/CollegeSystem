package com.usama.runtime.doctor.makeExam;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.usama.runtime.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MakeExamFragment extends Fragment {
    private ProgressDialog loadingBar;
    private String levelChoose, departmentChoose, subjectChoose;
    private ArrayList<String> arrayListOfSubjects;
    private ArrayList<String> arrayListOfDepartment;
    private ArrayList<String> arrayListOfChapterCheck;

    //    private ArrayList<String> arrayListOfChapter;
    private MaterialSpinner spinner_chooseLevel, spinner_chooseDepartment, spinner_chooseSubject;
    private String cha1, cha2, cha3, cha4, cha5, cha6;
    private CheckBox chapterOne, chapterTwo, chapterThree, chapterFour, chapterFive, chapterSex;
    private Button buttonAddQuestion;
    private String realName, nationalID;

    public MakeExamFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_make_exam, container, false);

        spinner_chooseLevel = view.findViewById(R.id.spinner_choose_Level_info_exam);
        spinner_chooseDepartment = view.findViewById(R.id.spinner_choose_department_info_exam);
        spinner_chooseSubject = view.findViewById(R.id.spinner_choose_subject_info_exam);
        chapterOne = view.findViewById(R.id.checkbox_chapter_one);
        chapterTwo = view.findViewById(R.id.checkbox_chapter_two);
        chapterThree = view.findViewById(R.id.checkbox_chapter_three);
        chapterFour = view.findViewById(R.id.checkbox_chapter_four);
        chapterFive = view.findViewById(R.id.checkbox_chapter_five);
        chapterSex = view.findViewById(R.id.checkbox_chapter_sex);
        buttonAddQuestion = view.findViewById(R.id.button_make_exam);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MakeExamFragmentArgs args = MakeExamFragmentArgs.fromBundle(getArguments());
        realName = args.getReakName();
        nationalID = args.getNationalId();


        arrayListOfDepartment = new ArrayList<>();
        arrayListOfChapterCheck = new ArrayList<>();

        loadingBar = new ProgressDialog(getActivity());
        arrayListOfSubjects = new ArrayList<>();

        departmentChoose = "Choose Department";

        spinner_chooseLevel.setItems("Choose Level", "Level_One", "Level_Two", "Level_Three", "Level_Four");
        spinner_chooseLevel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item == "Choose Level") {
                    Toast.makeText(getContext(), "Please choose Level", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Choose department available");
                    loadingBar.setMessage("please wait , while we are checking the credentials .");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    levelChoose = item.toString();
                    getDepartment();
                }
                Log.d("TAG", item.toString());
            }
        });
        spinner_chooseDepartment.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (departmentChoose != item) {
                    arrayListOfSubjects.clear();
                }
                loadingBar.setTitle("Choose Subject available");
                loadingBar.setMessage("please wait , while we are checking the credentials .");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                departmentChoose = item.toString();
                getSubject(levelChoose, departmentChoose);
            }
        });
        spinner_chooseSubject.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item == null || subjectChoose == null) {
                    Toast.makeText(getContext(), "Make sure this department have a subject ", Toast.LENGTH_LONG).show();
                }
                subjectChoose = item.toString();
            }
        });

        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levelChoose == "Choose Level" || levelChoose == null) {
                    Toast.makeText(getActivity(), "Please choose level ", Toast.LENGTH_SHORT).show();
                } else if (departmentChoose == null || departmentChoose == "Choose Department") {
                    Toast.makeText(getActivity(), "please choose department", Toast.LENGTH_SHORT).show();
                } else if (subjectChoose == null || subjectChoose == "Choose Subject") {
                    Toast.makeText(getActivity(), "please choose subject", Toast.LENGTH_SHORT).show();
                } else {
                    if (chapterOne.isChecked()) {
                        cha1 = "Chapter_one";
                        arrayListOfChapterCheck.add(cha1);
                    } else
                        cha1 = "";
                    if (chapterTwo.isChecked()) {
                        cha2 = "Chapter_two";
                        arrayListOfChapterCheck.add(cha2);
                    } else
                        cha2 = "";
                    if (chapterThree.isChecked()) {
                        cha3 = "Chapter_three";
                        arrayListOfChapterCheck.add(cha3);
                    } else
                        cha3 = "";
                    if (chapterFour.isChecked()) {
                        cha4 = "Chapter_four";
                        arrayListOfChapterCheck.add(cha4);
                    } else
                        cha4 = "";
                    if (chapterFive.isChecked()) {
                        cha5 = "Chapter_five";
                        arrayListOfChapterCheck.add(cha5);
                    } else
                        cha5 = "";
                    if (chapterSex.isChecked()) {
                        cha6 = "Chapter_sex";
                        arrayListOfChapterCheck.add(cha6);
                    } else
                        cha6 = "";

                    if (arrayListOfChapterCheck.size() != 0) {
                        loadingBar.setTitle("Choose Question available");
                        loadingBar.setMessage("please wait ");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        publishExam();

                    } else {
                        Toast.makeText(getContext(), "Please choose any chapter ", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
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
                examDataMap.put("level", levelChoose);
                examDataMap.put("department", departmentChoose);
                examDataMap.put("subject", subjectChoose);

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
                rootRef.child("Exam").child(levelChoose).child(departmentChoose).child(subjectChoose).updateChildren(examDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Add this Exam is success", Toast.LENGTH_LONG).show();
                                    loadingBar.dismiss();
                                    Navigation.findNavController(getView()).navigate(MakeExamFragmentDirections.actionInfoOfExamFragmentToDoctorHomeFragment(realName, nationalID));
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

    private void getDepartment() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("departments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", dataSnapshot.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String subject = snapshot.getKey();
                    arrayListOfDepartment.add(subject);
                }
                loadingBar.dismiss();
                spinner_chooseDepartment.setItems(arrayListOfDepartment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });

    }


    private void getSubject(final String levelChoose, final String department) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Subjects").child(levelChoose).child(department);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", dataSnapshot.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String subject = snapshot.getKey();
                    arrayListOfSubjects.add(subject);
                }
                loadingBar.dismiss();
                spinner_chooseSubject.setItems("Choose Subject");
                spinner_chooseSubject.setItems(arrayListOfSubjects);
//                chooseChapter.setItems("Choose chapter", "Chapter_one", "Chapter_two", "Chapter_three", "Chapter_four", "Chapter_five", "Chapter_sex", "Chapter_seven", "Chapter_eight");
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }
}
