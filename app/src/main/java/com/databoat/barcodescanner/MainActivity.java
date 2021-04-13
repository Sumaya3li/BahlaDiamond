package com.databoat.barcodescanner;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.databoat.barcodescanner.data.Client;
import com.databoat.barcodescanner.data.ClientViewModel;
import com.databoat.barcodescanner.data.Form;
import com.databoat.barcodescanner.data.FormViewModel;
import com.databoat.barcodescanner.util.MyCsvHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnScan;

    private TextView tvIdts;
    private TextView tvName;
    private TextView tvPreviousReading;
    private EditText etCurrentReading;
    private EditText etNotes;
    private Button btnSave;
    private Button btnExport;
    private int recordCount;
    private String currentIdstType;
    private String currentClientId;

    private FormViewModel formViewModel;
    private ClientViewModel clientViewModel;

    private List<Form> currentFormData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initViews();

        // Set button disabled initially
        btnSave.setEnabled(false);
        setTextWatcher();

        btnScan.setOnClickListener(v -> scanBarcode());
        btnSave.setOnClickListener(v -> saveForm());
        btnExport.setOnClickListener(v -> exportForm());

        formViewModel = new ViewModelProvider(this).get(FormViewModel.class);
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);

        setRecordCount();
        insertClients();
        getFormData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                currentClientId = result.getContents().trim();
                tvIdts.setText(currentClientId);
                clientViewModel.getClientByIdst(currentClientId).observe(this, new Observer<Client>() {
                    @Override
                    public void onChanged(Client client) {
                        tvName.setText(client.getName());
                        currentIdstType = client.getIdst_type();
                    }
                });
                formViewModel.getPrevious(currentClientId, getDate(false)).observe(this, new Observer<Form>() {
                    @Override
                    public void onChanged(Form form) {
                        if (form != null) {
                            Log.d("CLIENT CURRENT ", form.getPerusal_current());
                            Log.d("CLIENT PREVIOUS ", form.getPerusal_previous());
                            tvPreviousReading.setText(form.getPerusal_current());
                        }
                    }
                });
            } else {
                Toast.makeText(this, "No Result", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /****************************************** HELPER ********************************************/

    private void initViews() {
        tvIdts = findViewById(R.id.tv_idts_value);
        tvName = findViewById(R.id.tv_name_value);
        tvPreviousReading = findViewById(R.id.tv_previous_reading_value);
        etCurrentReading = findViewById(R.id.et_current_reading);
        etNotes = findViewById(R.id.et_notes);
        btnSave = findViewById(R.id.btn_save);
        btnScan = findViewById(R.id.btn_scan);
        btnExport = findViewById(R.id.btn_export);
    }

    private void setRecordCount() {
        clientViewModel.getRecordCount().observe(this, integer -> {
            recordCount = integer;
            Log.d("MainActivity: ", "record Count: " + recordCount);
        });
    }

    private void insertClients() {
        if (recordCount == 0) {
            List<Client> clients = MyCsvHelper.importClients(this);
            for (Client client : clients) {
                clientViewModel.insert(client);
            }
        }
    }

    private void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScannerActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    private void getFormData() {
        formViewModel.getAllFormData(getDate(true)).observe(this,
                formList -> currentFormData = formList
        );
    }

    private String getDate(boolean isCurrent) {
        Calendar now = Calendar.getInstance();
        if (!isCurrent) {
            return String.valueOf(now.get(Calendar.HOUR_OF_DAY) - 1);
        }
        return String.valueOf(now.get(Calendar.HOUR_OF_DAY));
    }

    private void saveForm() {
        for (Form form : currentFormData) {
            if (form.getIdst().equals(tvIdts.getText().toString().trim()) && form.getDate_do().equals(getDate(true))) {
                formViewModel.updateDuplicate(new Form(
                        tvIdts.getText().toString(),
                        tvName.getText().toString(),
                        tvPreviousReading.getText().toString(),
                        etCurrentReading.getText().toString(),
                        currentIdstType,
                        "0",
                        etNotes.getText().toString(),
                        getDate(true))
                );
            }
        }
        Form newForm = new Form(
                tvIdts.getText().toString(),
                tvName.getText().toString(),
                tvPreviousReading.getText().toString(),
                etCurrentReading.getText().toString(),
                currentIdstType,
                "0",
                etNotes.getText().toString(),
                getDate(true)
        );
        formViewModel.insert(newForm);
    }

    private void exportForm() {
        String[] header = {"idst", "name", "perusalLast","perusalFirst","idst_type", "consumption", "note","month/year"};
//        SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
        String fileName = "BahlaDiamond";

        String csv = (
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + File.separator + fileName + "-" + getDate(true) + ".csv"
        );
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(csv));
            List<String[]> data = new ArrayList<>();
            data.add(header);
            for (Form form : currentFormData) {
                String[] line = {
                        form.getIdst(), form.getName_id(), form.getPerusal_previous(),
                        form.getPerusal_current(), form.getIdst_type(),
                        form.getConsumption(), form.getNote(), form.getDate_do()
                };
                data.add(line);
            }
            writer.writeAll(data);
            if (!csv.isEmpty()) {
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "File saved",
                        Snackbar.LENGTH_LONG).show();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*************************************** TextWatcher ******************************************/

    private void setTextWatcher() {
        tvIdts.addTextChangedListener(generalTextWatcher);
        etCurrentReading.addTextChangedListener(generalTextWatcher);
        etNotes.addTextChangedListener(generalTextWatcher);
    }

    private TextWatcher generalTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            enableButton();
        }

        private void enableButton() {
            String id = tvIdts.getText().toString().trim();
            String currentReading = etCurrentReading.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();

            boolean isEmpty = !id.isEmpty() && !currentReading.isEmpty() && !notes.isEmpty();
            btnSave.setEnabled(isEmpty);
        }
    };

//    private  String getDate() {
//        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(
//                "MM/yyyy",
//                Resources.getSystem().getConfiguration().locale
//        );
//        return simpleDateFormat.format(new Date());
//    }

//    private String getPreviousDate() {
//        Calendar now=Calendar.getInstance();
//        return String.valueOf(now.get(Calendar.HOUR_OF_DAY) - 1);
//    }
//
//    private String getDate() {
//        Calendar now=Calendar.getInstance();
//        return String.valueOf(now.get(Calendar.HOUR_OF_DAY));
//    }
}