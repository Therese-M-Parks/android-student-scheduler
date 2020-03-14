package com.thereseparks.familyintouch.View.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.thereseparks.familyintouch.Model.BL.BirthdayDatePickerFragment;
import com.thereseparks.familyintouch.Model.DB.My_SQLiteOpenHelper;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.Family_Member_Provider;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.Relation_Status_Provider;
import com.thereseparks.familyintouch.R;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditorFamilyMember extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String action;
    private CheckBox setBirthdayAlarm;
    private ImageView imageView;

    //Filter Variables for Where clause for sql statements
    private String familyMember_Filter;
    private String relation_Filter;
    // spinner variable
    private Spinner relation_spinner;
    // button variables
    private Button button_cancel; //goes back to home screen without saving changes
    private Button button_save; // goes back to home screen as it saves changes.

    // edit text variables
    private EditText et_first_name;
    private EditText et_last_name;

    // test variables without database
    private String firstName;
    private String lastName;

    // TODO: EditorFamilyMember needs to recognize to recognize from which view it is being
    // navigated to and change it's title accordingly,
    // TODO: EditorFamilyMember needs to be able to display the appropriate menu(e.g. trash can for edit)
    // TODO: The cancel and save buttons need to recognize their appropriate parent view so that they go back to that view
    // instead of the home page every time. This way the user doesn't get lost or disappointed by this lazy code flow.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imageView = (ImageView) findViewById(R.id.image);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_family_member);
        // add the functionality to go back to the parent page from action bar using the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent(); // the intent that launched this activity

        Uri uriFamilyMemberProvider = intent.getParcelableExtra( Family_Member_Provider.CONTENT_ITEM_TYPE_FAMILY_MEMBER);
        Uri uriRelationProvider = intent.getParcelableExtra( Relation_Status_Provider.CONTENT_ITEM_TYPE_RELATION_STATUS);
//        relation_Filter = My_SQLiteOpenHelper.RELATION_ID + "=" + uriRelationProvider.getLastPathSegment();

        /**-----------------------Testing Edit Text fields-------------------------------*/
        et_first_name = (EditText) findViewById(R.id.et_firstName);
        et_last_name = (EditText) findViewById((R.id.et_lastName));
//        et_first_name.setEnabled(true);
//        et_first_name.isFocusable();
//        et_first_name.isFocusableInTouchMode();
//        et_first_name.setInputType(InputType.TYPE_CLASS_TEXT);

        /**-----------------------Code for Relations Spinner--------------------------------*/
        // Notes from docs: use following code in you Activity to supply the spinner with the array
        // using the instance of ArrayAdapter
