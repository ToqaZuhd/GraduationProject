package com.example.graduationproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.CarProvider.PostCar;
import com.example.graduationproject.Model.user;
import com.example.graduationproject.PointsCharger.chargePoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;


public class SignIn extends AppCompatActivity {
    private EditText  edtPassword , edtEmail;
    private CheckBox edtRemember;
    private int id;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtEmail =findViewById(R.id.EmailNum);
        edtPassword=findViewById(R.id.Password);
        edtRemember=findViewById(R.id.checkBox1);

        String ipFromHost = findIpv4();
        Toast.makeText(SignIn.this, ipFromHost, Toast.LENGTH_SHORT).show();
        SharedPreferences preferences=getSharedPreferences("session",MODE_PRIVATE);
        int session=preferences.getInt("login",-1);
        String role=preferences.getString("role","");
        if (session!=-1){
            Intent intent;
            if(role.equals("user")){
                intent = new Intent(SignIn.this, postNews.class);
                startActivity(intent);

            }

            if(role.equals("car_provider")){
                intent = new Intent(SignIn.this, PostCar.class);
                startActivity(intent);

            }
//            else{
//                intent = new Intent(SignIn.this, EmployeeResRoom.class);
//            }
        }

        SharedPreferences remember = getSharedPreferences("checkBox", MODE_PRIVATE);
        String rem = remember.getString("remember", "");
        if (rem.equals("true")) {
            String Email = remember.getString("email", "");
            String pass = remember.getString("password", "");
            edtEmail.setText(Email);
            edtPassword.setText(pass);

        }


    }



    public void btnClkForRegister(View view) {
        Intent intent =new Intent(SignIn.this,Registration.class);
        startActivity(intent);

    }



    public void btnClkLogin(View view) {
       String Email = edtEmail.getText().toString();
       String pass= edtPassword.getText().toString();

        if (Email.isEmpty())
            Toast.makeText(SignIn.this,("ادخل الإيميل"), Toast.LENGTH_SHORT).show();

        else if (pass.isEmpty())
            Toast.makeText(SignIn.this,("ادخل كلمة السر"), Toast.LENGTH_SHORT).show();

        else {
            String ipFromHost = findIpv4();
            String url = "http://192.168.1.142/graduationProject/Search.php?email="+Email+"&pass="+pass;
            RequestQueue queue = Volley.newRequestQueue(SignIn.this);
            StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("true"))
                        Toast.makeText(SignIn.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    else{
                        id=Integer.parseInt(jsonObject.getString("id"));
                        role=jsonObject.getString("role");

                        if (edtRemember.isChecked()){
                            SharedPreferences remember = getSharedPreferences("checkBox",MODE_PRIVATE);
                            SharedPreferences.Editor editor = remember.edit();
                            String rem=remember.getString("remember","");
                            if (!rem.equals("true")){
                                editor.putString("remember","true");
                                editor.putString("email", Email);
                                editor.putString("password",pass);
                                editor.apply();}
                        }
                        else{
                            SharedPreferences remember = getSharedPreferences("checkBox",MODE_PRIVATE);
                            SharedPreferences.Editor editor = remember.edit();
                            editor.putString("remember", "false");
                            editor.apply();
                        }

                        SharedPreferences session = getSharedPreferences("session",MODE_PRIVATE);
                        SharedPreferences.Editor editor = session.edit();
                        editor.putInt("login", id);
                        editor.putString("role", role);

                        editor.apply();

                        Intent intent;
                        if (role.equals("user")){
                            intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);

                        }

                        else if (role.equals("car_provider")){
                            intent = new Intent(SignIn.this, PostCar.class);
                            startActivity(intent);

                        }

                        else if (role.equals("points_charger")){
                            intent = new Intent(SignIn.this, chargePoints.class);
                            startActivity(intent);

                        }

//                        else {
//                            intent = new Intent(SignIn.this, EmployeeResRoom.class);
//                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }} ,new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {Toast.makeText(SignIn.this,
                    "Fail to get response = " + error, Toast.LENGTH_SHORT).show();}});

           queue.add(request);



    }



    }
    public String findIpv4(){
        String ip = "";
        Enumeration<NetworkInterface> e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements()) {
                NetworkInterface n = e.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = ee.nextElement();
                    if(i.getHostAddress().startsWith("192.168"))
                        ip = i.getHostAddress();
                    // System.out.println(i.getHostAddress());
                    // Do stuff here
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ip;
    }

    public void btnClkSentEmail(View view) {
        String IDNum = edtEmail.getText().toString();
        if(IDNum.isEmpty()){
            Toast.makeText(SignIn.this,
                    "ادخل الاإيميل", Toast.LENGTH_SHORT).show();
        }
        else{
            String url = "http://10.0.2.2/GraduationProject/Search.php?email="+IDNum;
            RequestQueue queue = Volley.newRequestQueue(SignIn.this);
            StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("true"))
                            Toast.makeText(SignIn.this,
                                    jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        else{
                            String realPassword;
                            realPassword=jsonObject.getString("password");
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_EMAIL,IDNum);
                            sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Your password");
                            sendIntent.putExtra(Intent.EXTRA_TEXT, realPassword);
                            sendIntent.setType("message/rfc822");

                            Intent shareIntent = Intent.createChooser(sendIntent, "choose an IDNum client");
                            startActivity(shareIntent);
                          }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }} ,new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {Toast.makeText(SignIn.this,
                        "Fail to get response = " + error, Toast.LENGTH_SHORT).show();}});

            queue.add(request);


        }
    }

}