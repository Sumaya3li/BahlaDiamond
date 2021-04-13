package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FormDao {

    @Insert
    void insert(Form form);

    @Query("SELECT * from data ORDER BY id")
    LiveData<List<Form>> getAllData();

    @Query("SELECT * FROM data ORDER BY id DESC LIMIT 1")
    LiveData<Form> readPrevious();

    @Query("SELECT * FROM data WHERE idst = :id AND date_do = :date")
    LiveData<Form> getPrevious(String id, String date);
}
