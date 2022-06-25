package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import android.text.format.DateFormat;

public class test2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();
    Toolbar toolbar;
    private DrawerLayout drawerLayout;

    View circle ;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    ListView myListView ;
    SharedPreferences preferences;
    int userID;
    IP ip = new IP ();

    int counterEnteredJericho = 1, counterEnteredJewsBorder = 1, counterEnteredJordan = 1;

    String address = "";

    TimelineRow myRow1 = new TimelineRow(0);
    TimelineRow myRow2 = new TimelineRow(0);
    TimelineRow myRow3 = new TimelineRow(0);
    ArrayAdapter<TimelineRow> myAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2);
        TextView datetxt  =findViewById(R.id.date);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        populateAllData("jericho rest");
                        populateAllData("jews border");
                        populateAllData("jordan border");

                        swipeRefreshLayout.setRefreshing(false);

                    }
                }
        );


        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);
        System.out.println(userID);


        String dateTime;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();

        simpleDateFormat = new SimpleDateFormat("E ، dd LLLL yyyy");
        dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        datetxt.setText(dateTime);

        myListView = (ListView) findViewById(R.id.timeline_listView);

        myRow1.setTitle("معبر الكرامة (استراحة أريحا)");
        myRow1.setImageSize(30);
        myRow1.setBellowLineSize(22);
        myRow1.setBellowLineColor(Color.argb(178, 178, 178, 178));
        myRow1.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow1.setDescriptionColor(Color.argb(255, 24, 50, 93));



        myRow2.setTitle("جسر اليهود");
        myRow2.setImageSize(30);
        myRow2.setBellowLineColor(Color.argb(178, 178, 178, 178));
        myRow2.setBellowLineSize(22);
        myRow2.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow2.setDescriptionColor(Color.argb(255, 24, 50, 93));



        myRow3.setTitle("جسر الملك حسين");
        myRow3.setImageSize(30);
        myRow3.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow3.setDescriptionColor(Color.argb(255, 24, 50, 93));

        timelineRowsList.add(myRow1);
        timelineRowsList.add(myRow2);
        timelineRowsList.add(myRow3);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("حالة الجسر");
        nav();


//        locationRequest =  LocationRequest.create();
//        locationRequest.setInterval(300 * 1000);
//        locationRequest.setFastestInterval(50 * 1000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        updateGPS();

        Location location = new Location("dummy");
        location.setLatitude(31.86421292082426);
        location.setLongitude(35.49231925108679);
        insideRangeOrNot(location);

//        System.out.println(location);
        populateAllData("jericho rest");
        populateAllData("jews border");
        populateAllData("jordan border");



        myAdapter = new TimelineViewAdapter(test2.this, 0, timelineRowsList,
                //if true, list will be sorted by date
                false);


    }

    private void insideRangeOrNot(Location location) {

        Toast.makeText(test2.this,  "obwein" + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();
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
        System.out.println(jerichoDist);

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
            Toast.makeText(test2.this, address + jerichoDist + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();

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
            Toast.makeText(test2.this, address + jewsDist + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();

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


            Toast.makeText(test2.this, address + jordanDist + "lon = " + location.getLongitude() + "lat = " + location.getLatitude(), Toast.LENGTH_LONG).show();

        }

        else if (jordanDist > radius && jordanArea.getLatitude() < location.getLatitude()){
            String address = "outside jordan";
            addEnteredTime(idUser, address);
            Toast.makeText(test2.this, "outside" + jerichoDist, Toast.LENGTH_LONG).show();
        }
        // populateAllData(a);

    }
    private void addEnteredTime(String IDNum,String coordinate){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String EnteredTime = formattedDate ;
        Date currentTime = Calendar.getInstance().getTime();
        System.out.println(currentTime);

        String url = "http://"+ip.getIp().trim()+"/GraduationProject/AddEnteredTime.php";
        RequestQueue queue = Volley.newRequestQueue(test2.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(test2.this,
                                jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(test2.this,
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(test2.this);

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            populateAllData("jericho rest");
                            populateAllData("jews border");
                            populateAllData("jordan border");
                           // insideRangeOrNot(location);
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



    public void nav (){
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    public void populateAllData(String coordinate){
        String BASE_URL = "http://"+ip.getIp().trim()+"/GraduationProject/getPeriodTime.php?coordinate="+coordinate;

        RequestQueue queue = Volley.newRequestQueue(test2.this);

        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int numOfPeople = 0;
                            JSONArray DataList = new JSONArray(response);
                            for (int i = 0; i < DataList.length(); i++) {
                                JSONObject jsonObject = DataList.getJSONObject(i);
                                String date = jsonObject.getString("enteredTime");
                                System.out.println("dd"+date);

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formattedDate = df.format(c.getTime());
                                Date currDate = df.parse(formattedDate);
                                Date inputDate = df.parse(date);
                                System.out.println("input date"+inputDate);


                                long diff = currDate.getTime() - inputDate.getTime();


                                long seconds = diff / 1000;
                                long minutes = seconds / 60;
                                long hours = minutes / 60;
                                long days = hours / 24;

                                System.out.println("minutes"+minutes);


                                if (minutes > 60)
                                    numOfPeople++;

                            }

                            System.out.println("number of people"+numOfPeople);



                            myListView.setAdapter(myAdapter);

                            if (coordinate.trim().equals("jericho rest")) {

                                if (numOfPeople <= 50) {
                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.blue));
                                    myRow1.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");


                                } else if (numOfPeople > 50 && numOfPeople <= 100) {
                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.yellow));
                                    myRow1.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");


                                } else if (numOfPeople > 100 && numOfPeople <= 300) {
                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.orange));
                                    myRow1.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");


                                } else if (numOfPeople > 300) {
                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.black));
                                    myRow1.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");

                                }
                            }
                            else if (coordinate.trim().equals("jews border")) {
                                if (numOfPeople <= 50) {

                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.blue));
                                    myRow2.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");


                                } else if (numOfPeople > 50 && numOfPeople <= 100) {
                                    System.out.println("salmaaaaa2" + numOfPeople);
                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.yellow));
                                    myRow2.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");


                                } else if (numOfPeople > 100 && numOfPeople <= 300) {
                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.orange));
                                    myRow2.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");


                                } else if (numOfPeople > 300) {
                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.black));
                                    myRow2.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");


                                }
                            }

                            else if (coordinate.equals("jordan border")) {
                                if (numOfPeople <= 50) {

                                    myRow3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.blue));
                                    myRow3.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");

                                } else if (numOfPeople > 50 && numOfPeople <= 100) {
                                    myRow3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.yellow));
                                    myRow3.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");


                                } else if (numOfPeople > 100 && numOfPeople <= 300) {
                                    myRow3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.orange));
                                    myRow3.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");

                                } else if (numOfPeople > 300) {
                                    myRow3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.black));
                                    myRow3.setDescription(" عدد المسافرين التقريبي حالياً : "+ DataList.length() +" مسافر ");


                                }
                            }

                            } catch(JSONException | ParseException e){
                                e.printStackTrace();
                            }

                        }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(test2.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.aboutApp_nav:
                intent=new Intent(test2.this, aboutApp.class);
                startActivity(intent);
                break;
            case R.id.myTrips_nav:
                intent=new Intent(test2.this, MyTrips.class);
                startActivity(intent);
                break;
            case R.id.setting_nav:
                intent=new Intent(test2.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.logOut_nav:
                intent=new Intent(test2.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}