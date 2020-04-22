package com.thereseparks.familyintouch.View.Activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thereseparks.familyintouch.Model.BL.BirthdayDatePickerFragment;
import com.thereseparks.familyintouch.Model.DB.My_SQLiteOpenHelper;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.Family_Member_Provider;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.MyReceiver;
import com.thereseparks.familyintouch.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditorFamilyMember<onActivityResult> extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102 ;
    private static final int SELECT_PHOTO = 1002;
    private CursorAdapter cursorAdapter;
    private String bdayToSet = "";
    private String action;
    private String whetherSetToEmergency = " ";
    private CheckBox setBirthdayAlarm;
    private ImageView imageView;
    private ImageButton buttonAddImage;
    Drawable drawable;
    Bitmap b;

    private int savedBirthday_Id;

    // spinner variables
    private Spinner relation_spinner;
    private Spinner month_spinner;
    private Spinner day_spinner;
    private Spinner year_spinner;

    // button variables
    private Button button_addExtraNotes;
    // private Button button_cancel; //goes back to home screen without saving changes
    // private Button button_save; // goes back to home screen as it saves changes.

    private TextView et_showRelationText;
    //db variables for family_member--> first, last, relation_id, phone_1, phone_2, email, birthday_id, address_id
    // edit text variables
    private Bitmap myImage;
    private EditText et_first_name;
    private EditText et_last_name;
    private CheckBox setEmergencyContact;
   // private int int_relation_id; // value determined in this class
    private String string_relation_id;
 //   private Spinner edit_spinner;
    private EditText et_phone_1;
    private EditText et_phone_2;
    private EditText et_email;
    private EditText et_birthday;
    private EditText et_description;

    // private int int_address_id;

    static String relationText;
    String monthText;
    String dayText;
    String yearText;

    String relationSpinnerValue;
    String monthSpinnerValue;
    String daySpinnerValue;
    String yearSpinnerValue;



    //Filter Variables for Where clause for sql statements
    private String familyMember_Filter;
    private String relation_Filter;

    // test variables without database
    private Bitmap old_imageView;
    private String old_firstName;
    private String old_lastName;
    private String old_set_emergency;
    private int old_relationStatusId;// TODO: set to be auto-filled
    private String old_phone_1;
    private String old_phone_2;
    private String old_email;
    private String old_birthday;
    private String old_description;
    private String old_relationTitle;


    Uri uriFamilyMemberProvider;


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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_family_member);
//        createNotificationChannel();
        // add the functionality to go back to the parent page from action bar using the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // image view to be changed
       // imageView = (ImageView) findViewById(R.id.image);
//        imageView = (ImageView) findViewById(R.id.image);
//        buttonAddImage = (ImageButton) findViewById(R.id.button_add_image);
//        //code for emergency button
//        buttonAddImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              //toastMsg("Camera button clicked");
//                addCameraPermissions();
//            }
//        });


        imageView = (ImageView) findViewById(R.id.image_informationView);
        et_showRelationText = (TextView) findViewById(R.id.et_showRelationText);
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
        relation_spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.relations_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        relation_spinner.setAdapter(adapter);

        /**-----------------------Code for Month Spinner--------------------------------*/
        // Notes from docs: use following code in you Activity to supply the spinner with the array
        // using the instance of ArrayAdapter
        month_spinner = (Spinner) findViewById(R.id.month_spinner);
        month_spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.month_strings, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        month_spinner.setAdapter(adapter1);

        /**-----------------------Code for Month Spinner--------------------------------*/
        // Notes from docs: use following code in you Activity to supply the spinner with the array
        // using the instance of ArrayAdapter
        day_spinner = (Spinner) findViewById(R.id.day_spinner);
        day_spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.day_strings, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        day_spinner.setAdapter(adapter2);

        /**-----------------------Code for Year Spinner--------------------------------*/
        // Notes from docs: use following code in you Activity to supply the spinner with the array
        // using the instance of ArrayAdapter
        year_spinner = (Spinner) findViewById(R.id.year_spinner);
        year_spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.year_strings, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        year_spinner.setAdapter(adapter3);


        Intent intent = getIntent(); // the intent that launched this activity

        Uri uriFamilyMemberProvider = intent.getParcelableExtra(Family_Member_Provider.CONTENT_ITEM_TYPE_FAMILY_MEMBER);
        //  Uri uriRelationProvider = intent.getParcelableExtra(Relation_Status_Provider.CONTENT_ITEM_TYPE_RELATION_STATUS);
