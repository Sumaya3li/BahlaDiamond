package com.databoat.barcodescanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.databoat.barcodescanner.data.User;
import com.databoat.barcodescanner.data.UserViewModel;
import com.databoat.barcodescanner.util.SaveSharedPreference;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginActivity extends AppCompatActivity {

    private int userRecordCount;
    private EditText etUsername;
    private EditText etPassword;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ConstraintLayout rootLayout = findViewById(R.id.root_login_activity);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new LogInButtonClicked());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Check if UserResponse is Already Logged In
        if (SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            rootLayout.setVisibility(View.VISIBLE);
            setUserRecordCount();
            insertUsers();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    /****************************************** HELPER ********************************************/

    private void setUserRecordCount() {
        userViewModel.getRecordCount().observe(this, integer -> {
            userRecordCount = integer;
            Log.d("MainActivity: ", "record Count: " + userRecordCount);
        });
    }

    private void insertUsers() {
        if (userRecordCount < 5) {
            User user = new User("admin","12345");
            userViewModel.insert(user);
            registerUsers();
        }
    }

    /***************************************** Button *********************************************/

    private class LogInButtonClicked implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String username = etUsername.getText().toString().toLowerCase().trim();
            userViewModel.getUserByName(username).observe(LoginActivity.this, user -> {
                if (user != null) {
                    if (user.getPassword().equals(etPassword.getText().toString())) {
                        Intent send = new Intent(LoginActivity.this, MainActivity.class);

                        SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                        send.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(send);
                        finish();
                    }
                }
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "تأكد من البيانات المدخلة ",
                        Snackbar.LENGTH_LONG).show();
            });
        }

    }

    /**********************************************************************************************/
    /****************************************** ADMIN *********************************************/
    /**********************************************************************************************/

    private void registerUsers() {
        User user1 = new User("admin1","12345");
        User user2 = new User("admin2","12345");
        User user3 = new User("admin3","12345");
        User user4 = new User("admin4","12345");
        User user5 = new User("admin5","12345");

        User[] userList = { user1, user2, user3, user4, user5 };

        for (User user : userList) {
            userViewModel.insert(user);
        }
    }
}