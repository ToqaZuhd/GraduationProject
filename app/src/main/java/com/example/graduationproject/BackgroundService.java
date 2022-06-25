package com.example.graduationproject;


import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class BackgroundService extends Service {

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    int counterEnteredJericho = 1, counterEnteredJewsBorder = 1, counterEnteredJordan = 1;

    SharedPreferences preferences;
    private int userID;

    IP ip = new IP();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        preferences=getSharedPreferences("session",MODE_PRIVATE);
        int userID=preferences.getInt("login",-1);
        super.onCreate();
//        initData();
        Location location = new Location("dummy");
        location.setLatitude(31.860889);
        location.setLongitude(35.478281);
        insideRangeOrNot(location);
    }



//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId){
//        updateGPS();
//        return START_STICKY;
//    }

    private void updateGPS(){

        // the exact location of the user
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(BackgroundService.this);

       /* if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Executor) this,
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            insideRangeOrNot(location);
                        }
                    });*/
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(this.locationRequest,
                this.locationCallback, Looper.myLooper());
        }
        /*else{
            //permission not granted, we will ask for it
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            }
        }*/
        private LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location currentLocation = locationResult.getLastLocation();
                Log.d("Locations", currentLocation.getLatitude() + "," + currentLocation.getLongitude());
                insideRangeOrNot(currentLocation);
                //Share/Publish Location
            }
        };

    private void insideRangeOrNot(Location location) {

        Toast.makeText(BackgroundService.this,  "obwein" + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();
        //rawabi 32.00810975566043, 35.18731940607394
        //abwein 32.031158019007904, 35.200929130217354
        String idUser = String.valueOf(userID);

        //consider the radius value = 1 km
        int radius = 1000;


        //use the sld to calculate the distance between the exact location of Jericho rest (center) and the exact location of the user
        //to check if the user is inside or outside the circle (range)
        Location jerichoArea = new Location("");
        jerichoArea.setLatitude(31.86421519892245);
        jerichoArea.setLongitude(35.49228173581841);
        float jerichoDist = jerichoArea.distanceTo(location);

        //31.86421519892245, 35.49228173581841
        Location jewsArea = new Location("");
        jewsArea.setLatitude(31.869016092998148);
        jewsArea.setLongitude(35.53160625666317);
        float jewsDist = jewsArea.distanceTo(location);

        Location jordanArea = new Location("");
        jordanArea.setLatitude(31.889659788142747);
        jordanArea.setLongitude(35.578367695985555);
        float jordanDist = jordanArea.distanceTo(location);

        //inside the range then the entered time in the database will be updated
        if (jerichoDist <= radius) {

            String address = "jericho rest";
            if(counterEnteredJericho == 1){
                addEnteredTime(idUser, address);
                counterEnteredJericho ++;
            }
            Toast.makeText(BackgroundService.this, address + jerichoDist + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();

        }
        else if (jerichoDist > radius && location.getLatitude() > jerichoArea.getLatitude() && location.getLatitude() < jewsArea.getLatitude()){
            String address = "betweenJerichoJews";
            addEnteredTime(idUser, address);
        }
        else if(jewsDist <= radius){

            String address = "jews border";
            if(counterEnteredJewsBorder == 1){
                addEnteredTime(idUser, address);
                counterEnteredJewsBorder ++;
            }
            Toast.makeText(BackgroundService.this, address + jewsDist + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();

        }
        else if (jewsDist > radius && location.getLatitude() > jewsArea.getLatitude() && location.getLatitude() < jordanArea.getLatitude()){
            String address = "betweenJewsJordan";
            addEnteredTime(idUser, address);
        }
        //between jews and jordan, coordinate betweenJewsJordan

        else if(jordanDist <= radius){

            String address = "jordan border";
            if(counterEnteredJordan == 1){
                addEnteredTime(idUser, address);
                counterEnteredJordan ++;
            }


            Toast.makeText(BackgroundService.this, address + jordanDist + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();

        }

        else if (jordanDist > radius && jordanArea.getLatitude() < location.getLatitude()){
            String address = "outside jordan";
            addEnteredTime(idUser, address);
            Toast.makeText(BackgroundService.this, "outside" + jerichoDist, Toast.LENGTH_LONG).show();
        }
        // populateAllData(a);

    }


    private void addEnteredTime(String IDNum,String coordinate){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String EnteredTime = formattedDate ;

        String url = "http://"+ip.getIp().trim()+"/GraduationProject/AddEnteredTime.php";
        RequestQueue queue = Volley.newRequestQueue(BackgroundService.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(BackgroundService.this,
                                jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(BackgroundService.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our
                // key and value pair to our parameters.
                params.put("coordinate", coordinate);
                params.put("IDNum", IDNum);
                params.put("time", EnteredTime);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }


    public void initData(){
        locationRequest =  LocationRequest.create();
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(3 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }
}
