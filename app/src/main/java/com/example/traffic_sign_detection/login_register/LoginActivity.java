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

                String userEmail = loginEmailInputEditText.getText().toString();
                String userPassword = loginPasswordInputEditText.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.126:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                UserModel userModel = new UserModel(userEmail, userPassword);

                LoginAndRegisterRetrofitInterface service = retrofit.create(LoginAndRegisterRetrofitInterface.class);

                Call<UserModel> login = service.authenticateUser(userModel);



                login.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        System.out.println("VO TEP VA" + response.body().getUserEmail());


                        if(!(response.body()==null))
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
