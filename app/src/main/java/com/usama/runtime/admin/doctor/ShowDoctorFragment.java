package com.usama.runtime.admin.doctor;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usama.runtime.R;
import com.usama.runtime.admin.department.ShowDepartmentFragment;
import com.usama.runtime.admin.department.ShowDepartmentFragmentDirections;
import com.usama.runtime.model.Department;
import com.usama.runtime.model.Doctors;

public class ShowDoctorFragment extends Fragment {
    private RecyclerView doctorList;
    private DatabaseReference doctorRef;

    public ShowDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_doctor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doctorRef = FirebaseDatabase.getInstance().getReference().child("Doctors");

        doctorList = getView().findViewById(R.id.doctor_list);
        doctorList.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions<Doctors> options =
                new FirebaseRecyclerOptions.Builder<Doctors>()
                        .setQuery(doctorRef, Doctors.class)
                        .build();

        FirebaseRecyclerAdapter<Doctors, DoctorViewHolder> adapter
                = new FirebaseRecyclerAdapter<Doctors, DoctorViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DoctorViewHolder holder, final int position, @NonNull final Doctors model) {
                holder.doctorNameItem.setText("Name : " + model.getRealName());
                holder.doctorNationalItem.setText("National ID : " + model.getNationalID());
                holder.doctorPasswordItem.setText("Password : " + model.getPassword());
                holder.editDoctorInfoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID = getRef(position).getKey();
                        String name = model.getRealName();
                        String national = model.getNationalID();
                        String password = model.getPassword();


                        Navigation.findNavController(getView()).navigate(ShowDoctorFragmentDirections.actionShowDoctorFragmentToEditDoctorDataFragment(name,national,password).setUID(uID));
                    }
                });
                holder.delete_doctor_btn.setOnClickListener(new View.OnClickListener() {
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public ShowDoctorFragment.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_one_item_layout, parent, false);

                return new ShowDoctorFragment.DoctorViewHolder(view);
            }
        };
        doctorList.setAdapter(adapter);
        adapter.startListening();

    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView doctorNameItem, doctorNationalItem, doctorPasswordItem;
        Button editDoctorInfoBtn;
        Button delete_doctor_btn;

        DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameItem = itemView.findViewById(R.id.doctor_name_item);
            doctorNationalItem = itemView.findViewById(R.id.doctor_national_item);
            doctorPasswordItem = itemView.findViewById(R.id.doctor_password_item);
            editDoctorInfoBtn = itemView.findViewById(R.id.edit_doctor_info_btn);
            delete_doctor_btn = itemView.findViewById(R.id.delete_doctor_btn);

        }
    }

    private void RemoverDepartment(String uID) {
        // first connect with database
        // orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        doctorRef.child(uID).removeValue();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
