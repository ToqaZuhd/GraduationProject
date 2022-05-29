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
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    SharedPreferences preferences;
    private int userID;
    EditText previousPass,NewPass,confirmPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setTitle("تغيير كلمة المرور");
        preferences=getSharedPreferences("session",MODE_PRIVATE);
        userID=preferences.getInt("login",-1);
        previousPass=findViewById(R.id.editPreviousPass);
        NewPass=findViewById(R.id.editNewPass);
        confirmPass=findViewById(R.id.editConfirmPass);


    }

    public void checkPass(){
        String url = "http://10.0.2.2/GraduationProject/searchName.php?id=" + userID;
        RequestQueue queue = Volley.newRequestQueue(ChangePassword.this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String pass=jsonObject.getString("password");
                    if (pass.equals(previousPass.getText().toString()))
                         comparePass();
                    else
                        Toast.makeText(ChangePassword.this,
                                "كلمة السر الحالية غير صحيحة", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePassword.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }


    public void comparePass(){

        String newPassword=NewPass.getText().toString();

        if (newPassword.equals(confirmPass.getText().toString())){
                String url = "http://10.0.2.2/GraduationProject/changes.php";
                RequestQueue queue = Volley.newRequestQueue(ChangePassword.this);
                StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                Toast.makeText(ChangePassword.this,
                                        jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangePassword.this,
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
                        params.put("pass", newPassword);
                        return params;
                    }
                };
                queue.add(request);
                Intent intent =new Intent(ChangePassword.this,Setting.class);
                startActivity(intent);
        }

        else {
            Toast.makeText(ChangePassword.this,
                    "لا يوجد تطابق, أعد كتابة كلمة السر الجديدة أو أعد كتابة تأكيدها", Toast.LENGTH_SHORT).show();
        }


    }

    public void btnClkChange(View view) {

        if (previousPass.getText().toString().isEmpty()){
            Toast.makeText(ChangePassword.this,"ادخل كلمة السر الحالية أولاً", Toast.LENGTH_SHORT).show();
        }

        else if (NewPass.getText().toString().isEmpty()){
            Toast.makeText(ChangePassword.this,"ادخل كلمة السر الجديدة أولاً", Toast.LENGTH_SHORT).show();
        }
        else if (confirmPass.getText().toString().isEmpty()){
            Toast.makeText(ChangePassword.this,"ادخل تأكيد كلمة السر أولاً", Toast.LENGTH_SHORT).show();
        }

        else
            checkPass();

    }

}