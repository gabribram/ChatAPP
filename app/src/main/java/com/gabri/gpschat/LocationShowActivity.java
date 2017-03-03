package com.gabri.gpschat;

import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gabri.gpschat.model.RecentModel;
import com.gabri.gpschat.model.UserModel;
import com.gabri.gpschat.utility.Constants;
import com.gabri.gpschat.utility.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocationShowActivity extends AppCompatActivity {
    FloatingActionButton backbutton;
    MapView show_map;
    GoogleMap googleMap;
    LatLng currentLocation;
    GPSTracker tracker;
    String other_userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_show);
        getSupportActionBar().hide();
        Bundle bundle=getIntent().getExtras();
        other_userID=bundle.getString("other_user");
        Log.d("other",other_userID);
        backbutton = (FloatingActionButton) findViewById(R.id.map_back_floatingActionButton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        show_map = (MapView)findViewById(R.id.show_mapView);
        show_map.onCreate(savedInstanceState);
        tracker = new GPSTracker(this);
        show_map.onResume(); // needed to get the map to display immediately


        show_map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                if (ActivityCompat.checkSelfPermission(LocationShowActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationShowActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(tracker.getLatitude(), tracker.getLongitude()))
                        .title("Me")).showInfoWindow();
                FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE).child(other_userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("data_testing",dataSnapshot.getValue().toString());
                        UserModel other = dataSnapshot.getValue(UserModel.class);
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(other.getLatitude()), Double.parseDouble(other.getLongitude())))
                                .title(other.getFirstName())).showInfoWindow();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                googleMap.setMyLocationEnabled(true);
                currentLocation = new LatLng(tracker.getLatitude(), tracker.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        });




    }
}
