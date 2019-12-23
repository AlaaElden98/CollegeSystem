package com.usama.runtime.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usama.runtime.R;
import com.usama.runtime.interfaces.ItemClickListner;

public class PostsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtPostDoctorName, txtPostSubject, txtPostDescription, dateAndTime;

    // this is our interface
    public ItemClickListner listner;

    public PostsViewHolder(@NonNull View itemView) {
        super(itemView);
        txtPostDoctorName = itemView.findViewById(R.id.doctor_name);
        txtPostSubject = itemView.findViewById(R.id.subject);
        txtPostDescription = itemView.findViewById(R.id.post_description);
        dateAndTime = itemView.findViewById(R.id.dateAndTime);

    }
    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
