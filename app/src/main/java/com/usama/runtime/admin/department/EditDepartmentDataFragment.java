package com.usama.runtime.admin.department;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.usama.runtime.R;

import java.util.HashMap;


public class EditDepartmentDataFragment extends Fragment {
    private Button buttonEditDepartment;
    private TextView getDepartmentName;
    private EditText getDepartmentCapacity, getDepartmentMinTotal, getDepartmentMinSpecial ,getDepartmentDescription ;
    private String selectedSubject;

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
        EditDepartmentDataFragmentArgs args = EditDepartmentDataFragmentArgs.fromBundle(getArguments());
        String name = args.getDepartmentName();
        String capacity = args.getDepartmentCapacity();
        String total = args.getDepartmentMinTotal();
        String minSpecial = args.getDepartmentMinSpecial();
        String specialSubject = args.getDepartmentSpecialSubject();
        String dec = args.getDepartmentDescription();

        getDepartmentName = getView().findViewById(R.id.editDepartmentName);
        getDepartmentCapacity = getView().findViewById(R.id.editDepartmentCapacity);
        getDepartmentMinTotal = getView().findViewById(R.id.editDepartmentMinTotal);
        getDepartmentMinSpecial = getView().findViewById(R.id.editDepartmentMinSpecial);
        getDepartmentDescription = getView().findViewById(R.id.editDepartmentDescription);
        buttonEditDepartment = getView().findViewById(R.id.editButtonEditDepartment);
        MaterialSpinner subjectSpinner = getView().findViewById(R.id.edit_spinner_choose_subject);
        subjectSpinner.setItems("Without", "arabic", "english", "french", "geography", "history", "philosophy", "psychology");

        subjectSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedSubject = item.toString();
                Log.d("SpinnerTAG", selectedSubject);
            }
        });

        getDepartmentName.setText(name);
        getDepartmentCapacity.setText(capacity);
        getDepartmentMinTotal.setText(total);
        getDepartmentMinSpecial.setText(minSpecial);
        subjectSpinner.setText(specialSubject);
        getDepartmentDescription.setText(dec);


        buttonEditDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDepartment();
                Navigation.findNavController(getView()).navigate(EditDepartmentDataFragmentDirections.actionEditDepartmentDataFragmentToShowDepartmentFragment());
            }
        });

    }


    private void editDepartment() {


        String departmentName = getDepartmentName.getText().toString().trim();
        String departmentCapacity = getDepartmentCapacity.getText().toString().trim();
        String departmentMinTotal = getDepartmentMinTotal.getText().toString().trim();
        String departmentMinSpecial = getDepartmentMinSpecial.getText().toString().trim();

        if (selectedSubject == null) {
            selectedSubject = "nothing";
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("departments");
        HashMap<String, Object> studentMap = new HashMap<>();
        studentMap.put("departmentCapacity", departmentCapacity);
        studentMap.put("departmentMinSpecial", departmentMinSpecial);
        studentMap.put("departmentMinTotal", departmentMinTotal);
//        studentMap.put("departmentName", departmentName);
        studentMap.put("departmentSpecialSubject", selectedSubject);


        ref.child(departmentName).updateChildren(studentMap);

        Toast.makeText(getActivity(), "Your Department update successfully.", Toast.LENGTH_SHORT).show();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
