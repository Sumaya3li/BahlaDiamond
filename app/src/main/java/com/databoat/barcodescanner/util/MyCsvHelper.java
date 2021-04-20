package com.databoat.barcodescanner.util;

import android.content.Context;
import android.util.Log;

import com.databoat.barcodescanner.R;
import com.databoat.barcodescanner.data.Client;
import com.databoat.barcodescanner.data.Form;

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

                Client client = new Client(tokens[0], tokens[1],tokens[5]);
                clientList.add(client);

                Log.d("MyCsvHelper", "User" + client.getName());
            }
        } catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }
        return clientList;
    }

    public static List<String> getClientIds(Context context) {
        List<String> clientIds = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(R.raw.mobile);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split(",");

                clientIds.add(tokens[0]);
            }
        } catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }
        clientIds.remove(0);
        return clientIds;
    }

    public static List<ClientHelper> getClientInfo(Context context) {
        List<ClientHelper> clientHelperList = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(R.raw.mobile);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split(",");

                ClientHelper helper = new ClientHelper(tokens[0], tokens[1], tokens[2]);
                clientHelperList.add(helper);
            }
        } catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }
        clientHelperList.remove(0);
        return clientHelperList;
    }
}
