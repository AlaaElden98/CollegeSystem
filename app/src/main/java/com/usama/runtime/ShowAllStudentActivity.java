package com.usama.runtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usama.runtime.model.Department;
import com.usama.runtime.model.Student;

public class ShowAllStudentActivity extends AppCompatActivity {

    // TODO :  MAKE EDIT IN ALL INFO

    private RecyclerView studentList;
    private DatabaseReference studentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_student);
        studentRef = FirebaseDatabase.getInstance().getReference().child("Student");

        studentList = findViewById(R.id.Student_list);
        studentList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Student> options =
                new FirebaseRecyclerOptions.Builder<Student>()
                        .setQuery(studentRef, Student.class)
                        .build();

        FirebaseRecyclerAdapter<Student, ShowAllStudentActivity.StudentViewHolder> adapter
                = new FirebaseRecyclerAdapter<Student, ShowAllStudentActivity.StudentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final StudentViewHolder holder, final int position, @NonNull final Student model) {
                holder.studentNameItem.setText("Name: " + model.getName());
                holder.studentNationalId.setText("NationalId:" + model.getNational_id());

//                holder.show_all_Student_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // holder.studentNameItem.setTextColor(Color.RED);
//                        String uID = getRef(position).getKey();
//
//                        Intent intent = new Intent(ShowAllStudentActivity.this, show_data_student.class);
//                        intent.putExtra("getUserName", model.getUserName());
//                        intent.putExtra("getNationalId", model.getNationalId());
//                        intent.putExtra("getPassword", model.getPassword());
//                        intent.putExtra("getSeatNumber", model.getSeatNumber());
//                        intent.putExtra("getSpecialize", model.getSpecialize());
//                        intent.putExtra("getTotal", model.getTotal());
//
//                        // here you send the user id to the ShowAllStudentActivity
//                        intent.putExtra("uid", uID);
//                        Log.d("TAG", uID + " ");
//                        startActivity(intent);
//                    }
//                });
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowAllStudentActivity.this);
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
                                } else {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public ShowAllStudentActivity.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_one_item_layout, parent, false);

                return new ShowAllStudentActivity.StudentViewHolder(view);
            }
        };
        studentList.setAdapter(adapter);
        adapter.startListening();

    }

    private static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView studentNameItem, studentNationalId,studentpassword, studentseatnumber,studentspecialize, studenttotal;
        public Button show_all_Student_btn;
        public Button Delete_student_btn;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameItem = itemView.findViewById(R.id.studentName);
            studentNationalId = itemView.findViewById(R.id.studentNationalId);
            show_all_Student_btn = itemView.findViewById(R.id.show_all_Student_btn);
            Delete_student_btn = itemView.findViewById(R.id.Delete_student_btn);

        }
    }

    private void RemoverDepartment(String uID) {
        // first connect with database
        // orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        studentRef.child(uID).removeValue();
    }

}
