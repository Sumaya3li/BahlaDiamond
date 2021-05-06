package com.databoat.barcodescanner.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> users);

    @Query("SELECT * FROM users WHERE name = :username")
    LiveData<User> getUser(String username);

    @Query("SELECT COUNT(*) FROM users")
    LiveData<Integer> recordCount();

}
