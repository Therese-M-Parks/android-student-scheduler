package com.thereseparks.familyintouch.View.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.thereseparks.familyintouch.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
