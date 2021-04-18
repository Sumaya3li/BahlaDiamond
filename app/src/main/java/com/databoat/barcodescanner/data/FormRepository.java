package com.databoat.barcodescanner.data;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.io.FileOutputStream;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FormRepository {

    private FormDao formdoa;
    private LiveData<Form> lastReading;

    FormRepository(Application app){
        DatabaseHelper db = DatabaseHelper.getDatabase(app);
        formdoa = db.formDao();
        lastReading = formdoa.readPrevious();
    }

    void insert(Form form){
        DatabaseHelper.databaseWriteExecutor.execute(()->{
            formdoa.insert(form);
        });
    }

    public LiveData<List<Form>> getAllDataByDate(String date){
        return formdoa.getDataByDate(date);
    }

    public LiveData<Form> getPreviousReading(){ return lastReading;}

    public LiveData<Form> getPrevious(String id, String date){
        return formdoa.getPrevious(id,date);
    }

    public void updateDuplicate(Form form) {
        DatabaseHelper.databaseWriteExecutor.execute(()-> {
            formdoa.update(form);
            Log.d("updateDuplicate: ", form.getPerusal_current());
        });
    }

    public LiveData<Form> getPreviousReadingById(String id) {
        return formdoa.getLastReading(id);
    }

    public LiveData<List<Form>> getListPrevious(String date) {
        return formdoa.getListPrevious(date);
    }

//    public LiveData<Form> getUpdateForm(String idst, String perusal_previous, String perusalCurrent, String note, String date){
//        return formdoa.updateEntry(idst,perusal_previous,perusalCurrent,note,date);
//    }
}
