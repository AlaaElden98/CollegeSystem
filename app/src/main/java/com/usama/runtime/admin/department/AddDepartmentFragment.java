package com.usama.runtime.admin.department;

import androidx.activity.OnBackPressedCallback;
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
import com.usama.runtime.doctor.AddNewPostFragmentDirections;

import java.util.HashMap;
import java.util.Objects;


public class AddDepartmentFragment extends Fragment {
    // init progress dialog
    private ProgressDialog loadingBar;

    // init editText and Button
    private EditText getDepartmentCapacity, getDepartmentName, getDepartmentMinSpecial, getDepartmentMinTotal, getDepartmentDescription;
    private Button buttonAddDepartment;

    // init String used
    private String selectedSubject, realName;

    // inti spinner show subject available
    private MaterialSpinner subjectSpinner;


    public AddDepartmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // handel back press .. when admin click back he will go to home fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(AddDepartmentFragmentDirections.actionAddDepartmentFragmentToAdminHomeFragment(realName));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_department, container, false);

        AddDepartmentFragmentArgs args = AddDepartmentFragmentArgs.fromBundle(getArguments());
        realName = args.getRealname();

        getDepartmentName = view.findViewById(R.id.getDepartmentName);
        getDepartmentCapacity = view.findViewById(R.id.getDepartmentCapacity);
        buttonAddDepartment = view.findViewById(R.id.buttonAddDepartment);
        subjectSpinner = view.findViewById(R.id.spinner_choose_subject);
        getDepartmentMinSpecial = view.findViewById(R.id.getDepartmentMinSpecial);
        getDepartmentMinTotal = view.findViewById(R.id.getDepartmentMinTotal);
        getDepartmentDescription = view.findViewById(R.id.getDepartmentDescription);


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadingBar = new ProgressDialog(getActivity());


        subjectSpinner.setItems("Without", "arabic", "english", "french", "geography", "history", "philosophy", "psychology");
        subjectSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedSubject = item.toString();
                Log.d("SpinnerTAG", selectedSubject);
            }
        });

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


        if (departmentName.equals("")) {
            Toast.makeText(getContext(), "please add department name ", Toast.LENGTH_SHORT).show();
        } else if (departmentCapacity.equals("")) {
            Toast.makeText(getContext(), "please add department capacity", Toast.LENGTH_SHORT).show();
        } else if (departmentMinTotal.equals("")) {
            Toast.makeText(getContext(), "please add department Min Total", Toast.LENGTH_SHORT).show();
        } else if (departmentMinSpecial.equals("")) {
            Toast.makeText(getContext(), "please add department Min Special", Toast.LENGTH_SHORT).show();
        } else if (departmentDescription.equals("")) {
            Toast.makeText(getContext(), "please add department description", Toast.LENGTH_SHORT).show();
        } else {
            // wait to check is phone number is available in database
            loadingBar.setTitle("Add Department");
            loadingBar.setMessage("please wait , while we are checking the credentials .");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateDepartment(departmentName, departmentCapacity, departmentMinTotal, departmentMinSpecial, departmentDescription, selectedSubject);
        }
    }

    private void ValidateDepartment(final String departmentName, final String departmentCapacity, final String departmentMinTotal, final String departmentMinSpecial, final String departmentDescription, final String selectedSubject) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
        void onFragmentInteraction(Uri uri);
    }
}
