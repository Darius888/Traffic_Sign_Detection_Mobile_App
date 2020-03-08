package com.example.traffic_sign_detection.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.traffic_sign_detection.R;
import com.example.traffic_sign_detection.stream_and_data.StreamAndDataActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout loginEmailInputLayout;
    private TextInputLayout loginPasswordInputLayout;

    private TextInputEditText loginEmailInputEditText;
    private TextInputEditText loginPasswordInputEditText;

    private MaterialButton loginMaterialButton;

    private Intent streamAndDataIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        login();

    }

    public void initViews()
    {
        loginEmailInputLayout = findViewById(R.id.login_email_layout);
        loginPasswordInputLayout = findViewById(R.id.login_password_layout);

        loginEmailInputEditText = findViewById(R.id.login_email_edit);
        loginPasswordInputEditText = findViewById(R.id.login_password_edit);

        loginMaterialButton = findViewById(R.id.login_login);

        streamAndDataIntent = new Intent(this, StreamAndDataActivity.class);
    }

    public void login()
    {
        loginMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(streamAndDataIntent);
            }
        });
    }
}
