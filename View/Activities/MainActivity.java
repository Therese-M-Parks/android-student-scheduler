package com.thereseparks.familyintouch.View.Activities;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.thereseparks.familyintouch.Model.DB.My_SQLiteOpenHelper;
import com.thereseparks.familyintouch.R;
/** <a target="_blank" href="https://icons8.com/icons/set/family">Family icon</a>
 * icon by <a target="_blank" href="https://icons8.com">Icons8</a>*/
public class MainActivity extends AppCompatActivity {

    private Button button_newMember;
    private Button button_familyMembers;
    private Button button_moreChoices;
    private Button button_emergency;
    /** TODO: The Relation Table needs to have pre-set data when the app is first down loaded
     * on each device with four choices with set id's
     * 1: Immediate Family 2. Extended Family 3. Family_In_Law 4. Other Family */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        My_SQLiteOpenHelper helper = new My_SQLiteOpenHelper(this);
       // getWritableDatabase() creates db and makes it writable
        SQLiteDatabase database = helper.getWritableDatabase();
           // Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
          //  setSupportActionBar(myToolbar);

        //code for list of terms button
        button_newMember = (Button) findViewById(R.id.button_newMember);
        button_newMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddMember();
            }
        });
        //code for list family members button
        button_familyMembers = (Button) findViewById(R.id.button_listOfAllFamilyMembers);
        button_familyMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFamilyMembers();
            }
        });

        //code for more choices members button
        button_moreChoices = (Button) findViewById(R.id.button_MoreChoices);
        button_moreChoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreChoices();
            }
        });

        //code for emergency button
        button_emergency = (Button) findViewById(R.id.button_Emergency);
        button_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmergency();
            }
        });


    }// end onCreate

    // On creating the options menu, add the setting option in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add this code later....
        getMenuInflater().inflate( R.menu.menu_main_screen, menu );
        return true;
    }

    /**--------------- methods for buttons on home screen ----------------**/

    // Click on the newMember button to go the list of terms)
    public void openAddMember(){
        Intent intent = new Intent(MainActivity.this, EditorFamilyMember.class);
        startActivity(intent);
    }
    // Click on the familyMembers button to go the list of Family Members)
    public void openFamilyMembers(){
        Intent intent = new Intent(MainActivity.this, FamilyMembers.class);
        startActivity(intent);
    }
    // Click on the familyMembers button to go the list of Family Members)
    public void openMoreChoices(){
        Intent intent = new Intent(MainActivity.this, MoreChoices.class);
        startActivity(intent);
    }
    // Click on the Emergency button to go the list of Family Members)
    public void openEmergency(){
        Intent intent = new Intent(MainActivity.this, Emergency.class);
        startActivity(intent);
    }



}

