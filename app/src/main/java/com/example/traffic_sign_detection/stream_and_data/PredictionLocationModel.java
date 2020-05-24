package com.example.traffic_sign_detection.stream_and_data;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PredictionLocationModel {
    Double lng;
    Double lat;
    Long timeMillis;
    String predictionClassName;
    String predictionProbability;


    public PredictionLocationModel(Double lng, Double lat, String predictionClassName, String predictionProbability) {
        this.lng = lng;
        this.lat = lat;
        this.predictionClassName = predictionClassName;
        this.predictionProbability = predictionProbability;
        Date date = new Date();
        long timeMilli = date.getTime();
        this.timeMillis = timeMilli;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng() {
        this.lng = lng;
    }

    public Double getltd() {
        return lat;
    }

    public void setltd() {
        this.lat = lat;
    }

    public String getPredictionClassName() { return predictionClassName; }

    public void setPredictionClassName() { this.predictionClassName = predictionClassName; }

    public String getPredictionProbability() { return predictionProbability; }

    public void setPredictionProbability() { this.predictionProbability = predictionProbability; }

}
