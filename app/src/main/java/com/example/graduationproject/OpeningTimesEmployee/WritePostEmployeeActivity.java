package com.example.graduationproject.OpeningTimesEmployee;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.IP;
import com.example.graduationproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WritePostEmployeeActivity extends AppCompatActivity {

    String postDate;
    int numMethod = 0;
    String idPost = "";
    String prevPost = "";
    EditText edtPost;
    TextView postDateEdt;
    Button chooseDate;
    Calendar calendar = Calendar.getInstance();
    IP ip = new IP();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post_employee);
        getSupportActionBar().setTitle("إضافة موعد أو معلومة");

        postDateEdt = findViewById(R.id.datePost2);
        edtPost = findViewById(R.id.PostEmployeeEdt);
        chooseDate = findViewById(R.id.chooseDate);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       /* if (intent != null) {
            numMethod = 1;
            idPost = intent.getStringExtra("id");
            postDate = intent.getStringExtra("prevDatePost");
            prevPost = intent.getStringExtra("prevPost");
        }*/
        if(intent.getStringExtra("prevPost") != null){
            numMethod = 1;

            idPost = intent.getStringExtra("idPost");

            postDate = intent.getStringExtra("prevDatePost");
            postDateEdt.setText(postDate);
            Toast.makeText(WritePostEmployeeActivity.this, postDate,Toast.LENGTH_SHORT).show();
            prevPost = intent.getStringExtra("prevPost");
            edtPost.setText(prevPost);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void selectDate( View view) {
        DatePickerDialog.OnDateSetListener setListener;

        final int year2 = calendar.get(Calendar.YEAR);
        final int month2 = calendar.get(Calendar.MONTH);
        final int day2 = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(WritePostEmployeeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        month = month + 1;
                        String date = "";
                        if (String.valueOf(month).length() == 1 && String.valueOf(day).length() == 1)
                            date = year + "-0" + month + "-0" + day;
                        else if (String.valueOf(month).length() > 1 && String.valueOf(day).length() == 1)
                            date = year + "-" + month + "-0" + day;
                        else if (String.valueOf(month).length() == 1 && String.valueOf(day).length() > 1)
                            date = year + "-0" + month + "-" + day;
                        else
                            date = year + "-" + month + "-" + day;

                        postDateEdt.setText(date);
                        postDate = date;


                    }
                }, year2, month2, day2);
                datePickerDialog.show();

    }



    public void btnClkPostByEm(View view) {

        String postData = edtPost.getText().toString();

        if (numMethod != 1) {
            if (postDate == null) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = df.format(c);
                String date = formattedDate;

                addPost(date, postData);

            } else {
                addPost(postDate, postData);
            }
        } else {
            updatePost(idPost, postDate, postData);
        }
        Intent intent = new Intent(WritePostEmployeeActivity.this, DaysAndTimesOpeningEmployeeActivity.class);
        startActivity(intent);

    }

    private void addPost(String date, String post) {
        String url = "http://"+ip.getIp().trim()+"/GraduationProject/addPostData.php";
        RequestQueue queue = Volley.newRequestQueue(WritePostEmployeeActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(WritePostEmployeeActivity.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WritePostEmployeeActivity.this,
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

                params.put("date", date);
                params.put("post", post);


                return params;
            }
        };
        queue.add(request);
    }

    private void updatePost(String id, String date, String post) {
        String url = "http://"+ip.getIp().trim()+"/GraduationProject/updatePostData.php";
        RequestQueue queue = Volley.newRequestQueue(WritePostEmployeeActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(WritePostEmployeeActivity.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WritePostEmployeeActivity.this,
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

                params.put("id", id);
                params.put("date", date);
                params.put("post", post);


                return params;
            }
        };
        queue.add(request);
    }


}