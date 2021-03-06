package com.thereseparks.familyintouch.View.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.thereseparks.familyintouch.R;

public class DistantFamily extends AppCompatActivity {

    private Button button_testQuickAccessView;
// test quick access button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distant_family);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_testQuickAccessView= (Button) findViewById(R.id.button_testButton);
        button_testQuickAccessView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test_OpenQuickAccessView();
            }
        }));


    }// end onCreate

    public void test_OpenQuickAccessView(){
        // tell user that changes were not saved
        toastMsg("Opened Quick Access Page");
        Intent intent = new Intent(DistantFamily.this, QuickAccess.class);
        startActivity(intent);
    }


    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();

    }
}
