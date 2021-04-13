package com.databoat.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnScan;

    private TextView tvIdts;
    private TextView tvName;
    private TextView tvPreviousReading;
    private EditText etCurrentReading;
    private EditText etNotes;
    private Button btnSave;
    private Button btnExport;
    private List<Form> formList;
    private List<Client> clientList;
    private int recordCount;

    private FormViewModel formViewModel;
    private ClientViewModel clientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initViews();

        // Set button disabled initially
        btnSave.setEnabled(false);
        setTextWatcher();

        //Extracting the stored data from the bundle

        btnScan.setOnClickListener(v -> scanBarcode());
        btnSave.setOnClickListener(v -> saveForm());
        btnExport.setOnClickListener(v -> exportForm());

        formViewModel = new ViewModelProvider(this).get(FormViewModel.class);
        formViewModel.getAllForm().observe(this, this::setFormList);
        formViewModel.getLastPersual().observe(this, new Observer<Form>() {
            @Override
            public void onChanged(Form form) {
                if (form != null) {
                    tvPreviousReading.setText(form.getPerusal_current());
                }
            }
        });

        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
        clientViewModel.getRecordCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                recordCount = integer;
                Log.d("MainActivity: ", "record Count: " + recordCount);
            }
        });
        clientViewModel.getAllClient().observe(this, this::setClientList);
        insertClients();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                tvIdts.setText(result.getContents());
                setClientName(result.getContents());
            } else {
                Toast.makeText(this, "No Result", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /****************************************** HELPER ********************************************/

    private void setFormList(List<Form> form) {
        formList = form;
    }

    private void setClientList(List<Client> clients) {
        clientList = clients;
    }

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

    private void setTextWatcher() {
        tvIdts.addTextChangedListener(generalTextWatcher);
        etCurrentReading.addTextChangedListener(generalTextWatcher);
        etNotes.addTextChangedListener(generalTextWatcher);
    }

    private void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScannerActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
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

    void saveForm() {
        Form form = new Form(
                tvIdts.getText().toString(),
                tvName.getText().toString(),
                "0",
                etCurrentReading.getText().toString(),
                "0",
                "جالون"  ,
                "0",
                etNotes.getText().toString()
        );
        formViewModel.insert(form);
    }

    void insertClients() {
        if (recordCount == 0) {
            List<Client> clients = MyCsvHelper.importClients(this);
            for (Client client : clients) {
                clientViewModel.insert(client);
            }
        }
    }

    private void setClientName(String clientId) {

        for (Client client : clientList) {
            if (clientId.trim().equals(client.getIdts())) {
                Log.d("Clieat Name ():", client.getName());
                tvName.setText(client.getName());
            }
        }
    }

    void exportForm()  {
        String[] header = {"idst", "name", "perusal", "consumption", "note"};
//        SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
        String fileName = "BahlaDiamond";

        String csv = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName + ".csv");
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(csv));
            List<String[]> data = new ArrayList<>();
            data.add(header);
            for (Form form : formList) {
                String[] line = {
                        form.getIdst(), form.getName_id(), form.getPerusal_current(),
                        form.getConsumption(), form.getNote()
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
}