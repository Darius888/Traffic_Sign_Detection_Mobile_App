package com.example.traffic_sign_detection.stream_and_data;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitStreamService {
    @GET("/findlastinsertimgurl")
    Call<JsonObject> getLastPredictionImageURL();
}
