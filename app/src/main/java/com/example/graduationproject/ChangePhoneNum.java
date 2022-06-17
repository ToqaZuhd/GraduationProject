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

public class ChangePhoneNum extends AppCompatActivity {
    SharedPreferences preferences;
    private int userID;

    EditText changePhone;
    IP ip = new IP ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_num);

        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);
        getSupportActionBar().setTitle("تغيير رقم الهاتف");
        changePhone=findViewById(R.id.editTextPhone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void btnClkChange(View view) {
        String NewPhone=changePhone.getText().toString();
        if(NewPhone.isEmpty()){
            Toast.makeText(ChangePhoneNum.this,
                    "ادخل رقم هاتف الجديد أولاً", Toast.LENGTH_SHORT).show();

        }
        else{

            String url = "http://"+ip.getIp().trim()+"/GraduationProject/changes.php";
            RequestQueue queue = Volley.newRequestQueue(ChangePhoneNum.this);
            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            Toast.makeText(ChangePhoneNum.this,
                                    jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ChangePhoneNum.this,
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
                    params.put("phone", NewPhone);
                    return params;
                }
            };
            queue.add(request);
            Intent intent =new Intent(ChangePhoneNum.this,Setting.class);
            startActivity(intent);
        }

    }
}