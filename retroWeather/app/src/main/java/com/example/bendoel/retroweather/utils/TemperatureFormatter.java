package com.example.bendoel.retroweather.utils;

/**
 * Created by dio on 3/1/15.
 */
public class TemperatureFormatter {
    public static String format(float temperature) {
        return String.valueOf(Math.round(temperature)) + "°";
    }
}
