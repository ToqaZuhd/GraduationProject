package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.graduationproject.PointsCharger.chargePoints;
import com.example.graduationproject.guest.StateOfPeopleGuest;

public class Choices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices);
    }

    public void btnClkLogin(View view) {
        Intent intent = new Intent(Choices.this,SignIn.class);
        startActivity(intent);
    }

    public void btnClkRegister(View view) {
        Intent intent = new Intent(Choices.this,Registration.class);
        startActivity(intent);
    }

    public void btnClkGuest(View view) {
        Intent intent = new Intent(Choices.this, StateOfPeopleGuest.class);
        startActivity(intent);


    }


}