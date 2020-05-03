package com.example.traffic_sign_detection.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import com.example.traffic_sign_detection.R;
import com.example.traffic_sign_detection.stream_and_data.StreamAndDataActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import lombok.SneakyThrows;
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
    private Boolean checkYoSelf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        if(!(getIntent().getExtras() == null))
        {
            String value = getIntent().getExtras().getString("email");
            loginEmailInputEditText.setText(value);
            login();

        } else {
            loginEmailInputEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!(isValidEmail(s))) {
                        loginEmailInputEditText.setError("Incorrect email");
                        loginEmailInputLayout.setErrorContentDescription("Email is invalid");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!(isValidEmail(s))) {
                        checkYoSelf = false;
                    }
                }
            });

            loginPasswordInputEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (loginPasswordInputEditText.getText().toString().isEmpty()) {
                        loginPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
                        loginPasswordInputEditText.setError("Cannot be empty");
                        loginPasswordInputLayout.setErrorContentDescription("Cannot be empty");
                    } else {
                        loginPasswordInputLayout.setPasswordVisibilityToggleEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if ((loginPasswordInputEditText.getText().toString().isEmpty())) {
                        checkYoSelf = false;
                    }
                }
            });


            login();
        }

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

                if (checkInputFields()) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://78.56.203.39:8070")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    UserModelLogin userModelLogin = new UserModelLogin(userEmail, userPassword, "");

                    LoginAndRegisterRetrofitInterface service = retrofit.create(LoginAndRegisterRetrofitInterface.class);

                    Call<UserModelLogin> login = service.authenticateUser(userModelLogin);


                    login.enqueue(new Callback<UserModelLogin>() {
                        @SneakyThrows
                        @Override
                        public void onResponse(Call<UserModelLogin> call, Response<UserModelLogin> response) {

//                        JSONObject tokenJson = new JSONObject(response.body().getToken());
                            System.out.println("VO TEP VA" + response.body().getToken());

                            if (!(response.body() == null)) {
                                startActivity(streamAndDataIntent);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModelLogin> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    public Boolean checkInputFields()
    {
        Boolean validInput = true;


        if((loginEmailInputEditText.getText().toString().isEmpty())||(loginPasswordInputEditText.getText().toString().isEmpty()))
        {
            if((loginEmailInputEditText.getText().toString().isEmpty()))
            {
                loginEmailInputEditText.setError("Cannot be empty");
                loginEmailInputLayout.setErrorContentDescription("Cannot be empty");
                validInput = false;
            } if ((loginPasswordInputEditText.getText().toString().isEmpty()))
            {
                loginPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
                loginPasswordInputEditText.setError("Cannot be empty");
                loginPasswordInputLayout.setErrorContentDescription("Cannot be empty");
                validInput = false;
            } if ((isValidEmail(loginEmailInputEditText.getText().toString())))
            {
                loginEmailInputEditText.setError("Incorrect email");
                loginEmailInputLayout.setErrorContentDescription("Email is invalid");
                validInput = false;
            }
        }
        return validInput;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
