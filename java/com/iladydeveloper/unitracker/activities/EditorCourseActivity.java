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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.iladydeveloper.unitracker.MyReceiver;
import com.iladydeveloper.unitracker.R;

import com.iladydeveloper.unitracker.content_providers.AssessmentProvider;
import com.iladydeveloper.unitracker.content_providers.CourseProvider;
import com.iladydeveloper.unitracker.content_providers.MentorProvider;
import com.iladydeveloper.unitracker.cursor_adapters.AssessmentCursorAdapter;
import com.iladydeveloper.unitracker.cursor_adapters.MentorCursorAdapter;
import com.iladydeveloper.unitracker.database.My_SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditorCourseActivity extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;
    private String action;

    private Button button_add_assessment;
    private Button button_add_mentor;

    // Work on getting these first fields to work first.
    private EditText editorCourseTitle;
    private EditText editorCourseStart;
    private EditText editorCourseEnd;
    private EditText editorCourseStatus;
    private EditText editorCourseDescription;

    //   private RadioGroup editorCourseStatus;
    public static String selectedCourseID;
    private String courseFilter;//Where clause for sql statements

    private String oldCourseTitle;// existing text of old term title
    private String oldCourseStart;// existing text of old term start
    private String oldCourseEnd;// existing text of old term end
    private String oldCourseStatus;// existing text of auto status
    private String oldCourseDescription;// existing text of description
    private String selectedTermId;

    private CheckBox setStartAlarm;
    private CheckBox setEndAlarm;

    // public static String selectedTermID;
    //Uri uri = intent.getParcelableExtra( TermsDataSource.CONTENT_ITEM_TYPE );

    //String selectedTermId;
    // private Intent intent = getIntent(); // the intent that launched this activity


    @Override
    protected void onRestart() {
        cursorAdapter.notifyDataSetChanged();
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cursorAdapter.notifyDataSetChanged();

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_editor_course );



        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        editorCourseTitle = (EditText) findViewById(R.id.etCourseTitle);
        editorCourseStart = (EditText) findViewById( R.id.etCourseStart);
        editorCourseEnd = (EditText) findViewById(R.id.etCourseEnd);
        editorCourseStatus = (EditText) findViewById( R.id.etStatusAuto );
        editorCourseDescription = (EditText) findViewById( R.id.etDescriptionText );
        Uri uri = intent.getParcelableExtra( CourseProvider.CONTENT_ITEM_TYPE_COURSE);

        //String termID = intent.getStringExtra( "Term ID" );
        // final String selectedTermID = intent.getParcelableExtra( "Selected Term ID" );


        //If the user wants to edit then it won't be null, but if the user pressed new to insert, it will be null
        if(uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Course");

            //Button for new Assessment
            button_add_assessment = (Button) findViewById(R.id.add_assessment_button);
            button_add_assessment.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    toastMsg("Save Course before adding assessments");

                }
            });
            //Button for new Mentor
            button_add_mentor = (Button) findViewById(R.id.add_mentor_button);
            button_add_mentor.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    toastMsg("Save Course before adding mentors");
                }
            });
            //Listener for Start
            setStartAlarm = (CheckBox) findViewById( R.id.setCourseStart);
            setStartAlarm.setOnClickListener(new View.OnClickListener() {

                Intent intent;
                @Override
                public void onClick(View view) {
                    String date = "";
                    Date date2 = null;
                    boolean checked = ((CheckBox) view).isChecked();
                    switch(view.getId()) {
                        case R.id.setCourseStart:
                            if(!checked)
                                toastMsg( "No reminder set" );
                            if (checked)
                                toastMsg( "Reminder set" );
                                date = editorCourseStart.getText().toString().trim();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                date2 = sdf.parse( date );
                            }catch(ParseException e) {
                                e.getStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            if(date2 != null) {
                                cal.setTime( date2 );
                                long mill = cal.getTimeInMillis();

                                intent = new Intent( EditorCourseActivity.this, MyReceiver.class );
                                PendingIntent sender = PendingIntent.getBroadcast(
                                        EditorCourseActivity.this, 0, intent, 0 );
                                AlarmManager alarmManager =
                                        (AlarmManager) getSystemService( Context.ALARM_SERVICE );
                                alarmManager.set( AlarmManager.RTC_WAKEUP,
                                        mill, sender ); //Date minus 24 hours converted to mill - 86400000
                            }
                            break;
                    }
                }

            });


            //Listener for End
            setEndAlarm = (CheckBox) findViewById( R.id.setCourseEnd);
            setEndAlarm.setOnClickListener(new View.OnClickListener() {
                Intent intent;
                @Override
                public void onClick(View view) {
                    String date = "";
                    Date date2 = null;
                    boolean checked = ((CheckBox) view).isChecked();
                    switch(view.getId()) {
                        case R.id.setCourseEnd:
                            if(!checked)
                                toastMsg( "No reminder Set" );
                            if (checked)
                                toastMsg( "Reminder Set" );
                                date = editorCourseEnd.getText().toString().trim();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                date2 = sdf.parse( date );
                            }catch(ParseException e) {
                                e.getStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            if(date2 != null) {
                                cal.setTime( date2 );
                                long mill = cal.getTimeInMillis();

                                intent = new Intent( EditorCourseActivity.this, MyReceiver.class );
                                PendingIntent sender = PendingIntent.getBroadcast(
                                        EditorCourseActivity.this, 0, intent, 0 );
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
            setTitle("Edit Course");
            action = Intent.ACTION_EDIT;
            //Button for new Assessment
            button_add_assessment = (Button) findViewById(R.id.add_assessment_button);
            button_add_assessment.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Open the editorAssessmentActivity
                    Intent intent = new Intent(EditorCourseActivity.this, EditorAssessmentActivity.class);
                    startActivity( intent );

                }
            });
            //Button for new Mentor
            button_add_mentor = (Button) findViewById(R.id.add_mentor_button);
            button_add_mentor.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Open the editorAssessmentActivity
                    Intent intent = new Intent(EditorCourseActivity.this, EditorMentorActivity.class);
                    startActivity( intent );

                }
            });
            courseFilter = My_SQLiteOpenHelper.COURSE_ID + "=" + uri.getLastPathSegment();
            selectedTermId = EditorTermActivity.selectedTermID;
            //retrieve the one row from database
            Cursor cursor = getContentResolver().query( uri,
                    My_SQLiteOpenHelper.ALL_COURSE_COLUMNS, courseFilter, null, null);
            //retrieve the data
            cursor.moveToFirst();
            selectedCourseID = courseFilter.substring( 4 );
           // selectedTermId = intent.getStringExtra( "selectedTermID" );
            //Title
            oldCourseTitle = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.COURSE_TITLE));
            editorCourseTitle.setText(oldCourseTitle);
            //Start
            oldCourseStart = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.COURSE_START));
            editorCourseStart.setText(oldCourseStart);
            //End
            oldCourseEnd = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.COURSE_END));
            editorCourseEnd.setText(oldCourseEnd);
            // editorEnd.requestFocus();
            //Status
            oldCourseStatus = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.COURSE_STATUS));
            editorCourseStatus.setText(oldCourseStatus);

            //Description
            oldCourseDescription = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.OPTIONAL_NOTES));
            editorCourseDescription.setText(oldCourseDescription);

            //Listener for Start
            setStartAlarm = (CheckBox) findViewById( R.id.setCourseStart);
            setStartAlarm.setOnClickListener(new View.OnClickListener() {
                Intent intent;
                @Override
                public void onClick(View view) {
                    String date = "";
                    Date date2 = null;
                    boolean checked = ((CheckBox) view).isChecked();
                    switch(view.getId()) {
                        case R.id.setCourseStart:
                            if(!checked)
                                toastMsg( "No reminder Set" );
                            if (checked)
                                toastMsg( "Reminder Set" );
                                date = editorCourseStart.getText().toString().trim();
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

                                intent = new Intent( EditorCourseActivity.this, MyReceiver.class );
                                PendingIntent sender = PendingIntent.getBroadcast(
                                        EditorCourseActivity.this, 0, intent, 0 );
                                AlarmManager alarmManager =
                                        (AlarmManager) getSystemService( Context.ALARM_SERVICE );
                                alarmManager.set( AlarmManager.RTC_WAKEUP,
                                        mill, sender ); //try testing setting with goal date
                            }
                            break;

                    }
                }

            });
            //Listener for End
            setEndAlarm = (CheckBox) findViewById( R.id.setCourseEnd);
            setEndAlarm.setOnClickListener(new View.OnClickListener() {
                Intent intent;
                Toast toast;
                @Override
                public void onClick(View view) {
                    String date = "";
                    Date date2 = null;
                    boolean checked = ((CheckBox) view).isChecked();
                    switch(view.getId()) {
                        case R.id.setCourseEnd:
                            if(!checked)
                                toastMsg( "No reminder Set" );
                            if (checked)
                                toastMsg( "Reminder Set" );
                                date = editorCourseEnd.getText().toString().trim();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                date2 = sdf.parse( date );
                            }catch(ParseException e) {
                                e.getStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            if(date2 != null) {
                                cal.setTime( date2 );
                                long mill = cal.getTimeInMillis();

                                intent = new Intent( EditorCourseActivity.this, MyReceiver.class );
                                PendingIntent sender = PendingIntent.getBroadcast(
                                        EditorCourseActivity.this, 0, intent, 0 );
                                AlarmManager alarmManager =
                                        (AlarmManager) getSystemService( Context.ALARM_SERVICE );
                                alarmManager.set( AlarmManager.RTC_WAKEUP,
                                        mill, sender ); //try testing setting with goal date
                            }
                            break;

                    }
                }

            });


            // editorEnd.requestFocus();
            /** Code to populate the associated assessments and mentors list views  **/
            ContentValues values = new ContentValues();
            /** Add Assessments into Assessment view **/
            // use *courseFilter*  to pull out the Course Id from the assessment's table's course id
              Cursor assessmentCursor = this.getContentResolver().query(AssessmentProvider.ASSESSMENT_CONTENT_URI,
                    My_SQLiteOpenHelper.ALL_ASSESSMENT_COLUMNS,
                    "ass_course_id " + "= " + selectedCourseID, null, null,
                    null);
