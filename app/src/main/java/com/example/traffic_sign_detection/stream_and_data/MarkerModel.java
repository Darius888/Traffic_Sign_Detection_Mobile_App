package com.example.traffic_sign_detection.stream_and_data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MarkerModel {
    Double lattitude;
    Double longitude;
    String title;
    int drawable;

    public MarkerModel(Double lattitude, Double longitude, String title, int drawable) {
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.title = title;
        this.drawable = drawable;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }


}
