package com.isaiko.quakereportapp;

/**
 * Created by ahmed on 3/6/2018.
 */

public class Earthquake {


    private String mMagnitude;

    private String mLocation;

    private String mDate;

    public Earthquake(String magnitude, String location, String date){
        this.mMagnitude = magnitude;
        this.mLocation = location;
        this.mDate = date;
    }

    public String getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmDate() {
        return mDate;
    }

}
