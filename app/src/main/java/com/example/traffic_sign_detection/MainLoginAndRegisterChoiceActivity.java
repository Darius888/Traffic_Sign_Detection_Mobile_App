package com.example.traffic_sign_detection;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.traffic_sign_detection.login_register.LoginActivity;
import com.example.traffic_sign_detection.login_register.RegisterActivity;
import com.google.android.material.button.MaterialButton;

public class MainLoginAndRegisterChoiceActivity extends AppCompatActivity {

    private MaterialButton loginButton;
    private MaterialButton registerButton;
    private Intent loginIntent;
    private Intent registerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login_and_register_choice);

        initViews();
        login();
        register();

    }

    private void initViews() {
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);

        loginIntent = new Intent(this, LoginActivity.class);
        registerIntent = new Intent(this, RegisterActivity.class);

    }


    private void login() {
        loginButton.setOnClickListener(v -> startActivity(loginIntent));
    }


    private void register() {
        registerButton.setOnClickListener(v -> startActivity(registerIntent));
    }
}
