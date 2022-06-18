package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.Model.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Review extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RequestQueue queue;
    IP ip = new IP();
    private final String BASE_URL = "http://"+ip.getIp().trim()+"/GraduationProject/getReview.php";
    RecyclerView recycler;

    List<Post> posts=new ArrayList<>();

    Toolbar toolbar;
    //Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircleImageView imageView;
    String imageName;
    EditText edtPost;
    RatingBar ratingBar;
    SharedPreferences preferences;
    private int userID;
     float rate;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("شراء التذاكر");


        nav ();


        recycler = findViewById(R.id.review_recycler);
        imageView=findViewById(R.id.imageView9);
        edtPost=findViewById(R.id.Post);
        ratingBar=findViewById(R.id.ratingBar);



        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);
        getImage();



        queue = Volley.newRequestQueue(this);
        if (savedInstanceState!=null)
            onRestoreInstanceState(savedInstanceState);

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
                    case R.id.profile:r:
                    startActivity(new Intent(getApplicationContext(),profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        populateReviwes();


    }
    public void populateReviwes(){

        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray postList=new JSONArray(response);

                            for(int i=0;i<postList.length();i++){

                                JSONObject jsonObject= postList.getJSONObject(i);
                                Post post = new Post();

                                String name = jsonObject.getString("name");
                                post.setName(name);

                                String userImage = jsonObject.getString("image");


                                    // decode base64 string
                                    byte[] bytes= Base64.decode(userImage,Base64.DEFAULT);
                                    // Initialize bitmap
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                    // set bitmap on imageView
//                                    imageView.setImageBitmap(bitmap);
                                    post.setImageURL(userImage);



                                String date = jsonObject.getString("date");
                                post.setDate(date);

                                String text = jsonObject.getString("review");
                                post.setText(text);

                               rate=(float) jsonObject.getDouble("rate");
                               post.setReview(rate);






                                posts.add(post);

                            }

                            recycler.setLayoutManager(new LinearLayoutManager(Review.this));
                            reviewCardView adapter = new reviewCardView(Review.this,posts);

                            recycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Review.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    public void btnClkPost2(View view) {

        boolean check;
//        Date c = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//        String formattedDate = df.format(c);
//        String date = formattedDate ;
        java.sql.Date date=new java.sql.Date(System.currentTimeMillis());


        String post=edtPost.getText().toString().trim();
        float rateUser=ratingBar.getRating();

//        if (rate==rateUser || rateUser==0)
//            flag=false;
//        else
//            flag=true;



        System.out.println(rateUser);
        if(rateUser==0.0  ){
            Toast.makeText(Review.this,"أدخل التقييم من 5", Toast.LENGTH_SHORT).show();
        }

        else if (post.isEmpty()){
            Toast.makeText(Review.this,"أدخل رأيك في المكان المخصص", Toast.LENGTH_SHORT).show();
        }


        else if (!post.isEmpty() &&rateUser!=0.0)
        {
        String url = "http://"+ip.getIp().trim()+"/GraduationProject/addReview.php";
        RequestQueue queue = Volley.newRequestQueue(Review.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(Review.this,"تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(Review.this,Review.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Review.this,
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

                params.put("user_id", userID+"");
                params.put("date", date.toString());
                params.put("post", post);
                params.put("rate", String.valueOf(rateUser));


                return params;
            }
        };
        queue.add(request);

        }

    }


    public void getImage(){
        queue = Volley.newRequestQueue(Review.this);
        String BASE_URL = "http://"+ip.getIp().trim()+"/graduationProject/searchName.php?id="+userID;
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
                Toast.makeText(Review.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

//    public void getImage() {
//
//        String url = "http://"+ip.getIp().trim()+"/GraduationProject/searchName.php?id=" +userID;
//        RequestQueue queue = Volley.newRequestQueue(Review.this);
//        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    Float rate=(float)jsonObject.getDouble("rate");
//                    if(rate!=null) {
//                        ratingBar.setRating(rate);
//                    }
//                    imageName = jsonObject.getString("image");
//                    if(!(imageName.isEmpty())){
//                        // decode base64 string
//                        byte[] bytes= Base64.decode(imageName,Base64.DEFAULT);
//                        // Initialize bitmap
//                        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                        // set bitmap on imageView
//                        imageView.setImageBitmap(bitmap);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Review.this,
//                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        queue.add(request);
//
//    }


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
                intent=new Intent(Review.this, aboutApp.class);
                startActivity(intent);
                break;
            case R.id.myTrips_nav:
                intent=new Intent(Review.this, MyTrips.class);
                startActivity(intent);
                break;
            case R.id.setting_nav:
                intent=new Intent(Review.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.logOut_nav:
                intent=new Intent(Review.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}