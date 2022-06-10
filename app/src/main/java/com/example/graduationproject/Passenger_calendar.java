package com.example.graduationproject;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.graduationproject.Model.PostEmployee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Passenger_calendar extends AppCompatActivity {

    // Define the variable of CalendarView type
    // and TextView type;
    CalendarView calendar;
    TextView date_view;
    TextView dayInfo;
    Toolbar toolbar;
    final List <PostEmployee> list = new ArrayList<>() ;
    private RequestQueue queue;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_calendar);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("تحديد تاريخ");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        calendar = (CalendarView)findViewById(R.id.calendar);
        date_view = (TextView)findViewById(R.id.date_view);
        dayInfo = (TextView)findViewById(R.id.dayInfo);

        date_view.setText(new java.sql.Date(System.currentTimeMillis()).toString());
        queue = Volley.newRequestQueue(this);

        getPosts();



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getPosts() {
        List<PostEmployee> employeeList = new ArrayList<>();
        String BASE_URL = "http://10.0.2.2/GraduationProject/getPostDataEmployee.php";
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray employeePosts = new JSONArray(response);
                            for (int i = 0; i < employeePosts.length(); i++) {
                                JSONObject jsonObject = employeePosts.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String date = jsonObject.getString("date");
                                String post = jsonObject.getString("post");


                                employeeList.add(new PostEmployee(id, date, post));


                            }

                            for(int i=0 ; i <employeeList.size();i++){
                                if(employeeList.get(i).getDate().trim().equals(date_view.getText().toString().trim()) ){
                                    dayInfo.setText(employeeList.get(i).getText());
                                    break;
                                }

                                else {
                                    dayInfo.setText("ساعات الدوام : من الساعة الثامنة صباحاً إلى الرابعة عصراً ");
                                }
                            }

                            calendar
                                    .setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                                        @Override
                                        public void onSelectedDayChange(
                                                CalendarView view,
                                                int year,
                                                int month,
                                                int day)
                                        {

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



                                            date_view.setText(date);

                                            for(int i=0 ; i <employeeList.size();i++){
                                                if(employeeList.get(i).getDate().trim().equals(date.trim()) || employeeList.get(i).getDate().trim().equals(date_view.getText().toString().trim()) ){
                                                    dayInfo.setText(employeeList.get(i).getText());
                                                    break;
                                                }

                                                else {
                                                    dayInfo.setText("ساعات الدوام : من الساعة الثامنة صباحاً إلى الرابعة عصراً ");
                                                }
                                            }

                                        }
                                    });





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Passenger_calendar.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);



    }
}