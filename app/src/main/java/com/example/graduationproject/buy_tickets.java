package com.example.graduationproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.Model.Trip;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class buy_tickets extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView score;
    NumberPicker bags_num ;
    Button bags_price;
    Button know_total;
    TextView total_cost;
    int bagsPr;
    int total ;
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    SharedPreferences preferences;
    private int userID;
    private RequestQueue queue;
    int scoreNum;
    Trip trip = new Trip();
    IP ip = new IP ();

    int ticketNumber;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.buy_tickets);




        getBags_number();
        total = 192 + bagsPr;

        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);
        System.out.println(userID);

        score = findViewById(R.id.score);
        bags_price = findViewById(R.id.bags_price);


        know_total = findViewById(R.id.know_total);
        total_cost = findViewById(R.id.total_cost);


        bagsPr = bags_num.getValue()*6;
        getScore();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("شراء التذاكر");


        nav ();


    }



    public void getScore(){
        queue = Volley.newRequestQueue(buy_tickets.this);
        String BASE_URL = "http://"+ip.getIp().trim()+"/graduationProject/searchName.php?id="+userID;
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject= new JSONObject(response);


                            scoreNum=Integer.parseInt(jsonObject.getString("score"));
                            score.setText(scoreNum +"");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(buy_tickets.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }





    private void getBags_number() {
        bags_num = (NumberPicker)findViewById(R.id.bags_num);
        bags_num.setMinValue(0);
        bags_num.setMaxValue(20);
        bags_num.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println(newVal);
                bagsPr = bags_num.getValue()*6;
                bags_price.setText(bagsPr + " نقطة ");
            }
        });
    }


    public void knowTotal(View view) {
        total = 0;
        total = 192 + bagsPr;
        System.out.println(total);
        total_cost.setText("التكلفة الإجمالية : " + total + " نقطة ");

    }





    public void updateScore() {
        String url ="http://"+ip.getIp().trim()+"/GraduationProject/updateScore.php";
        RequestQueue queue = Volley.newRequestQueue(buy_tickets.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(buy_tickets.this,
                                jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(buy_tickets.this,
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

                params.put("id", String.valueOf(userID));

                int newScore = scoreNum-total+rewardPoints();

                params.put("score", newScore +"");
                return params;
            }
        };
        queue.add(request);

    }

    public int rewardPoints (){
        return (int)(total * 0.02);
    }

    public void makeTrip()  {

        Random rnd = new Random();

        ticketNumber = rnd.nextInt(99999);


        Intent intent = new Intent(buy_tickets.this,carDescription.class);
        intent.putExtra("TicketID", ticketNumber);

        trip.setPassengerID(userID);
        trip.setTicketID(ticketNumber);
        trip.setNumOfBags(bags_num.getValue());

        java.sql.Date date=new java.sql.Date(System.currentTimeMillis());
        trip.setTripDate(date.toString());

        trip.setRewardPoints(rewardPoints());


    }

    public void insertTrip (){
        String url = "http://"+ip.getIp().trim()+"/GraduationProject/addTrip.php";
        RequestQueue queue = Volley.newRequestQueue(buy_tickets.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(buy_tickets.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(buy_tickets.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our
                // key and value pair to our parameters.
                params.put("PassengerID", trip.getPassengerID()+"");
                params.put("TicketID", trip.getTicketID()+"");
                params.put("NumOfBags", trip.getNumOfBags()+"");
                params.put("TripDate", trip.getTripDate()+"");
                params.put("rewardPoints", trip.getRewardPoints()+"");


                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);

    }

    void getTripID(){
        queue = Volley.newRequestQueue(buy_tickets.this);
        String BASE_URL = "http://"+ip.getIp().trim()+"/graduationProject/gettripid.php?id="+trip.getTicketID();
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject= new JSONObject(response);
                            String tripID = jsonObject.getString("TripID");
                            trip.setTripID(Integer.parseInt(tripID));

                            System.out.println(tripID);
                            SharedPreferences session = getSharedPreferences("session",MODE_PRIVATE);
                            SharedPreferences.Editor editor = session.edit();
                            editor.putInt("TripID", trip.getTripID());
                            editor.apply();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(buy_tickets.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }



    public void buyOnClick(View view)  {
        if (total <= scoreNum) {

            AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ask)
                    .setTitle("تأكيد عملية الشراء")
                    .setMessage("التكلفة الإجمالية هي "+total+ " هل تريد المتابعة ؟" )
                    .setPositiveButton("متابعة", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog alertDialog2 = new AlertDialog.Builder(buy_tickets.this)
                                    .setIcon(R.drawable.rewards)
                                    .setTitle(" لقد حصلت على " + rewardPoints() + " نقاط إضافية !" )
                                    .setMessage("تم إضافة النقاط إلى رصيد نقاطك")
                                    .setPositiveButton("حسناً", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            updateScore();
                                            makeTrip();

                                            System.out.println(trip);
                                            insertTrip();
                                            getTripID();



                                            Intent intent = new Intent(buy_tickets.this, tickets_information.class);
                                            int newScore = scoreNum-total+rewardPoints();
                                            intent.putExtra("score",newScore +"");
                                            intent.putExtra("ticketNumber",trip.getTicketID() +"");
                                            startActivity(intent);
                                        }
                                    })
                                    .show();
                        }
                    })

                    .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    })

                    .show();





        }

        else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("خطأ")
                    .setMessage("ليس لديك نقاط كافية")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    })
                    .show();
        }
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
                intent=new Intent(buy_tickets.this, aboutApp.class);
                startActivity(intent);
                break;
            case R.id.myTrips_nav:
                intent=new Intent(buy_tickets.this, MyTrips.class);
                startActivity(intent);
                break;
            case R.id.setting_nav:
                intent=new Intent(buy_tickets.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.logOut_nav:
                intent=new Intent(buy_tickets.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}