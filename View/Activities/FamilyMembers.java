package com.thereseparks.familyintouch.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.thereseparks.familyintouch.R;

public class FamilyMembers extends AppCompatActivity {
     private Button button_testQuickAccessView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_members);

      //  getSupportActionBar().setTitle("Family Members");
        // add the functionality to go back to the parent page from action bar using the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**-----------------------Code for Test Quick Access View Button---------------------*/
        // test quick access button
        button_testQuickAccessView= (Button) findViewById(R.id.button_testButton);
        button_testQuickAccessView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test_OpenQuickAccessView();
            }
        }));
    }// end onCreate

    /**-------------------------method for Cancel and Save Button----------------------------*/
    //Cancel Button method
    public void test_OpenQuickAccessView(){
        // tell user that changes were not saved
        toastMsg("Opened Quick Access Page");
        Intent intent = new Intent(FamilyMembers.this, QuickAccess.class);
        startActivity(intent);
    }


    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();

    }
}
