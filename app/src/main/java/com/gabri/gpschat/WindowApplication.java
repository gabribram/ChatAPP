package com.gabri.gpschat;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.google.firebase.FirebaseApp;

/**
 * Created by gabri on 18/02/2017.
 */

public class WindowApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
