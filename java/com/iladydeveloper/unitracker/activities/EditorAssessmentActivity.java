package com.iladydeveloper.unitracker.activities;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.iladydeveloper.unitracker.MyReceiver;
import com.iladydeveloper.unitracker.R;
import com.iladydeveloper.unitracker.content_providers.AssessmentProvider;
import com.iladydeveloper.unitracker.content_providers.MentorProvider;
import com.iladydeveloper.unitracker.content_providers.TermProvider;
import com.iladydeveloper.unitracker.database.My_SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditorAssessmentActivity extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;
    private String action;



    // Work on getting these first fields to work first.
    private EditText editorAssessmentTitle;
    private EditText editorAssessmentType;
    private EditText editorAssessmentDate;

    // private String assessmentFilter;//Where clause for sql statements
    private String assessmentFilter;

    private String oldAssessmentTitle;// existing text of old term title
    private String oldAssessmentType;// existing text of old term start
    private String oldAssessmentDate;// existing text of old term end


    private String selectedCourseId;
    private CheckBox setAlarm;
    Intent backhomeIntent = getIntent();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_editor_assessment_actvity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent backhomeIntent = getIntent();
        editorAssessmentTitle = (EditText) findViewById(R.id.etAssessmentTitle);
        editorAssessmentType = (EditText) findViewById( R.id.etAssessmentType);
        editorAssessmentDate = (EditText) findViewById(R.id.etScheduledDate);

        Uri uri = backhomeIntent.getParcelableExtra( AssessmentProvider.CONTENT_ITEM_TYPE_ASSESSMENT);

        // If the user wants to edit then it won't be null,
        // but if the user pressed new to insert, it will be null

