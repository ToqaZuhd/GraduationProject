package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.qap.ctimelineview.TimelineRow;
import org.qap.ctimelineview.TimelineViewAdapter;

import java.util.ArrayList;

public class test2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
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


        TimelineRow myRow1 = new TimelineRow(1);
        myRow1.setTitle("معبر الكرامة (استراحة أريحا)");
        myRow1.setDescription(getDescription(1000, "أزمة خانقة"));
        myRow1.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.red));
        myRow1.setImageSize(30);
        myRow1.setBellowLineSize(28);
        myRow1.setBellowLineColor(Color.argb(178, 178, 178, 178));
        myRow1.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow1.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow1);
//        myRow1.setBackgroundColor(Color.argb(255, 0, 0, 0));
//        myRow1.setBackgroundSize(60);



        TimelineRow myRow2 = new TimelineRow(2);
        myRow2.setTitle("جسر اليهود");
        myRow2.setDescription(getDescription(500, "أزمة متوسطة"));
        myRow2.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.orange));
        myRow2.setImageSize(30);
        myRow2.setBellowLineColor(Color.argb(178, 178, 178, 178));
        myRow2.setBellowLineSize(28);
        myRow2.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow2.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow2);


        TimelineRow myRow3 = new TimelineRow(3);
        myRow3.setTitle("جسر الملك حسين (جسر ألنبي)");
        myRow3.setDescription(getDescription(200, "لا يوجد أزمة"));
        myRow3.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.green));
        myRow3.setImageSize(30);
        myRow3.setTitleColor(Color.argb(255, 24, 50, 93));
        myRow3.setDescriptionColor(Color.argb(255, 24, 50, 93));
        timelineRowsList.add(myRow3);



    }

    public String getDescription (int numOfPeople, String state){
        return "عدد المسافرين التقريبي حالياً : " + numOfPeople + " مسافر \n" + state;

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