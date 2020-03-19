package com.example.traffic_sign_detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.traffic_sign_detection.login_register.LoginActivity;
import com.example.traffic_sign_detection.login_register.RegisterActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

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

    private void initViews()
    {
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);

        loginIntent = new Intent(this, LoginActivity.class);
        registerIntent = new Intent(this, RegisterActivity.class);

    }


    private void login()
    {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
            }
        });
    }


    private void register()
    {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(registerIntent);
            }
        });
    }
}
