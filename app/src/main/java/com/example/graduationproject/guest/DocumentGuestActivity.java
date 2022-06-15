package com.example.graduationproject.guest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.graduationproject.DocumentActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.Registration;
import com.example.graduationproject.SignIn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class DocumentGuestActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_guest);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("الوثائق المطلوبة");

        nav();


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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.aboutAppGuest_nav:
                intent=new Intent(DocumentGuestActivity.this, AboutAppGuest.class);
                startActivity(intent);
                break;
            case R.id.login:
                intent=new Intent(DocumentGuestActivity.this, SignIn.class);
                startActivity(intent);
                break;
            case R.id.SignUp:
                intent=new Intent(DocumentGuestActivity.this, Registration.class);
                startActivity(intent);
                break;




        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}