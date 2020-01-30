package com.usama.runtime.student;

import android.Manifest;
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
import com.usama.runtime.MainActivity;
import com.usama.runtime.R;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BarCodeFragment extends Fragment {
    // barCode
    private FirebaseVisionBarcodeDetector detector;
    private Button btnDone;
    private boolean isDetected = false;
    private TextView result_of_barcode;
    private String subjectResult, uniqueResult;

    private static final String MY_NATIONAL_ID = "MyNationalId";
    private SharedPreferences prefs;
    private String nationalId;
    private HashMap<String, Object> studentMap;
    private DatabaseReference rootRef;

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
        result_of_barcode = getView().findViewById(R.id.result_of_barcode);
        // camera and record permission
        Dexter.withActivity(getActivity())
                .withPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
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
            final SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
            // get the time now
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            final String currentDateAndTime = sdf.format(new Date());
            btnDone = getView().findViewById(R.id.btn_done_barcode);
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


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getActivity().getSharedPreferences(MY_NATIONAL_ID, MODE_PRIVATE);
                nationalId = prefs.getString("nationalId", "1");//"No name defined" is the default value.
                if (subjectResult == null) {
                    Toast.makeText(getContext(), "please Come close to the barcode", Toast.LENGTH_SHORT).show();
                } else {
//                    saveAttendOnDataBase();
                }
                final SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);


                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                final String currentDateandTime = sdf.format(new Date(System.currentTimeMillis() + 7200000));
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("TIMENOW", currentDateandTime);
                editor.commit();

                Navigation.findNavController(getView()).navigate(BarCodeFragmentDirections.actionBarCodeFragmentToHomeFragment());

                btnDone.setEnabled(false);
            }
        });
    }

    private void setUpCamera() {
        btnDone = getView().findViewById(R.id.btn_done_barcode);
        btnDone.setEnabled(isDetected);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDetected = !isDetected;
            }
        });
        CameraView camera_view = getView().findViewById(R.id.camera_view);
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
            btnDone.setEnabled(isDetected);
            for (FirebaseVisionBarcode item : firebaseVisionBarCodes) {
                int value_type = item.getValueType();
                Log.d("TAG", item.getRawValue() + " ");
                switch (value_type) {
                    case FirebaseVisionBarcode.TYPE_CONTACT_INFO: {
                        String info = new StringBuilder("Name : ")
                                .append(item.getContactInfo().getName().getFormattedName())
                                .toString();

                        Log.d("TAG", item.getContactInfo().getTitle());
                        result_of_barcode.setText(info);
                        result_of_barcode.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
                        subjectResult = item.getContactInfo().getName().getFormattedName();
                        uniqueResult = item.getContactInfo().getTitle();
                        Log.d("TAGRESULT", uniqueResult);
                    }
                    break;
                    default:
                        break;

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


//    private void saveAttendOnDataBase() {
//        rootRef = FirebaseDatabase.getInstance().getReference().child("students_attendance").child(nationalId);
//
//        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!(dataSnapshot.child(result).exists())) {
//                    studentMap = new HashMap<>();
//                    studentMap.put(result, 1);
//                    rootRef.updateChildren(studentMap);
//                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//                    String currentDateandTime = sdf.format(new Date(System.currentTimeMillis() + 7200000));
//
//                    //Toast.makeText(BarCodeFragment.this, currentDateandTime , Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), "Thanks ", Toast.LENGTH_SHORT).show();
//
//                    // TODO : WE NEED LOCK THIS PAGE MINIMUM 1 HOUR
//
//                } else {
//
//                    int plate = Integer.parseInt(String.valueOf(dataSnapshot.child(result).getValue()));
//                    plate++;
//                    Log.d("TAG", "name of subject = " + plate);
//                    studentMap = new HashMap<>();
//                    studentMap.put(result, plate);
//                    rootRef.updateChildren(studentMap);
//
//                    Toast.makeText(getActivity(), "Thanks:)", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "check your QR", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
