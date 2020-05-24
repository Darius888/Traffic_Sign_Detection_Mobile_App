package com.example.traffic_sign_detection.stream_and_data;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @GET("/predictions")
    Observable<List<PredictionModel>> getAllPredictionModels();

    @POST("/predictions/gps")
    Call<PredictionLocationModel> postPredictionLocation(@Body PredictionLocationModel predictionLocationModel);



}