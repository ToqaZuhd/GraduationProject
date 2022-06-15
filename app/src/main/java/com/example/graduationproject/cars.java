package com.example.graduationproject;

import android.Manifest;
import android.content.Intent;
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
import com.example.graduationproject.Model.Trip;
import com.example.graduationproject.Model.carOOB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class cars extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static ArrayList<carOOB> carsArray = new ArrayList<carOOB>();


    ArrayAdapter<carOOB> itemsAdapter;
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    private RequestQueue queue;

//    ArrayList <carOOB> carsArray = new ArrayList<carOOB>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cars);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("سيارات اجرة");
        carsArray.clear();
        getcars();

        nav();

    }

    public void getcars () {
        queue = Volley.newRequestQueue(cars.this);
        String BASE_URL = "http://10.0.2.2/GraduationProject/getAllCars.php";
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] array = response.split("@");


                        for (int i = 0; i < array.length; i++) {
                            if (i != array.length - 1) {
                                String[] info = array[i].split(",");
                                carOOB car = new carOOB();

                                car.setCar_number(Integer.parseInt(info[0].trim()));
                                car.setCarImage(info[1].trim());
                                car.setCar_type(info[2].trim());
                                car.setCar_price(Integer.parseInt(info[3].trim()));
                                car.setSeats_number(Integer.parseInt(info[4].trim()));
                                car.setGear_type(info[5].trim());
                                car.setProviderID(Integer.parseInt(info[6].trim()));
                                carsArray.add(car);


                            }

                        }

                        RecyclerView recycler = (RecyclerView) findViewById(R.id.cars_recycler);

                        int[] cars_numbers = new int[carsArray.size()];
                        String[] cars_Images = new String[carsArray.size()];
                        String[] cars_types = new String[carsArray.size()];
                        int[] cars_prices = new int[carsArray.size()];

                        for(int i = 0; i<carsArray.size();i++){
                            cars_numbers[i] = carsArray.get(i).getCar_number();
                            cars_Images[i] = carsArray.get(i).getCarImage();
                            cars_types[i] = carsArray.get(i).getCar_type();
                            cars_prices[i] = carsArray.get(i).getCar_price();
                        }

                        recycler.setLayoutManager(new LinearLayoutManager(cars.this));
                        carsAdapter adapter = new carsAdapter(cars_numbers,cars_Images, cars_types, cars_prices,cars.this);
                        recycler.setAdapter(adapter);







                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(cars.this, error.toString(),
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.aboutApp_nav:
                intent=new Intent(cars.this, aboutApp.class);
                startActivity(intent);
                break;
            case R.id.myTrips_nav:
                intent=new Intent(cars.this, MyTrips.class);
                startActivity(intent);
                break;
            case R.id.setting_nav:
                intent=new Intent(cars.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.logOut_nav:
                intent=new Intent(cars.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



}