//        relation_Filter = My_SQLiteOpenHelper.RELATION_ID + "=" + uriRelationProvider.getLastPathSegment();

        //If the user wants to edit then it won't be null, but if the user pressed new to insert, it will be null
        if (uriFamilyMemberProvider == null) {

            buttonAddImage = (ImageButton) findViewById(R.id.button_add_image);
            //code for emergency button
            buttonAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //toastMsg("Camera button clicked");
                    addCameraPermissions();
                }
            });
            /**-----------------------Code for Relations Spinner--------------------------------*/

            action = Intent.ACTION_INSERT;
            setTitle("New Family Member");
            et_birthday.setEnabled(false); // disable this as it will only confuse the user.
            et_showRelationText.setEnabled(false);
            // extra notes button
            button_addExtraNotes = (Button) findViewById(R.id.button_add_notes);
            button_addExtraNotes.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toastMsg("Save Family Member before adding more notes.");
                }
            }));
            /**-----------------------Code for Birthday Alarm-------------------------------*/
            //Listener for Birthday Alarm

//            setBirthdayAlarm.setOnClickListener(new View.OnClickListener() {
//                Intent intent;
//                @Override
//                public void onClick(View view) {
//                    boolean checked = ((CheckBox) view).isChecked();
//                    switch (view.getId()) {
//                        case R.id.setBirthdayAlarm:
//                            if(checked){
//                                Intent intent = new Intent(EditorFamilyMember.this, MyReceiver.class);
//                                PendingIntent pendingIntent = PendingIntent.getBroadcast(EditorFamilyMember.this,
//                                        0, intent, 0 );
//                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                                long timeAtCheckClicked = System.currentTimeMillis();
//                                long tenSecondsInMillis = 10;
//                                //RTC_WAKEUP wakes up device to fire pending intent at time specified
//                                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtCheckClicked + tenSecondsInMillis,
//                                        pendingIntent);
//                            }
//                            break;
//                    }
//
//
//                }
//
//            });

            /** Whether user chose this contact as emergency contact/ checkbox is checkd*/
            setEmergencyContact = (CheckBox) findViewById(R.id.setEmergencyContact);
            setEmergencyContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = ((CheckBox) view).isChecked();
                    switch (view.getId()) {
                        case R.id.setEmergencyContact:
                            if (!checked)
                                // toastMsg("Added to Emergency Contacts");
                                whetherSetToEmergency = "0";
                            if (checked)
                                whetherSetToEmergency = "1";
                            toastMsg("Added to Emergency Contacts");
                    }

                }
            });


            setBirthdayAlarm = (CheckBox) findViewById(R.id.setBirthdayAlarm);
            setBirthdayAlarm.setOnClickListener(new View.OnClickListener() {

                //Intent intent;
                Intent intent = new Intent(EditorFamilyMember.this, MyReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(EditorFamilyMember.this,
                        0, intent, 0 );
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                @Override
                public void onClick(View view) {
                    bdayToSet = monthText + " " + dayText + " " + yearText;
                    String date = "";
                    Date date2 = null;
                    boolean checked = ((CheckBox) view).isChecked();
                    switch (view.getId()) {
                        case R.id.setBirthdayAlarm:
                            if (!checked)
                                toastMsg("No birthday reminder set!");
                            if (checked)
                                toastMsg("Birthday reminder set!");
                           // date = et_birthday.getText().toString().trim();
                            //-----------------------
                          //  Intent intent = new Intent(EditorFamilyMember.this, MyReceiver.class);
//                            long timeAtCheckClicked = System.currentTimeMillis();
//                            long tenSecondsInMillis = 10;
                            //RTC_WAKEUP wakes up device to fire pending intent at time specified
//                            alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtCheckClicked + tenSecondsInMillis,
//                                    pendingIntent);
                            //----------------------
                            date = bdayToSet.trim();
                            //  SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                            try {
                                date2 = sdf.parse( date );
                            }catch(ParseException e) {
                                e.getStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            if(date2 != null) {
                                cal.setTime( date2 );
                                long mill = cal.getTimeInMillis();

                                intent = new Intent( EditorFamilyMember.this, MyReceiver.class );
                                PendingIntent sender = PendingIntent.getBroadcast(
                                        EditorFamilyMember.this, 0, intent, 0 );
                                AlarmManager alarmManager =
                                        (AlarmManager) getSystemService( Context.ALARM_SERVICE );

                                alarmManager.set(AlarmManager.RTC_WAKEUP, mill - 86400000,
                                        pendingIntent);


//                                alarmManager.set( AlarmManager.RTC_WAKEUP,
//                                        mill, sender ); //Date minus 24 hours converted to mill - 86400000
                            }
                            break;
                    }
                }

            });

        } else {


            action = Intent.ACTION_EDIT;
            setTitle("Edit Family Member");
            familyMember_Filter = My_SQLiteOpenHelper.FAMILY_MEMBER_ID + "=" + uriFamilyMemberProvider.getLastPathSegment();
            et_birthday.setEnabled(false); // disable this as it will only confuse the user.
            //retrieve the one row from database
            Cursor cursor = getContentResolver().query(uriFamilyMemberProvider,
                    My_SQLiteOpenHelper.ALL_FAMILY_MEMBER_COLUMNS, familyMember_Filter, null, null);
            //retrieve the data
            cursor.moveToFirst();
            //First Name
            //subString index of four is where the actual integer starts after the _id =
            selectedFamilyMember_Id = familyMember_Filter.substring(4);
            // cursor is retrieving all the columns including the image
            byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(My_SQLiteOpenHelper.KEY_FAMILY_IMAGE));
            myImage = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            imageView.setImageBitmap(myImage);

            buttonAddImage = (ImageButton) findViewById(R.id.button_add_image);
            //code for emergency button
            buttonAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //toastMsg("Camera button clicked");
                    addCameraPermissions();
                }
            });
            old_firstName = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.FIRST_NAME));
            et_first_name.setText(old_firstName);
            //Last Name
            old_lastName = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.LAST_NAME));
            et_last_name.setText(old_lastName);
            //emergency checkbox
            old_set_emergency = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.EMERGENCY_CONTACT));

            //relation
            old_relationTitle = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.RELATION_TITLE));
            et_showRelationText.setText(old_relationTitle);
            et_showRelationText.setEnabled(false);

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
            cursor.close();
            //TODO: DO Something with retrieved birthday_Id.........
            //TODO: May have to enter test data into table for birthday, and address, and relation tables
            //Address ID
            //  old_address_id = cursor.getInt(cursor.getColumnIndex(My_SQLiteOpenHelper.ADDRESS_ID));
            //TODO: DO Something with retrieved birthday_Id.........
            //TODO: May have to enter test data into table for birthday, and address, and relation tables
            /** figure out a way to populate the associated courses here ***/
            // extra notes button
            button_addExtraNotes = (Button) findViewById(R.id.button_add_notes);
            button_addExtraNotes.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishEditing();
                    Intent intent2 = new Intent(EditorFamilyMember.this, ListOfNotes.class);
                    startActivity(intent2);

                }
            }));
            //Listener for Birthday Alarm
            setBirthdayAlarm = findViewById(R.id.setBirthdayAlarm);
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
                            date = et_birthday.getText().toString().trim();
                            //  SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                            try {
                                date2 = sdf.parse( date );
                            }catch(ParseException e) {
                                e.getStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            if(date2 != null) {
                                cal.setTime( date2 );
                                long mill = cal.getTimeInMillis();

                                intent = new Intent( EditorFamilyMember.this, MyReceiver.class );
                                PendingIntent sender = PendingIntent.getBroadcast(
                                        EditorFamilyMember.this, 0, intent, 0 );
                                AlarmManager alarmManager =
                                        (AlarmManager) getSystemService( Context.ALARM_SERVICE );
                                alarmManager.set( AlarmManager.RTC_WAKEUP,
                                        mill, sender ); //Date minus 24 hours converted to mill - 86400000
                            }
                            break;
                    }
                }

            });
        }



    }// end onCreate

    private void addCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);

        }else{
            openCamera();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                 openCamera();
            }else{
             toastMsg("Camera Permission Required to Use Camera.");
            }
        }
    }

    private void openCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }



