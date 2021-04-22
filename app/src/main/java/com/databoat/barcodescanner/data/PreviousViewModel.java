package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PreviousViewModel extends AndroidViewModel {

    private final PreviousRepository previousRepository;
    private final LiveData<List<Previous>> allReadings;

    public PreviousViewModel(@NonNull Application application) {
        super(application);
        previousRepository = new PreviousRepository(application);
        allReadings = previousRepository.getAllReadings();
    }

    public void insert(Previous client) {
        previousRepository.insert(client);
    }

    public LiveData<List<Previous>> getPreviousList() {
        return  allReadings;
    }

    public LiveData<Previous> getClientByIdst(String id) {
        return previousRepository.getClientById(id);
    }

    public void insertAll(List<Previous> previousList) {
        previousRepository.insertAll(previousList);
    }
}
