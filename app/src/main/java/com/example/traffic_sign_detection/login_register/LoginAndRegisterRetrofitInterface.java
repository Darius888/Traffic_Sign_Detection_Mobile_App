package com.example.traffic_sign_detection.login_register;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginAndRegisterRetrofitInterface {

    @POST("/register")
    Call<UserRegisterResponse> registerUser(@Body UserRegisterResponse userRegisterResponse);

    @POST("/authenticate")
    Call<UserModelLogin> authenticateUser(@Body UserModelLogin userModelLogin);

}
