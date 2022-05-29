package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.bumptech.glide.Glide;
import com.example.graduationproject.Model.Trip;
import com.example.graduationproject.Model.carOOB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyTrips extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    SharedPreferences preferences;
    private int userID;
    Toolbar toolbar;
    private RequestQueue queue;
    private DrawerLayout drawerLayout;
    ListView myTripsList;
    TripsAdapter tripsAdapter;
    ArrayList <Trip>trips = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("رحلاتي");

        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);


        System.out.println("SALMA");
        getTrips();

        nav();



    }





    public void getTrips () {
            queue = Volley.newRequestQueue(MyTrips.this);
            String BASE_URL = "http://10.0.2.2/GraduationProject/getUserTrips.php?PassengerID="+userID;
            StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String[] array = response.split("@");


                            for (int i = 0; i < array.length; i++) {
                                if (i != array.length - 1) {
                                    String[] info = array[i].split(",");
                                    Trip trip = new Trip();
                                    trip.setTripID(Integer.parseInt(info[0].trim()));
                                    trip.setTicketID(Integer.parseInt(info[1].trim()));
                                    trip.setNumOfBags(Integer.parseInt(info[2].trim()));
                                    trip.setTripDate(info[3].trim());
                                    trip.setRewardPoints(Integer.parseInt(info[4].trim()));
                                    trips.add(trip);




                                }

                            }

                            System.out.println(trips);
                            RecyclerView recycler = (RecyclerView) findViewById(R.id.myTrips);

                            int[] tripsids = new int[trips.size()];
                            int[] bags = new int[trips.size()];
                            String[] dates = new String[trips.size()];
                            int[] rewardPoints = new int[trips.size()];

                            for(int i = 0; i<trips.size();i++){
                                tripsids[i] = trips.get(i).getTripID();
                                bags[i] = trips.get(i).getNumOfBags();
                                dates[i] = trips.get(i).getTripDate();
                                rewardPoints[i] = trips.get(i).getRewardPoints();
                            }

                            recycler.setLayoutManager(new LinearLayoutManager(MyTrips.this));
                            TripsAdapter adapter = new TripsAdapter(tripsids, bags, dates,rewardPoints);
                            recycler.setAdapter(adapter);







                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MyTrips.this, error.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);

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
                        return true;
                }
                return false;
            }
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.aboutApp_nav:
                intent=new Intent(MyTrips.this, aboutApp.class);
                startActivity(intent);
                break;
            case R.id.myTrips_nav:
                intent=new Intent(MyTrips.this, MyTrips.class);
                startActivity(intent);
                break;
            case R.id.setting_nav:
                intent=new Intent(MyTrips.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.logOut_nav:
                intent=new Intent(MyTrips.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}