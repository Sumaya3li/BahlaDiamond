package com.databoat.barcodescanner.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PreviousDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Previous client);

    @Query("SELECT * FROM previous WHERE idst = :clientId")
    LiveData<Previous> getClientById(String clientId);

    @Query("SELECT * FROM previous ORDER BY idst")
    LiveData<List<Previous>> getAllClients();

    @Query("SELECT COUNT(*) FROM previous")
    int getRecordCount();

    @Insert()
    void insertAll(List<Previous> previousList);
}
