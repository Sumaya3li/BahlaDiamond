package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ClientRepository {

    private ClientDao clientDao;
    private LiveData<List<Client>> allclient;

    public ClientRepository(Application app) {
        DatabaseHelper db = DatabaseHelper.getDatabase(app);
        clientDao = db.clientDao();
        allclient = clientDao.getClient();
    }

    void insert(Client client) {
        DatabaseHelper.databaseWriteExecutor.execute(() -> {
            clientDao.insert(client);
        });
    }

    public LiveData<List<Client>> getAllClient() {
        return allclient;
    }

    public LiveData<Client> getClientById(String id) {
        return clientDao.getClientById(id);
    }

    public int getNumFiles() {
        return clientDao.getRecordCount();
    }
}
