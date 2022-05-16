package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChangeName extends AppCompatActivity {
    SharedPreferences preferences;
    private int userID;
    EditText changeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        getSupportActionBar().setTitle("تغيير الاسم");
        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);
        changeName=findViewById(R.id.editTextName);
    }

    public void btnClkChange(View view) {
            String NewName=changeName.getText().toString();
            if(NewName.isEmpty()){
                Toast.makeText(ChangeName.this,
                        "ادخل الاسم الجديد أولاً", Toast.LENGTH_SHORT).show();

            }
           else{

                String url = "http://10.0.2.2:82/GraduationProject/changes.php";
                RequestQueue queue = Volley.newRequestQueue(ChangeName.this);
                StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                Toast.makeText(ChangeName.this,
                                        jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangeName.this,
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
                        params.put("name", NewName);
                        return params;
                    }
                };
                queue.add(request);

                Intent intent =new Intent(ChangeName.this,Setting.class);
                startActivity(intent);
            }

        }


}