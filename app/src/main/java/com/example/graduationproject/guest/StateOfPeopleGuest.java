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

import com.example.graduationproject.DocumentActivity;
import com.example.graduationproject.LogOut;
import com.example.graduationproject.MainActivity;
import com.example.graduationproject.MyTrips;
import com.example.graduationproject.R;
import com.example.graduationproject.Setting;
import com.example.graduationproject.aboutApp;
import com.example.graduationproject.profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.qap.ctimelineview.TimelineRow;
import org.qap.ctimelineview.TimelineViewAdapter;

import java.util.ArrayList;
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



        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);


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

            case R.id.aboutAppGuest_nav:
                intent=new Intent(StateOfPeopleGuest.this, AboutAppGuest.class);
                startActivity(intent);
                break;

            //here the state of people activity
            case R.id.stateOfPeopleGuest_nav:
                intent=new Intent(StateOfPeopleGuest.this, StateOfPeopleGuest.class);
                startActivity(intent);
                break;


            case R.id.documentGuest_nav:
                intent=new Intent(StateOfPeopleGuest.this, DocumentGuestActivity.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}