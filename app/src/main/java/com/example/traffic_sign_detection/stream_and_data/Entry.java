package com.example.traffic_sign_detection.stream_and_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//@NoArgsConstructor
public class Entry {

    private String predictionClassName;
    private Float predictionProbability;

}
