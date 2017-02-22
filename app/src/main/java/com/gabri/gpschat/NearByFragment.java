package com.gabri.gpschat;


import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabri.gpschat.fragment.AvailableFragment;
import com.gabri.gpschat.utility.GPSTracker;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NearByFragment extends Fragment //implements LocationListener
{
    View nearby_view;
    MapView mapview;
    GoogleMap googleMap;
    LatLng currentLocation;
    GPSTracker tracker;
    FloatingActionButton gocontact_actionbutton;
    public NearByFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        nearby_view=inflater.inflate(R.layout.fragment_near_by, container, false);
        gocontact_actionbutton=(FloatingActionButton)nearby_view.findViewById(R.id.availe_floatingActionButton);
        gocontact_actionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new AvailableFragment()).commit();
            }
        });

        mapview = (MapView) nearby_view.findViewById(R.id.mapView);
        mapview.onCreate(savedInstanceState);
        tracker = new GPSTracker(getContext());
        mapview.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapview.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
//                LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
//                Location location = service.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                if (location == null) {
//
//                } else {
//
//                    location = service.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//                }

                currentLocation = new LatLng(tracker.getLatitude(), tracker.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        });


        return nearby_view;
    }

//    @Override
//    public void onLocationChanged(Location location) {
//
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//    }
}
