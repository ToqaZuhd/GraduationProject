package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class writePost extends AppCompatActivity{



    SharedPreferences preferences;
    private int userID;

//    TextView Name;
    EditText edtPost;
    Button btnPost;
    CircleImageView imageView;
    Toolbar toolbar;
    IP ip = new IP ();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);


        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);
        Intent intent=getIntent();
//        String userName=intent.getStringExtra("name");
//        String imageName=intent.getStringExtra("image");
//
//        Name=findViewById(R.id.Name);
//        Name.setText(userName);
//
//        imageView=findViewById(R.id.imageView);
//        Glide.with(writePost.this).load(imageName).into(imageView);

        edtPost=findViewById(R.id.Post);
        btnPost=findViewById(R.id.btnPost);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("كتابة منشور");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void btnClkPost(View view) {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String date = formattedDate ;


        String post=edtPost.getText().toString().trim();

        if (post.isEmpty())
           Toast.makeText(writePost.this,
                "اكتب المنشور أولاً", Toast.LENGTH_SHORT).show();
        else
        addPost(String.valueOf(userID),date,post);


    }

    private void addPost(String userID, String date, String post) {


        String url = "http://"+ip.getIp().trim()+"/graduationProject/addPost.php";
        RequestQueue queue = Volley.newRequestQueue(writePost.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);



                    Toast.makeText(writePost.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        Intent intent =new Intent(writePost.this,postNews.class);
                        startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(writePost.this,
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

                params.put("user_id", userID);
                params.put("date", date);
                params.put("post", post);


                return params;
            }
        };
        queue.add(request);
    }


}