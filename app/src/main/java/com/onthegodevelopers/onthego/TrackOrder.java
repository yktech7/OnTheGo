package com.onthegodevelopers.onthego;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class TrackOrder extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    private static final float DEFAULT_ZOOM = 15f;
    public double deliveryBoyLatitude;
    public double getDeliveryBoyLongitude;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                        &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownCustLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateMap(lastKnownCustLocation, deliveryBoyLatitude, getDeliveryBoyLongitude);
                }
//                mMap.setMyLocationEnabled(true);
            }
        }
    }

    public void updateMap(Location custLocation, double mDeliveryBoyLatitude, double mDeliveryBoyLongitude) {
        LatLng customerLocation = new LatLng(custLocation.getLatitude(), custLocation.getLongitude());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Clear previous markers
        mMap.clear();

        ArrayList<Marker> markers = new ArrayList<>();
        markers.add(mMap.addMarker(new MarkerOptions().position(customerLocation)
                .title("My Location")));

        //add marker for delivery boy location
        LatLng deliveryBoyLocation = new LatLng(mDeliveryBoyLatitude, mDeliveryBoyLongitude);
        markers.add(mMap.addMarker(new MarkerOptions().position(deliveryBoyLocation)
                .title("Food Location")));

        //Enable my location point
        mMap.setMyLocationEnabled(true);

        //Create Latitude and Longitude array
        LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            latLongBuilder.include(marker.getPosition());
        }
        LatLngBounds latLongBounds = latLongBuilder.build();

        int padding = 200; //30 pixels

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLongBounds, padding);
        // mMap.animateCamera(cu);
        mMap.moveCamera(cu);

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(customerLocation, DEFAULT_ZOOM));
//        mMap.addMarker(new MarkerOptions().position(customerLocation)
//                .title("My Location"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location custlocation) {
                //Write Logic here to update customer location to DB

                //Write logic here to get delivery boy location from DB
                deliveryBoyLatitude = 17.91;
                getDeliveryBoyLongitude = 77.50;
                updateMap(custlocation, deliveryBoyLatitude, getDeliveryBoyLongitude);
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

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION ,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        1
                );
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownCustLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownCustLocation != null){

                    //Write Logic here to update customer location to DB

                    //Write logic here to get delivery boy location from DB
                    deliveryBoyLatitude = 17.91;
                    getDeliveryBoyLongitude = 77.50;

                    updateMap(lastKnownCustLocation, deliveryBoyLatitude, getDeliveryBoyLongitude);
                }
            }
//            mMap.setMyLocationEnabled(true);
        }

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
