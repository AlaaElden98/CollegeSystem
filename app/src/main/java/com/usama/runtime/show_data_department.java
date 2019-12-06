package com.usama.runtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

public class show_data_department extends AppCompatActivity {

    TextView departmentNameItem112,departmentMinSpecialItem112,departmentMinCapacityItem112,DepartmentMinValueItem112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data_department);
        departmentNameItem112=(TextView)findViewById(R.id.departmentNameItem11);
        departmentMinSpecialItem112=(TextView)findViewById(R.id.departmentMinSpecialItem11);
        departmentMinCapacityItem112=(TextView)findViewById(R.id.departmentMinCapacityItem11);
        DepartmentMinValueItem112=(TextView)findViewById(R.id.DepartmentMinValueItem11);
        departmentNameItem112.setText("Department Name:" +getIntent().getStringExtra("Departmentname"));
        departmentMinSpecialItem112.setText("Department Min Special:" +getIntent().getStringExtra("Departmentminspecial"));
        departmentMinCapacityItem112.setText("Department Capacity:" +getIntent().getStringExtra("DepartmentCapacity"));
        DepartmentMinValueItem112.setText("Department Min Value:" +getIntent().getStringExtra("Departmentminvalue"));
    }
}
