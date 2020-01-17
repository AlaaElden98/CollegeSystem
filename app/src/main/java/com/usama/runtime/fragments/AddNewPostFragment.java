package com.usama.runtime.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRouter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usama.runtime.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class AddNewPostFragment extends Fragment {
    private String subject, description, postTimeAndDate;
    private Button addPostBtn,showPostBtn;
    private EditText nameOfSubject, descriptionOfTopic;
    private TextView nameOfProfessor;
    private DatabaseReference postRef;
    private ProgressDialog loadingBar;
    String nameOfDoctor ;
    private static final String DoctorName = "DoctorName";


    public AddNewPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_post, container, false);
    }

    // final String CHANNEL_1_ID="channel1";
    //final String CHANNEL_2_ID="channel2";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        addPostBtn = getView().findViewById(R.id.addPostBtn);
        nameOfProfessor = getView().findViewById(R.id.nameOfProfessor);
        nameOfSubject = getView().findViewById(R.id.nameOfSubject);
        descriptionOfTopic = getView().findViewById(R.id.descriptionOfTopic);
        loadingBar = new ProgressDialog(getActivity());
        showPostBtn= getView().findViewById(R.id.showPostBtn);


        SharedPreferences prefs = getActivity().getSharedPreferences(DoctorName, MODE_PRIVATE);

        nameOfDoctor= prefs.getString("DoctorName", "No Name");//"No name defined" is the default value.
        nameOfProfessor.setText(nameOfDoctor);

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidatePostData();
            }
        });
        showPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment, new showpostFragment(), "fragment_screen");
                ft.commit();
            }
        });

    }


    private void ValidatePostData() {
        subject = nameOfSubject.getText().toString();
        description = descriptionOfTopic.getText().toString();

        if (TextUtils.isEmpty(subject)) {
            Toast.makeText(getActivity(), "Please write Name of subject...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(getActivity(), "Please write Description...", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 =new NotificationChannel(
                    CHANNEL_1_ID,"Channel 1", NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription( "this is channel 1" );
            NotificationChannel channel2 =new NotificationChannel(
                    CHANNEL_2_ID,"Channel 2", NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription( "this is channel 2" );
//            NotificationManager manager =getFragmentManager(AddNewPostFragment.this);
  //          manager.createNotificationChannel( channel1 );
    //        manager.createNotificationChannel( channel2 );
           // Notification notification =new NotificationCompat.Builder( this,CHANNEL_1_ID ).set
        }*/

    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Add New Post");
        loadingBar.setMessage("Dear Doctor, please wait while we are adding the new post.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("E, dd MMM .. HH : mm ");
        postTimeAndDate = currentDate.format(calendar.getTime());

        SavePostInfoToDatabase();
    }
    public  TextView  DoctorName1, Post_subject1, Post_description1, post_date1;

    //private final int NOTIFICATION_ID=001;
    //  private final String CHANNEL_ID ="personsl_notificstion";
    private void SavePostInfoToDatabase() {
        SharedPreferences prefs = getActivity().getSharedPreferences(DoctorName, MODE_PRIVATE);

        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("dataAndTime", postTimeAndDate);
        postMap.put("name", prefs.getString("DoctorName", ""));
        postMap.put("subject", subject);
        postMap.put("id",prefs.getString("DoctorID", ""));
        postMap.put("description", description);
        String message = "this is a notification";
        postRef.push().updateChildren(postMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            Intent intent = new Intent(getActivity(), AddNewPostActivity.class);
//                            startActivity(intent);


                            loadingBar.dismiss();
                            Toast.makeText(getActivity(), "Post is added successfully..", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
