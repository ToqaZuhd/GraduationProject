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
import android.widget.Switch;
import android.widget.TextView;
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
    IP ip = new IP ();
    private int userID;
    TimelineRow myRow1 = new TimelineRow(1);
    TimelineRow myRow2 = new TimelineRow(2);
    TimelineRow myRow3 = new TimelineRow(3);

    int counterEnteredJericho = 1, counterEnteredJewsBorder = 1, counterEnteredJordan = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_of_people);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("حالة الجسر");

        TextView datetxt  =findViewById(R.id.date);

        String dateTime;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();

        simpleDateFormat = new SimpleDateFormat("E ، dd LLLL yyyy");
        dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        datetxt.setText(dateTime);

        myRow1.setTitle("معبر الكرامة (استراحة أريحا)");
        myRow1.setDescription("عدد المسافرين التقريبي حالياً : 400 مسافر");
        myRow1.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.black));
        myRow1.setImageSize(30);
        myRow1.setBellowLineSize(22);
        myRow1.setBellowLineColor(Color.argb(178, 178, 178, 178));
        myRow1.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow1.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow1);


        myRow2.setTitle("جسر اليهود");
        myRow2.setDescription("عدد المسافرين التقريبي حالياً : 250 مسافر");
        myRow2.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.orange));
        myRow2.setImageSize(30);
        myRow2.setBellowLineColor(Color.argb(178, 178, 178, 178));
        myRow2.setBellowLineSize(22);
        myRow2.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow2.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow2);


        myRow3.setTitle("جسر الملك حسين");
        myRow3.setDescription("عدد المسافرين التقريبي حالياً : 80 مسافر");
        myRow3.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.yellow));
        myRow3.setImageSize(30);
        myRow3.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow3.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow3);




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
                        startActivity(new Intent(getApplicationContext(),postNews.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


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

        //rawabi 32.00810975566043, 35.18731940607394
        //abwein 32.031158019007904, 35.200929130217354
        String idUser = String.valueOf(userID);

        //consider the radius value = 1 km
        int radius = 1000;


        //use the sld to calculate the distance between the exact location of Jericho rest (center) and the exact location of the user
        //to check if the user is inside or outside the circle (range)
        Location jerichoArea = new Location("");
        jerichoArea.setLatitude(32.031158019007904);
        jerichoArea.setLongitude(35.200929130217354);
        float jerichoDist = jerichoArea.distanceTo(location);

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
            Toast.makeText(StateOfPeople.this, address + jerichoDist + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();

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
            Toast.makeText(StateOfPeople.this, address + jewsDist + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();

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


            Toast.makeText(StateOfPeople.this, address + jordanDist + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();

        }

        else if (jordanDist > radius && jordanArea.getLatitude() < location.getLatitude()){
            String address = "outside jordan";
            addEnteredTime(idUser, address);
            Toast.makeText(StateOfPeople.this, "outside" + jerichoDist, Toast.LENGTH_LONG).show();
        }
        populateAllData("jericho rest");

    }


    private void addEnteredTime(String IDNum,String coordinate){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String EnteredTime = formattedDate ;

        String url = "http://"+ip.getIp().trim()+"/GraduationProject/AddEnteredTime.php";
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


    public void populateAllData(String coordinate){
        String BASE_URL = "http://"+ip.getIp().trim()+"/GraduationProject/getPeriodTime.php?coordinate="+coordinate;

        RequestQueue queue = Volley.newRequestQueue(StateOfPeople.this);

        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int numOfPeople=0;
                            JSONArray DataList=new JSONArray(response);
                            for(int i=0;i<DataList.length();i++){
                                JSONObject jsonObject= DataList.getJSONObject(i);
                                String date = jsonObject.getString("enteredTime");

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formattedDate = df.format(c.getTime());
                                Date currDate=df.parse(formattedDate);
                                Date inputDate = df.parse(date);


                                long diff = currDate.getTime() - inputDate.getTime();
                                long seconds = diff / 1000;

                                if (seconds>3600)
                                    numOfPeople++;

                            }

                            System.out.println(numOfPeople);
                            if (numOfPeople<50){
                                myRow1.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.blue));

                            }
                            else if (numOfPeople>50 && numOfPeople<=100){
                                myRow1.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.yellow));

                            }
                            else if (numOfPeople>100 && numOfPeople<=300){
                                myRow1.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.orange));

                            }
                            else if (numOfPeople>300){
                                myRow1.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.black));

                            }

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



//    public void addRow (String Title, int color){
//        TimelineRow myRow = new TimelineRow(2);
//        myRow.setTitle(Title);
//        myRow.setBellowLineColor(color);
//        myRow.setBellowLineSize(6);
//        timelineRowsList.add(myRow);
//
//    }

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