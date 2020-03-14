package com.thereseparks.familyintouch.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.thereseparks.familyintouch.R;

public class QuickAccess extends AppCompatActivity {
    /** EditText Fields that will be auto-filled and made un-editable*/
    private EditText et_fullName;
    private EditText et_relation;
    //phone 1
    private EditText et_phoneNumber_1;
    //phone 2
    private EditText et_phoneNumber_2;

    //email
    private EditText et_email;

    /**--------Buttons------------*/
    //more details button that opens the View only Details
    private Button button_detailedInformation;
    //call button for phone 1
    private ImageButton bt_Call;
    //call button for phone 2
    private ImageButton bt_Call2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_access);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //full name
        et_fullName = (EditText) findViewById(R.id.et_fullName);
        et_fullName.setEnabled(false); // This makes it NOT editable
        //relation
        et_relation=(EditText) findViewById((R.id.et_Relation));
        et_relation.setEnabled(false); // makes it view only
        //primary pone
        et_phoneNumber_1 = (EditText)findViewById(R.id.et_phoneNumber1);
        et_phoneNumber_1.setEnabled(false); // makes it view only
        //secondary phone
        et_phoneNumber_2 = (EditText)findViewById(R.id.et_phoneNumber2);
        et_phoneNumber_2.setEnabled(false); // makes it view only
        // email
        et_email = (EditText) findViewById(R.id.et_email);
        et_email.setEnabled(false); // makes it view only

        // connect this button object to the button family details button object in the Quick Access Activity
        button_detailedInformation = (Button) findViewById(R.id.button_detailedInformation);
        button_detailedInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInformationView();
            }
        });
        // tell the user what to do when the user clicks on the family Details button:
        //connect this first call button object to the first call button object in the QuickAccess Activity
        bt_Call = (ImageButton) findViewById(R.id.button_call);
        //tell the program what to do when the user clicks on the first phone button
        bt_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**--------------------code for phone 1-----------------------------*/
                String phone1 = et_phoneNumber_1.getText().toString();
                if (phone1.isEmpty()) {
                    toastMsg("Please Enter Phone number.");
                } else {
                    toastMsg("Dialing primary phone number.");
                    //   https://developer.android.com/training/permissions/requesting
                    // Here, thisActivity is the current activity --A.Studio
                    if (ContextCompat.checkSelfPermission(QuickAccess.this,
                            Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        // Should we show an explanation?
                        String s = "tel:" + phone1;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(s));
                        startActivity(intent);
                    }
                    //else
                    //{
//                        // No explanation needed; request the permission
//                        ActivityCompat.requestPermissions(QuickAccess.this,
//                                new String[]{Manifest.permission.READ_CONTACTS},
//                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                                // app-defined int constant. The callback method gets the
//                                // result of the request.
//                    }
//                } else {
//                    // Permission has already been granted
                }


            }
            });

              //connect this second call button object to the first call button object in the QuickAccess Activity
             bt_Call2 = (ImageButton) findViewById(R.id.button_call2);
             // tell the user what to do when the user clicks on the second phone call button
             bt_Call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**--------------------code for phone 1-----------------------------*/
                String phone2 = et_phoneNumber_2.getText().toString();
                if (phone2.isEmpty()) {
                    toastMsg("Enter phone number!");
                } else {
                    toastMsg("Dialing secondary phone number.");
                    //   https://developer.android.com/training/permissions/requesting
                    // Here, thisActivity is the current activity --A.Studio
                    if (ContextCompat.checkSelfPermission(QuickAccess.this,
                            Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        // Should we show an explanation?
                        String s = "tel:" + phone2;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(s));
                        startActivity(intent);
                    }
                }


            }
        });
    }// end onCreate

    /** TODO: for the Quick Access Page, PHone numbers and names will be auto filled. For optionalil information, for example the secondary
     * phone number, a fill-in text must be provdiede. FOr example, a simple not saying: "No Secondary Phone"
     * @param msg
     */

    public void toastMsg(String msg) {

        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();

    }


    //TODO: I still need to make the icons show instead of the literal word in all of the menus
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                //   finishEditing();
               openMainActivity();
                break;
            case R.id.more_details:
                openInformationView();
                break;
//            case R.id.edit:
//                openEditorFamilyMember();
//                break;
////                Toast.makeText(this, "Action code for Delete reached", Toast.LENGTH_SHORT).show();
////                break;
        }
        return true;
    }


    // method that the details button calls
    public void openInformationView(){
        Intent intent = new Intent(QuickAccess.this, InformationView.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(QuickAccess.this, MainActivity.class);
        startActivity(intent);
    }
    public void openEditorFamilyMember(){
        Intent intent = new Intent(QuickAccess.this, EditorFamilyMember.class);
        startActivity(intent);
}
}
