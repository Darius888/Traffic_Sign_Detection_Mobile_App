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

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout registerEmailInputLayout;
    private TextInputLayout registerNewPasswordInputLayout;
    private TextInputLayout registerRepeatNewPasswordInputLayout;


    private TextInputEditText registerEmailInputEditText;
    private TextInputEditText registerNewPasswordInputEditText;
    private TextInputEditText registerRepeatNewPasswordInputEditText;

    private MaterialButton registerMaterialButton;

    private Intent streamAndDataIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        register();
    }

    public void initViews()
    {
        registerEmailInputLayout = findViewById(R.id.register_email_layout);
        registerNewPasswordInputLayout = findViewById(R.id.register_new_password_layout);
        registerRepeatNewPasswordInputLayout = findViewById(R.id.register_repeat_new_password_layout);

        registerEmailInputEditText = findViewById(R.id.register_email_edit);
        registerNewPasswordInputEditText = findViewById(R.id.register_new_password_edit);
        registerRepeatNewPasswordInputEditText = findViewById(R.id.register_repeat_new_password_edit);

        registerMaterialButton = findViewById(R.id.register_register);

        streamAndDataIntent = new Intent(this, StreamAndDataActivity.class);
    }

    public void register()
    {
        registerMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(streamAndDataIntent);
            }
        });
    }
}
