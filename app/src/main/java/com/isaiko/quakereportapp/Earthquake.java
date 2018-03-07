package com.isaiko.quakereportapp;


/**
 * Created by ahmed on 3/6/2018.
 */

public class Earthquake {


    private Double mMagnitude;

    private String mLocation;

    private String mURL;

    private Long mTimeInMilliseconds;

    public Earthquake(Double magnitude, String location, long timeInMilliseconds, String URL) {
        this.mMagnitude = magnitude;
        this.mLocation = location;
        this.mTimeInMilliseconds = timeInMilliseconds;
        this.mURL = URL;
    }

    public Double getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public Long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getmURL() {
        return mURL;
    }
}
