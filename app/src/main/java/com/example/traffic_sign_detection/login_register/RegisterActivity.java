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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

                String userEmail = registerEmailInputEditText.getText().toString();
                String userNewPassword = registerNewPasswordInputEditText.getText().toString();
                String userRepeatNewPassword = registerRepeatNewPasswordInputEditText.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.126:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                UserModel userModel = new UserModel(userEmail, userRepeatNewPassword);

                LoginAndRegisterRetrofitInterface service = retrofit.create(LoginAndRegisterRetrofitInterface.class);

                Call<UserModel> registration = service.registerUser(userModel);



                registration.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        System.out.println("VO TEP VA" + response.body().getUserEmail());
                        String value=response.body().getUserEmail();

                        if(!value.isEmpty())
                        {
                            startActivity(streamAndDataIntent);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                    }
                });

            }
        });
    }



}
