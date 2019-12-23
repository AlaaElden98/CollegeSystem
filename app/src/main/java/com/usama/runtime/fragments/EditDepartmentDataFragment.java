package com.usama.runtime.fragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.usama.runtime.R;

public class EditDepartmentDataFragment extends Fragment {
    // TODO: MAKE EDIT IN ALL INFO << Done<< Alaa
    Button buttonEditDepartment;
    EditText getDepartmentName, getDepartmentCapacity;

    public EditDepartmentDataFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_department_data, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDepartmentName = getView().findViewById(R.id.getDepartmentName);
        getDepartmentCapacity = getView().findViewById(R.id.getDepartmentCapacity);
        buttonEditDepartment = getView().findViewById(R.id.buttonEditDepartment);
        buttonEditDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDepartment();
                Navigation.findNavController(getView()).navigate(EditDepartmentDataFragmentDirections.actionEditDepartmentDataFragmentToShowDepartmentFragment());
            }
        });

    }


    // TODO : ALAA LOOK HERE AND FIX
    private void editDepartment() {


//        String departmentName = getDepartmentName.getText().toString().trim();
//        String departmentCapacity = getDepartmentCapacity.getText().toString().trim();
//        String departmentOldName = getIntent().getStringExtra("DepartmentName");
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference mDatabaseRef = database.getReference();
//
//        mDatabaseRef.child("Department").child(departmentOldName).child("departmentName").setValue(departmentName);
//        mDatabaseRef.child("Department").child(departmentOldName).child("departmentCapacity").setValue(departmentCapacity);
//
//
//    }
//
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
    }
}