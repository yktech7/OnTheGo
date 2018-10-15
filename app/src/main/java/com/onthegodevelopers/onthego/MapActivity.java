package com.onthegodevelopers.onthego;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.onthegodevelopers.onthego.models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;

        //Marker draggable
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Geocoder gc = new Geocoder(MapActivity.this);
                LatLng ll = marker.getPosition();
                List<Address> listAddress = null;
                String complAdd = null;
                try{
                    listAddress = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                    Address addr = listAddress.get(0);
                    complAdd = addr.getAddressLine(0);
                    //Place current location address in Autosuggestion field
                    mSearchText.setText(complAdd);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            init();
        }
    }

    public static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private AutoSuggest mAutoSuggest;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    //Widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps;
    //private TextView mCurrLocation;

    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private PlaceInfo mPlace;

    //asign default toolbar this project
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /* Initialize tool bar variable */
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //add menu button to toolbar
        final ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
        //mText = (TextView) findViewById(R.id.currentLocation) ;
        //Check Location Permission
        getLocationPermission();

        //Search location
      //  init();

    }

    //Logic for handling menu icon clicks

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        Log.d(TAG, "init: initializing..");
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearchText.setOnItemClickListener(mAutoCompleteClickListener);
        mAutoSuggest = new AutoSuggest(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mAutoSuggest);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == event.ACTION_DOWN
                        || event.getAction() == event.KEYCODE_ENTER){
                    //Execute our Method for searching
                    geoLocate();

                }
                return false;
            }
        });

        //Goto my Location
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: clicked gps icon");
                getDeviceLocation();
            }
        });
        hideSoftKeyBoard();
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> aList = new ArrayList<>();
        try {
            aList = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }
        if(aList.size() > 0)
        {
            Address address = aList.get(0);
            //Either print address or copy to address input field
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            //Set the pointer to the searched location
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }



    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLoation: getting the current location of the device");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
                if(mLocationPermissionGranted){
                    Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "onComplete: found location!");
                                Location currentLocation = (Location) task.getResult();

                                String complAddress = null;
                                //Place current location address in Autosuggestion field
                                complAddress = placeAddress(currentLocation);
                                mSearchText.setText(complAddress);


                                //move Camera
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My location");
                            }
                            else{
                                Log.d(TAG, "onComplete: current location is null");
                                Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityExceptions: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//        if (title != "My location") {
            //Display marker
//            MarkerOptions options = new MarkerOptions();
//            options.position(latLng);
//            options.title(title);
//            mMap.addMarker(options);
            MarkerOptions options = new MarkerOptions();
            options .position(latLng);
            options.title(title);
            options.draggable(true);
            mMap.addMarker(options);
  //      }
        hideSoftKeyBoard();
    }
    private void initMap(){
        Log.d(TAG, "intiMap: initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }
    private void getLocationPermission(){
        Log.d(TAG, "getLcoationPermission: getting location permissions");
            String[] permissions = {
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
            };
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                                                  FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                                                  COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted = true;
                    initMap();
                }
                else{
                    ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
                }
            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                        mLocationPermissionGranted = true;
                        //Initialize our Map
                        initMap();

                }
            }
        }
    }
    private void hideSoftKeyBoard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

        /*
        *******Google Places API autocomplete suggestions*****************
         */
    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Hide keyboard
                hideSoftKeyBoard();

                final AutocompletePrediction item = mAutoSuggest.getItem(position);
                final String placeId = item.getPlaceId();
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailCallback);
            }
        };
    private final ResultCallback<PlaceBuffer> mUpdatePlaceDetailCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try {
                mPlace = new PlaceInfo();

                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getId());
                mPlace.setLatLng(place.getLatLng());
                mPlace.setRating(place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setWebsiteUri(place.getWebsiteUri());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            } catch (NullPointerException e) {
                Log.e(TAG, "onResult: " + e.getMessage());
            }
            //Move camera now
            //moveCamera(mPlace.getLatLng(), DEFAULT_ZOOM, mPlace.getName());

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                           place.getViewport().getCenter().longitude),
                    DEFAULT_ZOOM, mPlace.getName());
            places.release();
        }
    };

    private String placeAddress(Location currentLocationupd) {
        Geocoder geocoder1 = new Geocoder(getApplicationContext());
        String str = null;
        Address returnAddress = null;
        try {
            List<Address> addressList = geocoder1.getFromLocation(currentLocationupd.getLatitude(), currentLocationupd.getLongitude(), 1);
            if (addressList.size() > 0){
               returnAddress = addressList.get(0);
               str = returnAddress.getAddressLine(0);
            /*    for (int i = 0; i < returnAddress.getMaxAddressLineIndex(); i++){
                    if(i == 0){
                        if (returnAddress.getAddressLine(i) != null){
                        str += returnAddress.getAddressLine(i);
                        }
                    }
                    else{
                        if (returnAddress.getAddressLine(i) != null) {
                            str += ", " + returnAddress.getAddressLine(i);
                        }
                    }
                }    */
            }
         //   str += ", " + addressList.get(0).getSubLocality();
         //   str += ", " + addressList.get(0).getLocality();
            //str += addressList.get(0).getLocale();

           // str += ", " + addressList.get(0).getPostalCode();
           // str += ", " + addressList.get(0).getCountryName();

        } catch (IOException e) {
            Log.e(TAG, "placeAddress: " + e.getMessage());
        }
        return str;
    }

}
