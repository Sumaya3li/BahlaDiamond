package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class UserRepository {

    private UserDoa userdoa;

    UserRepository(Application app) {
        DatabaseHelper db = DatabaseHelper.getDatabase(app);
        userdoa = db.userDao();
    }

    public void insertAllUsers(List<User> userList) {
        DatabaseHelper.databaseWriteExecutor.execute(()->{
            userdoa.insertAll(userList);
        });
    }

    public LiveData<User> getUserByName(String username) {
        return userdoa.getUser(username);
    }

    public LiveData<Integer> getRecordCount() {
        return userdoa.recordCount();
    }
}