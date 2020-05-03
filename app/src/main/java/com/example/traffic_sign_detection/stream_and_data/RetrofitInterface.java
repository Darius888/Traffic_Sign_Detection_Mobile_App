package com.example.traffic_sign_detection.stream_and_data;


import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {
    @GET("/predictions")
    Observable<List<PredictionModel>> getAllPredictionModels();
}