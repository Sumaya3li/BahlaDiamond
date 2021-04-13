package com.databoat.barcodescanner.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Client client);

    @Query("SELECT * FROM clients ")
    LiveData<List<Client>> getClient();

    @Query("SELECT COUNT(*) FROM clients")
    LiveData<Integer> recordCount();

    @Query("SELECT * FROM clients WHERE idts = :clientId")
    LiveData<Client> getClientById(String clientId);


}
