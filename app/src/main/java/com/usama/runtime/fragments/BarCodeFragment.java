package com.usama.runtime.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.usama.runtime.R;

import java.time.LocalTime;
import java.util.HashMap;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class BarCodeFragment extends Fragment {
    TextView txt_result;
    private SurfaceView surfaceView;
    QREader qrEader;
    ToggleButton btn_on_off;
    String result;
    Button btnDone;
    HashMap<String, Object> studentMap;

    DatabaseReference rootRef;

    public static final String MY_NATIONAL_ID = "MyNationalId";
    SharedPreferences prefs;
    String nationalId;

    public BarCodeFragment() {
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
        return inflater.inflate(R.layout.fragment_bar_code, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //create shared preferences called My prefs
        final SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        // get the time now
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        final String currentDateandTime = sdf.format(new Date());
        btnDone = getView().findViewById(R.id.btnDone);
        //Convert time from string to localtime
        LocalTime wait = LocalTime.parse(sharedpreferences.getString("TIMENOW", "").toString());
        LocalTime now = LocalTime.parse(currentDateandTime);
        //LocalTime wait=LocalTime.parse( "14:50:50");
        //LocalTime now=LocalTime.parse("15:50:50");
        //Toast.makeText(BarCodeFragment.this, sharedpreferences.getString("TIMENOW", "").toString(), Toast.LENGTH_SHORT).show();
        //if pass two hours from user clicked make the button Done is work if not don't make that button work
        if (wait.isBefore(now)) {
            Toast.makeText(getActivity(), wait.toString(), Toast.LENGTH_SHORT).show();
            btnDone.setEnabled(true);
        } else {
            btnDone.setEnabled(false);
            Toast.makeText(getActivity(), wait.toString(), Toast.LENGTH_SHORT).show();
        }
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getActivity().getSharedPreferences(MY_NATIONAL_ID, MODE_PRIVATE);
                nationalId = prefs.getString("nationalId", "1");//"No name defined" is the default value.
                saveAttendOnDataBase();


                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                final String currentDateandTime = sdf.format(new Date(System.currentTimeMillis() + 7200000));
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("TIMENOW", currentDateandTime);
                editor.commit();

                Intent intent = new Intent(getActivity(), ButtonAdminFragment.class);
                startActivity(intent);

                btnDone.setEnabled(false);
            }
        });
        // request permission
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        setUpCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getActivity(), "You must accept permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }

                }).check();

    }

    private void saveAttendOnDataBase() {
        rootRef = FirebaseDatabase.getInstance().getReference().child("students_attendance").child(nationalId);

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child(result).exists())) {
                    studentMap = new HashMap<>();
                    studentMap.put(result, 1);
                    rootRef.updateChildren(studentMap);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date(System.currentTimeMillis() + 7200000));

                    //Toast.makeText(BarCodeFragment.this, currentDateandTime , Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "Thanks ", Toast.LENGTH_SHORT).show();

                    // TODO : WE NEED LOCK THIS PAGE MINIMUM 1 HOUR

                } else {

                    int plate = Integer.parseInt(String.valueOf(dataSnapshot.child(result).getValue()));
                    plate++;
                    Log.d("TAG", "name of subject = " + plate);
                    studentMap = new HashMap<>();
                    studentMap.put(result, plate);
                    rootRef.updateChildren(studentMap);

                    Toast.makeText(getActivity(), "Thanks:)", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "check your QR", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void setUpCamera() {
        txt_result = getView().findViewById(R.id.code_info);
        btn_on_off = getView().findViewById(R.id.btn_enable_disable);

        btn_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrEader.isCameraRunning()) {
                    btn_on_off.setChecked(false);
                    qrEader.stop();
                } else {
                    btn_on_off.setChecked(true);
                    qrEader.start();
                }
            }
        });
        surfaceView = getView().findViewById(R.id.cameraView);
        setUpQREader();
    }


    private void setUpQREader() {
        qrEader = new QREader.Builder(getActivity(), surfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                txt_result.post(new Runnable() {
                    @Override
                    public void run() {
                        txt_result.setText(data);
                        result = data;
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(surfaceView.getHeight())
                .width(surfaceView.getWidth())
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (qrEader != null) {
                            qrEader.initAndStart(surfaceView);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getActivity(), "You must accept permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }


                }).check();
    }

    @Override
    public void onPause() {
        super.onPause();
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (qrEader != null) {
                            qrEader.releaseAndCleanup();

                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getActivity(), "You must accept permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                    }

                }).check();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
