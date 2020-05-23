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
    Double ltd;
    Long timeMillis;


    public Double getLng()
    {
        return lng;
    }


    public void setLng()
    {
        this.lng = lng;
    }

    public Double getltd()
    {
        return ltd;
    }

    public void setltd()
    {
        this.ltd = ltd;
    }

    public PredictionLocationModel(Double lng, Double ltd)
    {
        this.lng = lng;
        this.ltd = ltd;
        Date date = new Date();
        long timeMilli = date.getTime();
        this.timeMillis = timeMilli;
    }

}
