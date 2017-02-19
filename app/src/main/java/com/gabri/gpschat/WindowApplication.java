package com.gabri.gpschat;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by gabri on 18/02/2017.
 */

public class WindowApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
    }
}
