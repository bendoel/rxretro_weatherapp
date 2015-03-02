package com.example.bendoel.retroweather.models;

/**
 * Created by dio on 3/1/15.
 */
public class WeatherForecast {

    private final String mLocationName;
    private final long mTimeStamp;
    private final String mDescription;
    private final float mMinimumTemperature;
    private final float mMaximumTemperature;


    public WeatherForecast(final String mLocationName,
                           final long mTimeStamp,
                           final String mDescription,
                           final float mMinimumTemperature,
                           final float mMaximumTemperature){

        this.mLocationName = mLocationName;
        this.mTimeStamp = mTimeStamp;
        this.mDescription = mDescription;
        this.mMinimumTemperature = mMinimumTemperature;
        this.mMaximumTemperature = mMaximumTemperature;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public String getDescription() {
        return mDescription;
    }

    public float getMinimumTemperature() {
        return mMinimumTemperature;
    }

    public float getMaximumTemperature() {
        return mMaximumTemperature;
    }
}
