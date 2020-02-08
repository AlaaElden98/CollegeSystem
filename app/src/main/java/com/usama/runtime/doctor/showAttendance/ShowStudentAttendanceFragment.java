package com.usama.runtime.doctor.showAttendance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usama.runtime.R;
import com.usama.runtime.doctor.subject.ShowSubjectToDoctorFragment;
import com.usama.runtime.model.StudentsAttendance;

public class ShowStudentAttendanceFragment extends Fragment {
    private String uniqueNumber;
    private DatabaseReference studentAttendanceRef;
    private RecyclerView studentList;

    public ShowStudentAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_student_attendance, container, false);
        studentList = view.findViewById(R.id.student_attendance_list);
        studentList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ShowStudentAttendanceFragmentArgs args = ShowStudentAttendanceFragmentArgs.fromBundle(getArguments());
        uniqueNumber = args.getUniqueNumber();

        studentAttendanceRef = FirebaseDatabase.getInstance().getReference().child("students_attendance").child(uniqueNumber);

        FirebaseRecyclerOptions<StudentsAttendance> options
                = new FirebaseRecyclerOptions.Builder<StudentsAttendance>()
                .setQuery(studentAttendanceRef, StudentsAttendance.class)
                .build();

        FirebaseRecyclerAdapter<StudentsAttendance, ShowStudentAttendanceFragment.StudentViewHolder> adapter
                = new FirebaseRecyclerAdapter<StudentsAttendance, StudentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StudentViewHolder holder, final int position, @NonNull StudentsAttendance model) {
                holder.studentName.setText(model.getStudent_name());

            }

            @NonNull
            @Override
            public ShowStudentAttendanceFragment.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_to_doctor_one_item_layout, parent, false);
                return new StudentViewHolder(view);
            }
        };
        studentList.setAdapter(adapter);
        adapter.startListening();

    }


    private static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView studentName;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.student_name_to_doctor);
        }
    }


}