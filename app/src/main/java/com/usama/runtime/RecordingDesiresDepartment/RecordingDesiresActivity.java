package com.usama.runtime.RecordingDesiresDepartment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usama.runtime.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RecordingDesiresActivity extends AppCompatActivity {
    MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    Button button;
    List<String> arrayList=new ArrayList<>();
    // variable to use in shared preference
    public static final String MY_NATIONAL_ID = "MyNationalId";


    MainActivityViewModel mainActivityViewModel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_desires);


        recyclerView = findViewById(R.id.desiresRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyRecyclerViewAdapter();


        recyclerView.setAdapter(adapter);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mainActivityViewModel.getArrayOfDepartmentName();


        mainActivityViewModel.departmentNameData.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                arrayList.addAll(list);
                adapter.setList(list);
            }
        });

//        Log.d("CHECK", arrayList.size() + "");



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        button = findViewById(R.id.confirmDesiresOfDepartment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                putDesiresInDatabase();

            }
        });

    }


    private void putDesiresInDatabase() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("student_desires");


        HashMap<String, Object> studentMap = new HashMap<>();
        Log.d("CHECKIN", arrayList.size() + "");
        studentMap.put("desires", arrayList);
        SharedPreferences prefs = getSharedPreferences(MY_NATIONAL_ID, MODE_PRIVATE);
        String nationalId = prefs.getString("nationalId", "1");//"No name defined" is the default value.
        System.out.println(nationalId);


        ref.child(nationalId).updateChildren(studentMap);

        Toast.makeText(RecordingDesiresActivity.this, "Your Desires update successfully.", Toast.LENGTH_SHORT).show();
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(arrayList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };


}