//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void createNotificationChannel(){
//        CharSequence name = "birthdayReminderChannel_1";
//        String description = "Channel for birthday reminder";
//        int importance = NotificationManager.IMPORTANCE_DEFAULT;
//    //    NotificationChannel channel = new NotificationChannel("notifyBirthday", name, importance);
//        channel.setDescription(description);
//
//        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(channel);
//    }
    // from android example: https://developer.android.com/guide/topics/ui/controls/spinner
   // @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

           switch (parent.getId()) {
               case R.id.relation_spinner:
                   if (pos == 0) {
                       string_relation_id = "0";
                       //  relationText = " No Selection "; // no selection made
                       relationText = old_relationTitle;
                    //   toastMsg("No Relation Selected.");
                   } else if (pos == 1) {
                       string_relation_id = "1"; // father
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 2) {
                       string_relation_id = "1";// mother
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 3) {
                       string_relation_id = "1"; // brother
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 4) {
                       string_relation_id = "1"; // sister
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 5) {
                       string_relation_id = "1"; //spouse
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 6) {
                       string_relation_id = "1"; //child
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 7) {
                       string_relation_id = "2"; //uncle
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 8) {
                       string_relation_id = "2"; //aunt
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 9) {
                       string_relation_id = "2"; //nephew
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 10) {
                       string_relation_id = "2"; //niece
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 11) {
                       string_relation_id = "2"; //cousin
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 12) {
                       string_relation_id = "2";//grandfather
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 13) {
                       string_relation_id = "2"; //grandmother
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 14) {
                       string_relation_id = "2"; //great_grandfather
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 15) {
                       string_relation_id = "2"; //great_grandmother
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 16) {
                       string_relation_id = "3"; //daughter-in-law
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 17) {
                       string_relation_id = "3"; //son-in-law
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 18) {
                       string_relation_id = "3"; //brother-in-law
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 19) {
                       string_relation_id = "3"; //sister-in-law
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 20) {
                       string_relation_id = "3"; //father-in-law
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 21) {
                       string_relation_id = "3"; //mother-in-law
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 22) {
                       string_relation_id = "4"; //friend-like-family
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   } else if (pos == 23) {
                       string_relation_id = "4"; //other-family
                       relationText = parent.getItemAtPosition(pos).toString(); // hold the value inside this var so we can enter it into db
                       toastMsg(relationText + " " + "Selected.");
                   }
                   break;
                   /**----------------code for month spinner---------------------------*/

               case R.id.month_spinner:
                   if (pos == 0) {
                       monthText = "01";//January
                   } else if (pos == 1) {
                       monthText = "02";//February
                   } else if (pos == 2) {
                       monthText = "03";//March
                   } else if (pos == 3) {
                       monthText = "04";//April
                   } else if (pos == 4) {
                       monthText = "05";//May
                   } else if (pos == 5) {
                       monthText = "06";//June
                   } else if (pos == 6) {
                       monthText = "07";//July
                   } else if (pos == 7) {
                       monthText = "08";//August
                   } else if (pos == 8) {
                       monthText = "09";//September
                   } else if (pos == 9) {
                       monthText = "10";//October
                   } else if (pos == 10) {
                       monthText = "11";//November
                   } else if (pos == 11) {
                       monthText = "12";//December
                   }
                   break;
                   /**----------------code for day spinner---------------------------*/
               case R.id.day_spinner:
                   if(pos == 0) {
                       dayText = parent.getItemAtPosition(pos).toString();
                   }else if (pos > 0){
                       dayText = parent.getItemAtPosition(pos).toString();
                   }
                   break;
                   /**----------------code for year spinner---------------------------*/
               case R.id.year_spinner:
                   if(pos == 0) {
                       yearText = parent.getItemAtPosition(pos).toString();
                   }else if (pos > 0){
                       yearText = parent.getItemAtPosition(pos).toString();
                   }
                   break;


           } // end switch
       } // end conditional if statement




    public void onNothingSelected(AdapterView<?> parent) {

    }
