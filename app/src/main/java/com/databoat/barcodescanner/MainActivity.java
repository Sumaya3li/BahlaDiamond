package com.databoat.barcodescanner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.databoat.barcodescanner.data.Current;
import com.databoat.barcodescanner.data.CurrentViewModel;
import com.databoat.barcodescanner.data.Previous;
import com.databoat.barcodescanner.data.PreviousRepository;
import com.databoat.barcodescanner.data.PreviousViewModel;
import com.databoat.barcodescanner.util.AdminHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.databoat.barcodescanner.util.AdminHelper.getDate;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnScan;
    private EditText tvIdst;
    private TextView tvName;
    private TextView tvPreviousReading;
    private EditText etCurrentReading;
    private EditText etNotes;
    private Button btnSave;
    private Button btnExport;

    private PreviousViewModel previousViewModel;
    private CurrentViewModel currentViewModel;

    private List<Current> currentReadings;
    private List<Previous> previousReadings;

    private static final int PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        // Set button disabled initially
        btnSave.setEnabled(false);
        setTextWatcher();

        btnScan.setOnClickListener(v -> scanBarcode());
        btnSave.setOnClickListener(new SaveButtonClick());
        btnExport.setOnClickListener(v -> writeFile());

        previousViewModel = new ViewModelProvider(this).get(PreviousViewModel.class);
        currentViewModel = new ViewModelProvider(this).get(CurrentViewModel.class);

        previousViewModel.getPreviousList().observe(this, this::getPreviousReadings);
        currentViewModel.getCurrentList().observe(this, this::getCurrentReadings);

        importPreviousReadings();
//        updateFiles();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String clientId = result.getContents().trim();
                tvIdst.setText(clientId);
                setClientName(clientId);
                setPreviousReading(clientId);
                setCurrentReading(clientId);
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "يجب عليك ادخال البيانات الصحيحة",
                    Snackbar.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportForm();
            } else {
                Toast.makeText(
                        this, tvIdst.getText().toString() + "طلب الاذن مرفوض",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    /****************************************** HELPER ********************************************/

    private void initViews() {
        tvIdst = findViewById(R.id.tv_idts_value);
        tvName = findViewById(R.id.tv_name_value);
        tvPreviousReading = findViewById(R.id.tv_previous_reading_value);
        etCurrentReading = findViewById(R.id.et_current_reading);
        etNotes = findViewById(R.id.et_notes);
        btnSave = findViewById(R.id.btn_save);
        btnScan = findViewById(R.id.btn_scan);
        btnExport = findViewById(R.id.btn_export);
    }

    private void getPreviousReadings(List<Previous> previousReadings) {
        this.previousReadings = previousReadings;
    }

    private void getCurrentReadings(List<Current> currentReadings) {
        this.currentReadings = currentReadings;
    }

    private void importPreviousReadings() {
        final AtomicInteger fcount = new AtomicInteger();
        PreviousRepository previousRepository = new PreviousRepository(getApplication());
        Thread t = new Thread(() -> {
            int num = previousRepository.getRecordCount();
            fcount.set(num);
            getPreviousReadings(num);
        });
        t.setPriority(10);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getPreviousReadings(int count) {
        List<Previous> clients = AdminHelper.importReadings(this);
        if (count < clients.size()) {
            previousViewModel.insertAll(clients);
            Log.d("getPreviousReadings", "COUNT : " + clients.size());
        }
    }

    private void setClientName(String id) {
        previousViewModel.getClientByIdst(id).observe(MainActivity.this, client -> {
            if (client != null) {
                tvName.setText(client.getNameId());
            } else {
                tvName.setText("");
            }
        });
    }

    private void setPreviousReading(String clientId) {
        previousViewModel.getClientByIdst(clientId).observe(this, previous -> {
            if (previous != null) {
                tvPreviousReading.setText(previous.getReading());
            } else {
                tvPreviousReading.setText("");
            }
        });
    }

    private void setCurrentReading(String clientId) {
        currentViewModel.getClientByIdst(clientId).observe(this, current -> {
            if (current != null) {
                etCurrentReading.setText(current.getPerusal());
            } else {
                etCurrentReading.setText("");
            }
        });
    }

    private void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScannerActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    private void writeFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            } else {
                exportForm();
            }
        } else {
            exportForm();
        }
    }

    private void exportForm() {
        String header = "idst, NAMEID, Perusallast, Perusalfirst, idsttype, Consumption, NOTE, MONTH/YEAR";
        String fileName = "BahlaDiamond";
        String csv = (
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + File.separator + fileName + " " + getDate() + ".csv"
        );

        Log.d("exportForm: ", csv);

        // Write Byte Order Mark (BOM) for UTF-8 at the start
        OutputStream os = null;
        try {
            os = new FileOutputStream(csv);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            os.write(239);
            os.write(187);
            os.write(191);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write csv file header
        PrintWriter w = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        w.println(header);

        // Write csv file contents line by line
        for (Previous previous : previousReadings) {
            String currentPerusal = "0";
            String note = "-";
            for (Current current : currentReadings) {
                if (previous.getIdst().equals(current.getIdst())) {
                    currentPerusal = current.getPerusal();
                    note = current.getNote();
                    break;
                }
            }

            String record = Arrays.asList(
                    previous.getIdst(), previous.getNameId(), previous.getReading(),
                    currentPerusal, previous.getIdstType(), previous.getConsumption(),
                    note, previous.getDateDo()).toString();

            w.println(record.substring(1, record.length() - 1));
        }
        w.flush();
        w.close();

        if (!csv.isEmpty()) {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    "File saved",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    /***************************************** Button *********************************************/

    private class SaveButtonClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            saveForm();
            clearForm();
            Toast.makeText(
                    MainActivity.this, tvIdst.getText().toString() + " تم الحفظ",
                    Toast.LENGTH_LONG).show();
        }

        private void saveForm() {
            String note = etNotes.getText().toString().trim();
            if (note.isEmpty()) {
                note = "-";
            }
            Current newPrevious = new Current(
                    tvIdst.getText().toString(),
                    etCurrentReading.getText().toString(),
                    note
            );
            currentViewModel.insert(newPrevious);
        }

        private void clearForm() {
            tvIdst.setText("");
            tvName.setText("");
            tvPreviousReading.setText("");
            etNotes.setText("");
            setCurrentReading("NULL");
        }
    }

    /*************************************** TextWatcher ******************************************/

    private void setTextWatcher() {
        tvIdst.addTextChangedListener(generalTextWatcher);
        etCurrentReading.addTextChangedListener(generalTextWatcher);
        tvIdst.addTextChangedListener(idTextWatcher);
//        etNotes.addTextChangedListener(generalTextWatcher);
    }

    private final TextWatcher generalTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            enableButton();
        }

        private void enableButton() {
            String id = tvIdst.getText().toString().trim();
            String currentReading = etCurrentReading.getText().toString().trim();

            boolean isEmpty = !id.isEmpty() && !currentReading.isEmpty();
            btnSave.setEnabled(isEmpty);
        }
    };

    private final TextWatcher idTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String clientId = tvIdst.getText().toString().trim();
            setClientName(clientId);
            setPreviousReading(clientId);
            setCurrentReading(clientId);
        }
    };

}