package com.databoat.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        User user = new User("admin","12345");
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

    private class LogInButtonClicked implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String username = etUsername.getText().toString().toLowerCase().trim();
            userViewModel.getUserByName(username).observe(LoginActivity.this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        if (user.getPassword().equals(etPassword.getText().toString())) {
                            Intent send = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(send);
                            finish();
                        }
                    }
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            "Make sure username and password are correct",
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }

    }
}