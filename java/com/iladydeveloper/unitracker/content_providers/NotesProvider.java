package com.iladydeveloper.unitracker.content_providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.iladydeveloper.unitracker.database.My_SQLiteOpenHelper;

public class NotesProvider extends ContentProvider {
    /****** Notes *****/

    //globally unique String that ids content provider to Android Framework
    public static final String AUTHORITY = "com.iladydeveloper.unitracker.notesprovider";

    //represents entire dataset
    private static final String NOTES_BASE_PATH = "Notes";
    public static final Uri NOTES_CONTENT_URI =
            Uri.parse( "content://" + AUTHORITY + "/" + NOTES_BASE_PATH );
    public static final String CONTENT_ITEM_TYPE_NOTES = "Note";
    //Constants to identify the requested operation. Parses the URI and tells
    //you which operation has been requested
    //Terms
    private static final int NOTE = 1; // Means "give me the data"
    private static final int NOTES_ID = 2; // Deals with only a single record


    // URI matcher executes first time anything is called form this class
    //Terms
    private static final UriMatcher uriMatcherForNotes = new UriMatcher( UriMatcher.NO_MATCH );

    static {
        uriMatcherForNotes.addURI( AUTHORITY, NOTES_BASE_PATH, NOTE );
        uriMatcherForNotes.addURI( AUTHORITY, NOTES_BASE_PATH + "/#", NOTES_ID );
    }

    public SQLiteDatabase database;


    @Override
    public boolean onCreate() {
        My_SQLiteOpenHelper helper = new My_SQLiteOpenHelper( getContext() );
        //getWritableDatabase() creates db and makes it writable
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (uriMatcherForNotes.match( uri ) == NOTES_ID) {
            selection = My_SQLiteOpenHelper.NOTES_ID + "=" + uri.getLastPathSegment();
        }
        return database.query( My_SQLiteOpenHelper.NOTES_TABLE, My_SQLiteOpenHelper.ALL_NOTES_COLUMNS,
                selection, null, null, null,
                My_SQLiteOpenHelper.NOTES_ID + " DESC" );
    }

    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert( My_SQLiteOpenHelper.NOTES_TABLE, null, values );
        return Uri.parse( NOTES_BASE_PATH + "/" + id );
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete( My_SQLiteOpenHelper.NOTES_TABLE, selection, selectionArgs );
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {

        return database.update( My_SQLiteOpenHelper.NOTES_TABLE, values, selection, selectionArgs );
    }
}

