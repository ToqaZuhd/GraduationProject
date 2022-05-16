package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

public class Setting extends AppCompatActivity {

    private ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        lst=findViewById(R.id.ListChanges);
        getSupportActionBar().setTitle("الإعدادات");

        populateList();
    }
    public void populateList(){
        String []arr={"تغيير الاسم","تغيير كلمة السر","تغيير الإيميل","تغيير رقم الهاتف"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Setting.this, R.layout.style,
                arr);

        lst.setAdapter(adapter);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    Intent intent = new Intent(view.getContext(), ChangePassword.class);
                    startActivity(intent);


                }

                else if (position == 2) {
                    Intent intent = new Intent(view.getContext(), ChangeEmail.class);
                    startActivity(intent);

                }

                else if (position == 3) {
                    Intent intent = new Intent(view.getContext(), ChangePhoneNum.class);
                    startActivity(intent);
                }

               else  {
                    Intent intent = new Intent(view.getContext(), ChangeName.class);
                    startActivity(intent);
                }
            }
        });
    }
}