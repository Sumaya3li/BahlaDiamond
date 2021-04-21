package com.databoat.barcodescanner.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.databoat.barcodescanner.R;
import com.databoat.barcodescanner.data.Current;
import com.databoat.barcodescanner.data.Previous;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyCsvHelper {

    public static List<Previous> importPreviousReadings(Context context, int filename) {
        List<Previous> readings = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(filename);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split(",");

                Previous client = new Previous(
                        tokens[0], tokens[1], tokens[2], tokens[5],
                        tokens[6], tokens[7], getDate());
                readings.add(client);
            }
        } catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }
        readings.remove(0);
        return readings;
    }

    public static String getDate() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(
                "MM-yyyy",
                Resources.getSystem().getConfiguration().locale
        );
        return simpleDateFormat.format(new Date());
    }
}
