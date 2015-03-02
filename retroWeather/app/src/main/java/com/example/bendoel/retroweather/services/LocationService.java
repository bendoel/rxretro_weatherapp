package com.example.bendoel.retroweather.services;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by dio on 3/1/15.
 */
public class LocationService {
    private final LocationManager mLocationManager;

    public LocationService(LocationManager locationManager){
        mLocationManager = locationManager;
    }

    public Observable<Location> getLocation(){
        return Observable.create(new Observable.OnSubscribe<Location>(){

            @Override
            public void call(final Subscriber<? super Location> subscriber) {

                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        subscriber.onNext(location);
                        subscriber.onCompleted();

                        Looper.myLooper().quit();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };

                Criteria locationCriteria = new Criteria();
                locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
                locationCriteria.setPowerRequirement(Criteria.POWER_LOW);

                String locationProvider = mLocationManager.getBestProvider(locationCriteria, true);

                Looper.prepare();

                mLocationManager.requestSingleUpdate(locationProvider,
                        locationListener, Looper.myLooper());

                Looper.loop();
            }
        });
    }

}
