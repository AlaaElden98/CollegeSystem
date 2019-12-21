package com.usama.runtime.BarCodePackageForTest;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class BarCodeActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences(MY_NATIONAL_ID, MODE_PRIVATE);
                nationalId = prefs.getString("nationalId", "1");//"No name defined" is the default value.
                saveAttendOnDataBase();


                // TODO : HANDEL THIS --> WE NEED LOCK THE THE ACTIVITY FROM OUTSIDE HERE
                btnDone.setEnabled(false);
                new CountDownTimer(500000, 10) { //Set Timer for 5 seconds
                    public void onTick(long millisUntilFinished) {
                        Toast.makeText(BarCodeActivity.this, "you can't take QR again :) ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
                        btnDone.setEnabled(true);
                    }
                }.start();
            }
        });
        // request permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        setUpCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(BarCodeActivity.this, "You must accept permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

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
                    Toast.makeText(BarCodeActivity.this, "Thanks ", Toast.LENGTH_SHORT).show();

                    // TODO : WE NEED LOCK THIS PAGE MINIMUM 1 HOUR

                } else {

                    int plate = Integer.parseInt(String.valueOf(dataSnapshot.child(result).getValue()));
                    plate++;
                    Log.d("TAG", "name of subject = " + plate);
                    studentMap = new HashMap<>();
                    studentMap.put(result, plate);
                    rootRef.updateChildren(studentMap);

                    Toast.makeText(BarCodeActivity.this, "Thanks:)", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BarCodeActivity.this, "check your QR", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void setUpCamera() {
        txt_result = findViewById(R.id.code_info);
        btn_on_off = findViewById(R.id.btn_enable_disable);

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
        surfaceView = findViewById(R.id.cameraView);
        setUpQREader();
    }

    private void setUpQREader() {
        qrEader = new QREader.Builder(this, surfaceView, new QRDataListener() {
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
    protected void onResume() {
        super.onResume();
        Dexter.withActivity(this)
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
                        Toast.makeText(BarCodeActivity.this, "You must accept permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }


                }).check();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Dexter.withActivity(this)
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
                        Toast.makeText(BarCodeActivity.this, "You must accept permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                    }

                }).check();
    }
}

/*
*
*         rootRef = FirebaseDatabase.getInstance().getReference().child("students_attendance");
        Log.d("TAG", result);

        Log.d("TAG", nationalId);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("students_attendance").child(result).exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        int plate = Integer.parseInt(String.valueOf(snapshot.child(result).getValue()));
                        plate++;
                        Log.d("TAG", "name of subject = " + plate);
                        studentMap = new HashMap<>();
                        studentMap.put(result, plate);
                        rootRef.child(nationalId).updateChildren(studentMap);

                    }
                } else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("students_attendance");

                    studentMap = new HashMap<>();
                    Log.d("CHECKIN", result);
                    studentMap.put(result, 1);
                    SharedPreferences prefs = getSharedPreferences(MY_NATIONAL_ID, MODE_PRIVATE);
                    nationalId = prefs.getString("nationalId", "1");//"No name defined" is the default value.
                    System.out.println(nationalId);


                    ref.child(nationalId).updateChildren(studentMap);

                    Toast.makeText(MainActivity.this, "Your Desires update successfully.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*
* */