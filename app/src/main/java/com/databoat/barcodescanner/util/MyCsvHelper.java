package com.databoat.barcodescanner.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.databoat.barcodescanner.R;
import com.databoat.barcodescanner.data.Current;
import com.databoat.barcodescanner.data.Previous;
import com.databoat.barcodescanner.data.User;

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

    public static final boolean UPDATE_USERS = false;

    public static List<Previous> importReadings(Context context, int filename) {
        List<Previous> readings = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line = "";

        try {
            while ((line = br.readLine()) != null) {
                // Split the csv file into different tokens.
                String[] tokens = line.split(",");

                // Ensure that the column attributes are in the correct order.
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

    public static User[] editUsers() {
        User userAdmin = new User("admin","12345");
        User user1 = new User("admin1","12345");
        User user2 = new User("admin2","12345");
        User user3 = new User("admin3","12345");
        User user4 = new User("admin4","12345");
        User user5 = new User("admin5","12345");
        return new User[]{userAdmin, user1, user2, user3, user4, user5};
    }
}
