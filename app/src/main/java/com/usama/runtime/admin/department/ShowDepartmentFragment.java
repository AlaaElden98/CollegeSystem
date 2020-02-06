package com.usama.runtime.admin.department;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.usama.runtime.R;
import com.usama.runtime.model.Department;

import java.util.Objects;

public class ShowDepartmentFragment extends Fragment {
    private RecyclerView department_list_recycler_view;
    private DatabaseReference departmentRef;

    // init String used
    private String uID, capacity, total, subject, name, dec, minSpecial, doctorName;

    public ShowDepartmentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(ShowDepartmentFragmentDirections.actionShowDepartmentFragmentToAdminHomeFragment(doctorName));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_department, container, false);

        ShowDepartmentFragmentArgs args = ShowDepartmentFragmentArgs.fromBundle(getArguments());
        doctorName = args.getRealName();

        department_list_recycler_view = view.findViewById(R.id.department_list);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        departmentRef = FirebaseDatabase.getInstance().getReference().child("departments");

        department_list_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        department_list_recycler_view.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Department> options =
                new FirebaseRecyclerOptions.Builder<Department>()
                        .setQuery(departmentRef, Department.class)
                        .build();

        FirebaseRecyclerAdapter<Department, DepartmentViewHolder> adapter
                = new FirebaseRecyclerAdapter<Department, DepartmentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DepartmentViewHolder holder, final int position, @NonNull final Department model) {
                holder.departmentNameItem.setText("Name : " + model.getDepartmentName());
                holder.departmentMinSpecialItem.setText("Min Special : " + model.getDepartmentMinSpecial());
                holder.departmentMinCapacityItem.setText("Capacity: " + model.getDepartmentCapacity());
                holder.DepartmentMinTotal.setText("Min Total: " + model.getDepartmentMinTotal());
                holder.DepartmentSpecialSubject.setText("Special Subject: " + model.getDepartmentSpecialSubject());
                holder.show_all_department_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uID = getRef(position).getKey();
                        capacity = model.getDepartmentCapacity();
                        total = model.getDepartmentMinTotal();
                        subject = model.getDepartmentSpecialSubject();
                        name = model.getDepartmentName();
                        dec = model.getDepartmentDescription();
                        minSpecial = model.getDepartmentMinSpecial();


                        Navigation.findNavController(getView()).navigate(ShowDepartmentFragmentDirections
                                .actionShowDepartmentFragmentToEditDepartmentDataFragment(name, capacity, minSpecial, total, subject, dec, doctorName)
                                .setUID(uID));
                    }
                });
                holder.Delete_department_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence option[] = new CharSequence[]{
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
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_one_item_layout, parent, false);

                return new DepartmentViewHolder(view);
            }
        };
        department_list_recycler_view.setAdapter(adapter);
        adapter.startListening();

    }

    static class DepartmentViewHolder extends RecyclerView.ViewHolder {
        TextView departmentNameItem, departmentMinSpecialItem, departmentMinCapacityItem, DepartmentMinTotal, DepartmentSpecialSubject;
        Button show_all_department_btn;
        Button Delete_department_btn;

        DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            departmentNameItem = itemView.findViewById(R.id.departmentNameItem);
            departmentMinCapacityItem = itemView.findViewById(R.id.departmentMinCapacityItem);
            departmentMinSpecialItem = itemView.findViewById(R.id.departmentMinSpecialItem);
            DepartmentMinTotal = itemView.findViewById(R.id.DepartmentMinTotal);
            DepartmentSpecialSubject = itemView.findViewById(R.id.DepartmentSpecialSubject);
            show_all_department_btn = itemView.findViewById(R.id.show_all_department_btn);
            Delete_department_btn = itemView.findViewById(R.id.Delete_department_btn);

        }
    }

    private void RemoverDepartment(String uID) {
        // first connect with database
        // orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        departmentRef.child(uID).removeValue();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
