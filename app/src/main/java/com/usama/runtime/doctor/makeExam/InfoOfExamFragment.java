package com.usama.runtime.doctor.makeExam;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;

public class InfoOfExamFragment extends Fragment {
    private ProgressDialog loadingBar;
    private String levelChoose, departmentChoose, subjectChoose;
    private ArrayList<String> arrayListOfSubjects;
    private ArrayList<String> arrayListOfDepartment;
    private ArrayList<String> arrayListOfChapterCheck;
    //    private ArrayList<String> arrayListOfChapter;
    private MaterialSpinner chooseLevel, chooseDepartment, chooseSubject;
    private String cha1, cha2, cha3, cha4, cha5, cha6;
    private CheckBox chapterOne, chapterTwo, chapterThree, chapterFour, chapterFive, chapterSex;
    private int numberOfQuestion;
    public InfoOfExamFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_of_exam, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        arrayListOfDepartment = new ArrayList<>();
//        arrayListOfChapter = new ArrayList<>();
        arrayListOfChapterCheck = new ArrayList<>();

        chooseLevel = getView().findViewById(R.id.spinner_choose_Level_info_exam);
        chooseDepartment = getView().findViewById(R.id.spinner_choose_department_info_exam);
        chooseSubject = getView().findViewById(R.id.spinner_choose_subject_info_exam);
        chapterOne = getView().findViewById(R.id.checkbox_chapter_one);
        chapterTwo = getView().findViewById(R.id.checkbox_chapter_two);
        chapterThree = getView().findViewById(R.id.checkbox_chapter_three);
        chapterFour = getView().findViewById(R.id.checkbox_chapter_four);
        chapterFive = getView().findViewById(R.id.checkbox_chapter_five);
        chapterSex = getView().findViewById(R.id.checkbox_chapter_sex);
        Button buttonAddQuestion = getView().findViewById(R.id.button_make_exam);

        loadingBar = new ProgressDialog(getActivity());
        arrayListOfSubjects = new ArrayList<>();


        chooseLevel.setItems("Choose Level", "Level_One", "Level_Two", "Level_Three", "Level_Four");
        chooseLevel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
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
        chooseDepartment.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
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
        chooseSubject.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item == null || subjectChoose == null) {
                    Toast.makeText(getContext(), "Make sure this department have a subject ", Toast.LENGTH_LONG).show();
                }
                subjectChoose = item.toString();
            }
        });

//        chooseChapter.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                chapterChoose = item.toString();
//            }
//        });

        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingBar.setTitle("Choose Question available");
                loadingBar.setMessage("please wait ");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                if (chooseLevel == null
                        || chooseSubject == null
                        || subjectChoose.equals("Choose Subject")) {
                    Toast.makeText(getContext(), "please make sure all field is valid", Toast.LENGTH_SHORT).show();
                } else {
                    if (chapterOne.isChecked()) {
                        cha1 = "Chapter_one";
                        arrayListOfChapterCheck.add(cha1);
                    }else
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

                    if (arrayListOfChapterCheck.size() != 0){
                        Log.d("TAG", arrayListOfChapterCheck + "");
                        getNumberOfQuestion(arrayListOfChapterCheck);
                    }else {
                        Toast.makeText(getContext(), "Please choose any chapter ", Toast.LENGTH_SHORT).show();
                    }
                }
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
                chooseDepartment.setItems(arrayListOfDepartment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });

    }

    private void getNumberOfQuestion(final ArrayList chapter) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Questions").child(levelChoose).child(departmentChoose).child(subjectChoose);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0 ; i < chapter.size();i++){
                    String s = (String) chapter.get(i);
                    numberOfQuestion += (int) dataSnapshot.child(s).getChildrenCount();
                    Log.d("heloooo" , dataSnapshot.child(s).getValue() + "");
                }
                Log.d("TAGGGG",numberOfQuestion + "");
                Navigation.findNavController(getView()).navigate(InfoOfExamFragmentDirections.actionInfoOfExamFragmentToMakeExamFragment(levelChoose, departmentChoose, subjectChoose, cha1, cha2, cha3, cha4, cha5, cha6,numberOfQuestion));
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                chooseSubject.setItems("Choose Subject");
                chooseSubject.setItems(arrayListOfSubjects);
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
