package com.iladydeveloper.unitracker.activities;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.iladydeveloper.unitracker.R;
import com.iladydeveloper.unitracker.content_providers.AssessmentProvider;
import com.iladydeveloper.unitracker.content_providers.CourseProvider;
import com.iladydeveloper.unitracker.content_providers.TermProvider;
import com.iladydeveloper.unitracker.cursor_adapters.CourseCursorAdapter;
import com.iladydeveloper.unitracker.database.My_SQLiteOpenHelper;

public class EditorTermActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;

    private String action;

    private EditText editorTitle;
    private EditText editorStart;
    private EditText editorEnd;

    private String termFilter;//Where clause for sql statements

    private String oldTermTitle;//existing text of old term title
    private String oldTermStart;//existing text of old term start
    private String oldTermEnd;//existing text of old term end
    public static String selectedTermID;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_editor_term );

//        getSupportActionBar().setTitle( R.string.edit_term);
//        // add the functionality to go back to the parent page from action bar using the up arrow
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editorTitle = (EditText) findViewById(R.id.etTermTitle);
        editorStart = (EditText) findViewById( R.id.etStart);
        editorEnd = (EditText) findViewById(R.id.etEnd);


        Intent intent = getIntent(); // the intent that launched this activity

        Uri uri = intent.getParcelableExtra( TermProvider.CONTENT_ITEM_TYPE_TERM);
        //If the user wants to edit then it won't be null, but if the user pressed new to insert, it will be null
        if(uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Term");
        }
        else {
            setTitle("Edit Term");
            action = Intent.ACTION_EDIT;
            termFilter = My_SQLiteOpenHelper.TERM_ID + "=" + uri.getLastPathSegment();

            //retrieve the one row from database
            Cursor cursor = getContentResolver().query( uri,
                    My_SQLiteOpenHelper.ALL_TERM_COLUMNS, termFilter, null, null);
            //retrieve the data
            cursor.moveToFirst();
            //Title
            //subString index of four is where the actual integer starts after the _id =
            selectedTermID = termFilter.substring( 4 );
            oldTermTitle = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.TERM_TITLE));
            editorTitle.setText(oldTermTitle);
            //Start
            oldTermStart = cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.TERM_START));
            editorStart.setText(oldTermStart);
            //End
            oldTermEnd= cursor.getString(cursor.getColumnIndex(My_SQLiteOpenHelper.TERM_END));
            editorEnd.setText(oldTermEnd);
            // editorEnd.requestFocus();
            /** figure out a way to populate the associated courses here ***/


            // Use termFilter, which contains the value of the term selected,
            // as the selection for filtering the associated Courses -T.P

            Cursor courseCursor = this.getContentResolver().query(CourseProvider.COURSE_CONTENT_URI,
                    My_SQLiteOpenHelper.ALL_COURSE_COLUMNS,
                    "ass_term_id " + "= " + selectedTermID, null, null,
                    null);

            cursorAdapter = new CourseCursorAdapter( this, courseCursor, false );
            //reference to listview
            ListView list = (ListView) findViewById( R.id.courses_list );
            list.setAdapter( cursorAdapter );
            //on item click listener for user selecting term item from the list

            list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(EditorTermActivity.this, EditorCourseActivity.class);
                    Uri uri = Uri.parse( CourseProvider.COURSE_CONTENT_URI + "/" + id); //represents PK value
                    intent.putExtra( CourseProvider.CONTENT_ITEM_TYPE_COURSE, uri); // uri to intent as extra
                    startActivityForResult(intent, EDITOR_REQUEST_CODE );
                }
            } );
            list.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return false;
                }
            } );

        }


    }

    //when creating options menu, put trash can icon in menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(action.equals( Intent.ACTION_EDIT )){
            getMenuInflater().inflate( R.menu.menu_term_editor, menu );
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_add_course:
                finishEditing();
                Intent intent = new Intent(EditorTermActivity.this, EditorCourseActivity.class);
                startActivity(intent);
                //finishEditing();
                break;
            case R.id.action_delete:
                if(cursorAdapter.getCount() != 0){
                    Toast.makeText(this, "Cannot Delete a Term that has Courses Assigned to It",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    deleteTerm();
                }
                break;
        }
        return true;
    }



    private void finishEditing(){
        // find out user input
        String newTermTitle = editorTitle.getText().toString().trim();
        String newTermStart = editorStart.getText().toString().trim();
        String newTermEnd = editorEnd.getText().toString().trim();


        //code for action insert
        switch (action) {
            case Intent.ACTION_INSERT:
                //if note is empty
                if (newTermTitle.length() == 0 && newTermStart.length() == 0 && newTermEnd.length() == 0) {
                    setResult( RESULT_CANCELED ); //CANCELLED OPERATION
                    //Toast.makeText( this, "Term Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    //if note is full
                    insertTerm( newTermTitle, newTermStart, newTermEnd );
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTermTitle.length() == 0 && newTermStart.length() == 0 && newTermEnd.length() == 0) {
                    if(cursorAdapter.getCount() != 0){
                        Toast.makeText(this, "Cannot Delete a Term that has Courses Assigned to It",
                                Toast.LENGTH_LONG).show();}
                    else
                        deleteTerm();
                    //  updateTerm( null, null, null );
                } else if (oldTermTitle.equals( newTermTitle ) && oldTermStart.equals( newTermStart ) && oldTermEnd.equals( oldTermEnd )) {
                    setResult( RESULT_CANCELED );
                } else {
                    updateTerm(newTermTitle, newTermStart, newTermEnd);
                }
                break;
        }
        //finished with activity so go back to parent activity
        finish();

    }

    private void updateTerm(String newTermTitle, String newTermStart, String newTermEnd) {
        ContentValues values = new ContentValues();
        values.put(My_SQLiteOpenHelper.TERM_TITLE, newTermTitle);
        values.put(My_SQLiteOpenHelper.TERM_START, newTermStart);
        values.put(My_SQLiteOpenHelper.TERM_END, newTermEnd);
        getContentResolver().update( TermProvider.TERM_CONTENT_URI, values, termFilter, null);
        Toast.makeText(this, "Term Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorTermActivity.this, ListOfTermsActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
    }


    private void insertTerm(String newTermTitle, String newTermStart, String newTermEnd) {
        ContentValues values = new ContentValues();
        values.put(My_SQLiteOpenHelper.TERM_TITLE, newTermTitle);
        values.put(My_SQLiteOpenHelper.TERM_START, newTermStart);
        values.put(My_SQLiteOpenHelper.TERM_END, newTermEnd);
        getContentResolver().insert( TermProvider.TERM_CONTENT_URI, values);
        Toast.makeText(this, "Term Added", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorTermActivity.this, ListOfTermsActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
    }

    //method to delete one row and show a message
    private void deleteTerm() {
        getContentResolver().delete( TermProvider.TERM_CONTENT_URI, termFilter, null);
        Toast.makeText(this, "Term Deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditorTermActivity.this, ListOfTermsActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }



    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader( this, TermProvider.TERM_CONTENT_URI,
                null, null, null, null );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            cursorAdapter.swapCursor( cursor );
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
        if (requestCode == EDITOR_REQUEST_CODE && requestCode == RESULT_OK){
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


//    @Override
//    public void onBackPressed(){
//        Intent intent = new Intent(EditorTerm_Activity.this, MainActivity.class);
//        startActivity(intent);
//        finishEditing();
//    }

}
