package com.thereseparks.familyintouch.View.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.ConnectionService;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.thereseparks.familyintouch.Model.DB.My_SQLiteOpenHelper;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.FamilyCursorAdapter;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.Family_Member_Provider;
import com.thereseparks.familyintouch.R;

public class QuickAccess extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    //androidx loader Manager? see if this one works
    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;

    private String action;


    /** EditText Fields that will be auto-filled and made un-editable*/
    private EditText et_fullName;
    // private EditText et_lastName;
    private EditText et_relation;
    private EditText et_phoneNumber_1;
    private EditText et_phoneNumber_2;
    private EditText et_email;

    /**--------Buttons------------*/
    //more details button that opens the View only Details
    private Button button_detailedInformation;
    //call button for phone 1
    private ImageButton bt_Call;
    //call button for phone 2
    private ImageButton bt_Call2;

    // send notes via email or save as other
    private ImageButton bt_Note_1;
    private ImageButton bt_Note_2;
    // send direct sms message from the app
    private ImageButton bt_smsMessage_1;
    private ImageButton bt_smsMessage_2;

    private String familyMemberFilter;//Where clause for sql statements

    // variables to hold value of retrieved text
    private String old_firstName;
    private String old_lastName;
    private String old_relation;
    private String old_phone_1;
    private String old_phone_2;
    private String old_email;

    private String old_fullname;

    private String selectedFamilyMemberID;

    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_access);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /// et_firstName = (EditText) findViewById((R.id.et_firstName));
        // et_lastName = (EditText) findViewById((R.id.et_lastName));
        et_fullName = (EditText) findViewById(R.id.et_fullName);
        et_relation = (EditText) findViewById((R.id.et_Relation));
        et_phoneNumber_1 = (EditText)findViewById(R.id.et_phoneNumber1);
        et_phoneNumber_2 = (EditText)findViewById(R.id.et_phoneNumber2);
        et_email = (EditText) findViewById(R.id.et_email);

        /** Set the text for the page and pull the selected row from the data base and fill the approprate
         * textFields.
         */

        Intent intent = getIntent(); // the intent that launched this activity
        Uri uri = intent.getParcelableExtra(Family_Member_Provider.CONTENT_ITEM_TYPE_FAMILY_MEMBER);

        // action = Intent.ACTION_EDIT;
        action = Intent.ACTION_EDIT;
        familyMemberFilter = My_SQLiteOpenHelper.FAMILY_MEMBER_ID + "=" + uri.getLastPathSegment();

        //retrieve the one row from database
        Cursor cursor = getContentResolver().query(uri,
                My_SQLiteOpenHelper.ALL_FAMILY_MEMBER_COLUMNS, familyMemberFilter, null,
                null);
        //retrieve the data
        cursor.moveToFirst();

        //subString index of four is where the actual integer starts after the _id =
        selectedFamilyMemberID = familyMemberFilter.substring(4);
        //pull values from first and last and place them in fullName text field.
        old_firstName = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.FIRST_NAME));
        old_lastName = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.LAST_NAME));
        //FullName
        old_fullname = old_firstName + " " + old_lastName;
        et_fullName.setText(old_fullname);
        //relation
        old_relation = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.RELATION_TITLE));
       // et_relation.setText("Relation:" + " " + old_relation);
        et_relation.setText(old_relation);
        //Phone 1
        old_phone_1 = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.PHONE_1));
        et_phoneNumber_1.setText(old_phone_1);
        //Phone 2
        old_phone_2 = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.PHONE_2));
        et_phoneNumber_2.setText(old_phone_2);
        //Email
        old_email = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.EMAIL));
        et_email.setText(old_email);
        cursor.close();
        // set the EditText Fields to read-only

        et_fullName.setEnabled(false); // makes it view only
        et_relation.setEnabled(false); // makes it view only
        et_phoneNumber_1.setEnabled(false); // makes it view only
        et_phoneNumber_2.setEnabled(false); // makes it view only
        et_email.setEnabled(false); // makes it view only

//        bt_smsMessage_1 = (ImageButton) findViewById(R.id.button_smsMessage);
//        bt_smsMessage_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//           toastMsg("This Feature is not yet available in this version of the app.");
//            }
//        });

       // bt_smsMessage_2 = (ImageButton) findViewById((R.id.button_smsMessage_2))
        bt_Note_1 = (ImageButton) findViewById(R.id.button_noteMessage);
        bt_Note_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNoteEditorForMessage();
            }
        });
