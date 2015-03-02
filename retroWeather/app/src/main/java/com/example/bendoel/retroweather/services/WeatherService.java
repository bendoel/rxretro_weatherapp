package com.example.bendoel.retroweather.services;

import com.example.bendoel.retroweather.models.CurrentWeather;
import com.example.bendoel.retroweather.models.WeatherForecast;
import com.google.gson.annotations.SerializedName;

import org.apache.http.HttpException;

import java.util.ArrayList;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by dio on 3/1/15.
 */
public class WeatherService {

    private static final String WEB_SERVICE_BASE_URL = "http://api.openweathermap.org/data/2.5";
    private final OpenWeatherMapWebService mWebService;

    public WeatherService(){
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WEB_SERVICE_BASE_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mWebService = restAdapter.create(OpenWeatherMapWebService.class);

    }


    //    using retrofit turn your REST API into a Java Interface
    private interface OpenWeatherMapWebService {
        @GET("/weather?units=metric")
        Observable<CurrentWeatherDataEnvelope> fetchCurrentWeather(@Query("lon") double longitude,
                                                                   @Query("lat") double latitude);

        @GET("/forecast/daily?units=metric&cnt=7")
        Observable<WeatherForecastDataEnvelope> fetchWeatherForecast(@Query("lon") double longitude,
                                                                     @Query("lat") double latitude);
    }

    public Observable<CurrentWeather> fetchCurrentWeather(final double longitude,
                                                          final double latitude){
     return mWebService.fetchCurrentWeather(longitude, latitude)
             .flatMap(new Func1<CurrentWeatherDataEnvelope,
                     Observable<? extends CurrentWeatherDataEnvelope>>() {

//                 error out if the request was unsuccesful
                 @Override
                 public Observable<? extends CurrentWeatherDataEnvelope> call(CurrentWeatherDataEnvelope data) {
                     return data.filterWebService();
                 }
             }).map(new Func1<CurrentWeatherDataEnvelope, CurrentWeather>() {

//                 parse the result and build a CurrentWeather Data object
                 @Override
                 public CurrentWeather call(CurrentWeatherDataEnvelope data) {
                     return new CurrentWeather(data.locationName, data.timestamp,
                             data.weather.get(0).description, data.main.temp,
                             data.main.temp_min, data.main.temp_max);
                 }
             });
    }

    public Observable<List<WeatherForecast>> fetchWeatherForecast(final double longitude,
                                                                  final double latitude){
        return mWebService.fetchWeatherForecast(longitude, latitude)
                .flatMap(new Func1<WeatherForecastDataEnvelope,
                        Observable<? extends WeatherForecastDataEnvelope>>() {

//                    error out if request was unsuccesful

                    @Override
                    public Observable<? extends WeatherForecastDataEnvelope> call(WeatherForecastDataEnvelope listData) {
                        return listData.filterWebService();
                    }
                }).map(new Func1<WeatherForecastDataEnvelope, List<WeatherForecast>>() {

//                    parse the result a build list of WeatherForecast object
                    @Override
                    public List<WeatherForecast> call(WeatherForecastDataEnvelope listData) {
                        final ArrayList<WeatherForecast> weatherForecasts = new ArrayList<WeatherForecast>();

                        for (WeatherForecastDataEnvelope.ForecastDataEnvelope data :listData.list){
                            final WeatherForecast weatherForecast = new WeatherForecast(
                                    listData.city.name, data.timestamp, data.weather.get(0).description,
                                    data.temp.min, data.temp.max );
                            weatherForecasts.add(weatherForecast);
                        }
                        return weatherForecasts;
                    }
                });

    }



    //    Create Base class for result returned by the weather web services

    public class WeatherDataEnvelope{
        @SerializedName("cod")
        private int httpcode;

        class Weather{
            public String description;
        }

        public Observable filterWebService(){
            if(httpcode == 200){
                return Observable.just(this);
            } else {
                return Observable.error(
                        new HttpException(("The was a problem fetching the weather data"))
                );
            }
        }

    }

    // create data structure for current weather result returned by the web services

    private class CurrentWeatherDataEnvelope extends WeatherDataEnvelope{
        @SerializedName("name")
        public String locationName;
        @SerializedName("dt")
        public long timestamp;
        public ArrayList<Weather> weather;

        public Main main;

        class Main {
            public float temp;
            public float temp_min;
            public float temp_max;
        }
    }

    //    create data structure for weather forecast result returned by the web services
    private class WeatherForecastDataEnvelope extends WeatherDataEnvelope{
        public Location city;
        public ArrayList<ForecastDataEnvelope> list;

        class Location{
            public String name;
        }

        class ForecastDataEnvelope{
            @SerializedName("dt")
            public long timestamp;
            public Temperature temp;
            public ArrayList<Weather> weather;
        }

        class Temperature{
            public float min;
            public float max;
        }
    }
}
