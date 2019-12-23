package com.usama.runtime.fragments;


import android.content.SharedPreferences;
import android.net.Uri;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usama.runtime.R;
import com.usama.runtime.viewModel.MainActivityViewModel;
import com.usama.runtime.RecordingDesiresDepartment.MyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecordingDesiresFragment extends Fragment {


    MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    Button button;
    List<String> arrayList=new ArrayList<>();
    // variable to use in shared preference
    public static final String MY_NATIONAL_ID = "MyNationalId";
    MainActivityViewModel mainActivityViewModel ;


    public RecordingDesiresFragment() {
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
        return inflater.inflate(R.layout.fragment_recording_desires, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.desiresRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

        button = getView().findViewById(R.id.confirmDesiresOfDepartment);
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
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_NATIONAL_ID, MODE_PRIVATE);
        String nationalId = prefs.getString("nationalId", "1");//"No name defined" is the default value.
        System.out.println(nationalId);


        ref.child(nationalId).updateChildren(studentMap);

        Toast.makeText(getActivity(), "Your Desires update successfully.", Toast.LENGTH_SHORT).show();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
