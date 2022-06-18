package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class chooseProfileImage extends AppCompatActivity {

    ImageView image ;
    Button add;
    Button button;
    String sImage;
    Toolbar toolbar;
    private RequestQueue queue;
    SharedPreferences preferences;
    private int userID;
    IP ip = new IP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_profile_image);

        image = findViewById(R.id.imageToUpload);
        add = findViewById(R.id.uploadPhoto);
        button = findViewById(R.id.button3);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("اختيار صورة شخصية");

        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);

    }

    public void uploadPhotoOnClick(View view) {

        if (ContextCompat.checkSelfPermission(chooseProfileImage.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(chooseProfileImage.this
                    , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

        }
        else
        {
            selectImage();
        }



    }

    private void selectImage() {
        image.setImageBitmap(null);
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
                image.setImageBitmap(bitmap);
                byte[] bytes=stream.toByteArray();
                // get base64 encoded string
                sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPhoto(View view) {
        String url = "http://"+ip.getIp().trim()+"/GraduationProject/AddUsers.php";
        RequestQueue queue = Volley.newRequestQueue(chooseProfileImage.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(chooseProfileImage.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(chooseProfileImage.this,profile.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(chooseProfileImage.this,
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
                params.put("image", sImage);
                params.put("IDNum", String.valueOf(userID));


                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);


    }
}