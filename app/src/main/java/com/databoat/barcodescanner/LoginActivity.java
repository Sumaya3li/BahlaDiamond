package com.databoat.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.databoat.barcodescanner.data.User;
import com.databoat.barcodescanner.data.UserViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private UserViewModel userViewModel;

    private List<User> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, this::setUsersList);
        User user = new User("sahar","12345");
        userViewModel.insert(user);

        btnLogin.setOnClickListener(new LogInButtonClicked());
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

    private void setUsersList(List<User> users) {
        usersList = users;
    }

    private class LogInButtonClicked implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (isUserValid()) {
                Intent send = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(send);
                finish();
            } else {
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "Make sure username and password are correct",
                        Snackbar.LENGTH_LONG).show();
            }
        }

        private boolean isUserValid() {
            for (User user : usersList) {
                if (user.getName().equals(etUsername.getText().toString()) &&
                    user.getPassword().equals(etPassword.getText().toString())) {
                    return true;
                }
            }
            return false;
        }
    }
}