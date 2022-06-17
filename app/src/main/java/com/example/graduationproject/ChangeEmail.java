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

import java.util.HashMap;
import java.util.Map;

public class ChangeEmail extends AppCompatActivity {
    SharedPreferences preferences;
    private int userID;
    EditText changeEmail;
    IP ip = new IP();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        getSupportActionBar().setTitle("تغيير الإيميل");
        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);
        changeEmail=findViewById(R.id.editTextEmail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void btnClkChange(View view) {
        String NewEmail=changeEmail.getText().toString();
        if(NewEmail.isEmpty()){
            Toast.makeText(ChangeEmail.this,
                    "ادخل الاسم الجديد أولاً", Toast.LENGTH_SHORT).show();

        }
        else{

            String url = "http://"+ip.getIp().trim()+"/GraduationProject/changes.php";
            RequestQueue queue = Volley.newRequestQueue(ChangeEmail.this);
            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            Toast.makeText(ChangeEmail.this,
                                    jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ChangeEmail.this,
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
                    params.put("email", NewEmail);
                    return params;
                }
            };
            queue.add(request);
            Intent intent =new Intent(ChangeEmail.this,Setting.class);
            startActivity(intent);
        }
    }
}