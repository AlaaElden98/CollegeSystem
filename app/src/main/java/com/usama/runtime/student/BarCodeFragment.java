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
    // barCode
    private FirebaseVisionBarcodeDetector detector;
    private boolean isDetected = false;
    private TextView result_of_barcode;
    private String subjectResult, doctorName, uniqueResult, lecNumber;

    private static final String MY_NATIONAL_ID = "MyNationalId";
    private HashMap<String, Object> studentMap;
    private DatabaseReference rootRef;
    private String studentName;

    public BarCodeFragment() {
        // Required empty public constructor
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
        result_of_barcode = Objects.requireNonNull(getView()).findViewById(R.id.result_of_barcode);
        Button btnDone = getView().findViewById(R.id.btn_done_barcode);
//        btnDone.setEnabled(true);

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
                Toast.makeText(getActivity(), wait.toString(), Toast.LENGTH_SHORT).show();
                btnDone.setEnabled(true);
            } else {
                btnDone.setEnabled(false);
                Toast.makeText(getActivity(), wait.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "WELCOME", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(MY_NATIONAL_ID, MODE_PRIVATE);
        String nationalId = prefs.getString("nationalId", "1");//"No name defined" is the default value.

        rootRef = FirebaseDatabase.getInstance().getReference().child("students").child(nationalId);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentName = Objects.requireNonNull(dataSnapshot.getValue(Student.class)).getName();
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
                    Toast.makeText(getContext(), "please Come close to the barcode", Toast.LENGTH_SHORT).show();
                }
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

    private void setUpCamera() {
        CameraView camera_view = Objects.requireNonNull(getView()).findViewById(R.id.camera_view);
        camera_view.setLifecycleOwner(this);
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
                    subjectResult = Objects.requireNonNull(Objects.requireNonNull(item.getContactInfo()).getName()).getFormattedName();
                    uniqueResult = item.getContactInfo().getTitle();
                    doctorName = item.getContactInfo().getOrganization();
                    lecNumber = item.getContactInfo().getPhones().toString();
                    result_of_barcode.setText(subjectResult);
                    result_of_barcode.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));

                }
            }
        }
    }

    // TODO : STEP 3
    //create a FirebaseVisionImage object from either a Bitmap, media.Image, ByteBuffer, byte array, or a file on the device.
    // Then, pass the FirebaseVisionImage object to the FirebaseVisionBarcodeDetector's detectInImage method.
    private FirebaseVisionImage getVisionImageFromFrame(Frame frame) {
        //To create a FirebaseVisionImage object from
        // byte array, first calculate the image rotation as described above for media.Image input
        byte[] data = frame.getData();
        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setHeight(frame.getSize().getHeight())
                .setWidth(frame.getSize().getWidth())
                .build();
        return FirebaseVisionImage.fromByteArray(data, metadata);
    }


    private void saveAttendOnDataBase(String subName, String unique, String doctor, String lec) {
        rootRef = FirebaseDatabase.getInstance().getReference().child("students_attendance").child(doctor).child(subName).child(lec).child(unique);

        /*
         * firebase structure
         *  students_attendance
         *      doctorName
         *          subjectName
         *              lec_one
         *                  uniqueResult
         * */
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentMap = new HashMap<>();
                studentMap.put("StudentName", studentName);
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
