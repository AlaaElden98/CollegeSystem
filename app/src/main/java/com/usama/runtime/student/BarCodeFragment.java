package com.usama.runtime.student;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Frame;
import com.otaliastudios.cameraview.FrameProcessor;
import com.usama.runtime.R;
import com.usama.runtime.model.Student;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

// TODO : generate qr with valid info
public class BarCodeFragment extends Fragment {
    //init barCode firebase
    private FirebaseVisionBarcodeDetector detector;
    private boolean isDetected = false;
    private FirebaseVisionImageMetadata metadata;
    private byte[] data;

    // init result of barcode
    private String subjectResult, doctorName, uniqueResult, lecNumber;
    private TextView result_of_barcode;

    // init camera
    private CameraView camera_view;

    // init view
    private Button btnDone;
    private String studentName, studentNationalId;


    // init shared preference
    private static final String MY_NATIONAL_ID = "MyNationalId";
    private HashMap<String, Object> studentMap;
    private DatabaseReference rootRef;

    public BarCodeFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_code, container, false);
        camera_view = view.findViewById(R.id.camera_view);
        camera_view.setLifecycleOwner(getViewLifecycleOwner());
        result_of_barcode = view.findViewById(R.id.result_of_barcode);
        btnDone = view.findViewById(R.id.btn_done_barcode);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            //create shared preferences called My prefs
            final SharedPreferences sharedpreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("MyPrefs", MODE_PRIVATE);
            // get the time now
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            final String currentDateAndTime = sdf.format(new Date());
            //Convert time from string to localtime
            LocalTime wait = LocalTime.parse(sharedpreferences.getString("TIMENOW", ""));
            LocalTime now = LocalTime.parse(currentDateAndTime);
            if (wait.isBefore(now)) {
                btnDone.setEnabled(true);
            } else {
                btnDone.setEnabled(false);
                Toast.makeText(getActivity(), "Wait until the next lecture to take another QR", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "WELCOME", Toast.LENGTH_LONG).show();
        }

        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(MY_NATIONAL_ID, MODE_PRIVATE);
        String nationalId = prefs.getString("nationalId", "1");//"No name defined" is the default value.

        rootRef = FirebaseDatabase.getInstance().getReference().child("students").child(nationalId);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentName = dataSnapshot.getValue(Student.class).getName();
                studentNationalId = dataSnapshot.getValue(Student.class).getNational_id();
                Log.d("TagStudentName", studentName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subjectResult == null) {
                    Toast.makeText(getContext(), "please make sure the barcode valid from doctor ", Toast.LENGTH_SHORT).show();
                } else if (uniqueResult == null) {
                    Toast.makeText(getContext(), "please make sure the barcode valid from doctor ", Toast.LENGTH_SHORT).show();
                } else if (doctorName == null) {
                    Toast.makeText(getContext(), "please make sure the barcode valid from doctor ", Toast.LENGTH_SHORT).show();
                } else if (lecNumber == null) {
                    Toast.makeText(getContext(), "please make sure the barcode valid from doctor ", Toast.LENGTH_SHORT).show();
                } else {
                    final SharedPreferences sharedpreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    final String currentDateAndTime = sdf.format(new Date(System.currentTimeMillis() + 7200000));
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("TIMENOW", currentDateAndTime);
                    editor.apply();
                    saveAttendOnDataBase(subjectResult, uniqueResult, doctorName, lecNumber);

//                btnDone.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // make sure user accepted permission camera and record permission
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        setUpCamera();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    private void setUpCamera() {
        camera_view.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                ProcessImage(getVisionImageFromFrame(frame));
            }
        });
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build();
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
    }


    private void ProcessImage(FirebaseVisionImage image) {
        if (!isDetected) {
            detector.detectInImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarCodes) {
                            processResult(firebaseVisionBarCodes);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarCodes) {
        if (firebaseVisionBarCodes.size() > 0) {
            isDetected = true;
//            btnDone.setEnabled(isDetected);
            for (FirebaseVisionBarcode item : firebaseVisionBarCodes) {
                int value_type = item.getValueType();
                Log.d("TAG", item.getRawValue() + " ");
                if (value_type == FirebaseVisionBarcode.TYPE_CONTACT_INFO) {
                    doctorName = item.getContactInfo().getName().getFirst();
                    subjectResult = item.getContactInfo().getName().getLast();
                    lecNumber = item.getContactInfo().getTitle();
                    uniqueResult = item.getContactInfo().getOrganization();

                    // first name , last name , title , organization ,
                    Log.d("TAGBARCODE", doctorName);
                    Log.d("TAGBARCODE", subjectResult);
                    Log.d("TAGBARCODE", lecNumber);
                    Log.d("TAGBARCODE", uniqueResult);
                    result_of_barcode.setText(doctorName + "\n" + "in subject " + subjectResult);
                    result_of_barcode.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

                }
            }
        }
    }

    private FirebaseVisionImage getVisionImageFromFrame(Frame frame) {
        //To create a FirebaseVisionImage object from
        // byte array, first calculate the image rotation as described above for media.Image input

        metadata = new FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setHeight(frame.getSize().getHeight())
                .setWidth(frame.getSize().getWidth())
                .build();
        Log.d("TAG", frame.getSize().getHeight() + "");
        Log.d("TAG", frame.getSize().getWidth() + "");
        data = frame.getData();

        return FirebaseVisionImage.fromByteArray(data, metadata);
    }


    private void saveAttendOnDataBase(final String subName, final String unique, final String doctor, final String lec) {
        rootRef = FirebaseDatabase.getInstance().getReference().child("students_attendance").child(doctor).child(subName).child(lec).child(unique).child("StudentName");

        /*
         * firebase structure
         *  students_attendance
         *      doctorName
         *              lec_one
         *                  uniqueResult
         * */
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentMap = new HashMap<>();
                studentMap.put(studentNationalId, studentName);
                rootRef.updateChildren(studentMap);

                Toast.makeText(getActivity(), "Thanks:)", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(BarCodeFragmentDirections.actionBarCodeFragmentToHomeFragment());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
