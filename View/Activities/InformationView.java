package com.thereseparks.familyintouch.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.VectorEnabledTintResources;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.thereseparks.familyintouch.R;
/** This class is basically a clone of the EditorFamilyMember Activity, but it is READ-ONLY.*/
public class InformationView extends AppCompatActivity {

    /** --------------Variables for Form Elements*--------------*/
    // name
    private EditText et_firstName;
    private EditText et_lastName;
    // phone
    private EditText et_phone_1;
    private EditText et_phone_2;
    // email
    private EditText et_email;
    // birthday
    private EditText et_birthday;
    // birthday checkBox
    private CheckBox cb_setBirthdayAlarm;
    /**address**/

    // street
    private EditText et_street_1;
    private EditText et_street_2;
    private EditText et_city;
    private EditText et_state;
    private EditText et_zip;
    private EditText et_country;

    /** notes */
    private EditText et_notes;

    // edit button
    private Button button_edit;

    //TODO : Add Edit option as menu item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_view);
        // add the functionality to go back to the parent page from action bar using the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** code for names*/
        //first
        et_firstName = findViewById(R.id.et_firstName);
        et_firstName.setEnabled(false); // makes read only
        //last
        et_lastName = findViewById(R.id.et_lastName);
        et_lastName.setEnabled(false); // makes read only
        /** code for phones */
        //code for phone 1
        et_phone_1 =  findViewById(R.id.et_phoneNumber1);
        et_phone_1.setEnabled(false); // makes read only
        //code for phone 2
        et_phone_2 =  findViewById(R.id.et_phoneNumber2);
        et_phone_2.setEnabled(false); // makes read only
        /**code for email*/
        et_email = findViewById(R.id.et_email);
        et_email.setEnabled(false); // makes it read only
        /**code for birthday*/
        et_birthday =  findViewById(R.id.etBirthday);
        et_birthday.setEnabled(false); // makes it read only
        // checkbox
        cb_setBirthdayAlarm =(CheckBox) findViewById(R.id.setBirthdayAlarm);
        //TODO: I can do a conditional statement to fill or leave empty according to the clicked on family member item. (See unitracker project)
        /**address*/
        // street 1
        et_street_1 =  findViewById(R.id.et_addressLine_1);
        et_street_1.setEnabled(false); // makes it read only
        // street 2
        et_street_2 = findViewById(R.id.et_addressLine_2);
        et_street_2.setEnabled(false); // makes it read only
        // city
        et_city =  findViewById(R.id.et_city);
        et_city.setEnabled(false); // makes it read only
        // state
        et_state = findViewById(R.id.et_state);
        et_state.setEnabled(false); // makes it read only
        // zip
        et_zip = findViewById(R.id.et_postalCode);
        et_zip.setEnabled(false); // makes it read only
        // country
        et_country = findViewById(R.id.et_country);
        et_country.setEnabled(false); // makes it read only
        //notes
        et_notes = findViewById(R.id.et_notes);
        et_notes.setEnabled(false);

        // code for list of edit button
        button_edit = findViewById(R.id.button_editBtnInInformationView);
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditorFamilyMember();
            }
        });
    }// end onCreate
    // Click on the newMember button to go the list of terms)
    public void openEditorFamilyMember(){
        Intent intent = new Intent(InformationView.this, EditorFamilyMember.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_information_view, menu );
        return true;
    }

    //TODO: I still need to make the icons show instead of the literal word in all of the menus
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                //   finishEditing();
                intent = new Intent(InformationView.this, QuickAccess.class);
                startActivity(intent);
                break;
            case R.id.edit:
               openEditorFamilyMember();
                break;
//                Toast.makeText(this, "Action code for Delete reached", Toast.LENGTH_SHORT).show();
//                break;
        }
        return true;
    }
}

// Do this for all the editable fields to make it view only.
// et_first_name.setEnabled(false); // This makes it NOT editable