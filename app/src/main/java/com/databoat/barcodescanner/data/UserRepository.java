package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class UserRepository {

    private UserDoa userdoa;
    private LiveData<List<User>> allUsers;

    UserRepository(Application app) {
        DatabaseHelper db = DatabaseHelper.getDatabase(app);
        userdoa = db.userDao();
        allUsers = userdoa.getAllUsers();
    }

    void insert(User user) {
        DatabaseHelper.databaseWriteExecutor.execute(() -> {
            userdoa.insert(user);
        });
    }

    public LiveData<List<User>> getUsers() {
        return allUsers;
    }

    public LiveData<User> getUserByName(String username) {
        return userdoa.getUser(username);
    }
}