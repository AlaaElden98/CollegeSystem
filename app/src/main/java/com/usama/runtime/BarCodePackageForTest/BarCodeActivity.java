package com.usama.runtime.BarCodePackageForTest;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.usama.runtime.R;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class BarCodeActivity extends AppCompatActivity {
    TextView txt_result;
    private SurfaceView surfaceView;
    QREader qrEader;
    ToggleButton btn_on_off;
    String result;
    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : HERE WE RETRIEVE QR DATA (NAME OF SUBJECT) ;
                // we have a lot of errors --> we don't know name of subject in department
                // we need a subject in any department in a specific table in database
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
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
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
                        System.out.println(result);
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
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }
}

