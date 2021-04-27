package com.databoat.barcodescanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class ReadPerusalActivity extends AppCompatActivity {

    private SurfaceView surfaceView;

    private CameraSource cameraSource;
    private TextRecognizer textRecognizer;

    private String stringResult = null;

    public static final String CURRENT_PERUSAL_KEY = "CURRENT_READING";
    public static final int CURRENT_REQ_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_perusal);
        textRecognizer();
    }

    private void textRecognizer() {
        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                .setRequestedPreviewSize(1280, 1024)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView = findViewById(R.id.activity_surfaceView);

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            getApplicationContext(),
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {

                    @Override
                    public void release() {
                    }

                    @Override
                    public void receiveDetections(Detector.Detections<TextBlock> detections) {
                        SparseArray<TextBlock> sparseArray = detections.getDetectedItems();
                        StringBuilder stringBuilder = new StringBuilder();

                        for (int i = 0; i<sparseArray.size(); ++i){
                            TextBlock textBlock = sparseArray.valueAt(i);
                            if (textBlock != null && textBlock.getValue() != null){
                                stringBuilder.append(textBlock.getValue() + " ");
                            }
                        }

                        final String stringText = stringBuilder.toString();

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                stringResult = stringText;
                                resultObtained();
                            }
                        });
                    }
                });

            }
        }, 5000);


    }

    private void resultObtained() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(CURRENT_PERUSAL_KEY, stringResult);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}