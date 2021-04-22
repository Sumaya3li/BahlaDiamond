package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CurrentViewModel extends AndroidViewModel {

    private final CurrentRepository currentRepository;
    private final LiveData<List<Current>> allReadings;

    public CurrentViewModel(@NonNull Application application) {
        super(application);
        currentRepository = new CurrentRepository(application);
        allReadings = currentRepository.getAllReadings();
    }

    public void insert(Current client) {
        currentRepository.insert(client);
    }

    public LiveData<List<Current>> getCurrentList() {
        return  allReadings;
    }

    public LiveData<Current> getClientByIdst(String id) {
        return currentRepository.getClientById(id);
    }

}
