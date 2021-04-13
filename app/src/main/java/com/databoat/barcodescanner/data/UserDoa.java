package com.databoat.barcodescanner.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users WHERE name = :username")
    LiveData<User> getUser(String username);
}
