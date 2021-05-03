package com.databoat.barcodescanner.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.databoat.barcodescanner.R;
import com.databoat.barcodescanner.data.Previous;
import com.databoat.barcodescanner.data.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminHelper {

    // Change filename
    private static final int FILENAME = R.raw.mobile;

    public static List<User> editUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("admin","12345"));
        userList.add(new User("admin1","12345"));
        userList.add(new User("admin2","12345"));
        userList.add(new User("admin3","12345"));
        userList.add(new User("admin4","12345"));
        userList.add(new User("admin5","12345"));
        return userList;
    }

    public static List<Previous> importReadings(Context context) {
        List<Previous> readings = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(FILENAME);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line = "";

        try {
            while ((line = br.readLine()) != null) {
                // Split the csv file into different tokens.
                String[] tokens = line.split(",");

                // Ensure that the column attributes are in the correct order.
                Previous client = new Previous(
                        tokens[0], tokens[1], tokens[2], tokens[5],
                        tokens[6], tokens[7]);
                readings.add(client);
            }
        } catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }
        readings.remove(0);
        return readings;
    }

    public static String getDate(boolean isDash) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "MM-yyyy",
                Resources.getSystem().getConfiguration().locale
        );
        String date = simpleDateFormat.format(new Date());
        return isDash ? date : date.replace("-", "");
    }

    public static String getDateTime() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
