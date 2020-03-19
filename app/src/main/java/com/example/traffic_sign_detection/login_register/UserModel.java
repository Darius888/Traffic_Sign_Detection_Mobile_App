package com.example.traffic_sign_detection.login_register;


import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserModel {

    @SerializedName("userEmail")
    private String userEmail;
    @SerializedName("userPassword")
    private String userPassword;

}