//        checkBox.setChecked(true);
//
//        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // update your model (or other business logic) based on isChecked
        if(uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Assessment");
            //Listener for Start
            setAlarm = (CheckBox) findViewById( R.id.setAssessmentNotificationBox);
            setAlarm.setOnClickListener(new View.OnClickListener() {
                Intent intent;
                @Override
                public void onClick(View view) {
                    String date = "";
                    Date date2 = null;

                    boolean checked = ((CheckBox) view).isChecked();
                    switch(view.getId()) {
                        case R.id.setAssessmentNotificationBox:
                            if(!checked)
                                toastMsg( "No reminder Set" );
                            if (checked)
                                toastMsg( "Reminder Set" );
                                date = editorAssessmentDate.getText().toString().trim();
                                SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );
                                try {
                                    date2 = sdf.parse( date );
                                } catch (ParseException e) {
                                    e.getStackTrace();
                                }
                            if(date2 != null) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime( date2 );
                                long mill = cal.getTimeInMillis();

                                intent = new Intent( EditorAssessmentActivity.this, MyReceiver.class );
                                PendingIntent sender = PendingIntent.getBroadcast(
                                        EditorAssessmentActivity.this, 0, intent, 0 );
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
        else {
            setTitle( "Edit Assessment" );
            action = Intent.ACTION_EDIT;
            assessmentFilter = My_SQLiteOpenHelper.ASSESSMENT_ID + "=" + uri.getLastPathSegment();
            selectedCourseId = EditorCourseActivity.selectedCourseID;
            //retrieve the one row from database
            Cursor cursor = getContentResolver().query( uri,
                    My_SQLiteOpenHelper.ALL_ASSESSMENT_COLUMNS, assessmentFilter, null, null );
            //retrieve the data
            cursor.moveToFirst();
            // selectedCourseId = intent.getStringExtra( "selectedCourseId" );
            //Title
            oldAssessmentTitle = cursor.getString( cursor.getColumnIndex( My_SQLiteOpenHelper.ASSESSMENT_TITLE ) );
            editorAssessmentTitle.setText( oldAssessmentTitle );
            //Start
            oldAssessmentType = cursor.getString( cursor.getColumnIndex( My_SQLiteOpenHelper.ASSESSMENT_TYPE ) );
            editorAssessmentType.setText( oldAssessmentType );
            //End
            oldAssessmentDate = cursor.getString( cursor.getColumnIndex( My_SQLiteOpenHelper.SCHEDULED_DATE) );
            editorAssessmentDate.setText( oldAssessmentDate );

            //Listener for Start
            setAlarm = (CheckBox) findViewById( R.id.setAssessmentNotificationBox);
            setAlarm.setOnClickListener(new View.OnClickListener() {
                Intent intent;
                @Override
                public void onClick(View view) {
                    String date = "";
                    Date date2 = null;
                    boolean checked = ((CheckBox) view).isChecked();
                    switch(view.getId()) {
                        case R.id.setAssessmentNotificationBox:
                            if(!checked)
                                toastMsg( "No reminder Set" );
                            if (checked)
                                toastMsg( "Reminder Set" );
                                date = editorAssessmentDate.getText().toString().trim();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                date2 = sdf.parse( date );
                            }catch(ParseException e) {
                                e.getStackTrace();
                            }
                            if(date2 != null) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime( date2 );
                                long mill = cal.getTimeInMillis();

                                intent = new Intent( EditorAssessmentActivity.this, MyReceiver.class );
                                PendingIntent sender = PendingIntent.getBroadcast(
                                        EditorAssessmentActivity.this, 0, intent, 0 );
                                AlarmManager alarmManager =
                                        (AlarmManager) getSystemService( Context.ALARM_SERVICE );
                                alarmManager.set( AlarmManager.RTC_WAKEUP,
                                        mill, sender ); //Date minus 24 hours converted to mill - 86400000
                                break;
                            }
                    }
                }

            });


        }//end else

    }// end onCreate()


    public void toastMsg(String msg) {

        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();

    }
    //when creating options menu, put trash can icon in menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(action.equals( Intent.ACTION_EDIT )) {
            getMenuInflater().inflate( R.menu.menu_assessment_editor, menu );
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                Intent intent = new Intent(EditorAssessmentActivity.this, ListOfTermsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_delete:
                deleteAssessment();
                break;
        }
        return true;
    }

    private void finishEditing(){

        // final String selectedCourseId = intent.getParcelableExtra( "Selected Term ID" );
        Intent intent = getIntent(); // the intent that launched this activity

        selectedCourseId = EditorCourseActivity.selectedCourseID;
        String title = editorAssessmentTitle.getText().toString().trim();
        String type = editorAssessmentType.getText().toString().trim();
        String date = editorAssessmentDate.getText().toString().trim();

        //code for action insert
        switch (action) {
            case Intent.ACTION_INSERT:
                //if assessment is empty
                if (title.length() == 0 && type.length() == 0
                        && date.length() == 0) {
                    setResult( RESULT_CANCELED ); //CANCELLED OPERATION
                    //Toast.makeText( this, "Term Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    //if assessment is full
                    insertAssessment(title, type,  selectedCourseId, date);
                }
                break;
            case Intent.ACTION_EDIT:
                if (title.length() == 0 && type.length() == 0
                        && date.length() == 0) {
                    deleteAssessment();
                    updateAssessment( title, type,
                            date);
                } else if (oldAssessmentTitle.equals( title ) &&
                        oldAssessmentType.equals( type )
                        && oldAssessmentDate.equals( date )) {
                    setResult( RESULT_CANCELED );
                } else {
                    updateAssessment( title, type,
                            date);
                }
                break;
        }
        //finished with activity so go back to parent activity
        finish();

    }

    //  ASSESSMENT_TITLE, ASSESSMENT_TYPE, ASSOCIATED_COURSE_ID, SCHEDULED_DATE
    private void insertAssessment( String title, String type,
                               String assCourseID, String date){

        ContentValues values = new ContentValues();
        values.put(My_SQLiteOpenHelper.ASSESSMENT_TITLE, title);
        values.put(My_SQLiteOpenHelper.ASSESSMENT_TYPE, type );
        values.put(My_SQLiteOpenHelper.ASSOCIATED_COURSE_ID, assCourseID );
        values.put(My_SQLiteOpenHelper.SCHEDULED_DATE, date );


        Uri assessmentUri = getContentResolver().insert(AssessmentProvider.ASSESSMENT_CONTENT_URI, values);
        Log.d("ListOfAssessments", "Inserted a new assessment " + assessmentUri.getLastPathSegment());
        Toast.makeText(this, "Term Assessment", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(EditorAssessmentActivity.this, EditorCourseActivity.class);
//        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    private void updateAssessment(  String newTitle, String newType,
                                String newDate) {
        ContentValues values = new ContentValues();
        values.put(My_SQLiteOpenHelper.ASSESSMENT_TITLE, newTitle);
        values.put(My_SQLiteOpenHelper.ASSESSMENT_TYPE, newType);
        values.put(My_SQLiteOpenHelper.SCHEDULED_DATE, newDate);

        getContentResolver().update( AssessmentProvider.ASSESSMENT_CONTENT_URI, values, assessmentFilter, null);
        Toast.makeText(this, "Assessment Updated", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(EditorAssessmentActivity.this, ListOfTermsActivity.class);
//        startActivity(intent);
        //setResult(EditorCourseActivity.RESULT_OK);
        setResult(RESULT_OK);
        finish();
    }


    //method to delete one row and show a message
    private void deleteAssessment() {
        getContentResolver().delete( AssessmentProvider.ASSESSMENT_CONTENT_URI, assessmentFilter, null);
        Toast.makeText(this, "Assessment Deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorAssessmentActivity.this, ListOfTermsActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finishEditing();
//    }
}


