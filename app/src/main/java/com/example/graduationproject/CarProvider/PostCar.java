package com.example.graduationproject.CarProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.ChangePassword;
import com.example.graduationproject.IP;
import com.example.graduationproject.LogOut;
import com.example.graduationproject.Logo;
import com.example.graduationproject.Model.carOOB;
import com.example.graduationproject.R;
import com.example.graduationproject.aboutApp;
import com.example.graduationproject.cars;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostCar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    byte compressed_string[]  = new byte[1024];
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView imageToUpload ;
    Button uploadPhoto;
    EditText car_number;
    EditText car_type;
    EditText car_price;
    EditText seats_number;
    EditText gear_type;
    Button post_car;
    Bitmap bitmap;
    String sImage;
    carOOB car = new carOOB();
    SharedPreferences preferences;
    private int providerID;
    IP ip = new IP();


    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_car);
        setUpViews();

        preferences=getSharedPreferences("session",MODE_PRIVATE);
        providerID=preferences.getInt("login",-1);

       // getSupportActionBar().setTitle("إدراج سيارة");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("إضافة سيارة");

        nav();

    }

    private void setUpViews() {
        car_number = findViewById(R.id.car_number);
        imageToUpload = findViewById(R.id.imageToUpload);
        uploadPhoto = findViewById(R.id.uploadPhoto);
        car_type = findViewById(R.id.car_type);
        car_price = findViewById(R.id.car_price);
        seats_number = findViewById(R.id.seats_number);
        gear_type = findViewById(R.id.gear_type);
        post_car = findViewById(R.id.post_car);
    }

    public void uploadPhotoOnClick(View view) {
//        Intent intent = new Intent(this, cars.class);
//        startActivity(intent);


        if (ContextCompat.checkSelfPermission(PostCar.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(PostCar.this
                    , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

        }
        else
        {
            selectImage();
        }



    }

    private void selectImage() {
        imageToUpload.setImageBitmap(null);
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Image"),100);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // check condition
        if (requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectImage();
        }
        else
        {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check condition
        if (requestCode==100 && resultCode==RESULT_OK && data!=null)
        {
            // when result is ok
            // initialize uri
            Uri uri=data.getData();
            // Initialize bitmap
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                // initialize byte stream
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                // compress Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                // Initialize byte array
                imageToUpload.setImageBitmap(bitmap);
                byte[] bytes=stream.toByteArray();
                // get base64 encoded string
                sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void post_car_onClick(View view) {

        if(car_number.getText().toString().isEmpty()){
            Toast.makeText(PostCar.this,("الرجاء ادخال رقم السيارة"), Toast.LENGTH_SHORT).show();
        }
        else if(car_type.getText().toString().isEmpty()){
            Toast.makeText(PostCar.this,("الرجاء ادخال نوع السيارة"), Toast.LENGTH_SHORT).show();
        }
        else if(car_price.getText().toString().isEmpty()){
            Toast.makeText(PostCar.this,("الرجاء ادخال سعر السيارة"), Toast.LENGTH_SHORT).show();
        }
        else if(seats_number.getText().toString().isEmpty()){
            Toast.makeText(PostCar.this,("الرجاء ادخال عدد المقاعد"), Toast.LENGTH_SHORT).show();
        }
        else if(gear_type.getText().toString().isEmpty()){
            Toast.makeText(PostCar.this,("الرجاء ادخال نوع الجير"), Toast.LENGTH_SHORT).show();
        }
        else{
            getCar();
            insertCar();
            Intent intent = new Intent(view.getContext(), PostCar.class);
            startActivity(intent);


        }
    }


    public void insertCar (){
        String url = "http://"+ip.getIp().trim()+"/GraduationProject/postCar.php";
        RequestQueue queue = Volley.newRequestQueue(PostCar.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(PostCar.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(PostCar.this,
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
                params.put("car_number", car.getCar_number()+"");
                params.put("car_image", car.getCarImage()+"");
                params.put("car_type", car.getCar_type()+"");
                params.put("car_price", car.getCar_price()+"");
                params.put("seats_number", car.getSeats_number()+"");
                params.put("gear_type", car.getGear_type()+"");
                params.put("providerID", providerID+"");


                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);

    }

    public void getCar()  {
        car.setCar_number(Integer.parseInt(car_number.getText().toString().trim()));
        car.setCarImage(sImage);
        car.setCar_type(car_type.getText().toString().trim());
        car.setCar_price(Integer.parseInt(car_price.getText().toString().trim()));
        car.setSeats_number(Integer.parseInt(seats_number.getText().toString().trim()));
        car.setGear_type(gear_type.getText().toString().trim());

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
                    case R.id.addCar:
                        startActivity(new Intent(getApplicationContext(),PostCar.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.manage_reservations:
                        startActivity(new Intent(getApplicationContext(),manage_reservations.class));
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

            case R.id.logOut_nav:
                intent=new Intent(PostCar.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}