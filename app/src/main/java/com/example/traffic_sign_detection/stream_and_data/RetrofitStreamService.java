package com.example.traffic_sign_detection.stream_and_data;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitStreamService {
    @GET("/predictions/last/image")
    Observable<Integer> getLastPredictionImageURL();
}
