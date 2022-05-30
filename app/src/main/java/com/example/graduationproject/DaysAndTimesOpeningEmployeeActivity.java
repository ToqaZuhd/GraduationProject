package com.example.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class DaysAndTimesOpeningEmployeeActivity extends AppCompatActivity {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_and_times_opening_employee);
        getSupportActionBar().setTitle("إضافة موعد أو معلومة");

        queue = Volley.newRequestQueue(this);
        getPosts();
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

                            RecyclerView recycler = findViewById(R.id.post_recyclerEm);
                            recycler.setLayoutManager(new LinearLayoutManager(DaysAndTimesOpeningEmployeeActivity.this));
                            DataCardEmployee adapter = new DataCardEmployee(DaysAndTimesOpeningEmployeeActivity.this, employeeList);
                            recycler.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DaysAndTimesOpeningEmployeeActivity.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);


    }

    public void btnClkPost(View view) {
        Intent intent = new Intent(DaysAndTimesOpeningEmployeeActivity.this, WritePostEmployeeActivity.class);
        startActivity(intent);
    }
}