package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FormViewModel extends AndroidViewModel {

    private FormRepository formRepository;
    private LiveData<Form> persualLast;

    public FormViewModel(Application app){
        super(app);
        formRepository  = new FormRepository(app);
        persualLast = formRepository.getPreviousReading();
    }

    public LiveData<List<Form>> getAllFormData(String date){
        return  formRepository.getAllDataByDate(date);
    }

    public void insert(Form form){formRepository.insert(form);}

    /**
     * Get last persual
     */
    public LiveData<Form> getLastPersual() {
        return persualLast;
    }

    public LiveData<Form> getPrevious(String id, String date){
        return formRepository.getPrevious(id,date);
    }
}
