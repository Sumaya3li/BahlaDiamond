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
import com.databoat.barcodescanner.util.AdminHelper;
import com.databoat.barcodescanner.util.SaveSharedPreference;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginActivity extends AppCompatActivity {

    private int userRecordCount;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private ConstraintLayout rootLayout;
    private UserViewModel userViewModel;

    public static final String PREF_KEY = "LOGIN_KEY";
    public static final String USER_KEY = "USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        btnLogin.setOnClickListener(new LogInButtonClicked());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setSharedPreferences();
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

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        rootLayout = findViewById(R.id.root_login_activity);
    }

    private void setSharedPreferences() {
        // Check if UserResponse is Already Logged In
        if (SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            rootLayout.setVisibility(View.VISIBLE);
            updateUsers();
            setUserRecordCount();
        }
    }

    private void setUserRecordCount() {
        userViewModel.getRecordCount().observe(this, integer -> {
            userRecordCount = integer;
            Log.d("MainActivity: ", "record Count: " + userRecordCount);
        });
    }

    private void updateUsers() {
        List<User> userList = AdminHelper.editUsers();
        userViewModel.insertAllUsers(userList);
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
                        SharedPreferences preferencesPut = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferencesPut.edit();
                        editor.putString(USER_KEY, username);
                        editor.apply();

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

}