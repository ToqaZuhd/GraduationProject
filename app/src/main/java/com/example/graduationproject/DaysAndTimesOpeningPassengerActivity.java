package com.example.graduationproject;

import android.os.Bundle;
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

public class DaysAndTimesOpeningPassengerActivity extends AppCompatActivity {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_and_times_opening_passenger);
        getSupportActionBar().setTitle("أوقات فتح الجسر");
        queue = Volley.newRequestQueue(this);
        getPosts();
    }

    public void getPosts() {
        List<PostEmployee> employeeList = new ArrayList<>();
        String BASE_URL = "http://10.0.2.2:82/GraduationProject/getPostDataEmployee.php";
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
                            RecyclerView recycler = findViewById(R.id.post_recyclerPass);
                            recycler.setLayoutManager(new LinearLayoutManager(DaysAndTimesOpeningPassengerActivity.this));
                            DataCard adapter = new DataCard(DaysAndTimesOpeningPassengerActivity.this, employeeList);

                            recycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DaysAndTimesOpeningPassengerActivity.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);


    }
}