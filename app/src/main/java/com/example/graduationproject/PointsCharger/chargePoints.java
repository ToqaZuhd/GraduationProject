package com.example.graduationproject.PointsCharger;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.CarProvider.PostCar;
import com.example.graduationproject.IP;
import com.example.graduationproject.LogOut;
import com.example.graduationproject.Model.Passenger;
import com.example.graduationproject.OpeningTimesEmployee.WritePostEmployeeActivity;
import com.example.graduationproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class chargePoints extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText passengerID;
    EditText pointsToCharge;

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    IP ip = new IP();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_points);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("شحن نقاط");

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        passengerID = findViewById(R.id.passengerID);
        pointsToCharge = findViewById(R.id.pointsToCharge);



    }


    public void chargePoints(View view) {
        addPoints();
    }


    private void addPoints() {
        String url = "http://"+ip.getIp().trim()+"/GraduationProject/updateScore.php";
        RequestQueue queue = Volley.newRequestQueue(chargePoints.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (response.trim().equals("failed")) {
                        Toast.makeText(chargePoints.this, "رقم الهوية غير موجود", Toast.LENGTH_LONG).show();
                        passengerID.setError("الرجاء إدخال رقم هوية صحيح");

                    } else {
                        Toast.makeText(chargePoints.this, "تم إضافة النقاط الى رصيد المستخدم بنجاح", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(chargePoints.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("score",pointsToCharge.getText().toString().trim() );
                params.put("userId",passengerID.getText().toString().trim() );



                return params;
            }
        };
        queue.add(request);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logOut_nav:
                intent=new Intent(chargePoints.this, LogOut.class);
                startActivity(intent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;


    }



}