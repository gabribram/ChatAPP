package com.gabri.gpschat.utility;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.gabri.gpschat.NearByFragment;


public class GPSTracker extends Service implements LocationListener {

    public  Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    public static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 update/min

    // Declaring a Location Manager
    protected LocationManager locationManager;

    protected String bestProvider;
    public  Handler handler;
    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public GPSTracker() {
        this.mContext = getBaseContext();
        getLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {

        handler.removeCallbacks(locationTask);
    }
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Log.d("GPSNetworkexception", null);
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {

                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {
                        Log.d("Networkexception", e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {

                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }

                    } catch (SecurityException e) {
                        Log.d("GPSexception", e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                }
            }

        } catch (Exception e) {
            Log.d("exception", e.getLocalizedMessage());
            e.printStackTrace();
        }

        return location;
    }


    public void stopUsingGPS() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(GPSTracker.this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }


    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();

        }


        return latitude;
    }


    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }


    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    @Override
    public void onLocationChanged(Location loc) {

        if (loc != null) {
            Log.d("location", loc.getLongitude() + " " + loc.getLatitude());
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();

        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    Runnable locationTask;
    public void waitForLocation() {

        handler = new Handler();
        locationTask = new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if (location != null) {


                }
                Log.d("testlocation",Double.toString(latitude)+":"+Double.toString(longitude));
                if (latitude == 0 && longitude == 0) {
                    Log.e("Provider Enabled", "invalid coordinates looping again");
                    waitForLocation();
                } else {
                    Log.e("Provider Enabled", "asking fragment to setup map");
//                    NearByFragment.setUpMap();
                }
            }
        };
        handler.postDelayed(locationTask, 5000);
    }

}