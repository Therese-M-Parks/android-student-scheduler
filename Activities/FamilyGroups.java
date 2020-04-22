package com.thereseparks.familyintouch.View.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.thereseparks.familyintouch.R;

public class FamilyGroups extends AppCompatActivity {

    private Button button_immediateFamily;
    private Button button_distantFamily;
    private Button button_familyInLaw;
    private Button button_otherFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_groups);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //code for list of terms button
        button_immediateFamily = (Button) findViewById(R.id.button_immediate_family);
        button_immediateFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImmediateFamily();
            }
        });
        //code for list family members button
        button_distantFamily = (Button) findViewById(R.id.button_distant_family);
        button_distantFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDistantFamily();
            }
        });

        //code for more choices members button
        button_familyInLaw = (Button) findViewById(R.id.button_family_in_law);
        button_familyInLaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 openFamilyInLaw();
            }
        });

        //code for emergency button
        button_otherFamily = (Button) findViewById(R.id.button_other_family);
        button_otherFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOtherFamily();
            }
        });

    }
    // Click on the open Immediate Family button to go the list of immediate family members)
    public void openImmediateFamily(){
        Intent intent = new Intent(FamilyGroups.this, ImmediateFamily.class);
        startActivity(intent);
    }

    // Click on the open Distant Family button to go the list of distant family members)
    public void openDistantFamily(){
        Intent intent = new Intent(FamilyGroups.this, DistantFamily.class);
        startActivity(intent);
    }

    // Click on the open family-in-law button to go the list of family-in-law members)
    public void openFamilyInLaw(){
        Intent intent = new Intent(FamilyGroups.this, FamilyInLaw.class);
        startActivity(intent);
    }

    // Click on the open Other Family button to go the list of other family members)
    public void openOtherFamily(){
        Intent intent = new Intent(FamilyGroups.this, OtherFamily.class);
        startActivity(intent);
    }

//    //when creating options menu, put save clickable word in menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate( R.menu.menu_list_of_family_members, menu );
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                openMainActivity();
//                break;
//        }
//        return true;
//    }

    public void openMainActivity(){
        Intent intent = new Intent(FamilyGroups.this, MoreChoices.class);
        startActivity(intent);
    }

}


