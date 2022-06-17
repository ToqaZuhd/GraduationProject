package com.example.graduationproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripMapActivity extends AppCompatActivity {
    TextView jericoCircle, jewsCircle, MHCircle, EArrivingTimeTxt, ArrivingTimeTxt, jericoClockTxt, jewsClockTxt, MHClockTxt;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_map);

        getSupportActionBar().setTitle("خريطة الرحلة");

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);


        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(),postNews.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:r:
                    startActivity(new Intent(getApplicationContext(),profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        jericoCircle = findViewById(R.id.jericoCircle);
        jewsCircle = findViewById(R.id.jewsCircle);
        MHCircle = findViewById(R.id.MHCircle);

        EArrivingTimeTxt = findViewById(R.id.EArrivingTimeTxt);
        ArrivingTimeTxt = findViewById(R.id.ArrivingTimeTxt);

        jericoClockTxt = findViewById(R.id.jericoClockTex);
        jewsClockTxt = findViewById(R.id.JewsClockTxt);
        MHClockTxt = findViewById(R.id.MHClockTxt);


        locationRequest =  LocationRequest.create();


        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        updateGPS();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 99){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                updateGPS();
            }
            else{
                Toast.makeText(this, "this app requires permissions to work properly", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void updateGPS(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(TripMapActivity.this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            updateUI(location);
                        }
                    });
        }
        else{
            //permission not granted, we will ask for it
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            }
        }
    }

    private void updateUI(Location location) {
        //it will determine the current address of the location & the current time
        try {
            Geocoder geocoder = new Geocoder(TripMapActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            System.out.println("salma"+location.getLatitude());
            System.out.println(location.getLongitude());
            String address = addresses.get(0).getAddressLine(0);
            String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

            if(address.equalsIgnoreCase(/*"General Administration of crossings and borders"*/"obwain")){
                jericoCircle.setBackgroundResource(R.drawable.visited);
                jericoClockTxt.setText(String.valueOf(currentTime));
            }
            if(address.equalsIgnoreCase("King Hussein Bridge Border Crossing")){//allenby bridge
                jewsCircle.setBackgroundResource(R.drawable.visited);
                jewsClockTxt.setText(String.valueOf(currentTime));
            }
            if(address.equalsIgnoreCase("King Hussein Border Crossing")){
                MHCircle.setBackgroundResource(R.drawable.visited);
                MHClockTxt.setText(String.valueOf(currentTime));
                ArrivingTimeTxt.setText(currentTime);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}