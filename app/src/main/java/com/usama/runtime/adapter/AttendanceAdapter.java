package com.usama.runtime.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usama.runtime.R;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    List<String> attendance_list = new ArrayList<>();


    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_attendance_counter_item, parent, false);
        AttendanceAdapter.ViewHolder viewHolder = new AttendanceAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.ViewHolder holder, int position) {
        holder.textView.setText(attendance_list.get(position));

    }

    public void setList(List<String> attendance_list) {
        this.attendance_list = attendance_list;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return attendance_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.subjectName);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
        }
    }
}
