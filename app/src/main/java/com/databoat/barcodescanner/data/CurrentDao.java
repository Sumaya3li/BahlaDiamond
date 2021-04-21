package com.databoat.barcodescanner.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CurrentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Current client);

    @Query("SELECT * FROM current WHERE idst = :clientId")
    LiveData<Current> getClientById(String clientId);

    @Query("SELECT * FROM current")
    LiveData<List<Current>> getAllClients();

    @Query("SELECT COUNT(*) FROM current")
    int getRecordCount();

    @Query("DELETE FROM current")
    void deleteAll();
}
