package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.CarProvider.PostCar;
import com.example.graduationproject.CarProvider.manage_reservations;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RequestQueue queue;
    SharedPreferences preferences;
    private int userID;
    CircleImageView imageView;
    String imageName;
    TextView edtName;
    TextView edtEmail;
    TextView edtPhone;
    TextView edtID;
    TextView edtScore;
    Button imgButton;
    String sImage;

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    IP ip = new IP ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //getSupportActionBar().setTitle("حسابي");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("حسابي");


        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);

        imageView=findViewById(R.id.imageView9);
        edtName=findViewById(R.id.Name);
        edtEmail=findViewById(R.id.emailDataBase);
        edtPhone=findViewById(R.id.phoneDataBase);
        edtID=findViewById(R.id.IdDataBase);
        edtScore=findViewById(R.id.scoreDataBase);
        imgButton=findViewById(R.id.imageButton);
//        imgButton.setOnClickListener(changePhoto);
        populateAllData();
        nav();

    }




    public void populateAllData(){
        queue = Volley.newRequestQueue(profile.this);
        String BASE_URL = "http://"+ip.getIp().trim()+"/GraduationProject/searchName.php?id="+userID;
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                                JSONObject jsonObject= new JSONObject(response);
                                String name = jsonObject.getString("name");
                                edtName.setText(name);

                                String email = jsonObject.getString("email");
                                edtEmail.setText(email);

                                String phone = jsonObject.getString("phoneNum");
                                edtPhone.setText(phone);

                                String score = jsonObject.getString("score");
                                edtScore.setText(score);

                                edtID.setText(String.valueOf(userID));

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
                Toast.makeText(profile.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

//    public View.OnClickListener changePhoto =new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Intent i = new Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//            final int ACTIVITY_SELECT_IMAGE = 1234;
//            startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
//        }
//    } ;




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
                intent=new Intent(profile.this, aboutApp.class);
                startActivity(intent);
                break;
            case R.id.myTrips_nav:
                intent=new Intent(profile.this, MyTrips.class);
                startActivity(intent);
                break;
            case R.id.setting_nav:
                intent=new Intent(profile.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.logOut_nav:
                intent=new Intent(profile.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void addPhoto(View view) {
        Intent intent = new Intent(this, chooseProfileImage.class);
        startActivity(intent);
    }
}