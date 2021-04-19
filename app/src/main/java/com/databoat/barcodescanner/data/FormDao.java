package com.databoat.barcodescanner.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FormDao {

    @Insert
    void insert(Form form);

    @Query("SELECT * from data WHERE date_do = :date GROUP BY idst,date_do ORDER BY id")
    LiveData<List<Form>> getDataByDate(String date);

    @Query("SELECT * FROM data ORDER BY id DESC LIMIT 1")
    LiveData<Form> readPrevious();

    @Query("SELECT * FROM data WHERE idst = :idst ORDER BY id DESC LIMIT 1")
    LiveData<Form> getLastReading(String idst);

    @Query("SELECT * FROM data WHERE date_do = :date")
    LiveData<List<Form>> getListPrevious(String date);

}
