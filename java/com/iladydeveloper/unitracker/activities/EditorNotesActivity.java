package com.iladydeveloper.unitracker.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;
import android.widget.Toolbar;

import com.iladydeveloper.unitracker.R;
import com.iladydeveloper.unitracker.content_providers.MentorProvider;
import com.iladydeveloper.unitracker.content_providers.NotesProvider;
import com.iladydeveloper.unitracker.database.My_SQLiteOpenHelper;

public class EditorNotesActivity extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;
    private String action;
    // Work on getting these first fields to work first.
    private EditText editorNoteTitle;
    private EditText editorNote;

    private String noteFilter;
    private String oldNoteTitle;
    private String oldNote;



    private String selectedCourseId;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_editor_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        editorNoteTitle = (EditText) findViewById(R.id.etTitle);
        editorNote = (EditText) findViewById( R.id.etNote);


        Uri uri = intent.getParcelableExtra( NotesProvider.CONTENT_ITEM_TYPE_NOTES);

        // If the user wants to edit then it won't be null,
        // but if the user pressed new to insert, it will be null

        if(uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Note");

        }
        else {
            setTitle( "Edit Note" );
            action = Intent.ACTION_EDIT;
            noteFilter = My_SQLiteOpenHelper.NOTES_ID + "=" + uri.getLastPathSegment();
            selectedCourseId = EditorCourseActivity.selectedCourseID;
            //retrieve the one row from database
            Cursor cursor = getContentResolver().query( uri,
                    My_SQLiteOpenHelper.ALL_NOTES_COLUMNS, noteFilter, null, null );
            //retrieve the data
            cursor.moveToFirst();
            // selectedCourseId = intent.getStringExtra( "selectedCourseId" );
            //Title
            oldNoteTitle = cursor.getString( cursor.getColumnIndex( My_SQLiteOpenHelper.NOTES_TITLE ) );
            editorNoteTitle.setText( oldNoteTitle );
            //Note
            oldNote = cursor.getString( cursor.getColumnIndex( My_SQLiteOpenHelper.NOTES_TEXT ) );
            editorNote.setText( oldNote );




        }//end else

    }// end onCreate()

    //when creating options menu, put trash can icon in menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(action.equals( Intent.ACTION_EDIT )) {
            getMenuInflater().inflate( R.menu.menu_note_editor, menu );
        }
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteNote();
                break;
            case R.id.action_share:
                //Code to Share the Note text Android Docs https://developer.android.com/training/sharing/send
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, oldNote);
                sendIntent.putExtra(Intent.EXTRA_EMAIL, oldNote );
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,
                        getResources().getText(R.string.send_to)));
                break;
        }
        return true;
    }


    private void finishEditing(){

        // final String selectedCourseId = intent.getParcelableExtra( "Selected Term ID" );
        Intent intent = getIntent(); // the intent that launched this activity

        selectedCourseId = EditorCourseActivity.selectedCourseID;
        String newNoteTitle = editorNoteTitle.getText().toString().trim();
        String newNote = editorNote.getText().toString().trim();


        //code for action insert
        switch (action) {
            case Intent.ACTION_INSERT:
                //if note is empty
                if (newNoteTitle.length() == 0 && newNote.length() == 0) {
                    setResult( RESULT_CANCELED ); //CANCELLED OPERATION
                    //Toast.makeText( this, "Term Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    //if note is full
                    insertNote(newNoteTitle, newNote, selectedCourseId);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newNoteTitle.length() == 0 && newNote.length() == 0) {
                    deleteNote();
                    updateNote( newNoteTitle, newNote);
                } else if (oldNoteTitle.equals( newNoteTitle ) &&
                        oldNote.equals( newNote )) {
                    setResult( RESULT_CANCELED );
                } else {
                    updateNote( newNoteTitle, newNote);
                }
                break;
        }
        //finished with activity so go back to parent activity
        finish();

    }

    //  ASSESSMENT_TITLE, ASSESSMENT_TYPE, ASSOCIATED_COURSE_ID, SCHEDULED_DATE
    private void insertNote( String newTitle, String newNote, String courseID){
        ContentValues values = new ContentValues();
        values.put(My_SQLiteOpenHelper.NOTES_TITLE, newTitle);
        values.put(My_SQLiteOpenHelper.NOTES_TEXT, newNote);
        values.put(My_SQLiteOpenHelper.ASS_COURSE_ID, courseID);

        Uri noteUri = getContentResolver().insert(NotesProvider.NOTES_CONTENT_URI, values);
        Log.d("ListOfNotes", "Inserted a new note " + noteUri.getLastPathSegment());
        Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorNotesActivity.this, ListOfNotesActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
    }

    private void updateNote(  String newTitle, String newNote) {
        ContentValues values = new ContentValues();
        values.put(My_SQLiteOpenHelper.NOTES_TITLE, newTitle);
        values.put(My_SQLiteOpenHelper.NOTES_TEXT, newNote);

        getContentResolver().update( NotesProvider.NOTES_CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorNotesActivity.this, ListOfNotesActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        setResult(RESULT_OK);
    }


    //method to delete one row and show a message
    private void deleteNote() {
        getContentResolver().delete( NotesProvider.NOTES_CONTENT_URI, noteFilter, null);
        Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent( EditorNotesActivity.this, ListOfNotesActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishEditing();
    }
}



