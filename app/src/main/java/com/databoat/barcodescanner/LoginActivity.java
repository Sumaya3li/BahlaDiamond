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
import androidx.lifecycle.ViewModelProvider;

import com.databoat.barcodescanner.data.User;
import com.databoat.barcodescanner.data.UserViewModel;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private int userRecordCount;
    private EditText etUsername;
    private EditText etPassword;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new LogInButtonClicked());
        SharedPreferences pref=getSharedPreferences("",MODE_PRIVATE);
        boolean isLoggedIn=pref.getBoolean("isLoggdIn",false);
//        SharedPreferences.Editor editor=getSharedPreferences("",MODE_PRIVATE).edit();
        if( isLoggedIn){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            return;
        }
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setUserRecordCount();
        insertUsers();
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
        if (userRecordCount == 0) {
            User user = new User("admin","12345");
            userViewModel.insert(user);
        }
    }

    private class LogInButtonClicked implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String username = etUsername.getText().toString().toLowerCase().trim();
            userViewModel.getUserByName(username).observe(LoginActivity.this, user -> {
                if (user != null) {
                    if (user.getPassword().equals(etPassword.getText().toString())) {
                        Intent send = new Intent(LoginActivity.this, MainActivity.class);
                        send.putExtra("finish",false);
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
}