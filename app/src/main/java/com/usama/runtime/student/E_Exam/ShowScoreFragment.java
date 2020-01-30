package com.usama.runtime.student.E_Exam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.usama.runtime.R;


// TODO : save the result of the exam in the firebase
public class ShowScoreFragment extends Fragment {

    private TextView scored, total;
    private Button doneBtn;

    public ShowScoreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_score, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ShowScoreFragmentArgs args = ShowScoreFragmentArgs.fromBundle(getArguments());

        int score = args.getScore();
        int totalInt = args.getTotal();
        scored = getView().findViewById(R.id.scored);
        total = getView().findViewById(R.id.total);
        doneBtn = getView().findViewById(R.id.done_btn);

        scored.setText(String.valueOf(score));
        total.setText("Out of " + totalInt);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(ShowScoreFragmentDirections.actionShowScoreFragmentToHomeFragment());
            }
        });

    }
}
