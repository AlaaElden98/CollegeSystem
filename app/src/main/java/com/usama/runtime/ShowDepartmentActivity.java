package com.usama.runtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
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

public class ShowDepartmentActivity extends AppCompatActivity {
    private RecyclerView departmentList;
    private DatabaseReference departmentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_department);

        departmentRef = FirebaseDatabase.getInstance().getReference().child("Department");

        departmentList = findViewById(R.id.department_list);
        departmentList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Department> options =
                new FirebaseRecyclerOptions.Builder<Department>()
                        .setQuery(departmentRef, Department.class)
                        .build();

        FirebaseRecyclerAdapter<Department, DepartmentViewHolder> adapter
                = new FirebaseRecyclerAdapter<Department, DepartmentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DepartmentViewHolder holder, final int position, @NonNull final Department model) {
                holder.departmentNameItem.setText("Name: " + model.getDepartmentName());
                holder.departmentMinSpecialItem.setText("Min Special: " + model.getDepartmentMinSpecial());
                holder.departmentMinCapacityItem.setText("Min Capacity: : " + model.getDepartmentCapacity());
                holder.DepartmentMinValueItem.setText("Min Value: " + model.getDepartmentMinValue());
                holder.show_all_department_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID = getRef(position).getKey();
                        Intent intent = new Intent(ShowDepartmentActivity.this, EditDepartmentData.class);
                        intent.putExtra("DepartmentName", model.getDepartmentName());

                        // here you send the user id to the ShowAllStudentActivity
                        intent.putExtra("uid", uID);
                        Log.d("TAG", uID + " ");
                        startActivity(intent);
                    }
                });
                holder.Delete_department_btn.setOnClickListener(new View.OnClickListener() {
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDepartmentActivity.this);
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
            public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_one_item_layout, parent, false);

                return new DepartmentViewHolder(view);
            }
        };
        departmentList.setAdapter(adapter);
        adapter.startListening();

    }

    static class DepartmentViewHolder extends RecyclerView.ViewHolder {
        public TextView departmentNameItem, departmentMinSpecialItem, departmentMinCapacityItem, DepartmentMinValueItem;
        public Button show_all_department_btn;
        public Button Delete_department_btn;

        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            departmentNameItem = itemView.findViewById(R.id.departmentNameItem);
            departmentMinSpecialItem = itemView.findViewById(R.id.departmentMinSpecialItem);
            departmentMinCapacityItem = itemView.findViewById(R.id.departmentMinCapacityItem);
            DepartmentMinValueItem = itemView.findViewById(R.id.DepartmentMinValueItem);
            show_all_department_btn = itemView.findViewById(R.id.show_all_department_btn);
            Delete_department_btn = itemView.findViewById(R.id.Delete_department_btn);

        }
    }

    private void RemoverDepartment(String uID) {
        // first connect with database
        // orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        departmentRef.child(uID).removeValue();
    }

}
