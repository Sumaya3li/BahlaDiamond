package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PreviousRepository {

    private final PreviousDao previousDao;
    private final LiveData<List<Previous>> allReadings;

    public PreviousRepository(Application app) {
        DatabaseHelper db = DatabaseHelper.getDatabase(app);
        previousDao = db.previousDao();
        allReadings = previousDao.getAllClients();
    }

    void insert(Previous client) {
        DatabaseHelper.databaseWriteExecutor.execute(()-> previousDao.insert(client));
    }

    public LiveData<List<Previous>> getAllReadings() {
        return allReadings;
    }

    public LiveData<Previous> getClientById(String id) {
        return previousDao.getClientById(id);
    }

    public int getRecordCount() {
        return previousDao.getRecordCount();
    }

    public void insertAll(List<Previous> previousList) {
        DatabaseHelper.databaseWriteExecutor.execute(()-> previousDao.insertAll(previousList));
    }
}
