package com.usama.runtime.admin.doctor;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usama.runtime.R;
import com.usama.runtime.admin.doctor.EditDoctorDataFragmentArgs;

import java.util.HashMap;
import java.util.Objects;

public class EditDoctorDataFragment extends Fragment {
    private TextView getDoctorName, getDoctorNational;
    private EditText getDoctorPassword;
    private Button buttonEditDoctorData ;

    private String name , nationalID ;
    public EditDoctorDataFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_edit_doctor_data, container, false);

        getDoctorName = view.findViewById(R.id.edit_doctor_name);
        getDoctorPassword = view.findViewById(R.id.edit_doctor_pass);
        getDoctorNational = view.findViewById(R.id.edit_doctor_national);
        buttonEditDoctorData = view.findViewById(R.id.edit_doctor_info_btn_item);

        return view ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditDoctorDataFragmentArgs args = EditDoctorDataFragmentArgs.fromBundle(Objects.requireNonNull(getArguments()));
        name = args.getRealName();
        nationalID = args.getNationalId();
        String password = args.getPassword();

        getDoctorName.setText(name);
        getDoctorNational.setText(nationalID);
        getDoctorPassword.setText(password);

        buttonEditDoctorData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDepartment();
            }
        });

    }


    private void editDepartment() {
        String doctorPassword = getDoctorPassword.getText().toString().trim();

        if (doctorPassword.equals("")) {
            Toast.makeText(getContext(), "please write doctor password", Toast.LENGTH_SHORT).show();
        } else {


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Doctors");
            HashMap<String, Object> doctorMap = new HashMap<>();
            doctorMap.put("nationalID", nationalID);
            doctorMap.put("realName", name);
            doctorMap.put("password", doctorPassword);
            ref.child(nationalID).updateChildren(doctorMap);

            Toast.makeText(getActivity(), "Doctor info update successfully.", Toast.LENGTH_LONG).show();

            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(EditDoctorDataFragmentDirections.actionEditDoctorDataFragmentToShowDoctorFragment());
        }

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
