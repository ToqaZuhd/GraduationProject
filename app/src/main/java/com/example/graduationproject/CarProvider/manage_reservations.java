package com.example.graduationproject.CarProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.LogOut;
import com.example.graduationproject.Logo;
import com.example.graduationproject.MainActivity;
import com.example.graduationproject.Model.Post;
import com.example.graduationproject.Model.Reservation;
import com.example.graduationproject.Model.Trip;
import com.example.graduationproject.Model.carOOB;
import com.example.graduationproject.MyTrips;
import com.example.graduationproject.R;
import com.example.graduationproject.Setting;
import com.example.graduationproject.aboutApp;
import com.example.graduationproject.cars;
import com.example.graduationproject.postCardView;
import com.example.graduationproject.postNews;
import com.example.graduationproject.profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class manage_reservations extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recycler;

    List<Reservation> reservations=new ArrayList<>();
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    private RequestQueue queue;
    SharedPreferences preferences;
    private int providerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_reservations);
        recycler = findViewById(R.id.recyclerView);
        preferences=getSharedPreferences("session",MODE_PRIVATE);
        providerID=preferences.getInt("login",-1);

        System.out.println(providerID);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("السيارات المؤجرة");
        getReservations();
//
        nav();

    }

    public void getReservations(){
        queue = Volley.newRequestQueue(manage_reservations.this);
        String url = "http://10.0.2.2/graduationProject/getReservations.php?id="+providerID;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray roomList=new JSONArray(response);
                            for(int i=0;i<roomList.length();i++){
                                JSONObject jsonObject= roomList.getJSONObject(i);
                                String number = jsonObject.getString("car_number");
                                String type = jsonObject.getString("car_type");
                                String rentDate = jsonObject.getString("rentDate");
                                String endRentalRequest = jsonObject.getString("endRentalRequest");
                                String Name = jsonObject.getString("Name");
                                String phoneNum = jsonObject.getString("phoneNum");



                                Reservation res = new Reservation(Integer.parseInt(number), type , rentDate, endRentalRequest, Name, phoneNum);
                                System.out.println("ssss"+res);
                                reservations.add(res);


                            }

                            recycler.setLayoutManager(new LinearLayoutManager(manage_reservations.this));
                            reservationsAdapter adapter = new reservationsAdapter(manage_reservations.this,reservations);

                            recycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(manage_reservations.this, error.toString(),
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
                    case R.id.addCar:
                        startActivity(new Intent(getApplicationContext(),PostCar.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.manage_reservations:
                        startActivity(new Intent(getApplicationContext(),manage_reservations.class));
                        overridePendingTransition(0,0);
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

            case R.id.logOut_nav:
                intent=new Intent(manage_reservations.this, Logo.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



}