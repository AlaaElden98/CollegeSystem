package com.usama.runtime.doctor.addQuestion;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.usama.runtime.R;

import java.util.ArrayList;

public class SpecificSubjectFragment extends Fragment {
    private ProgressDialog loadingBar;
    private String levelChoose, departmentChoose, subjectChoose, chapterChoose;
    private ArrayList<String> arrayListOfSubjects;
    private MaterialSpinner spinner_chooseLevel, spinner_chooseDepartment, spinner_chooseSubject, spinner_chooseChapter;
    private ArrayList<String> arrayListOfDepartment;

    public SpecificSubjectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_specific_subject, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        arrayListOfDepartment = new ArrayList<>();

        spinner_chooseLevel = getView().findViewById(R.id.spinner_choose_Level_specific_subject);
        spinner_chooseDepartment = getView().findViewById(R.id.spinner_choose_department_specific_subject);
        spinner_chooseSubject = getView().findViewById(R.id.spinner_choose_subject_specific_subject);
        spinner_chooseChapter = getView().findViewById(R.id.spinner_choose_chapter_specific_subject);
        Button buttonAddQuestion = getView().findViewById(R.id.button_add_question);

        loadingBar = new ProgressDialog(getActivity());
        arrayListOfSubjects = new ArrayList<>();

        chapterChoose = "Choose chapter";
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

        spinner_chooseChapter.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                chapterChoose = item.toString();
            }
        });

        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levelChoose == null) {
                    Toast.makeText(getActivity(), "Please choose level", Toast.LENGTH_SHORT).show();
                } else if (departmentChoose == null) {
                    Toast.makeText(getActivity(), "Please choose department", Toast.LENGTH_SHORT).show();
                } else if (subjectChoose == null || subjectChoose.equals("Choose Subject")) {
                    Toast.makeText(getActivity(), "Please choose subject", Toast.LENGTH_SHORT).show();
                } else if (chapterChoose == null || chapterChoose.equals("Choose chapter")) {
                    Toast.makeText(getActivity(), "Please choose chapter ", Toast.LENGTH_SHORT).show();
                } else
                    Navigation.findNavController(getView()).navigate(SpecificSubjectFragmentDirections.actionSpecificSubjectFragmentToAddQuestionFragment(chapterChoose, levelChoose, subjectChoose, departmentChoose));
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
                spinner_chooseChapter.setItems("Choose chapter", "Chapter_one", "Chapter_two", "Chapter_three", "Chapter_four", "Chapter_five", "Chapter_sex");
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
