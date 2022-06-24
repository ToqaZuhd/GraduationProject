package com.example.graduationproject.guest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.DocumentActivity;
import com.example.graduationproject.LogOut;
import com.example.graduationproject.MainActivity;
import com.example.graduationproject.MyTrips;
import com.example.graduationproject.R;
import com.example.graduationproject.Registration;
import com.example.graduationproject.Setting;
import com.example.graduationproject.SignIn;
import com.example.graduationproject.StateOfPeople;
import com.example.graduationproject.aboutApp;
import com.example.graduationproject.profile;
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

public class StateOfPeopleGuest extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_of_people_geuest);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("حالة الجسر");


        nav();


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








        float crisisPercentage = (float) 5.2;
        if (crisisPercentage <6) {
            addRow("8 AM", Color.RED );
            addRow("10 AM", Color.RED );
            crisisPercentage +=3;
        }
        else {
            addRow("11 AM", Color.RED );
            addRow("12 AM", Color.RED );
            crisisPercentage +=3;
        }
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


    }

    public void addRow (String Title, int color){
        TimelineRow myRow = new TimelineRow(2);
        myRow.setTitle(Title);
        myRow.setBellowLineColor(color);
        myRow.setBellowLineSize(6);
        timelineRowsList.add(myRow);

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
                    case R.id.stateOfPeople:
                        startActivity(new Intent(getApplicationContext(),StateOfPeopleGuest.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.requiredDocs:
                        startActivity(new Intent(getApplicationContext(),DocumentGuestActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.openingTimes:
                        startActivity(new Intent(getApplicationContext(), DaysOpeningGuest.class));
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
        String BASE_URL = "http://192.168.1.143/GraduationProject/getPeridTime.php";

        RequestQueue queue = Volley.newRequestQueue(StateOfPeopleGuest.this);
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
                Toast.makeText(StateOfPeopleGuest.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.aboutAppGuest_nav:
                intent=new Intent(StateOfPeopleGuest.this, AboutAppGuest.class);
                startActivity(intent);
                break;
            case R.id.login:
                intent=new Intent(StateOfPeopleGuest.this, SignIn.class);
                startActivity(intent);
                break;
            case R.id.SignUp:
                intent=new Intent(StateOfPeopleGuest.this, Registration.class);
                startActivity(intent);
                break;




        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}