package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FormRepository {

    private FormDao formdoa;

    private LiveData<List<Form>> allData;
    private LiveData<Form> lastReading;

    FormRepository(Application app){
        DatabaseHelper db = DatabaseHelper.getDatabase(app);
        formdoa = db.formDao();
        allData = formdoa.getAllData();
        lastReading = formdoa.readPrevious();
    }

    void insert(Form form){
        DatabaseHelper.databaseWriteExecutor.execute(()->{
            formdoa.insert(form);
        });
    }

    public LiveData<List<Form>> getAllData(){
        return  allData;
    }

    public LiveData<Form> getPreviousReading(){ return lastReading;}

    public LiveData<Form> getPrevious(String id, String date){
        return formdoa.getPrevious(id,date);
    }
}
