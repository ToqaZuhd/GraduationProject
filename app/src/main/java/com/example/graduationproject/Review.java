package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.bumptech.glide.Glide;
import com.example.graduationproject.Model.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Review extends AppCompatActivity {

    private RequestQueue queue;
    private static final String BASE_URL = "http://10.0.2.2:82/GraduationProject/getReview.php";
    RecyclerView recycler;

    List<Post> posts=new ArrayList<>();

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
        recycler = findViewById(R.id.review_recycler);
        imageView=findViewById(R.id.imageView);
        edtPost=findViewById(R.id.Post);
        ratingBar=findViewById(R.id.ratingBar);

        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);
        getImage();

        getSupportActionBar().setTitle("التقييمات والآراء");

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

        populateAllData();


    }
    public void populateAllData(){

        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray postList=new JSONArray(response);

                            for(int i=0;i<postList.length();i++){

                                JSONObject jsonObject= postList.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String date = jsonObject.getString("date");

                               rate=(float) jsonObject.getDouble("rate");
                                String text = jsonObject.getString("review");
                                String imageName = jsonObject.getString("image");

                                Post post = new Post(name, date, text, rate, imageName);
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
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String date = formattedDate ;


        String post=edtPost.getText().toString().trim();
        float rateUser=ratingBar.getRating();

        if (rate==rateUser || rateUser==0)
            flag=false;
        else
            flag=true;

        if (!post.isEmpty() || flag)
        {
        String url = "http://10.0.2.2:82/GraduationProject/addReview.php";
        RequestQueue queue = Volley.newRequestQueue(Review.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(Review.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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

                params.put("user_id", String.valueOf(userID));
                params.put("date", date);
                params.put("post", post);
                params.put("rate", String.valueOf(rateUser));


                return params;
            }
        };
        queue.add(request);
            Intent intent =new Intent(Review.this,Review.class);
            startActivity(intent);
        }
        if(rateUser==0){
            Toast.makeText(Review.this,"fill rate please", Toast.LENGTH_SHORT).show();
        }
    }


    public void getImage() {

        String url = "http://10.0.2.2:82/GraduationProject/searchName.php?id=" +userID;
        RequestQueue queue = Volley.newRequestQueue(Review.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Float rate=(float)jsonObject.getDouble("rate");
                    if(rate!=null) {
                        ratingBar.setRating(rate);
                    }
                    imageName=jsonObject.getString("image");
                    Glide.with(Review.this).load(imageName).into(imageView);

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
        });

        queue.add(request);

    }

}