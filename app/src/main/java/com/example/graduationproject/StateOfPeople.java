package com.example.graduationproject;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.qap.ctimelineview.TimelineRow;
import org.qap.ctimelineview.TimelineViewAdapter;

import java.util.ArrayList;
import java.util.Date;

public class StateOfPeople extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;

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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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