package com.example.traffic_sign_detection.stream_and_data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PredictionModel {

    @SerializedName("id")
    @Expose
    public long id;

    @SerializedName("predictionClassName")
    @Expose
    public String predictionClassName;

    @SerializedName("predictionProbability")
    @Expose
    public String predictionProbability;

    @SerializedName("timestamp")
    @Expose
    public String timestamp;

    @SerializedName("leftY")
    @Expose
    public String leftY;

    @SerializedName("topX")
    @Expose
    public String topX;

    @SerializedName("width")
    @Expose
    public String width;

    @SerializedName("height")
    @Expose
    public String height;


    public PredictionModel() {

    }

    public PredictionModel(String predictionClassName, String predictionProbability) {

    }

    public PredictionModel(long id, String predictionProbability, String predictionClassName) {

    }


}