//        Spinner spinner = (Spinner) findViewById(R.id.relation_spinner);
//        spinner.setOnItemSelectedListener(this);
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.relations_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);
        relation_spinner = (Spinner) findViewById(R.id.relation_spinner);
        relation_spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.relations_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        relation_spinner.setAdapter(adapter);


        /**-----------------------Code for Cancel and Save Buttons---------------------*/
        // cancel button
        button_cancel = (Button) findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_BackToHomeScreen();
            }
        }));

        // save button
        button_save = (Button) findViewById(R.id.button_save);
        button_save.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_BackToHomeScreen();
            }
        }));
        /**-----------------------Code for Birthday Alarm-------------------------------*/
        //Listener for Birthday Alarm
        setBirthdayAlarm = (CheckBox) findViewById( R.id.setBirthdayAlarm);
        setBirthdayAlarm.setOnClickListener(new View.OnClickListener() {

            Intent intent;
            @Override
            public void onClick(View view) {
                String date = "";
                Date date2 = null;
                boolean checked = ((CheckBox) view).isChecked();
                switch(view.getId()) {
                    case R.id.setBirthdayAlarm:
                        if(!checked)
                            toastMsg( "No birthday reminder set!" );
                        if (checked)
                            toastMsg( "Birthday reminder set!" );
//                        date = editorCourseStart.getText().toString().trim();
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                        try {
//                            date2 = sdf.parse( date );
//                        }catch(ParseException e) {
//                            e.getStackTrace();
//                        }
//                        Calendar cal = Calendar.getInstance();
//                        if(date2 != null) {
//                            cal.setTime( date2 );
//                            long mill = cal.getTimeInMillis();
//
//                            intent = new Intent( EditorCourseActivity.this, MyReceiver.class );
//                            PendingIntent sender = PendingIntent.getBroadcast(
//                                    EditorCourseActivity.this, 0, intent, 0 );
//                            AlarmManager alarmManager =
//                                    (AlarmManager) getSystemService( Context.ALARM_SERVICE );
//                            alarmManager.set( AlarmManager.RTC_WAKEUP,
//                                    mill, sender ); //Date minus 24 hours converted to mill - 86400000
//                        }
                        break;
                }
            }

        });

    }// end onCreate
    // from android example: https://developer.android.com/guide/topics/ui/controls/spinner
    @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
        String relationTitle;
        String relationNumber;
        // add code here so that when this new family_member is saved, enter this value as a new record into the relations_table...

        if(pos == 0){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "0"; // no selection
          //  toastMsg("No Relation Selected.");
        }else if(pos == 1){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "1"; // brother-->immediate
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 2){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "1"; //sister-->immediate
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 3){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "2"; // uncle-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 4){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "2"; // aunt-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 5){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "2"; // nephew-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 6){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "2"; // niece-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 7){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "2"; // cousin-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 8){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "2"; // grandfather-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 9){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "2"; // grandmother-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 10){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "2"; // great_grandfather-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 11){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "2"; // great_grandmother-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 12){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "3"; // daughter_in_law-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 13){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "3"; // son_in_law-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 14){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "3"; // bro_in_law-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 15){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "3"; // sis_in_law-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 16){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "3"; // father_in_law-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 17){
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "3"; // mother_in_law-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }else if(pos == 18) {
            relationTitle = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
            relationNumber = "4"; // other-->extended
            toastMsg(relationTitle + " " + "Selected.");
        }
        parent.getItemAtPosition(pos);

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }

        // android docs: "When user clicks this button, the system calls the following method:"
        public void showDatePickerDialog(View v) {
            DialogFragment newFragment = new BirthdayDatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }

        private void insertRelationStatus(String newRelationTitle, String newRelationNumber){
            ContentValues values = new ContentValues();
            values.put(My_SQLiteOpenHelper.RELATION_TITLE, newRelationTitle);
            values.put(My_SQLiteOpenHelper.RELATION_NUMBER, newRelationNumber);
            getContentResolver().insert(Relation_Status_Provider.RELATION_STATUS_CONTENT_URI,
                    values);
            Toast.makeText(this, "Term Added", Toast.LENGTH_SHORT).show();
           // Intent intent = new Intent(EditorFamilyMember.this, ListOfTermsActivity.class);
           // startActivity(intent);
           // setResult(RESULT_OK);
        }
    public void toastMsg(String msg) {

        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();

    }

    // from android docs: Here's a function that invokes an intent to capture a photo
    static final int REQUEST_IMAGE_CAPTURE = 1;

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }

    //from Android docs: get the image /thumbnail back from the camera application to do something with it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
    //from Android docs: save the full-size photo
    //collision-resistent file name
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // from Android docs: With this method available to create a file for the photo,
    // you can now create and invoke the Intent like this:
    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    // when creating options menu, put edit and save can icon in menu
    // for editing family member
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add this code later....

//        if(action.equals( Intent.ACTION_EDIT )){
//            getMenuInflater().inflate( R.menu.menu_edit_family_member, menu );
//        }
//        else
//            getMenuInflater().inflate( R.menu.menu_add_family_member, menu );

       // getMenuInflater().inflate( R.menu.menu_edit_family_member, menu );
       getMenuInflater().inflate( R.menu.menu_add_family_member, menu );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
             //   finishEditing();
                intent = new Intent(EditorFamilyMember.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.action_save:
                Toast.makeText(this, "Action code for Save reached", Toast.LENGTH_SHORT).show();
                intent = new Intent(EditorFamilyMember.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.action_cancel:
             //   finishEditing();
                Toast.makeText(this, "Action code for Cancel reached", Toast.LENGTH_SHORT).show();
                intent = new Intent(EditorFamilyMember.this, MainActivity.class);
                startActivity(intent);
                //finishEditing();
                break;
            case R.id.action_delete:
//                if(cursorAdapter.getCount() != 0){
//                    Toast.makeText(this, "Cannot Delete a Term that has Courses Assigned to It",
//                            Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    deleteTerm();
//                }
                Toast.makeText(this, "Action code for Delete reached", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    /**-------------------------method for Cancel and Save Button----------------------------*/
    //Cancel Button method
    public void cancel_BackToHomeScreen(){
        // tell user that changes were not saved
        toastMsg("No changes were saved.");
        Intent intent = new Intent(EditorFamilyMember.this, MainActivity.class);
        startActivity(intent);
    }
    //Save Button method
    public void save_BackToHomeScreen(){
        // tell user that changes were not saved
        toastMsg("Changes were saved!");
        Intent intent = new Intent(EditorFamilyMember.this, MainActivity.class);
        startActivity(intent);
    }
  }

