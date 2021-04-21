package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CurrentRepository {

    private CurrentDao currentDao;
    private LiveData<List<Current>> allReadings;

    public CurrentRepository(Application app) {
        DatabaseHelper db = DatabaseHelper.getDatabase(app);
        currentDao = db.currentDao();
        allReadings = currentDao.getAllClients();
    }

    void insert(Current client) {
        DatabaseHelper.databaseWriteExecutor.execute(() -> {
            currentDao.insert(client);
        });
    }

    public LiveData<List<Current>> getAllReadings() {
        return allReadings;
    }

    public LiveData<Current> getClientById(String id) {
        return currentDao.getClientById(id);
    }

    public int getRecordCount() {
        return currentDao.getRecordCount();
    }

    public void deleteAll() {
        DatabaseHelper.databaseWriteExecutor.execute(() -> {
            currentDao.deleteAll();
        });
    }

}
