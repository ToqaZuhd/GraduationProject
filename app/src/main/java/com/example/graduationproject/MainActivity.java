package com.example.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void rentCarOnClickBtn(View view) {
        Intent intent = new Intent(this, cars.class);
        startActivity(intent);
    }

    public void buyTicketOnClickBtn(View view) {
        Intent intent = new Intent(this, buy_tickets.class);
        startActivity(intent);
    }

    public void neededInfoOnClickBtn(View view) {
        Intent intent = new Intent(this, DocumentActivity.class);
        startActivity(intent);
    }

    public void tripMapOnClickBtn(View view) {
        Intent intent = new Intent(this, TripMapActivity.class);
        startActivity(intent);
    }
}