//            Cursor assessmentCursor = getContentResolver().query( AssessmentProvider.ASSESSMENT_CONTENT_URI,
//                    My_SQLiteOpenHelper.ALL_ASSESSMENT_COLUMNS, courseFilter,
//                    null, null, null);
            cursorAdapter = new AssessmentCursorAdapter( this, assessmentCursor, false );
            //reference to listview
            ListView list = (ListView)findViewById(R.id.assessment_list);
            list.setAdapter(cursorAdapter);
            //on item click listener for user selecting term item from the list
            list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(EditorCourseActivity.this, EditorAssessmentActivity.class);
                    Uri uri = Uri.parse( AssessmentProvider.ASSESSMENT_CONTENT_URI + "/" + id); //represents PK value
                    intent.putExtra( AssessmentProvider.CONTENT_ITEM_TYPE_ASSESSMENT, uri); // uri to intent as extra
                    startActivityForResult(intent, EDITOR_REQUEST_CODE );
                }
            } );
            list.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return false;
                }
            } );

        }// finish adding assessments to listView
        /** Add Mentors into Mentor view **/
        // use *courseFilter* to pull out the Course Id from the mentor's table's course id
        Cursor mentorCursor = this.getContentResolver().query(MentorProvider.MENTOR_CONTENT_URI,
                My_SQLiteOpenHelper.ALL_MENTOR_COLUMNS,
                "assigned_course_id " + "= " + selectedCourseID, null, null,
                null);
