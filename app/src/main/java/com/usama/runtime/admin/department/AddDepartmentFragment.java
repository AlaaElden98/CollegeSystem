package com.usama.runtime.admin.department;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.usama.runtime.R;

import java.util.HashMap;


public class AddDepartmentFragment extends Fragment {
    private ProgressDialog loadingBar;
    private EditText getDepartmentCapacity, getDepartmentName, getDepartmentMinSpecial, getDepartmentMinTotal , getDepartmentDescription;
    private String selectedSubject;
    private String realName;
    public AddDepartmentFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_department, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AddDepartmentFragmentArgs args = AddDepartmentFragmentArgs.fromBundle(getArguments());
        realName = args.getRealname();

        getDepartmentName = getView().findViewById(R.id.getDepartmentName);
        getDepartmentCapacity = getView().findViewById(R.id.getDepartmentCapacity);
        Button buttonAddDepartment = getView().findViewById(R.id.buttonAddDepartment);
        MaterialSpinner subjectSpinner = getView().findViewById(R.id.spinner_choose_subject);
        getDepartmentMinSpecial = getView().findViewById(R.id.getDepartmentMinSpecial);
        getDepartmentMinTotal = getView().findViewById(R.id.getDepartmentMinTotal);
        getDepartmentDescription = getView().findViewById(R.id.getDepartmentDescription);


        subjectSpinner.setItems("Without", "arabic", "english", "french", "geography", "history", "philosophy", "psychology");
        subjectSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedSubject = item.toString();
                Log.d("SpinnerTAG", selectedSubject);
            }
        });


        loadingBar = new ProgressDialog(getActivity());

        buttonAddDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDepartment();
            }
        });

    }


    private void addDepartment() {
        String departmentName = getDepartmentName.getText().toString().trim();
        String departmentCapacity = getDepartmentCapacity.getText().toString().trim();
        String departmentMinTotal = getDepartmentMinTotal.getText().toString().trim();
        String departmentMinSpecial = getDepartmentMinSpecial.getText().toString().trim();
        String departmentDescription = getDepartmentDescription.getText().toString().trim();


        if (selectedSubject == null)
            selectedSubject = "without";

        // wait to check is phone number is available in database
        loadingBar.setTitle("Add Department");
        loadingBar.setMessage("please wait , while we are checking the credentials .");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        ValidateDepartment(departmentName, departmentCapacity, departmentMinTotal, departmentMinSpecial,departmentDescription, selectedSubject);


    }

    private void ValidateDepartment(final String departmentName, final String departmentCapacity, final String departmentMinTotal, final String departmentMinSpecial, final String departmentDescription, final String selectedSubject) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // if this national id  valid create a new Department
                // i make a national id a unique field in database

                if (!(dataSnapshot.child("departments").child(departmentName).exists())) {
                    // use hashMap to store data into the database (firebase)
                    HashMap<String, Object> departmentDataMap = new HashMap<>();
                    departmentDataMap.put("departmentName", departmentName);
                    departmentDataMap.put("departmentCapacity", departmentCapacity);
                    departmentDataMap.put("departmentMinTotal", departmentMinTotal);
                    departmentDataMap.put("departmentSpecialSubject", selectedSubject);
                    departmentDataMap.put("departmentMinSpecial", departmentMinSpecial);
                    departmentDataMap.put("departmentDescription", departmentDescription);


                    RootRef.child("departments").child(departmentName).updateChildren(departmentDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Add this department is success", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                        Navigation.findNavController(getView()).navigate(AddDepartmentFragmentDirections.actionAddDepartmentFragmentToAdminHomeFragment(realName));
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(getActivity(), "Network Error: please try again after some time..", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(getActivity(), "This " + departmentName + " already exist ", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Navigation.findNavController(getView()).navigate(AddDepartmentFragmentDirections.actionAddDepartmentFragmentToAdminHomeFragment(realName));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
