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

import com.iladydeveloper.unitracker.R;
import com.iladydeveloper.unitracker.content_providers.MentorProvider;
import com.iladydeveloper.unitracker.content_providers.NotesProvider;
import com.iladydeveloper.unitracker.content_providers.NotesProvider;
import com.iladydeveloper.unitracker.cursor_adapters.MentorCursorAdapter;
import com.iladydeveloper.unitracker.cursor_adapters.NoteCursorAdapter;
import com.iladydeveloper.unitracker.database.My_SQLiteOpenHelper;

public class ListOfNotesActivity extends AppCompatActivity 
        implements LoaderManager.LoaderCallbacks<Cursor> {

        private static final int EDITOR_REQUEST_CODE = 1001;
        private CursorAdapter cursorAdapter;
        private String selectedCourseId;
        private String noteFilter;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate( savedInstanceState );

            setContentView( R.layout.activity_list_of_notes );

            getSupportActionBar().setTitle("List Of Notes");
            // add the functionality to go back to the parent page from action bar using the up arrow
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//            Cursor cursor = getContentResolver().query(NotesProvider.NOTES_CONTENT_URI,
//                    My_SQLiteOpenHelper.ALL_NOTES_COLUMNS,
//                    null, null, null, null);

            selectedCourseId = EditorCourseActivity.selectedCourseID;
            /** Add Notes into Note view **/
            // use *courseFilter* to pull out the Course Id from the mentor's table's course id
            Cursor noteCursor = this.getContentResolver().query(NotesProvider.NOTES_CONTENT_URI,
                    My_SQLiteOpenHelper.ALL_NOTES_COLUMNS,
                    "ass_course_id " + "= " + selectedCourseId, null, null,
                    null);

            cursorAdapter = new NoteCursorAdapter(this, noteCursor, false );
            //reference to listview
            ListView list2 = (ListView)findViewById(android.R.id.list);
            list2.setAdapter(cursorAdapter);
            //on item click listener for user selecting term item from the list
            list2.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ListOfNotesActivity.this, EditorNotesActivity.class);
                    Uri uri = Uri.parse( NotesProvider.NOTES_CONTENT_URI + "/" + id); //represents PK value
                    intent.putExtra( NotesProvider.CONTENT_ITEM_TYPE_NOTES, uri); // uri to intent as extra
                    startActivityForResult(intent, EDITOR_REQUEST_CODE );
                }
            } );
            list2.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return false;
                }
            } );
        }


        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            return new CursorLoader( this, NotesProvider.NOTES_CONTENT_URI,
                    null, null, null, null );
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            cursorAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            cursorAdapter.swapCursor( null );

        }

        public void openEditorforNewNote(View view){
            Intent intent = new Intent( this, EditorNotesActivity.class );
            startActivityForResult( intent, EDITOR_REQUEST_CODE);

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
            ListView list = (ListView)findViewById(android.R.id.list);
            list.setAdapter(cursorAdapter);
            super.onResume();
            //    if (getLoaderManager() != null){
            //   restartLoader();
            //  }
        }

        //when creating options menu, put save clickable word in menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate( R.menu.menu_list_of_notes, menu );

            return true;
        }

        public boolean onOptionsItemSelected(MenuItem item){
            int id = item.getItemId();

            switch (item.getItemId()) {
                case android.R.id.home:
                    // finishEditing();
                    Intent intent = new Intent(ListOfNotesActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_add_note:
                    // deleteNote();
                    Intent intent2 = new Intent(ListOfNotesActivity.this, EditorNotesActivity.class);
                    startActivity(intent2);
                    break;
            }
            return true;
        }
//    /** method to delete all NOTEs **/
//    private void deleteAllNotes() {
//        DialogInterface.OnClickListener dialogClickListener =
//                (dialog, button) -> {
//                    if (button == DialogInterface.BUTTON_POSITIVE) {
//                        //insert data management code here
//                        getContentResolver().delete(
//                                NoteProvider.Note_CONTENT_URI, null, null
//                        );
//                        restartLoader();
//
//                        Toast.makeText(ListOfNotes_Activity.this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
//                    }
//                };
//        AlertDialog.Builder builder = new AlertDialog.Builder( this );
//        builder.setMessage("Are you sure you want to delete All Notes?");
//        builder.setPositiveButton(getString(android.R.string.yes), dialogClickListener);
//        builder.setNegativeButton(getString(android.R.string.no), dialogClickListener);
//        builder.show();
//    }

    }

