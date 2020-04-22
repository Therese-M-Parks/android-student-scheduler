package com.thereseparks.familyintouch.View.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.thereseparks.familyintouch.Model.DB.My_SQLiteOpenHelper;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.Note_Provider;
import com.thereseparks.familyintouch.R;

public class EditorForAll_Notes extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;
    private String action;
    // Work on getting these first fields to work first.
    private EditText editorNoteTitle;
    private EditText editorNote;

    private String noteFilter;
    private String oldNoteTitle;
    private String oldNote;



    private String selectedFamilyMember_Id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_for_all__notes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        editorNoteTitle = (EditText) findViewById(R.id.etTitle);
        editorNote = (EditText) findViewById( R.id.etNote);


        Uri uri = intent.getParcelableExtra( Note_Provider.CONTENT_ITEM_TYPE_NOTE);

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
            selectedFamilyMember_Id = EditorFamilyMember.selectedFamilyMember_Id;
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
        if(action.equals( Intent.ACTION_INSERT )) {
            getMenuInflater().inflate(R.menu.menu_new_note, menu);
        }else {
            getMenuInflater().inflate(R.menu.menu_note_editor, menu);
        }
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                openListOfAllNotes();
            case R.id.action_save:
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
        selectedFamilyMember_Id = EditorFamilyMember.selectedFamilyMember_Id;
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
                    insertNote(newNoteTitle, newNote, selectedFamilyMember_Id);
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
    private void insertNote( String newTitle, String newNote, String famMemberID){
        ContentValues values = new ContentValues();
        values.put(My_SQLiteOpenHelper.NOTES_TITLE, newTitle);
        values.put(My_SQLiteOpenHelper.NOTES_TEXT, newNote);
        values.put(My_SQLiteOpenHelper.NOTES_FAM_MEMBER_ID, famMemberID);

        Uri noteUri = getContentResolver().insert(Note_Provider.NOTE_CONTENT_URI, values);
        Log.d("ListOfNotes", "Inserted a new note " + noteUri.getLastPathSegment());
        Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorForAll_Notes.this, ListAll_Notes.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    private void updateNote(  String newTitle, String newNote) {
        ContentValues values = new ContentValues();
        values.put(My_SQLiteOpenHelper.NOTES_TITLE, newTitle);
        values.put(My_SQLiteOpenHelper.NOTES_TEXT, newNote);
        getContentResolver().update( Note_Provider.NOTE_CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorForAll_Notes.this, ListAll_Notes.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }


    //method to delete one row and show a message
    private void deleteNote() {
        getContentResolver().delete( Note_Provider.NOTE_CONTENT_URI, noteFilter, null);
        Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent( EditorForAll_Notes.this, ListAll_Notes.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

   public void openListOfAllNotes(){
       Intent intent = new Intent( EditorForAll_Notes.this, ListAll_Notes.class);
       startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishEditing();
    }
}


