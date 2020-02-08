package com.usama.runtime.admin.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.usama.runtime.R;
import com.usama.runtime.model.Student;

public class ShowAllStudentFragment extends Fragment {
    private RecyclerView studentList;
    private DatabaseReference studentRef;
    private Button searchBtn;
    private EditText inputText;

    private String searchInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_show_all_student, container, false);

        studentList = view.findViewById(R.id.Student_list);
        searchBtn = view.findViewById(R.id.search_btn);
        inputText = view.findViewById(R.id.search_student_name);


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        studentRef = FirebaseDatabase.getInstance().getReference().child("students");


        studentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = inputText.getText().toString();
                onStart();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Query query = studentRef.orderByChild("name");
        FirebaseRecyclerOptions<Student> options =
                new FirebaseRecyclerOptions.Builder<Student>()
                        .setQuery(query.startAt(searchInput), Student.class)
                        .build();

        FirebaseRecyclerAdapter<Student, ShowAllStudentFragment.StudentViewHolder> adapter
                = new FirebaseRecyclerAdapter<Student, ShowAllStudentFragment.StudentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final StudentViewHolder holder, final int position, @NonNull final Student model) {
                holder.studentId.setText("ID" + model.getId());
                holder.studentNameItem.setText("Name: " + model.getName());
                holder.studentNationalId.setText("NationalId: " + model.getNational_id());
                holder.studentSeatNumber.setText("Student SeatNumber : " + model.getNt_ID());
                holder.studentTotal.setText("Student Total degree  : " + model.getTotal());


                holder.Delete_student_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // display a message
                        // dialog
                        CharSequence option[] = new CharSequence[]{
                                // here you put the option you need to show
                                "Yes",
                                "No"

                        };
                        // alert dialog take a context
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Are you sure !! you will delete this department ");

                        // here you show the option in dialog yes or no with on click listener
                        // here you have a int as a position
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                // if i == 0 --> the user chick in yes button
                                if (i == 0) {
                                    String uID = getRef(position).getKey();

                                    // this method to remove have one parameter uID --> id of user order (phone)
                                    RemoverDepartment(uID);
                                    // else if the user chick in the no button
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public ShowAllStudentFragment.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_one_item_layout, parent, false);

                return new ShowAllStudentFragment.StudentViewHolder(view);
            }
        };
        studentList.setAdapter(adapter);
        adapter.startListening();

    }

    private static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView studentId, studentNameItem, studentNationalId, studentSeatNumber, studentTotal;

        Button Delete_student_btn;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            studentId = itemView.findViewById(R.id.studentId);
            studentNameItem = itemView.findViewById(R.id.studentName);
            studentNationalId = itemView.findViewById(R.id.studentNationalId);
            studentSeatNumber = itemView.findViewById(R.id.studentSeatNumber);
            studentTotal = itemView.findViewById(R.id.studentTotal);
            Delete_student_btn = itemView.findViewById(R.id.Delete_student_btn);

        }
    }

    private void RemoverDepartment(String uID) {
        studentRef.child(uID).removeValue();
    }

}
