package com.onthegodevelopers.onthego;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class HotelsList extends AppCompatActivity {

    //asign default toolbar to this project
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels_list);

        /* Initialize tool bar variable */
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //add menu button to toolbar
        final ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //get drawer layout id
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_hotels);

        //get Navigaton layout id
        navigationView = (NavigationView) findViewById(R.id.navigationView_Hotels);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.menuLogin:{
                        menuItem.setChecked(true);
                        Intent loginIntent = new Intent(HotelsList.this, Login.class);
                        startActivity(loginIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                    case R.id.menuOrderHistory:{
                        menuItem.setChecked(true);
                        //call Order history activity/page here
                        drawerLayout.closeDrawers();
                        return true;
                    }
                    case R.id.menuTrackOrder:{
                        menuItem.setChecked(true);
                        //call track order map
                        Intent trackIntent = new Intent(HotelsList.this, TrackOrder.class);
                        startActivity(trackIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                    case R.id.menuSettings:{
                        menuItem.setChecked(true);
                        //call Settings activity or page here
                        drawerLayout.closeDrawers();
                        return true;
                    }
                    case R.id.menuSignOut:{
                        menuItem.setChecked(true);
                        //call sing out action
                        drawerLayout.closeDrawers();
                    }
                }
                return false;
            }
        });

        hotelsList = (ListView) findViewById(R.id.HotelsListViewID);
        CustomHotelsListView customHotelsListView = new CustomHotelsListView(this, hotelNames, foodTypes , ImageIDs);
        hotelsList.setAdapter(customHotelsListView);
    }


    //Logic for handling menu icon clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Begin of logic to identify user name and display on Menu side page
        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
        String guestText = "Guest";
        String userName = null;
        String welcomeUser = null;
        if (userName == null){
            welcomeUser = "Welcome, " + guestText;
        }
        //End of logic to identify user name and display on Menu side page

        switch (item.getItemId()){
            case android.R.id.home: {
                welcomeText.setText(welcomeUser);
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    ListView hotelsList;
    String [] hotelNames = {
            "Laddoos", "Barbeque Delight",
//            "Godavari Delights", "Balle-Licious Kitchen",
//            "Curries & Pickles", "Mark", "Hydrabadi Spice", "New Punjabi Food Corner",
//            "Sila Restaurant", "Kolkatta Rolls & Fast food"
    };
    String [] foodTypes = {
            "StreetFood, Desserts, Mithai",
            "Arabian, Kerala, Biriyani, South Indian",
//            "South Indian, North Indian, Biriyani", "North Indian", "Hydrabadi",
//            "Andhra, Chinese", "Andhra, North Indian", "North Indian",
//            "Biriyani, Chinese, North Indian, Desserts", "Rolls, Fast Food, Chinese"
    };

    Integer [] ImageIDs = {
            R.drawable.ic_magnify,
            R.drawable.ic_settings
    };
}
