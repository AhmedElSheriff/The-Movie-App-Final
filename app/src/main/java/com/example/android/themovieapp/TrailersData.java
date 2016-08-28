package com.example.android.themovieapp;

/**
 * Created by Abshafi on 8/14/2016.
 */
public class TrailersData {

    String trailerName, trailerURL;

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public String getTrailerURL() {
        return "www.youtube.com/watch?v=" + trailerURL;
    }

    @Override
    public String toString() {
        return trailerName;
    }
}
