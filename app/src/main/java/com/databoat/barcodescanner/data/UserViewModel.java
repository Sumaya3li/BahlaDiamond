package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private final LiveData<List<User>> allUsers;

    public UserViewModel(Application app){
        super(app);
        userRepository  = new UserRepository(app);
        allUsers = userRepository.getUsers();
    }

    public void insert(User user) {
        userRepository.insert(user);
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
}
