package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;


import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.graduationproject.Model.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class postNews extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences preferences;
    private int userID;
    private RequestQueue queue;
    private RequestQueue queue1;
    IP ip = new IP ();
    private final String BASE_URL = "http://"+ip.getIp().trim()+"/GraduationProject/getPostData.php";
    RecyclerView recycler;

    List<Post> posts=new ArrayList<>();



    CircleImageView imageView;
    String UserName,imageName;

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_news);
        preferences=getSharedPreferences("session",MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("نشر الأخبار");




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

        userID=preferences.getInt("login",-1);
        getImage();

        recycler = findViewById(R.id.recyclerView);
        imageView=findViewById(R.id.imageView9);

        getSupportActionBar().setTitle("آخر الأخبار");

            queue = Volley.newRequestQueue(this);
            queue1 = Volley.newRequestQueue(this);
            if (savedInstanceState!=null)
                onRestoreInstanceState(savedInstanceState);


            populateAllData();



    }

    public void getImage(){
        queue = Volley.newRequestQueue(postNews.this);
        String BASE_URL = "http://"+ip.getIp().trim()+"/GraduationProject/searchName.php?id="+userID;
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            JSONObject jsonObject= new JSONObject(response);
                            imageName = jsonObject.getString("image");
                            if(!(imageName.isEmpty())){
                                // decode base64 string
                                byte[] bytes= Base64.decode(imageName,Base64.DEFAULT);
                                // Initialize bitmap
                                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                // set bitmap on imageView
                                imageView.setImageBitmap(bitmap);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(postNews.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    public void populateAllData(){

        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray roomList=new JSONArray(response);
                            for(int i=0;i<roomList.length();i++){
                                JSONObject jsonObject= roomList.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String date = jsonObject.getString("date");

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formattedDate = df.format(c.getTime());
                                Date currDate=df.parse(formattedDate);
                                Date inputDate = df.parse(date);


                                long diff = currDate.getTime() - inputDate.getTime();
                                long seconds = diff / 1000;
                                long minutes = seconds / 60;
                                long hours = minutes / 60;


                                if (hours>0)
                                    date="منذ "+hours+" ساعة";
                                else if (minutes>0)
                                    date="منذ "+minutes+" دقيقة";
                                else
                                    date="منذ "+seconds+" ثانية";
                                String text = jsonObject.getString("post");
                                String imageName = jsonObject.getString("image");

                                Post post = new Post(name, date, text, imageName);
                                posts.add(post);

                            }

                            recycler.setLayoutManager(new LinearLayoutManager(postNews.this));
                            postCardView adapter = new postCardView(postNews.this,posts);

                            recycler.setAdapter(adapter);


                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(postNews.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


//    public void getName() {
//        String url = "http://"+ip.getIp().trim()+"/GraduationProject/searchName.php?id=" + userID;
//        RequestQueue queue = Volley.newRequestQueue(postNews.this);
//        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//
//                    UserName = jsonObject.getString("name");
//                    imageName=jsonObject.getString("image");
//
//                    Glide.with(postNews.this).load(imageName).into(imageView);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(postNews.this,
//                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        queue.add(request);
//
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.aboutApp_nav:
                intent=new Intent(postNews.this, aboutApp.class);
                startActivity(intent);
                break;
            case R.id.myTrips_nav:
                intent=new Intent(postNews.this, MyTrips.class);
                startActivity(intent);
                break;
            case R.id.setting_nav:
                intent=new Intent(postNews.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.logOut_nav:
                intent=new Intent(postNews.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View view) {
        Intent intent =new Intent(postNews.this,writePost.class);
        intent.putExtra("name",UserName);
        intent.putExtra("image",imageName);
        System.out.println("ImageSizzze"+imageName.length());
        startActivity(intent);
    }
}
