package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    public UserViewModel(Application app){
        super(app);
        userRepository  = new UserRepository(app);
    }

    public void insertAllUsers(List<User> userList) {
        userRepository.insertAllUsers(userList);
    }

    public LiveData<User> getUserByName(String name) {
        return userRepository.getUserByName(name);
    }

    public LiveData<Integer> getRecordCount() {
        return userRepository.getRecordCount();
    }

}
