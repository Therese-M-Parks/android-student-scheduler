package com.thereseparks.familyintouch.View.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.thereseparks.familyintouch.Model.BL.BirthdayDatePickerFragment;
import com.thereseparks.familyintouch.Model.DB.My_SQLiteOpenHelper;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.Family_Member_Provider;
import com.thereseparks.familyintouch.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class View_Family_Member extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;

    private String action;
    private CheckBox setBirthdayAlarm;
    private ImageView imageView;

    private int savedBirthday_Id;

    // spinner variable
    private Spinner relation_spinner;
    // button variables
    private Button button_addExtraNotes;
    // private Button button_cancel; //goes back to home screen without saving changes
    // private Button button_save; // goes back to home screen as it saves changes.
    //db variables for family_member--> first, last, relation_id, phone_1, phone_2, email, birthday_id, address_id
    // edit text variables
    private EditText et_first_name;
    private EditText et_last_name;
    private int int_relation_id; // value determined in this class
    private Spinner edit_spinner;
    private EditText et_phone_1;
    private EditText et_phone_2;
    private EditText et_email;
    private EditText et_birthday;
    private EditText et_description;

    // private int int_address_id;

    String relationTitle;


    //Filter Variables for Where clause for sql statements
    private String familyMember_Filter;
    private String relation_Filter;

    // test variables without database
    private String old_firstName;
    private String old_lastName;
    private int old_relationStatusId;// TODO: set to be auto-filled
    private String old_phone_1;
    private String old_phone_2;
    private String old_email;
    private String old_birthday;
    private String old_description;
    private String old_relationTitle;
    // private int old_address_id; // TODO: set to be auto-filled

    public static String selectedFamilyMember_Id;

    // TODO: EditorFamilyMember needs to recognize to recognize from which view it is being
    // navigated to and change it's title accordingly,
    // TODO: EditorFamilyMember needs to be able to display the appropriate menu(e.g. trash can for edit)
    // TODO: The cancel and save buttons need to recognize their appropriate parent view so that they go back to that view
    // instead of the home page every time. This way the user doesn't get lost or disappointed by this lazy code flow.

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imageView = (ImageView) findViewById(R.id.image);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_family_member);
        // add the functionality to go back to the parent page from action bar using the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**-----------------------Testing Edit Text fields-------------------------------*/
        et_first_name = (EditText) findViewById(R.id.et_firstName);
        et_last_name = (EditText) findViewById((R.id.et_lastName));
        // int_relation_id = 2; //test data manual entry
        et_phone_1 = (EditText) findViewById(R.id.et_phoneNumber1);
        et_phone_2 = (EditText) findViewById(R.id.et_phoneNumber2);
        et_email = (EditText) findViewById(R.id.et_email);
        et_birthday = (EditText) findViewById(R.id.et_Birthday);
        et_description = (EditText) findViewById(R.id.et_description);
        int maxLengthForDescription = 150;
        et_description.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthForDescription)});
        //    int_address_id = 4; //test data manual entry--> unique
        /**-----------------------Code for Relations Spinner--------------------------------*/
        // Notes from docs: use following code in you Activity to supply the spinner with the array
        // using the instance of ArrayAdapter
        relation_spinner = (Spinner) findViewById(R.id.relation_spinner);
       // relation_spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.relations_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        relation_spinner.setAdapter(adapter);
        Intent intent = getIntent(); // the intent that launched this activity

        Uri uriFamilyMemberProvider = intent.getParcelableExtra(Family_Member_Provider.CONTENT_ITEM_TYPE_FAMILY_MEMBER);
        //  Uri uriRelationProvider = intent.getParcelableExtra(Relation_Status_Provider.CONTENT_ITEM_TYPE_RELATION_STATUS);
//        relation_Filter = My_SQLiteOpenHelper.RELATION_ID + "=" + uriRelationProvider.getLastPathSegment();

        //If the user wants to edit then it won't be null, but if the user pressed new to insert, it will be null
