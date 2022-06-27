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

                        populateAllData();
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

//        Location location = new Location("dummy");
//        location.setLatitude(31.86421292082426);
//        location.setLongitude(35.49231925108679);
//        insideRangeOrNot(location);

//        System.out.println(location);
        populateAllData();




        myAdapter = new TimelineViewAdapter(test2.this, 0, timelineRowsList,
                //if true, list will be sorted by date
                false);


    }



    private void updateGPS(){
        // the exact location of the user
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(test2.this);

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            populateAllData();

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


    public void populateAllData(){
        String BASE_URL = "http://"+ip.getIp().trim()+"/GraduationProject/getPeriodTime.php";

        RequestQueue queue = Volley.newRequestQueue(test2.this);

        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int numOfPeople_Jericho = 0;
                            int numOfPeople_Jews = 0;
                            int numOfPeople_Jordan = 0;

                            int total_Jericho = 0;
                            int total_Jews = 0;
                            int total_Jordan = 0;
                            String rest = "";

                            JSONArray DataList = new JSONArray(response);
                            for (int i = 0; i < DataList.length(); i++) {
                                JSONObject jsonObject = DataList.getJSONObject(i);
                                String date = jsonObject.getString("enteredTime");
                                rest = jsonObject.getString("coordinate");
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


                                if(rest.equals("jericho rest")){
                                    total_Jericho++;
                                    if (minutes > 45)
                                        numOfPeople_Jericho++;
                                }

                                if(rest.equals("jews border")){
                                    total_Jews++;
                                    if (minutes > 45)
                                        numOfPeople_Jews++;
                                }

                                if(rest.equals("jordan border")){
                                    total_Jordan++;
                                    if (minutes > 45)
                                        numOfPeople_Jordan++;
                                }

                            }

                            System.out.println("number of people"+numOfPeople_Jericho);



                            myListView.setAdapter(myAdapter);

                                if (numOfPeople_Jericho <= 50) {
                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.blue));
                                    myRow1.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jericho +" مسافر ");


                                } else if (numOfPeople_Jericho > 50 && numOfPeople_Jericho <= 100) {
                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.yellow));
                                    myRow1.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jericho +" مسافر ");


                                } else if (numOfPeople_Jericho > 100 && numOfPeople_Jericho <= 300) {
                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.orange));
                                    myRow1.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jericho +" مسافر ");


                                } else if (numOfPeople_Jericho > 300) {
                                    myRow1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.black));
                                    myRow1.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jericho +" مسافر ");

                                }

                                if (numOfPeople_Jews <= 50) {

                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.blue));
                                    myRow2.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jews +" مسافر ");


                                } else if (numOfPeople_Jews > 50 && numOfPeople_Jews <= 100) {
                                    System.out.println("salmaaaaa2" + numOfPeople_Jericho);
                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.yellow));
                                    myRow2.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jews +" مسافر ");


                                } else if (numOfPeople_Jews > 100 && numOfPeople_Jews <= 300) {
                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.orange));
                                    myRow2.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jews +" مسافر ");


                                } else if (numOfPeople_Jews > 300) {
                                    myRow2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.black));
                                    myRow2.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jews +" مسافر ");


                                }

                                if (numOfPeople_Jordan <= 50) {

                                    myRow3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.blue));
                                    myRow3.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jordan +" مسافر ");

                                } else if (numOfPeople_Jordan > 50 && numOfPeople_Jordan <= 100) {
                                    myRow3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.yellow));
                                    myRow3.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jordan +" مسافر ");


                                } else if (numOfPeople_Jordan > 100 && numOfPeople_Jordan <= 300) {
                                    myRow3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.orange));
                                    myRow3.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jordan +" مسافر ");

                                } else if (numOfPeople_Jordan > 300) {
                                    myRow3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.black));
                                    myRow3.setDescription(" عدد المسافرين التقريبي حالياً : "+ total_Jordan +" مسافر ");


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