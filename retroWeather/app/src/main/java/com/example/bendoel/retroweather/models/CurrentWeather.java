package com.example.bendoel.retroweather.models;

/**
 * Created by dio on 3/1/15.
 */
public class CurrentWeather extends WeatherForecast {
    private final float mTemperature;

    public CurrentWeather(final String locationName,
                          final long timestamp,
                          final String description,
                          final float minimum,
                          final float maximum,
                          final float temperature){

        super(locationName, timestamp, description, minimum, maximum);
        this.mTemperature = temperature;
    }

    public float getTemperature() {
        return mTemperature;
    }
}