//        if (uriFamilyMemberProvider == null) {
//
//            action = Intent.ACTION_INSERT;
//            setTitle("New Family Member");
//
//            // extra notes button
//            button_addExtraNotes = (Button) findViewById(R.id.button_add_notes);
//            button_addExtraNotes.setOnClickListener((new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    toastMsg("Save Family Member before adding more notes.");
//                }
//            }));
      //  } else {
           // setTitle("Edit Family Member");
            action = Intent.ACTION_EDIT;
            familyMember_Filter = My_SQLiteOpenHelper.FAMILY_MEMBER_ID + "=" + uriFamilyMemberProvider.getLastPathSegment();

            //retrieve the one row from database
            Cursor cursor = getContentResolver().query(uriFamilyMemberProvider,
                    My_SQLiteOpenHelper.ALL_FAMILY_MEMBER_COLUMNS, familyMember_Filter, null, null);
            //retrieve the data
            cursor.moveToFirst();
            //First Name
            //subString index of four is where the actual integer starts after the _id =
            selectedFamilyMember_Id = familyMember_Filter.substring(4);
            old_firstName = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.FIRST_NAME));
            et_first_name.setText(old_firstName);
            //Last Name
            old_lastName = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.LAST_NAME));
            et_last_name.setText(old_lastName);
            //relation_status
            old_relationTitle = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.RELATION_TITLE));
            //  edit_spinner.setTooltipText(old_relationTitle);
//            old_relationStatusId = cursor.getInt(cursor.getColumnIndex(My_SQLiteOpenHelper.FAM_MEMBER_RELATION_ID));
//            //TODO: DO Something with retrieved relation_Id.........e.g. search the relation for relation text and set
//            // spinner to that text at that index.....
//            //TODO: May have to enter test data into table for birthday, and address, and relation tables
            //Phone 1
            old_phone_1 = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.PHONE_1));
            et_phone_1.setText(old_phone_1);
            //Phone 2
            old_phone_2 = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.PHONE_2));
            et_phone_2.setText(old_phone_2);
            //Email
            old_email = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.EMAIL));
            et_email.setText(old_email);
            //Birthday ID
            old_birthday = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.BIRTH_DAY));
            et_birthday.setText(old_birthday);
            // Description
            old_description = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.DESCRIPTION));
            et_description.setText(old_description);
            //set these text views as view only
        et_first_name.setEnabled(false); // makes it view only
        et_last_name.setEnabled(false); // makes it view only
       // old_relationTitle.setEnabled(false); // makes it view only
        et_phone_1.setEnabled(false); // makes it view only
        et_phone_2.setEnabled(false);
        et_email.setEnabled(false);
        et_birthday.setEnabled(false);
        et_description.setEnabled(false);
        cursor.close();

        /**-----------------------Code for Birthday Alarm-------------------------------*/
        //Listener for Birthday Alarm
        setBirthdayAlarm = (CheckBox) findViewById(R.id.setBirthdayAlarm);
        setBirthdayAlarm.setOnClickListener(new View.OnClickListener() {

            Intent intent;

            @Override
            public void onClick(View view) {
                String date = "";
                Date date2 = null;
                boolean checked = ((CheckBox) view).isChecked();
                switch (view.getId()) {
                    case R.id.setBirthdayAlarm:
                        if (!checked)
                            toastMsg("No birthday reminder set!");
                        if (checked)
                            toastMsg("Birthday reminder set!");
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






    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    // android docs: "When user clicks this button, the system calls the following method:"
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new BirthdayDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDITOR_REQUEST_CODE && requestCode == RESULT_OK) {
            restartLoader();
        }
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
        //getMenuInflater().inflate( R.menu.menu_quick_access_view, menu );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                //finishEditing();
                intent = new Intent(View_Family_Member.this, All_Family_Members.class);
                startActivity(intent);
                break;
        }
        return true;
    }



    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, Family_Member_Provider.FAMILY_MEMBER_CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    private void restartLoader(){
        //android.app.LoaderManager.LoaderCallbacks<Cursor> test = (android.app.LoaderManager.LoaderCallbacks<Cursor>) this;
        getLoaderManager().restartLoader( 0, null, (android.app.LoaderManager.LoaderCallbacks<Cursor>) this );
    }
    //This method is triggered when user presses back button

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

