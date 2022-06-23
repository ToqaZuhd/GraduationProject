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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class test2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();
    Toolbar toolbar;
    private DrawerLayout drawerLayout;


    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    SharedPreferences preferences;
    private int userID;
    IP ip = new IP ();

    int counterEnteredJericho = 1, counterEnteredJewsBorder = 1, counterEnteredJordan = 1;

    String address = "";

    TimelineRow myRow1 = new TimelineRow(1);
    TimelineRow myRow2 = new TimelineRow(2);
    TimelineRow myRow3 = new TimelineRow(3);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("حالة الجسر");
        nav();

        ArrayAdapter<TimelineRow> myAdapter = new TimelineViewAdapter(this, 0, timelineRowsList,
                //if true, list will be sorted by date
                false);

// Get the ListView and Bind it with the Timeline Adapter
        ListView myListView = (ListView) findViewById(R.id.timeline_listView);
        myListView.setAdapter(myAdapter);


        /*TimelineRow myRow1 = new TimelineRow(1);
        myRow1.setTitle("معبر الكرامة (استراحة أريحا)");
        myRow1.setDescription(getDescription(1000, "أزمة خانقة"));
        myRow1.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.checkmark));
        myRow1.setImageSize(50);
        myRow1.setBellowLineSize(22);
        myRow1.setBellowLineColor(Color.argb(178, 178, 178, 178));
        myRow1.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow1.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow1);*/




       /* TimelineRow myRow2 = new TimelineRow(2);
        myRow2.setTitle("جسر اليهود");
        myRow2.setDescription(getDescription(500, "أزمة متوسطة"));
        myRow2.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.orange));
        myRow2.setImageSize(30);
        myRow2.setBellowLineColor(Color.argb(178, 178, 178, 178));
        myRow2.setBellowLineSize(22);
        myRow2.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow2.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow2);*/


        /*TimelineRow myRow3 = new TimelineRow(3);
        myRow3.setTitle("جسر الملك حسين (جسر ألنبي)");
        myRow3.setDescription(getDescription(200, "لا يوجد أزمة"));
        myRow3.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.green));
        myRow3.setImageSize(30);
        myRow3.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow3.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow3);*/

        myRow1.setImageSize(30);
        myRow1.setBellowLineSize(22);
        myRow1.setBellowLineColor(Color.argb(178, 178, 178, 178));
        myRow1.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow1.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow1);

        myRow2.setImageSize(30);
        myRow2.setBellowLineColor(Color.argb(178, 178, 178, 178));
        myRow2.setBellowLineSize(22);
        myRow2.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow2.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow2);


        myRow3.setImageSize(30);
        myRow3.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow3.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow3);


        locationRequest =  LocationRequest.create();
        locationRequest.setInterval(300 * 1000);
        locationRequest.setFastestInterval(50 * 1000);
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

//    public String getDescription (int numOfPeople){
//        return "عدد المسافرين التقريبي حالياً : " + numOfPeople ;
//
//    }

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
                                String date = jsonObject.getString("date");

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formattedDate = df.format(c.getTime());
                                Date currDate = df.parse(formattedDate);
                                Date inputDate = df.parse(date);


                                long diff = currDate.getTime() - inputDate.getTime();
                                long seconds = diff / 1000;

                                if (seconds > 3600)
                                    numOfPeople++;

                            }

                            if (coordinate.equals("jericho rest")) {
                                if (numOfPeople < 50) {

                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.blue));


                                } else if (numOfPeople > 50 && numOfPeople <= 100) {
                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.yellow));

                                } else if (numOfPeople > 100 && numOfPeople <= 300) {
                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.orange));

                                } else if (numOfPeople > 300) {
                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.black));

                                }
                            }
                            else if (coordinate.equals("jews bridge")) {
                                if (numOfPeople < 50) {

                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.blue));

                                } else if (numOfPeople > 50 && numOfPeople <= 100) {
                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.yellow));

                                } else if (numOfPeople > 100 && numOfPeople <= 300) {
                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.orange));

                                } else if (numOfPeople > 300) {
                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.black));

                                }
                            }

                            else if (coordinate.equals("jordan bridge")) {
                                if (numOfPeople < 50) {

                                    myRow3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.blue));

                                } else if (numOfPeople > 50 && numOfPeople <= 100) {
                                    myRow3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.yellow));


                                } else if (numOfPeople > 100 && numOfPeople <= 300) {
                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.orange));

                                } else if (numOfPeople > 300) {
                                    myRow3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.black));


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