package com.onthegodevelopers.onthego;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
//import com.onthegodevelopers.onthego.models.MapsActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackOrder extends FragmentActivity implements OnMapReadyCallback
//        RoutingListener
                {

    private GoogleMap mMap;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    LocationManager locationManager;
    LocationListener locationListener;
    private static final float DEFAULT_ZOOM = 15f;
    public double deliveryBoyLatitude;
    public double getDeliveryBoyLongitude;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[] {
//                                                   R.color.primary_dark,
//                                                   R.color.primary,
//                                                   R.color.primary_light,
//                                                   R.color.accent,
                                                   R.color.primary_dark_material_light};


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
//        erasePolylines();    //Clear route in map

        ArrayList<Marker> markers = new ArrayList<>();
        markers.add(mMap.addMarker(new MarkerOptions().position(customerLocation)
                .title("My Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));

        //add marker for delivery boy location
        LatLng deliveryBoyLocation = new LatLng(mDeliveryBoyLatitude, mDeliveryBoyLongitude);
        markers.add(mMap.addMarker(new MarkerOptions().position(deliveryBoyLocation)
                .title("Food Location")));

        //Draw routes between customer location and delivery boy
        getRouteToMarker(customerLocation, deliveryBoyLocation);

        //Enable my location point
        mMap.setMyLocationEnabled(true);

        //Create Latitude and Longitude array
        LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            latLongBuilder.include(marker.getPosition());
        }
        LatLngBounds latLongBounds = latLongBuilder.build();

        int padding = 200; //30 pixels

//        Polyline line = mMap.addPolyline(new PolylineOptions()
//                .add(customerLocation, deliveryBoyLocation)
//                .width(5)
//                .color(Color.RED));

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLongBounds, padding);
        // mMap.animateCamera(cu);
        mMap.moveCamera(cu);

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(customerLocation, DEFAULT_ZOOM));
//        mMap.addMarker(new MarkerOptions().position(customerLocation)
//                .title("My Location"));
    }

    private void getRouteToMarker(LatLng customerLocation, LatLng deliveryBoyLocation) {

        String url = getDirectionsUrl(customerLocation, deliveryBoyLocation);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

//        Routing routing = new Routing.Builder()
//                .travelMode(AbstractRouting.TravelMode.BIKING)
//                .withListener(this)
//                .alternativeRoutes(true)
//                .key("AIzaSyBXagKSXlsLOGKQJ8YE4ephYeD_pjdVrTU")
//                .waypoints(deliveryBoyLocation, customerLocation)
//                .build();
//        routing.execute();
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//        // Sensor enabled
//        String sensor = "sensor=false";
//        String mode = "mode=biking";
//
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
//
//        // Output format
//        String output = "json";
//
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+origin.latitude+","+origin.longitude);
        googleDirectionsUrl.append("&destination="+dest.latitude+","+dest.longitude);
        googleDirectionsUrl.append("&key="+"AIzaSyCzm1wtcrGILEZvaduQrriFZh1PL5o7zkM");

        return googleDirectionsUrl.toString();
//        return url;


    }

    private class DownloadTask extends AsyncTask<Object,String,String> {

        @Override
        protected String doInBackground(Object[] objects) {

            String data = "";

            try {
                data = downloadUrl((String)objects[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            TrackOrder.ParserTask parserTask = new TrackOrder.ParserTask();


            parserTask.execute(result);

        }

    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap>> routes = new ArrayList<>();

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes =  (ArrayList) parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    String lat_temp = (String) point.get("lat");
                    String lng_temp = (String) point.get("lng");
                    double lat = Double.parseDouble(lat_temp);
                    double lng = Double.parseDouble(lng_temp);
//                    double lat = (double) point.get("lat");
//                    double lng = (double) point.get("lng");
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        polylines = new ArrayList<>();
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
                deliveryBoyLatitude = 14.91;
                getDeliveryBoyLongitude = 75.50;
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
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        1
                );
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownCustLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownCustLocation != null) {

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


//    @Override
//    public void onRoutingFailure(RouteException e) {
//        if (e != null){
//            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(this, "Something went wrong, try again!!", Toast.LENGTH_SHORT).show();
//        }
//    }

//    @Override
//    public void onRoutingStart() {
//
//    }

//    @Override
//    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
//        if (polylines.size() > 0){
//            for (Polyline poly : polylines){
//                poly.remove();
//            }
//        }
//
//        polylines = new ArrayList<>();
//        //add routes to the map
//        for (int i = 0; i <route.size(); i++){
//            //in case of more than 5 alternative routes
//            int colorIndex = i % COLORS.length;
//
//            PolylineOptions polyOptions = new PolylineOptions();
//            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
//            polyOptions.width(10 + i + 3);
//            polyOptions.addAll(route.get(i).getPoints());
//            Polyline polyline = mMap.addPolyline(polyOptions);
//            polylines.add(polyline);
//
//            Toast.makeText(this, "Route " + (i+1) + ": distance " + route.get(i).getDistanceValue() + ": duration - "
//                                                + route.get(i).getDurationValue(), Toast.LENGTH_LONG).show();
//        }
//    }

//    @Override
//    public void onRoutingCancelled() {
//
//    }

//    private void erasePolylines(){
//        for(Polyline line : polylines){
//            line.remove();
//        }
//        polylines.clear();
//    }
}
