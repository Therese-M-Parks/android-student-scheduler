package com.thereseparks.familyintouch.View.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.thereseparks.familyintouch.Model.DB.My_SQLiteOpenHelper;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.FamilyCursorAdapter;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.Family_Member_Provider;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.NoteCursorAdapter;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.Note_Provider;
import com.thereseparks.familyintouch.R;

public class ListOfNotes extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;
    private String selectedFamilyMemberId;
    private String noteFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_list_of_notes );

        getSupportActionBar().setTitle("List Of Notes");
        // add the functionality to go back to the parent page from action bar using the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedFamilyMemberId = EditorFamilyMember.selectedFamilyMember_Id;
        /** Add Notes into Note view **/
        // use *courseFilter* to pull out the Course Id from the mentor's table's course id
        Cursor noteCursor = this.getContentResolver().query(Note_Provider.NOTE_CONTENT_URI,
                My_SQLiteOpenHelper.ALL_NOTES_COLUMNS,
                "family_member_id " + "= " + selectedFamilyMemberId, null, null,
                null);

        cursorAdapter = new NoteCursorAdapter(this, noteCursor, false );
        //reference to listview
        ListView list = (ListView)findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);
        //on item click listener for user selecting term item from the list
        list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListOfNotes.this, EditorNotes.class);
                Uri uri = Uri.parse( Note_Provider.NOTE_CONTENT_URI + "/" + id); //represents PK value
                intent.putExtra( Note_Provider.CONTENT_ITEM_TYPE_NOTE, uri); // uri to intent as extra
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


    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader( this, Note_Provider.NOTE_CONTENT_URI,
                null, null, null, null );
    }


    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }


    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor( null );

    }

    public void openEditorforNewNote(View view){
        Intent intent = new Intent( this, EditorNotes.class );
        startActivityForResult( intent, EDITOR_REQUEST_CODE);

    }

    private void restartLoader(){
        //android.app.LoaderManager.LoaderCallbacks<Cursor> test = (android.app.LoaderManager.LoaderCallbacks<Cursor>) this;
        getLoaderManager().restartLoader( 0, null, (android.app.LoaderManager.LoaderCallbacks<Cursor>) this );
    }

    //This method is triggered when user presses back button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDITOR_REQUEST_CODE && requestCode == RESULT_OK) {
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
                Intent intent = new Intent(ListOfNotes.this, All_Family_Members.class);
                startActivity(intent);
                break;
            case R.id.action_add_note:
                // deleteNote();
                Intent intent2 = new Intent(ListOfNotes.this, EditorNotes.class);
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

