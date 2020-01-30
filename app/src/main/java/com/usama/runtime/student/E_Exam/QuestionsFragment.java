package com.usama.runtime.student.E_Exam;

import android.animation.Animator;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.usama.runtime.R;
import com.usama.runtime.model.Exam;
import com.usama.runtime.model.QuestionModel;

import java.util.ArrayList;
import java.util.List;


public class QuestionsFragment extends Fragment {

    private String Chapter_one, Chapter_two, Chapter_three, Chapter_four, Chapter_five, Chapter_sex, degree_of_exam, department, level, num_of_question, subject;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    private Exam examData;
    private ArrayList<String> arrayOfNumberOfChapter;

    private List<QuestionModel> list;
    private TextView question, noIndecate;
    private LinearLayout optionsContain;
    private Button nextBtn;
    private int count = 0;

    private int position = 0;
    private int score = 0;

    private String category;
    private int setNo;

    private Dialog loadingdialog;


    public QuestionsFragment() {
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
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        QuestionsFragmentArgs args = QuestionsFragmentArgs.fromBundle(getArguments());
        subject = args.getSubject();
        department = args.getDepartment();

        question = getView().findViewById(R.id.question);
        noIndecate = getView().findViewById(R.id.no_indicator);
        optionsContain = getView().findViewById(R.id.option_container);
        nextBtn = getView().findViewById(R.id.next_btn);

        arrayOfNumberOfChapter = new ArrayList<>();


        loadingdialog = new Dialog(getContext());
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);

        // TODO : first we fetch data from Exam node
        // TODO : don't mess this line ... replace it by real data
        myRef.child("Exam").child("Level_One").child("English language and Literature").child("English").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                examData = dataSnapshot.getValue(Exam.class);
                Chapter_one = examData.getChapter_one();
                if (Chapter_one != null) arrayOfNumberOfChapter.add(Chapter_one);
                Chapter_two = examData.getChapter_two();
                if (Chapter_two != null) arrayOfNumberOfChapter.add(Chapter_two);
                Chapter_three = examData.getChapter_three();
                if (Chapter_three != null) arrayOfNumberOfChapter.add(Chapter_three);
                Chapter_four = examData.getChapter_four();
                if (Chapter_four != null) arrayOfNumberOfChapter.add(Chapter_four);
                Chapter_five = examData.getChapter_five();
                if (Chapter_five != null) arrayOfNumberOfChapter.add(Chapter_five);
                Chapter_sex = examData.getChapter_sex();
                if (Chapter_sex != null) arrayOfNumberOfChapter.add(Chapter_sex);
                degree_of_exam = examData.getDegree_of_exam();
                department = examData.getDepartment();
                level = examData.getLevel();
                num_of_question = examData.getNum_of_question();
                subject = examData.getSubject();
                Log.d("EXAMDATA", examData + Chapter_one + Chapter_two + Chapter_three + Chapter_four + Chapter_five + Chapter_sex);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        list = new ArrayList<>();
        loadingdialog.show();
        // TODO : make level is public
        myRef.child("Questions").child("Level_One").child("English language and Literature").child("English").child("Chapter_one").orderByChild("Chapter_one").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue(QuestionModel.class));
                    Log.d("TAGCORRECT", snapshot.getValue(QuestionModel.class).getCorrectANS());
                }

                if (list.size() > 0) {
                    for (int i = 0; i < 4; i++) {
                        optionsContain.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkAnswer((Button) v);
                            }
                        });
                    }
                    playAnim(question, 0, list.get(position).getQuestion());
                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextBtn.setEnabled(false);
                            nextBtn.setAlpha(0.7f);
                            enableOption(true);
                            position++;
                            if (position == list.size()) {
                                Navigation.findNavController(getView()).navigate(QuestionsFragmentDirections.actionQuestionsFragmentToShowScoreFragment(score, list.size()));
                                return;
                            }
                            count = 0;
                            playAnim(question, 0, list.get(position).getQuestion());
                        }
                    });
                } else {
                    getActivity().finish();
                    Toast.makeText(getContext(), "no questions ", Toast.LENGTH_SHORT).show();
                }
                loadingdialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingdialog.dismiss();
                getActivity().finish();
            }
        });

    }

    private void playAnim(final View view, final int value, final String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4) {
                    // TODO : VIDEO NUM 5
                    String option = "";
                    if (count == 0) {
                        option = list.get(position).getOptionA();
                    } else if (count == 1) {
                        option = list.get(position).getOptionB();
                    } else if (count == 2) {
                        option = list.get(position).getOptionC();
                    } else if (count == 3) {
                        option = list.get(position).getOptionD();
                    }
                    playAnim(optionsContain.getChildAt(count), 0, option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (value == 0) {
                    try {
                        ((TextView) view).setText(data);
                        noIndecate.setText(position + 1 + "/" + list.size());
                    } catch (ClassCastException ex) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view, 1, data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button selectedOption) {
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        Log.d("correctAns", list.get(position).getCorrectANS());
        if (selectedOption.getText().toString().equals(list.get(position).getCorrectANS())) {
            // correct
            score++;
            Log.d("TAG", score + "");
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));

        } else {
            // incorrect
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctOption = optionsContain.findViewWithTag(list.get(position).getCorrectANS());
            Log.d("Tagf", optionsContain.findViewWithTag(list.get(position).getCorrectANS()) + " ");
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));

        }
    }

    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            optionsContain.getChildAt(i).setEnabled(enable);
            if (enable) {
                optionsContain.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }
}
