package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FormViewModel extends AndroidViewModel {

    private final FormRepository formRepository;

    public FormViewModel(Application app){
        super(app);
        formRepository  = new FormRepository(app);
    }

    public LiveData<List<Form>> getAllFormData(String date){
        return  formRepository.getAllDataByDate(date);
    }

    public void insert(Form form){formRepository.insert(form);}

    public LiveData<Form> getPreviousReadingById(String id) {
        return formRepository.getPreviousReadingById(id);
    }

    public LiveData<List<Form>> getListPrevious(String date) {
        return formRepository.getListPrevious(date);
    }

    public LiveData<Form> getPreviousPerusal(String id, String date) {
        return formRepository.getPreviousPerusal(id, date);
    }

    public LiveData<List<Form>> getAll() {
        return formRepository.getAll();
    }

    public void deleteAll() {
        formRepository.deleteAll();
    }

}
