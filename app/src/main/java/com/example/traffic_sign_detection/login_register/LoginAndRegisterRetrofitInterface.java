package com.example.traffic_sign_detection.login_register;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginAndRegisterRetrofitInterface {

    @POST("/register")
    Call<UserModel> registerUser(@Body UserModel userModel);

    @POST("/authenticate")
    Call<UserModel> authenticateUser(@Body UserModel userModel);

}