//        Cursor mentorCursor = getContentResolver().query( MentorProvider.MENTOR_CONTENT_URI,
//                My_SQLiteOpenHelper.ALL_MENTOR_COLUMNS,  courseFilter,
//                null, null, null);
        cursorAdapter = new MentorCursorAdapter(this, mentorCursor, false );
        //reference to listview
        ListView list2 = (ListView)findViewById(R.id.mentor_list);
        list2.setAdapter(cursorAdapter);
        //on item click listener for user selecting term item from the list
        list2.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EditorCourseActivity.this, EditorMentorActivity.class);
                Uri uri = Uri.parse( MentorProvider.MENTOR_CONTENT_URI + "/" + id); //represents PK value
                intent.putExtra( MentorProvider.CONTENT_ITEM_TYPE_MENTOR, uri); // uri to intent as extra
                startActivityForResult(intent, EDITOR_REQUEST_CODE );
            }
        } );
        list2.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        } );

    }// end onCreate()


    public void toastMsg(String msg) {

        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();

    }


    //when creating options menu, put trash can icon in menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(action.equals( Intent.ACTION_EDIT )) {
            getMenuInflater().inflate( R.menu.menu_course_editor, menu );
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_extra_notes:
                Intent intent2 = new Intent(EditorCourseActivity.this, ListOfNotesActivity.class);
                startActivity(intent2);
                finishEditing();
                break;
            case R.id.action_delete:
                deleteCourse();
                break;
        }
        return true;
    }

    private void finishEditing(){

        // final String selectedTermID = intent.getParcelableExtra( "Selected Term ID" );
        Intent intent = getIntent(); // the intent that launched this activity

        selectedTermId = EditorTermActivity.selectedTermID;
        String newAssociatedTermId = "1";
        String newCourseTitle = editorCourseTitle.getText().toString().trim();
        String newCourseStart = editorCourseStart.getText().toString().trim();
        String newCourseEnd = editorCourseEnd.getText().toString().trim();
        String newCourseStatus = editorCourseStatus.getText().toString().trim();
        String newCourseDescription = editorCourseDescription.getText().toString().trim();

        //code for action insert
        switch (action) {
            case Intent.ACTION_INSERT:
                //if note is empty
                if (newCourseTitle.length() == 0 && newCourseStart.length() == 0
                        && newCourseEnd.length() == 0) {
                    setResult( RESULT_CANCELED ); //CANCELLED OPERATION
                    //Toast.makeText( this, "Term Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    //if note is full
                    insertCourse(selectedTermId, newCourseTitle, newCourseStart,
                            newCourseEnd, newCourseStatus, newCourseDescription );
                }
                break;
            case Intent.ACTION_EDIT:
                if (newCourseTitle.length() == 0 && newCourseStart.length() == 0 && newCourseEnd.length() == 0) {
                    deleteCourse();
                    updateCourse( null,null, null, null,
                            null, null);
                } else if (oldCourseTitle.equals( newCourseTitle ) && oldCourseStart.equals( newCourseStart )
                        && oldCourseEnd.equals( newCourseEnd ) && oldCourseStatus.equals( newCourseStatus ) &&
                        oldCourseDescription.equals( newCourseDescription )) {
                    setResult( RESULT_CANCELED );
                } else {
                    updateCourse( selectedTermId, newCourseTitle, newCourseStart, newCourseEnd,
                            newCourseStatus, newCourseDescription);
                }
                break;
        }
        //finished with activity so go back to parent activity
        finish();

    }

    private void updateCourse(  String newAssociatedTermID, String newCourseTitle, String newCourseStart,
                                String newCourseEnd, String newCourseStatus, String newOptionalNotes) {
        ContentValues values = new ContentValues();

        values.put(My_SQLiteOpenHelper.ASS_TERM_ID, newAssociatedTermID);
        values.put(My_SQLiteOpenHelper.COURSE_TITLE, newCourseTitle);
        values.put(My_SQLiteOpenHelper.COURSE_START, newCourseStart);
        values.put(My_SQLiteOpenHelper.COURSE_END, newCourseEnd);
        values.put(My_SQLiteOpenHelper.COURSE_STATUS, newCourseStatus);
        values.put(My_SQLiteOpenHelper.OPTIONAL_NOTES, newOptionalNotes);
        getContentResolver().update( CourseProvider.COURSE_CONTENT_URI, values, courseFilter, null);
        Intent intent = new Intent(EditorCourseActivity.this, ListOfTermsActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Course Updated", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(EditorCourseActivity.this, ListOfTermsActivity.class);
//        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }



    private void insertCourse(String associatedTermID, String title, String start,
                              String end, String status, String notes){

        ContentValues values = new ContentValues();
        values.put(My_SQLiteOpenHelper.ASS_TERM_ID, associatedTermID);
        values.put(My_SQLiteOpenHelper.COURSE_TITLE, title );
        values.put(My_SQLiteOpenHelper.COURSE_START, start );
        values.put(My_SQLiteOpenHelper.COURSE_END, end );
        values.put(My_SQLiteOpenHelper.COURSE_STATUS, status );
        values.put(My_SQLiteOpenHelper.OPTIONAL_NOTES, notes);
        Uri courseUri = getContentResolver().insert(CourseProvider.COURSE_CONTENT_URI, values);
        Toast.makeText(this, "Course Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorCourseActivity.this, ListOfTermsActivity.class);
        startActivity(intent);
//        Intent intent = new Intent(EditorCourseActivity.this, ListOfTermsActivity.class);
//        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    //method to delete one row and show a message
    private void deleteCourse() {
        getContentResolver().delete( CourseProvider.COURSE_CONTENT_URI, courseFilter, null);
        Toast.makeText(this, "Course Deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorCourseActivity.this, ListOfTermsActivity.class);
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
