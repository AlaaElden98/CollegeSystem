package com.usama.runtime.doctor;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usama.runtime.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewPostFragment extends Fragment {
    private String subject, description, postTimeAndDate, name, nationalId;
    private Button addPostBtn;
    private EditText nameOfSubject, descriptionOfTopic;
    private TextView nameOfProfessor;
    private DatabaseReference postRef;
    private ProgressDialog loadingBar;

    private long countPosts = 0;
    private String saveCurrentDateKey, saveCurrentTimeKey, productRandomKey;


    public AddNewPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getView()).navigate(AddNewPostFragmentDirections.actionAddNewPostFragmentToDoctorHomeFragment(name, nationalId));
                Toast.makeText(getActivity(), "Your post not uploading.", Toast.LENGTH_SHORT).show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_add_new_post, container, false);

        addPostBtn = view.findViewById(R.id.addPostBtn);
        nameOfProfessor = view.findViewById(R.id.nameOfProfessor);
        nameOfSubject = view.findViewById(R.id.nameOfSubject);
        descriptionOfTopic = view.findViewById(R.id.descriptionOfTopic);

        return view;
    }


    // final String CHANNEL_1_ID="channel1";
    //final String CHANNEL_2_ID="channel2";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadingBar = new ProgressDialog(getActivity());


        AddNewPostFragmentArgs args = AddNewPostFragmentArgs.fromBundle(getArguments());
        name = args.getRealName();
        nationalId = args.getNationalId();
        nameOfProfessor.setText(name);

        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countPosts = dataSnapshot.getChildrenCount();
                Log.d("counter", countPosts + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidatePostData();
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
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Please check internet .. refresh your app ", Toast.LENGTH_SHORT).show();
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


        SimpleDateFormat currentDateToKey = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDateKey = currentDateToKey.format(calendar.getTime());

        SimpleDateFormat currentTimeToKey = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTimeKey = currentTimeToKey.format(calendar.getTime());

        productRandomKey = saveCurrentDateKey + saveCurrentTimeKey;


        SavePostInfoToDatabase(name, subject, description);
    }

    //private final int NOTIFICATION_ID=001;
    //  private final String CHANNEL_ID ="personsl_notificstion";
    private void SavePostInfoToDatabase(String nameS, String subS, String desc) {

        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("dataAndTime", postTimeAndDate);
        postMap.put("name", nameS);
        postMap.put("subject", subS);
        postMap.put("id", nationalId);
        postMap.put("description", desc);
        postMap.put("counter", countPosts);
        String message = "this is a notification";
        postRef.child(productRandomKey).updateChildren(postMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(getActivity(), "Post is added successfully..", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getView()).navigate(AddNewPostFragmentDirections.actionAddNewPostFragmentToDoctorHomeFragment(name, nationalId));
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
