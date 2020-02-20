package com.iladydeveloper.unitracker.activities;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.iladydeveloper.unitracker.R;
import com.iladydeveloper.unitracker.content_providers.CourseProvider;
import com.iladydeveloper.unitracker.cursor_adapters.CourseCursorAdapter;
import com.iladydeveloper.unitracker.database.My_SQLiteOpenHelper;

public class ListOfCoursesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list_of_courses );

        getSupportActionBar().setTitle( "List Of Courses" );
        // add the functionality to go back to the parent page from action bar using the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );


        Cursor cursor = getContentResolver().query( CourseProvider.COURSE_CONTENT_URI,
                My_SQLiteOpenHelper.ALL_COURSE_COLUMNS,
                null, null, null, null );

        cursorAdapter = new CourseCursorAdapter( this, cursor, false );

        //reference to listview
        ListView list = (ListView) findViewById( android.R.id.list );
        list.setAdapter( cursorAdapter );
        // on item click listener for user selecting term item from the list
        list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toastMsg( "To view Course go to it's Term" );

            }
        } );
        list.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        } );


    }

    public void toastMsg(String msg) {

        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();

    }
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader( this, CourseProvider.COURSE_CONTENT_URI,
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

    public void openEditorforNewTerm(View view) {
        Intent intent = new Intent( this, EditorCourseActivity.class );
        startActivityForResult( intent, EDITOR_REQUEST_CODE );

    }

    private void restartLoader() {
        //android.app.LoaderManager.LoaderCallbacks<Cursor> test = (android.app.LoaderManager.LoaderCallbacks<Cursor>) this;
        getLoaderManager().restartLoader( 0, null, (android.app.LoaderManager.LoaderCallbacks<Cursor>) this );
    }

    //This method is triggered when user presses back button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && requestCode == RESULT_OK) {
            restartLoader();
        }
    }

    @Override
    protected void onResume() {
        ListView list = (ListView) findViewById( android.R.id.list );
        list.setAdapter( cursorAdapter );
        super.onResume();
        //    if (getLoaderManager() != null){
        //   restartLoader();
        //  }
    }

    //when creating options menu, put save clickable word in menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_list_of_courses, menu );

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                // finishEditing();
                Intent intent = new Intent( ListOfCoursesActivity.this, MainActivity.class );
                startActivity( intent );
                break;
            case R.id.action_go_to_terms:
                // deleteTerm();
                Intent intent2 = new Intent( ListOfCoursesActivity.this, ListOfTermsActivity.class );
                startActivity( intent2 );
                break;
        }
        return true;
    }
//    /** method to delete all terms **/
//    private void deleteAllTerms() {
//        DialogInterface.OnClickListener dialogClickListener =
//                (dialog, button) -> {
//                    if (button == DialogInterface.BUTTON_POSITIVE) {
//                        //insert data management code here
//                        getContentResolver().delete(
//                                TermProvider.TERM_CONTENT_URI, null, null
//                        );
//                        restartLoader();
//
//                        Toast.makeText(ListOfTerms_Activity.this, "All Terms Deleted", Toast.LENGTH_SHORT).show();
//                    }
//                };
//        AlertDialog.Builder builder = new AlertDialog.Builder( this );
//        builder.setMessage("Are you sure you want to delete All Terms?");
//        builder.setPositiveButton(getString(android.R.string.yes), dialogClickListener);
//        builder.setNegativeButton(getString(android.R.string.no), dialogClickListener);
//        builder.show();
//    }

}

