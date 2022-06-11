package com.example.graduationproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private EditText edtUserName, edtIDNumber,edtPhoneNum,edtPassword,edtConfirm,edtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        edtUserName = findViewById(R.id.UserName);
        edtIDNumber = findViewById(R.id.IDNumber);
        edtPassword = findViewById(R.id.Password);
        edtConfirm=findViewById(R.id.ConfirmPassword);
        edtPhoneNum=findViewById(R.id.phoneNumber);
        edtEmail=findViewById(R.id.editTextTextEmailAddress);

    }


    public void btnClkRegister(View view) {
        String UserName = edtUserName.getText().toString();
        String IDNumber = edtIDNumber.getText().toString();
        String Password = edtPassword.getText().toString();
        String confirmPassword = edtConfirm.getText().toString();
        String PhoneNumber = edtPhoneNum.getText().toString();
        String email=edtEmail.getText().toString();
        if (UserName.isEmpty())
            Toast.makeText(Registration.this,("ادخل الاسم أولاً"), Toast.LENGTH_SHORT).show();

        else if (IDNumber.isEmpty())
            Toast.makeText(Registration.this,("ادخل رقم الهوية أولاً"), Toast.LENGTH_SHORT).show();

        else if (PhoneNumber.isEmpty())
            Toast.makeText(Registration.this,("ادخل رقم الهاتف أولاً"), Toast.LENGTH_SHORT).show();

        else if (email.isEmpty())
            Toast.makeText(Registration.this,("ادخل رقم الهاتف أولاً"), Toast.LENGTH_SHORT).show();

        else if (Password.isEmpty())
            Toast.makeText(Registration.this,("ادخل كملة المرور"), Toast.LENGTH_SHORT).show();

        else if (confirmPassword.isEmpty())
            Toast.makeText(Registration.this,("ادخل كلمة السر مرة أخرى"), Toast.LENGTH_SHORT).show();

        else if (!Password.equals(confirmPassword))
            Toast.makeText(Registration.this,("لا يوجد تطابق بين كلمة السر وإعادتها"), Toast.LENGTH_SHORT).show();


        else check_Email_Validation(UserName, IDNumber,PhoneNumber,email ,Password);
    }

    private void addPerson(String UserName, String IDNum,String PhoneNum,String Email ,String Password){


        String url = "http://10.0.2.2:80/GraduationProject/AddUsers.php";
        RequestQueue queue = Volley.newRequestQueue(Registration.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(Registration.this,
                                jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        SharedPreferences session = getSharedPreferences("session",MODE_PRIVATE);
                        SharedPreferences.Editor editor = session.edit();
                        editor.putInt("login", Integer.parseInt(IDNum));
                        Intent intent =new Intent(Registration.this,MainActivity.class);
                        startActivity(intent);

                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(Registration.this,
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
                params.put("username", UserName);
                params.put("IDNum", IDNum);
                params.put("phoneNum", PhoneNum);
                params.put("email", Email);
                params.put("password", Password);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);



    }



    public void btnClkForLogin(View view) {
        Intent intent =new Intent(Registration.this,SignIn.class);
        startActivity(intent);


    }

    public void check_Email_Validation(String UserName, String IDNum,String PhoneNum,String Email ,String Password) {
        String url = "https://emailvalidation.abstractapi.com/v1/?api_key=a703d1c9abc24588ab6fbf208d26d057&email=" +Email;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,
                        null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject validationObj = response.getJSONObject("is_valid_format");
                            String isValid=validationObj.getString("value");

                            if (isValid.equals("true")){
                                JSONObject verificationObj = response.getJSONObject("is_smtp_valid");
                                String isVerify=verificationObj.getString("value");

                                if (isVerify.equals("true")){

                                    addPerson(UserName, IDNum,PhoneNum,Email ,Password);

                                }
                                else {
                                    Toast.makeText(Registration.this,
                                            "الرجاء إدخال إيميلك الصحيح", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(Registration.this,
                                        "الرجاء إدخال إيميلك الصحيح", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}