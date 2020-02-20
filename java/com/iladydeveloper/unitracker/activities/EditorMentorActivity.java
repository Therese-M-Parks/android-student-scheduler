package com.iladydeveloper.unitracker.activities;
import android.content.ContentValues;
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
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.iladydeveloper.unitracker.R;
import com.iladydeveloper.unitracker.blueprints.Term;
import com.iladydeveloper.unitracker.content_providers.AssessmentProvider;
import com.iladydeveloper.unitracker.content_providers.MentorProvider;
import com.iladydeveloper.unitracker.content_providers.TermProvider;
import com.iladydeveloper.unitracker.database.My_SQLiteOpenHelper;



public class EditorMentorActivity extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;
    private String action;



    // Work on getting these first fields to work first.
    private EditText editorMentorTitle;
    private EditText editorMentorPhone;
    private EditText editorMentorEmail;

    // private String assessmentFilter;//Where clause for sql statements
    private String mentorFilter;

    private String oldMentorTitle;// existing text of old term title
    private String oldMentorPhone;// existing text of old term start
    private String oldMentorEmail;// existing text of old term end


    private String selectedCourseId;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_editor_mentor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        editorMentorTitle = (EditText) findViewById(R.id.etMentorTitle);
        editorMentorPhone = (EditText) findViewById( R.id.etMentorPhone);
        editorMentorEmail = (EditText) findViewById(R.id.etMentorEmail);

        Uri uri = intent.getParcelableExtra( MentorProvider.CONTENT_ITEM_TYPE_MENTOR);

        // If the user wants to edit then it won't be null,
        // but if the user pressed new to insert, it will be null

        if(uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Mentor");

        }
        else {
            setTitle( "Edit Mentor" );
            action = Intent.ACTION_EDIT;
            mentorFilter = My_SQLiteOpenHelper.MENTOR_ID + "=" + uri.getLastPathSegment();
            selectedCourseId = EditorCourseActivity.selectedCourseID;
            //retrieve the one row from database
            Cursor cursor = getContentResolver().query( uri,
                    My_SQLiteOpenHelper.ALL_MENTOR_COLUMNS, mentorFilter, null, null );
            //retrieve the data
            cursor.moveToFirst();
            // selectedCourseId = intent.getStringExtra( "selectedCourseId" );
            //Title
            oldMentorTitle = cursor.getString( cursor.getColumnIndex( My_SQLiteOpenHelper.MENTOR_TITLE ) );
            editorMentorTitle.setText( oldMentorTitle );
            //Start
            oldMentorPhone = cursor.getString( cursor.getColumnIndex( My_SQLiteOpenHelper.MENTOR_PHONE ) );
            editorMentorPhone.setText( oldMentorPhone );
            //End
            oldMentorEmail = cursor.getString( cursor.getColumnIndex( My_SQLiteOpenHelper.MENTOR_EMAIL) );
            editorMentorEmail.setText( oldMentorEmail );

        }//end else

    }// end onCreate()

    //when creating options menu, put trash can icon in menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(action.equals( Intent.ACTION_EDIT )) {
            getMenuInflater().inflate( R.menu.menu_mentor_editor, menu );
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                Intent intent = new Intent(EditorMentorActivity.this, ListOfTermsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_delete:
                deleteMentor();
                break;
        }
        return true;
    }

    private void finishEditing(){

        // final String selectedCourseId = intent.getParcelableExtra( "Selected Term ID" );
        Intent intent = getIntent(); // the intent that launched this activity

        selectedCourseId = EditorCourseActivity.selectedCourseID;
        String newMentorTitle = editorMentorTitle.getText().toString().trim();
        String newMentorPhone = editorMentorPhone.getText().toString().trim();
        String newMentorEmail = editorMentorEmail.getText().toString().trim();

        //code for action insert
        switch (action) {
            case Intent.ACTION_INSERT:
                //if assessment is empty
                if (newMentorTitle.length() == 0 && newMentorPhone.length() == 0
                        && newMentorEmail.length() == 0) {
                    setResult( RESULT_CANCELED ); //CANCELLED OPERATION
                    //Toast.makeText( this, "Term Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    //if assessment is full
                    insertMentor(newMentorTitle, newMentorPhone,  newMentorEmail, selectedCourseId);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newMentorTitle.length() == 0 && newMentorPhone.length() == 0
                        && newMentorEmail.length() == 0) {
                    deleteMentor();
                    updateMentor( newMentorTitle, newMentorPhone,
                            newMentorEmail);
                } else if (oldMentorTitle.equals( newMentorTitle ) &&
                        oldMentorPhone.equals( newMentorPhone )
                        && oldMentorEmail.equals( newMentorEmail )) {
                    setResult( RESULT_CANCELED );
                } else {
                    updateMentor( newMentorTitle, newMentorPhone,
                            newMentorEmail);
                }
                break;
        }
        //finished with activity so go back to parent activity
        finish();

    }

    //  ASSESSMENT_TITLE, ASSESSMENT_TYPE, ASSOCIATED_COURSE_ID, SCHEDULED_DATE
    private void insertMentor( String title, String phone,
                                   String email, String assCourseID){

        ContentValues values = new ContentValues();
        values.put(My_SQLiteOpenHelper.MENTOR_TITLE, title);
        values.put(My_SQLiteOpenHelper.MENTOR_PHONE, phone );
        values.put(My_SQLiteOpenHelper.MENTOR_EMAIL, email );
        values.put(My_SQLiteOpenHelper.ASSIGNED_COURSE_ID, assCourseID );

        Uri assessmentUri = getContentResolver().insert(MentorProvider.MENTOR_CONTENT_URI, values);
        Log.d("ListOfMentor", "Inserted a new mentor " + assessmentUri.getLastPathSegment());
//        Intent intent = new Intent(EditorMentorActivity.this, ListOfTermsActivity.class);
//        startActivity(intent);
        setResult(RESULT_OK);
        finish();

    }

    private void updateMentor(  String newMentorTitle, String newMentorPhone,
                                    String newMentorEmail) {
        ContentValues values = new ContentValues();
        values.put(My_SQLiteOpenHelper.MENTOR_TITLE, newMentorTitle);
        values.put(My_SQLiteOpenHelper.MENTOR_PHONE, newMentorPhone);
        // values.put(My_SQLiteOpenHelper.ASSOCIATED_COURSE_ID, associatedCourseID);
        values.put(My_SQLiteOpenHelper.MENTOR_EMAIL, newMentorEmail);

        getContentResolver().update( MentorProvider.MENTOR_CONTENT_URI, values, mentorFilter, null);
        Toast.makeText(this, "Mentor Updated", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(EditorMentorActivity.this, ListOfTermsActivity.class);
//        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }


    //method to delete one row and show a message
    private void deleteMentor() {
        getContentResolver().delete( MentorProvider.MENTOR_CONTENT_URI, mentorFilter, null);
        Toast.makeText(this, "Mentor Deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorMentorActivity.this, ListOfTermsActivity.class);
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


