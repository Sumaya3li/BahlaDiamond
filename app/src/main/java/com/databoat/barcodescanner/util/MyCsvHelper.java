package com.databoat.barcodescanner.util;

import android.content.Context;
import android.util.Log;

import com.databoat.barcodescanner.R;
import com.databoat.barcodescanner.data.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MyCsvHelper {

    public static List<Client> importClients(Context context) {
        List<Client> clientList = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(R.raw.mobile);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split(",");

                Client client = new Client(tokens[0], tokens[1],tokens[2]);
                clientList.add(client);

                Log.d("MyCsvHelper", "User" + client.getName());
            }
        } catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }
        return clientList;
    }
}
