package com.example.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.Model.PostEmployee;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DaysAndTimesOpeningPassengerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RequestQueue queue;
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_and_times_opening_passenger);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("أيام و أوقات دوام الجسر");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        queue = Volley.newRequestQueue(this);
        getPosts();
        nav();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getPosts() {
        List<PostEmployee> employeeList = new ArrayList<>();
        String BASE_URL = "http://10.0.2.2/GraduationProject/getPostDataEmployee.php";
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray employeePosts = new JSONArray(response);
                            for (int i = 0; i < employeePosts.length(); i++) {
                                JSONObject jsonObject = employeePosts.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String date = jsonObject.getString("date");
                                String post = jsonObject.getString("post");


                                employeeList.add(new PostEmployee(id, date, post));

                            }
                            RecyclerView recycler = findViewById(R.id.post_recyclerPass);
                            recycler.setLayoutManager(new LinearLayoutManager(DaysAndTimesOpeningPassengerActivity.this));
                            DataCard adapter = new DataCard(DaysAndTimesOpeningPassengerActivity.this, employeeList);

                            recycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DaysAndTimesOpeningPassengerActivity.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);


    }

    public void showCalendarForPassenger(View view) {
        Intent intent = new Intent(this, Passenger_calendar.class);
        startActivity(intent);
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
                intent=new Intent(DaysAndTimesOpeningPassengerActivity.this, aboutApp.class);
                startActivity(intent);
                break;
            case R.id.myTrips_nav:
                intent=new Intent(DaysAndTimesOpeningPassengerActivity.this, MyTrips.class);
                startActivity(intent);
                break;
            case R.id.setting_nav:
                intent=new Intent(DaysAndTimesOpeningPassengerActivity.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.logOut_nav:
                intent=new Intent(DaysAndTimesOpeningPassengerActivity.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}