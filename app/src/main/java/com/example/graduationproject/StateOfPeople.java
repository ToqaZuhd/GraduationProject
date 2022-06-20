package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.qap.ctimelineview.TimelineRow;
import org.qap.ctimelineview.TimelineViewAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StateOfPeople extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    SharedPreferences preferences;
    private int userID;

    int counterEnteredJericho = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_of_people);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("حالة الجسر");



        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);


        preferences=getSharedPreferences("session",MODE_PRIVATE);
        int userID=preferences.getInt("login",-1);

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
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

//        ProgressBar p1 = findViewById(R.id.progressBar1);
//        ProgressBar p2 = findViewById(R.id.progressBar2);
//        ProgressBar p3 = findViewById(R.id.progressBar3);
//        ProgressBar p4 = findViewById(R.id.progressBar4);
////        ProgressBar p5 = findViewById(R.id.progressBar5);
////        ProgressBar p6 = findViewById(R.id.progressBar6);
////        ProgressBar p7 = findViewById(R.id.progressBar7);
//
//
//        p1.setScaleY(3f);



// Create new timeline row (Row Id)
//        TimelineRow myRow = new TimelineRow(0);
//        myRow.setTitle("8 AM");
//        myRow.setBellowLineColor(Color.RED);
//        myRow.setBellowLineSize(6);


//
//        // Create new timeline row (Row Id)
//        TimelineRow myRow2 = new TimelineRow(1);
//        myRow2.setTitle("9 AM");
//        myRow2.setBellowLineColor(Color.BLACK);
//        myRow2.setBellowLineSize(6);
//
//        TimelineRow myRow3 = new TimelineRow(2);
//        myRow3.setTitle("10 AM");
//        myRow3.setBellowLineColor(Color.YELLOW);
//        myRow3.setBellowLineSize(6);
//
//        TimelineRow myRow3 = new TimelineRow(2);
//        myRow3.setTitle("10 AM");
//        myRow3.setBellowLineColor(Color.YELLOW);
//        myRow3.setBellowLineSize(6);
//        myRow3.setDescription();



//        float crisisPercentage = (float) 5.2;
//        if (crisisPercentage <6) {
//            addRow("8 AM", Color.RED );
//            addRow("10 AM", Color.RED );
//            crisisPercentage +=3;
//        }
//        else {
//            addRow("11 AM", Color.RED );
//            addRow("12 AM", Color.RED );
//            crisisPercentage +=3;
//        }
//// Add the new row to the list
//        timelineRowsList.add(myRow);
//        timelineRowsList.add(myRow2);
//        timelineRowsList.add(myRow3);

// Create the Timeline Adapter
        ArrayAdapter<TimelineRow> myAdapter = new TimelineViewAdapter(this, 0, timelineRowsList,
                //if true, list will be sorted by date
                false);

