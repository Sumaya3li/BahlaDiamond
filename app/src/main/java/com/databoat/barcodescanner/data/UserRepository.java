package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class UserRepository {

    private final UserDao userDao;

    UserRepository(Application app) {
        DatabaseHelper db = DatabaseHelper.getDatabase(app);
        userDao = db.userDao();
    }

    public void insertAllUsers(List<User> userList) {
        DatabaseHelper.databaseWriteExecutor.execute(()-> userDao.insertAll(userList));
    }

    public LiveData<User> getUserByName(String username) {
        return userDao.getUser(username);
    }

    public LiveData<Integer> getRecordCount() {
        return userDao.recordCount();
    }
}