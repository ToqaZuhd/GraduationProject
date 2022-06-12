package com.example.graduationproject.PointsCharger;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.graduationproject.LogOut;
import com.example.graduationproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class chargePoints extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText passengerID;
    EditText pointsToCharge;

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_points);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("عن التطبيق");

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
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String restUrl = "http://10.0.2.2/GraduationProject/updatePassengerScore.php";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    123);

        } else {
            SendUpdateRequest runner = new SendUpdateRequest();
            runner.execute(restUrl);
        }
    }

//    Passenger getPassengerData (){
//        Passenger passenger = new Passenger();
//        passenger.setID(Integer.parseInt(passengerID.getText().toString().trim()));
//        int score = passenger.getScore()+Integer.parseInt(pointsToCharge.getText().toString().trim());
//        System.out.println(score);
//        passenger.setScore(score);
//
//        return passenger;
//    }

    private String processRequest(String restUrl) throws UnsupportedEncodingException {

        String data = URLEncoder.encode("ID", "UTF-8") + "="
                + URLEncoder.encode(passengerID.getText().toString().trim(), "UTF-8");

        data += "&" + URLEncoder.encode("score", "UTF-8") + "="
                + URLEncoder.encode(pointsToCharge.getText().toString().trim(), "UTF-8");


        String text = "";
        BufferedReader reader=null;

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL(restUrl);

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        // Show response on activity
        return text;

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

    private class SendUpdateRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return processRequest(urls[0]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("result : "+result);
            if (result.trim().equals("failed")) {
                Toast.makeText(chargePoints.this, "رقم الهوية غير موجود", Toast.LENGTH_LONG).show();
                passengerID.setError("الرجاء إدخال رقم هوية صحيح");

            } else {
                Toast.makeText(chargePoints.this, "تم إضافة النقاط الى رصيد المستخدم بنجاح", Toast.LENGTH_LONG).show();
            }

        }
    }

}