//TODO: if there is a month a day and a year selected, add them up and enter them into the text box.

//        else if (month_spinner.callOnClick()) {
//           // code goes here...
//            toastMsg("month spinner was clicked");
//        }// end conditional for month_spinner
//        else if (day_spinner.callOnClick()) {
//            // code goes here...
//            toastMsg("day spinner was clicked");
//        }// end conditional for day_spinner
//        else if (year_spinner.callOnClick()) {
//            // code goes here...
//            toastMsg("year spinner was clicked");
//        }// end conditional for year_spinner



    /**
     * method that tells the program what to do once the user chooses to add or edit....
     */
    //db variables for family_member--> first, last, relation_id, phone_1, phone_2, email, birthday_id, address_id
    private void finishEditing() {


        drawable = imageView.getDrawable();
        b = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bitmapData = outputStream.toByteArray();
        String newFamilyMemberFirstName = et_first_name.getText().toString().trim();
        String newFamilyMemberLastName = et_last_name.getText().toString().trim();
        String newSetEmergencyContact = whetherSetToEmergency;
       // int newFamilyMemberRelation_id = int_relation_id; // test data
        String newFamilyMemberRelation_id = string_relation_id;
        String newRelationTitle = relationText;
        String newFamilyMemberPhone_1 = et_phone_1.getText().toString().trim();
        String newFamilyMemberPhone_2 = et_phone_2.getText().toString().trim();
        String newFamilyMemberEmail = et_email.getText().toString().trim();
        String newRelationMonth = monthText;
        //String newRelationDay = "-00";
        String newRelationDay = "-" + dayText;
       // String newRelationYear = "-0000";
        String newRelationYear = "-" + yearText;
        String newRelationBirthday = newRelationMonth + newRelationDay + newRelationYear;
       // String newFamilyMemberBirthday = et_birthday.getText().toString().trim();
        String newFamilyMemberBirthday = newRelationBirthday.trim();
        String newFamilyMemberDescription = et_description.getText().toString().trim();



        // int newFamilyMemberBirthday_Id = savedBirthday_Id;
        //int newFamilyMemberAddress_Id = int_address_id;


        //code for action insert
        switch (action) {
            case Intent.ACTION_INSERT:
                // If familyMember is empty--> if any of the required fields are empty then cancel the note...
                // Required fields are the first name and the primary phone number
                if (newFamilyMemberFirstName.length() == 0 || newFamilyMemberPhone_1.length() == 0) {
                    setResult(RESULT_CANCELED);
                    toastMsg("Action Cancelled");
                } else {
                    insertFamilyMember(bitmapData, newFamilyMemberFirstName, newFamilyMemberLastName, newSetEmergencyContact,
                            newFamilyMemberRelation_id, newFamilyMemberPhone_1, newFamilyMemberPhone_2, newFamilyMemberEmail,
                            newFamilyMemberBirthday, newFamilyMemberDescription, newRelationTitle);
                }
                break;
            // if user sets any of these required fields to empty, than delete that family member, but ask if its okay...
            case Intent.ACTION_EDIT:

                if (newFamilyMemberFirstName.length() == 0 || newFamilyMemberPhone_1.length() == 0) {
                   deleteFamilyMember();
                    // if all the elements are still the same
                } else if (old_imageView.equals(imageView) && old_firstName.equals(newFamilyMemberFirstName) && old_lastName.equals(newFamilyMemberLastName)
                        && old_set_emergency.equals(newSetEmergencyContact) && old_phone_1.equals(newFamilyMemberPhone_1)
                        && old_phone_2.equals(newFamilyMemberPhone_2) && old_email.equals(newFamilyMemberEmail)
                        && old_birthday.equals(newFamilyMemberBirthday) && old_description.equals(newFamilyMemberDescription)
                        && old_relationTitle.equals(newRelationTitle)) {
                    setResult(RESULT_CANCELED);
                } else {
                    deleteFamilyMember();
                    updateFamilyMember(bitmapData, newFamilyMemberFirstName, newFamilyMemberLastName, newSetEmergencyContact,
                            newFamilyMemberRelation_id, newFamilyMemberPhone_1, newFamilyMemberPhone_2, newFamilyMemberEmail,
                            newFamilyMemberBirthday, newFamilyMemberDescription, newRelationTitle);
                }
                break;
        }
        //finished with activity so go back to parent activity
        finish();


    }// end finishEditing



    private void updateFamilyMember(byte[] myImage, String newFirstName, String newLastName, String emergencyContact, String newRelation_Id,
                                    String newPhone_1, String newPhone_2, String newEmail,
                                    String newBirthday, String newDescription, String newRelatonTitle) {

        ContentValues values = new ContentValues();
     //   byte[] data = getBitMapAsByteArray(myImage);
     //   values.put(My_SQLiteOpenHelper.KEY_FAMILY_IMAGE, data);
        values.put(My_SQLiteOpenHelper.FIRST_NAME, newFirstName);
        values.put(My_SQLiteOpenHelper.LAST_NAME, newLastName);
        values.put(My_SQLiteOpenHelper.EMERGENCY_CONTACT, emergencyContact);
        values.put(My_SQLiteOpenHelper.FAM_MEMBER_RELATION_ID, newRelation_Id);
        values.put(My_SQLiteOpenHelper.PHONE_1, newPhone_1);
        values.put(My_SQLiteOpenHelper.PHONE_2, newPhone_2);
        values.put(My_SQLiteOpenHelper.EMAIL, newEmail);
        values.put(My_SQLiteOpenHelper.BIRTH_DAY, newBirthday);
        values.put(My_SQLiteOpenHelper.DESCRIPTION, newDescription);
        values.put(My_SQLiteOpenHelper.RELATION_TITLE, newRelatonTitle);
        getContentResolver().insert(Family_Member_Provider.FAMILY_MEMBER_CONTENT_URI, values);
        Toast.makeText(this, "Family Member Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorFamilyMember.this, MainActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
    }
    // to insert image to db
    // used this source for help on this method: https://stackoverflow.com/questions/9357668/how-to-store-image-in-sqlite-database/9357943#9357943
    public static byte[] getBitMapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    //db variables for family_member--> first, last, relation_id, phone_1, phone_2, email, birthday_id, address_id
    private void insertFamilyMember(byte[] image, String newFirstName, String newLastName, String emergencyContact,
                                    String newRelation_Id, String newPhone_1, String newPhone_2, String newEmail,
                                    String newBirthday, String newDescription, String newRelationTitle) {

        ContentValues values = new ContentValues();

       // byte[] data = getBitMapAsByteArray(myImage);
        values.put(My_SQLiteOpenHelper.KEY_FAMILY_IMAGE, image);
        values.put(My_SQLiteOpenHelper.FIRST_NAME, newFirstName);
        values.put(My_SQLiteOpenHelper.LAST_NAME, newLastName);
        values.put(My_SQLiteOpenHelper.EMERGENCY_CONTACT, emergencyContact);
        values.put(My_SQLiteOpenHelper.FAM_MEMBER_RELATION_ID, newRelation_Id);
        values.put(My_SQLiteOpenHelper.PHONE_1, newPhone_1);
        values.put(My_SQLiteOpenHelper.PHONE_2, newPhone_2);
        values.put(My_SQLiteOpenHelper.EMAIL, newEmail);
        values.put(My_SQLiteOpenHelper.BIRTH_DAY, newBirthday);
        values.put(My_SQLiteOpenHelper.DESCRIPTION, newDescription);
        values.put(My_SQLiteOpenHelper.RELATION_TITLE, newRelationTitle);
        getContentResolver().insert(Family_Member_Provider.FAMILY_MEMBER_CONTENT_URI, values);
        Toast.makeText(this, "Family Member Added", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorFamilyMember.this, MainActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
    }

    //method to delete one row and show a message
    private void deleteFamilyMember() {
        getContentResolver().delete(Family_Member_Provider.FAMILY_MEMBER_CONTENT_URI, familyMember_Filter, null);
        Toast.makeText(this, "Family Member Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorFamilyMember.this, All_Family_Members.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    public void checkBeforeDelete(){
        //    private void deleteAllNotes() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            getContentResolver().delete(Family_Member_Provider.FAMILY_MEMBER_CONTENT_URI, familyMember_Filter, null);
                            Intent intent = new Intent(EditorFamilyMember.this, All_Family_Members.class);
                            startActivity(intent);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                };
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage("Are you sure you want to delete this Family Member?");
        builder.setPositiveButton(getString(android.R.string.yes), dialogClickListener);
        builder.setNegativeButton(getString(android.R.string.no), dialogClickListener);
        builder.show();
    }
    public void warnUserNeedsRequiredForSave(){
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            getContentResolver().delete(Family_Member_Provider.FAMILY_MEMBER_CONTENT_URI, familyMember_Filter, null);
                            Intent intent = new Intent(EditorFamilyMember.this, All_Family_Members.class);
                            startActivity(intent);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                };
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage("Cancel Adding Family Member? Required First Name and Primary Phone Number to save.");
        builder.setPositiveButton(getString(android.R.string.yes), dialogClickListener);
        builder.setNegativeButton(getString(android.R.string.no), dialogClickListener);
        builder.show();
    }
    public void checkBeforeSave(){
        //    private void deleteAllNotes() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                           // getContentResolver().delete(Family_Member_Provider.FAMILY_MEMBER_CONTENT_URI, familyMember_Filter, null);
                            finishEditing();
                            Intent intent = new Intent(EditorFamilyMember.this, All_Family_Members.class);
                            startActivity(intent);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                };
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage("Save Changes?");
        builder.setPositiveButton(getString(android.R.string.yes), dialogClickListener);
        builder.setNegativeButton(getString(android.R.string.no), dialogClickListener);
        builder.show();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);
        }

