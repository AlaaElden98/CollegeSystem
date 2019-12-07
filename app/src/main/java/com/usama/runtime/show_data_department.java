package com.usama.runtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class show_data_department extends AppCompatActivity {

    TextView departmentNameItem112, departmentMinSpecialItem112, departmentMinCapacityItem112, DepartmentMinValueItem112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data_department);
        departmentNameItem112 = findViewById(R.id.departmentNameItem11);
        departmentMinSpecialItem112 = findViewById(R.id.departmentMinSpecialItem11);
        departmentMinCapacityItem112 = findViewById(R.id.departmentMinCapacityItem11);
        DepartmentMinValueItem112 = findViewById(R.id.DepartmentMinValueItem11);
        departmentNameItem112.setText("Department Name:" + getIntent().getStringExtra("Departmentname"));
        departmentMinSpecialItem112.setText("Department Min Special:" + getIntent().getStringExtra("Departmentminspecial"));
        departmentMinCapacityItem112.setText("Department Capacity:" + getIntent().getStringExtra("DepartmentCapacity"));
        DepartmentMinValueItem112.setText("Department Min Value:" + getIntent().getStringExtra("Departmentminvalue"));
    }
}
