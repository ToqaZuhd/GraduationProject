package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class carDescription extends AppCompatActivity {
    private RequestQueue queue;
    int carID ;
    int TripID;
    ImageView car_image ;
    TextView car_number;
    TextView car_type;
    TextView car_price;
    TextView seats_number;
    TextView car_provider;
    TextView provider_numbertxt;
    TextView gear_type;
    NumberPicker days_num ;
    Button rentCar;
    SharedPreferences preferences;
    int days=1;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_description);

        preferences=getSharedPreferences("session",MODE_PRIVATE);
        TripID=preferences.getInt("TripID",-1);

        Intent intent = getIntent();
        carID = intent.getIntExtra("carID", 0);

        populateviews();

        car_number.setText(" رقم السيارة : " + carID);
        getCarData();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("تفاصيل الإيجار");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void populateviews() {
        car_image = findViewById(R.id.imageView);
        car_number = findViewById(R.id.car_number);
        car_type = findViewById(R.id.car_type);
        car_price = findViewById(R.id.car_price);
        seats_number = findViewById(R.id.seats_number);
        car_provider = findViewById(R.id.car_provider);
        provider_numbertxt = findViewById(R.id.providor_number);
        gear_type = findViewById(R.id.gear_type);
        days_num = findViewById(R.id.days_num);

        days_num.setMinValue(1);
        days_num.setMaxValue(30);
        days_num.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                days=newVal;

            }
        });




        rentCar = findViewById(R.id.rent);

    }



    public void getCarData(){
        queue = Volley.newRequestQueue(carDescription.this);
        String BASE_URL = "http://10.0.2.2/graduationProject/searchcarid.php?id="+carID;
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject= new JSONObject(response);

                            String imagetxt = jsonObject.getString("car_image");
                            if(!(imagetxt.isEmpty())){
                                // decode base64 string
                                byte[] bytes= Base64.decode(imagetxt,Base64.DEFAULT);
                                // Initialize bitmap
                                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                // set bitmap on imageView
                                car_image.setImageBitmap(bitmap);
                            }


                            String type = jsonObject.getString("car_type");
                            car_type.setText("نوع السيارة : "+type);

                            String price = jsonObject.getString("car_price");
                            car_price.setText(" السعر : "+price +" دينار أردني لليوم الواحد");

                            String seats = jsonObject.getString("seats_number");
                            seats_number.setText("عدد المقاعد : "+seats );

                            String gear = jsonObject.getString("gear_type");
                            gear_type.setText("نوع الجير : "+gear );


                            getProviderNameAndNumber ();





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(carDescription.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    private void getProviderNameAndNumber() {
        queue = Volley.newRequestQueue(carDescription.this);
        String BASE_URL = "http://10.0.2.2/graduationProject/searchcarprovider.php?id="+carID;
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject= new JSONObject(response);
                            String provider_name = jsonObject.getString("name");
                            car_provider.setText("اسم المزود : "+provider_name);

                            String provider_number = jsonObject.getString("number");
                            provider_numbertxt.setText("رقم المزود : "+provider_number);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(carDescription.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }


    public void rentCar(View view) {
        String url = "http://10.0.2.2/graduationProject/addRentRequest.php";
        RequestQueue queue = Volley.newRequestQueue(carDescription.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(carDescription.this,
                                jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(carDescription.this, successfullOperation.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(carDescription.this,
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


                System.out.println(TripID);
                params.put("TripID", String.valueOf(TripID));
                params.put("CarNumber", String.valueOf(carID));
                java.sql.Date date=new java.sql.Date(System.currentTimeMillis());
                params.put("rentDate", date.toString());
                params.put("daysNumber", String.valueOf(days));


                return params;
            }
        };
        queue.add(request);


    }
}