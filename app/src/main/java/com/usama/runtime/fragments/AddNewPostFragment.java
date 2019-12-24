package com.usama.runtime.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usama.runtime.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddNewPostFragment extends Fragment {
    private String professorName, subject, description, timetostring;
    private Button addPostBtn;
    private EditText nameOfProfessor, nameOfSubject, descriptionOfTopic;
    private DatabaseReference postRef;
    private ProgressDialog loadingBar;


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


        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidatePostData();
            }
        });

    }


    private void ValidatePostData() {
        professorName = nameOfProfessor.getText().toString();
        subject = nameOfSubject.getText().toString();
        description = descriptionOfTopic.getText().toString();

        if (TextUtils.isEmpty(professorName)) {
            Toast.makeText(getActivity(), "Please write Your Name sir...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(subject)) {
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

        Date currentTime = Calendar.getInstance().getTime();
        timetostring = currentTime.toString();

        SavePostInfoToDatabase();
    }
//private final int NOTIFICATION_ID=001;
  //  private final String CHANNEL_ID ="personsl_notificstion";
    private void SavePostInfoToDatabase() {
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("name", professorName);
        postMap.put("dataAndTime", timetostring);
        postMap.put("subject", subject);
        postMap.put("description", description);
String message="this is a notification";
        postRef.child(professorName).updateChildren(postMap)
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

     /*   NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        builder.setSmallIcon(R.drawable.ic_menu_manage);
        builder.setContentTitle("Simple Notification");
        builder.setContentText("this is a simple notification");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = (NotificationManager) NotificationManagerCompat.from(getActivity());

        notificationManager.notify(0, builder.build());
*/
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