// Get the ListView and Bind it with the Timeline Adapter
        ListView myListView = (ListView) findViewById(R.id.timeline_listView);
        myListView.setAdapter(myAdapter);

        locationRequest =  LocationRequest.create();
        locationRequest.setInterval(300 * 1000);
        locationRequest.setFastestInterval(50 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        updateGPS();


    }

    //to get the permission from the user 
    //I think it is not necessary here 
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


        // the exact location of the user
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(StateOfPeople.this);

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            insideRangeOrNot(location);
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

    private void insideRangeOrNot(Location location) {

        // rawabi 32.00810975566043, 35.18731940607394
        //abwein 32.031158019007904, 35.200929130217354
        String idUser = String.valueOf(userID);
        //the exact location of the Jericho rest
        double lat = 32.00810975566043;
        double lon = 35.18731940607394;

        //consider the radius value = 1 km
        int radius = 1000; // I should ensure that it is correct // I think it should be less than 1 km => 200 m. // it should be double


        //use the sld to calculate the distance between the exact location of Jericho rest (center) and the exact location of the user
        //to check if the user is inside or outside the circle (range)

        //the distance in meter
        //double distance = Math.sqrt(Math.pow(lat - location.getLatitude(), 2) + Math.pow(lon - location.getLongitude(), 2));

        /*float []results = new float[1];
        Location.distanceBetween(lat, lon, location.getLatitude(), location.getLatitude(), results);*/

        Location areaOfInterest = new Location("");
        areaOfInterest.setLatitude(32.031158019007904);
        areaOfInterest.setLongitude(35.200929130217354);
        float dist = areaOfInterest.distanceTo(location);
        /*double ky = 40000 / 360;
        double kx = Math.cos(Math.PI * lat / 180.0) * ky;
        double dx = Math.abs(lon - location.getLongitude()) * kx;
        double dy = Math.abs(lat - location.getLatitude()) * ky;
        double results = Math.sqrt(dx * dx + dy * dy);*/

        //inside the range then the entered time in the database will be updated
        if (dist <= radius) {

            String address = "jericho rest";
            if(counterEnteredJericho == 1){
                addEnteredTime(idUser, address);
                counterEnteredJericho ++;
            }
            addLocation(idUser, address);
            populateAllData();
            Toast.makeText(StateOfPeople.this, address + dist + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(StateOfPeople.this, "outside" + dist, Toast.LENGTH_LONG).show();
        }

    }

    /*function arePointsNear(checkPoint, centerPoint, km) {
        var ky = 40000 / 360;
        var kx = Math.cos(Math.PI * centerPoint.lat / 180.0) * ky;
        var dx = Math.abs(centerPoint.lng - checkPoint.lng) * kx;
        var dy = Math.abs(centerPoint.lat - checkPoint.lat) * ky;
        return Math.sqrt(dx * dx + dy * dy) <= km;
    }*/


    private void addEnteredTime(String IDNum,String coordinate){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String EnteredTime = formattedDate ;

        String url = "http://192.168.1.143/GraduationProject/AddEnteredTime.php";
        RequestQueue queue = Volley.newRequestQueue(StateOfPeople.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(StateOfPeople.this,
                                jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(StateOfPeople.this,
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

    private void addLocation(String IDNum,String coordinate){

        String url = "http://192.168.1.143/GraduationProject/AddLocation.php";
        RequestQueue queue = Volley.newRequestQueue(StateOfPeople.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(StateOfPeople.this,
                                jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(StateOfPeople.this,
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

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    public void populateAllData(){
        String BASE_URL = "http://192.168.1.143/GraduationProject/getPeridTime.php";

        RequestQueue queue = Volley.newRequestQueue(StateOfPeople.this);
        ArrayList<Long> time=new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray roomList=new JSONArray(response);
                            for(int i=0;i<roomList.length();i++){
                                JSONObject jsonObject= roomList.getJSONObject(i);
                                String date = jsonObject.getString("date");

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formattedDate = df.format(c.getTime());
                                Date currDate=df.parse(formattedDate);
                                Date inputDate = df.parse(date);


                                long diff = currDate.getTime() - inputDate.getTime();
                                long seconds = diff / 1000;
                                time.add(seconds);
//                                long minutes = seconds / 60;
//                                long hours = minutes / 60;




                            }

                            long total=0;
                            for (int i=0;i<time.size();i++){
                                total=+time.get(i);
                            }

                            long avg=total/time.size();

                            long avgMinutes=avg/60;
                            long avgHours=avgMinutes/60;

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StateOfPeople.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }



    public void addRow (String Title, int color){
        TimelineRow myRow = new TimelineRow(2);
        myRow.setTitle(Title);
        myRow.setBellowLineColor(color);
        myRow.setBellowLineSize(6);
        timelineRowsList.add(myRow);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.aboutApp_nav:
                intent=new Intent(StateOfPeople.this, aboutApp.class);
                startActivity(intent);
                break;
            case R.id.myTrips_nav:
                intent=new Intent(StateOfPeople.this, MyTrips.class);
                startActivity(intent);
                break;
            case R.id.setting_nav:
                intent=new Intent(StateOfPeople.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.logOut_nav:
                intent=new Intent(StateOfPeople.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}