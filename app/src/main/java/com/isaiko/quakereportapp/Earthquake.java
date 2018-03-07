package com.isaiko.quakereportapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * Created by ahmed on 3/6/2018.
 */

public class Earthquake {


    private Double mMagnitude;

    private String mLocation;



    private Long mTimeInMilliseconds;

    public Earthquake(Double magnitude, String location, long timeInMilliseconds) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
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

}
