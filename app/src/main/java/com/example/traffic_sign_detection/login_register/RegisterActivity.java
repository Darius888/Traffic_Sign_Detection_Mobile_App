package com.example.traffic_sign_detection.login_register;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;

import com.example.traffic_sign_detection.R;
import com.example.traffic_sign_detection.stream_and_data.StreamAndDataActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    private Intent loginIntent;

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

        registerEmailInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(isValidEmail(s)))
                {
                    registerEmailInputEditText.setError("Incorrect email");
                    registerEmailInputLayout.setErrorContentDescription("Email is invalid");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerNewPasswordInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(registerNewPasswordInputEditText.getText().toString().isEmpty())
                {
                    registerNewPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
                    registerNewPasswordInputEditText.setError("Cannot be empty");
                    registerNewPasswordInputLayout.setErrorContentDescription("Cannot be empty");
                } else
                {
                    registerNewPasswordInputLayout.setPasswordVisibilityToggleEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerRepeatNewPasswordInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(registerRepeatNewPasswordInputEditText.getText().toString().isEmpty())
                {
                    registerRepeatNewPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
                    registerRepeatNewPasswordInputEditText.setError("Cannot be empty");
                    registerRepeatNewPasswordInputLayout.setErrorContentDescription("Cannot be empty");
                } else
                {
                    registerRepeatNewPasswordInputLayout.setPasswordVisibilityToggleEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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

        loginIntent = new Intent(this, LoginActivity.class);
    }

    public void register()
    {
        registerMaterialButton.setOnClickListener(v -> {

            String userEmail = registerEmailInputEditText.getText().toString();
            String userNewPassword = registerNewPasswordInputEditText.getText().toString();
            String userRepeatNewPassword = registerRepeatNewPasswordInputEditText.getText().toString();


            if(checkInputFields()) {



                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://78.56.203.39:8070")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                UserRegisterResponse userModel = new UserRegisterResponse(userEmail, userRepeatNewPassword,"");

                LoginAndRegisterRetrofitInterface service = retrofit.create(LoginAndRegisterRetrofitInterface.class);

                Call<UserRegisterResponse> registration = service.registerUser(userModel);
                System.out.println("SSSSWASAWLAWLAWLA");


                registration.enqueue(new Callback<UserRegisterResponse>() {
                    @SneakyThrows
                    @Override
                    public void onResponse(Call<UserRegisterResponse> call, Response<UserRegisterResponse> response) {


                        if(response.body().getResponse().equals("User with such email already exists"))
                        {
                            registerEmailInputEditText.setError(response.body().getResponse());
                            registerEmailInputLayout.setErrorContentDescription(response.body().getResponse());
                        } else
                        {

                            loginIntent.putExtra("email", response.body().getUserEmail());
                            startActivity(loginIntent);
                        }


                    }

                    @Override
                    public void onFailure(Call<UserRegisterResponse> call, Throwable t) {
                                System.out.println("CIA KAZKAS NEGERAI" + t);
                    }
                });



            }
        });
    }

    public Boolean checkInputFields()
    {
        boolean validInput = true;


        if((registerEmailInputEditText.getText().toString().isEmpty())||(registerNewPasswordInputEditText.getText().toString().isEmpty())||(registerRepeatNewPasswordInputEditText.getText().toString().isEmpty()))
        {
            if((registerEmailInputEditText.getText().toString().isEmpty()))
            {
                registerEmailInputEditText.setError("Cannot be empty");
                registerEmailInputLayout.setErrorContentDescription("Cannot be empty");
                validInput = false;
            }
            if ((registerNewPasswordInputEditText.getText().toString().isEmpty()))
        {
            registerNewPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
            registerNewPasswordInputEditText.setError("Cannot be empty");
            registerNewPasswordInputLayout.setErrorContentDescription("Cannot be empty");
            validInput = false;
        }
            if (registerRepeatNewPasswordInputEditText.getText().toString().isEmpty())
        {
            registerRepeatNewPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
            registerRepeatNewPasswordInputEditText.setError("Incorrect email");
            registerRepeatNewPasswordInputLayout.setErrorContentDescription("Email is invalid");
            validInput = false;
        }
        }
        if(!(registerNewPasswordInputEditText.getText().toString().equals(registerRepeatNewPasswordInputEditText.getText().toString())))
        {
            registerRepeatNewPasswordInputEditText.setError("Passwords are not matching");
            registerRepeatNewPasswordInputLayout.setErrorContentDescription("Passwords are not matching");
            registerRepeatNewPasswordInputLayout.setErrorEnabled(true);
            registerRepeatNewPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
            validInput=false;
        }
        return validInput;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }





}
