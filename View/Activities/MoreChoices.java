package com.thereseparks.familyintouch.View.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.thereseparks.familyintouch.R;

public class MoreChoices extends AppCompatActivity {

    private Button button_familyGroups;
    private Button button_familyBirthdays;
    private Button button_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_choices);
        // add the functionality to go back to the parent page from action bar using the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //code for list of familyGroups button
        button_familyGroups = (Button) findViewById(R.id.button_FamilyByGroup);
        button_familyGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFamilyGroups();
            }
        });
        //code for list familyBirthdays button
        button_familyBirthdays = (Button) findViewById(R.id.button_FamilyBirthdays);
        button_familyBirthdays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFamilyBirthdays();
            }
        });

        //code for Settings button
        button_settings = (Button) findViewById(R.id.button_Settings);
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettings();
            }
        });

    }// end onCreate

    // Click on the familyGroups button to go the list of terms)
    public void openFamilyGroups(){
        Intent intent = new Intent(MoreChoices.this, FamilyGroups.class);
        startActivity(intent);
    }

    // Click on the familyBirthdays button to go the list of terms)
    public void openFamilyBirthdays(){
        Intent intent = new Intent(MoreChoices.this, Birthdays.class);
        startActivity(intent);
    }


    // Click on the Settings button to go the list of terms)
    public void openSettings(){
        Intent intent = new Intent(MoreChoices.this, Settings.class);
        startActivity(intent);
    }
}