//        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//        }


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

        if(action.equals( Intent.ACTION_EDIT )){
            getMenuInflater().inflate( R.menu.menu_edit_family_member, menu );
        }
        else
            getMenuInflater().inflate( R.menu.menu_add_family_member, menu );

//        // getMenuInflater().inflate( R.menu.menu_edit_family_member, menu );
//        getMenuInflater().inflate(R.menu.menu_add_family_member, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case android.R.id.home:
               // finishEditing();
              //  checkBeforeSave();
                intent = new Intent(EditorFamilyMember.this, All_Family_Members.class);
                startActivity(intent);
                break;
            case R.id.action_save:
                //   Toast.makeText(this, "Action code for Save reached", Toast.LENGTH_SHORT).show();
                finishEditing();
                intent = new Intent(EditorFamilyMember.this, All_Family_Members.class);
                startActivity(intent);
                break;
            case R.id.action_cancel:
                toastMsg("No changes saved.");
                intent = new Intent(EditorFamilyMember.this, All_Family_Members.class);
                startActivity(intent);
                break;
            case R.id.action_delete:
                checkBeforeDelete();
              //  deleteFamilyMember();
                break;
        }
        return true;
    }




    /**
     * -------------------------method for Cancel and Save Button----------------------------
     */
    //Cancel Button method
    public void cancel_BackToHomeScreen() {
        // tell user that changes were not saved
        toastMsg("No changes were saved.");
        Intent intent = new Intent(EditorFamilyMember.this, MainActivity.class);
        startActivity(intent);
    }

    //Save Button method
    public void save_BackToHomeScreen() {
        // tell user that changes were not saved
        toastMsg("Changes were saved!");
        Intent intent = new Intent(EditorFamilyMember.this, MainActivity.class);
        startActivity(intent);
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
