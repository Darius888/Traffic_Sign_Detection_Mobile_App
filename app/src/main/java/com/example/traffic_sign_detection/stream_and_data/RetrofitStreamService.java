package com.example.traffic_sign_detection.stream_and_data;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RetrofitStreamService {

    @GET("/predictions/last")
    Observable<PredictionModel> getLastPredictionData();

    @GET("/predictions/gps")
    Observable<List<PredictionLocationModel>> getPredictionLocations();

}
