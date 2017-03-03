package com.gabri.gpschat.utility;

import android.Manifest;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

public class LocatioinService extends Service {

    private static final String TAG = "HelloService";

    private boolean isRunning = false;
    LocationTracker tracker;

    public LocatioinService() {

        Log.i(TAG, "Service onCreate");

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTracking();

        return Service.START_STICKY;
    }

    public void startTracking() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        tracker = new LocationTracker(
                getBaseContext(),
                new TrackerSettings()
                        .setUseGPS(true)
                        .setUseNetwork(false)
                        .setUsePassive(false)
                        .setTimeout(3 * 60 * 1000)
                        .setMetersBetweenUpdates(10)
                        .setTimeBetweenUpdates(10 * 1000)

        ) {

            @Override
            public void onLocationFound(Location location) {

                Log.d("mylocations",location.getLongitude()+":"+location.getLatitude());

            }


            @Override
            public void onTimeout() {
                // Do some stuff when a new GPS Location has been found
            }


        };



    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        Log.i(TAG, "Service onDestroy");
    }
}