//        bt_Note_2 = (ImageButton) findViewById(R.id.button_textMessage2);
//        bt_Note_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openNoteEditorForMessage();
//            }
//        });

        bt_Call = (ImageButton) findViewById(R.id.button_call);
        bt_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall_1();
            }
        });

        bt_Call2 = (ImageButton) findViewById(R.id.button_call2);
        bt_Call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall_2();
            }
        });
    }// end onCreate

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CALL){
            if(grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall_1();
            } else
               Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
        }
    }

    // call usinig another app
    private void makePhoneCall_1(){
        String number = et_phoneNumber_1.getText().toString();
         if(number.trim().length()> 0){
             if (ContextCompat.checkSelfPermission(QuickAccess.this, Manifest.permission.CALL_PHONE)
                     != PackageManager.PERMISSION_GRANTED) {
              ActivityCompat.requestPermissions(QuickAccess.this, new String[] {
                      Manifest.permission.CALL_PHONE}, REQUEST_CALL);
             }else {
                 String dial = "tel:" + number;
                 startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
             }
         }else{
             toastMsg("Enter Phone Number!");
         }

    }

    private void makePhoneCall_2(){
        String number = et_phoneNumber_2.getText().toString();
        if(number.trim().length()> 0){
            if (ContextCompat.checkSelfPermission(QuickAccess.this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(QuickAccess.this, new String[] {
                        Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else{
            toastMsg("Enter Phone Number!");
        }

    }
    //send SMS directly from app


    public void toastMsg(String msg) {

        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //if(action.equals( Intent.ACTION_EDIT )){
        getMenuInflater().inflate( R.menu.menu_quick_access_view, menu );
        //    }
        return true;
    }

    //TODO: I still need to make the icons show instead of the literal word in all of the menus
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case android.R.id.home:
                openAllFamilyMembers();
                break;
            case R.id.edit_quick_access:
                openEditorFamilyMember();
                break;
        }
        return true;
    }
    // method that the details button calls
    public void openAllFamilyMembers(){
        Intent intent = new Intent(QuickAccess.this, All_Family_Members.class);
        startActivity(intent);
    }
    //opens a note editor with options to send as email or text ...
    public void openNoteEditorForMessage(){
        Intent intent = new Intent(QuickAccess.this, EditorNotes.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(QuickAccess.this, MainActivity.class);
        startActivity(intent);
    }
    public void openEditorFamilyMember(){
//        Intent intent = new Intent(QuickAccess.this, EditorFamilyMember.class);
//        startActivity(intent);
        Intent intent = new Intent(QuickAccess.this, EditorFamilyMember.class);
        Uri uri = Uri.parse( Family_Member_Provider.FAMILY_MEMBER_CONTENT_URI + "/" + selectedFamilyMemberID); //represents PK value
        intent.putExtra( Family_Member_Provider.CONTENT_ITEM_TYPE_FAMILY_MEMBER, uri); // uri to intent as extra
        startActivityForResult(intent, EDITOR_REQUEST_CODE );
    }

    public void openViewFamilyMember(){
//        Intent intent = new Intent(QuickAccess.this, EditorFamilyMember.class);
//        startActivity(intent);
        Intent intent = new Intent(QuickAccess.this, View_Family_Member.class);
        Uri uri = Uri.parse( Family_Member_Provider.FAMILY_MEMBER_CONTENT_URI + "/" + selectedFamilyMemberID); //represents PK value
        intent.putExtra( Family_Member_Provider.CONTENT_ITEM_TYPE_FAMILY_MEMBER, uri); // uri to intent as extra
        startActivityForResult(intent, EDITOR_REQUEST_CODE );
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader( this, Family_Member_Provider.FAMILY_MEMBER_CONTENT_URI,
                null, null, null, null );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor( data );
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor( null );

    }
    private void restartLoader(){
        //android.app.LoaderManager.LoaderCallbacks<Cursor> test = (android.app.LoaderManager.LoaderCallbacks<Cursor>) this;
        getLoaderManager().restartLoader( 0, null, (android.app.LoaderManager.LoaderCallbacks<Cursor>) this );
    }

    //This method is triggered when user presses back button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDITOR_REQUEST_CODE && requestCode == RESULT_OK) {
            restartLoader();
        }
    }

    @Override
    protected void onResume() {
        // ListView list = (ListView)findViewById(android.R.id.list);
        super.onResume();
        //list.setAdapter(cursorAdapter);
//
//            if (getLoaderManager() != null){
//           restartLoader();
//          }
    }
}