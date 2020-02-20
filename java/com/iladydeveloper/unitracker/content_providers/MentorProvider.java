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

public class MentorProvider extends ContentProvider {
    /****** MENTOR *****/

    //globally unique String that ids content provider to Android Framework
    public static final String AUTHORITY = "com.iladydeveloper.unitracker.mentorprovider";
    //represents entire dataset
    private static final String MENTOR_BASE_PATH = "Mentors";
    public static final Uri MENTOR_CONTENT_URI =
            Uri.parse( "content://" + AUTHORITY + "/" + MENTOR_BASE_PATH );
    public static final String CONTENT_ITEM_TYPE_MENTOR = "MENTOR";
    //Constants to identify the requested operation. Parses the URI and tells
    //you which operation has been requested
    //Terms
    private static final int MENTOR = 1; // Means "give me the data"
    private static final int MENTOR_ID = 2; // Deals with only a single record


    // URI matcher executes first time anything is called form this class
    //Terms
    private static final UriMatcher uriMatcherForMENTORs = new UriMatcher( UriMatcher.NO_MATCH );
    static {

        uriMatcherForMENTORs.addURI( AUTHORITY, MENTOR_BASE_PATH, MENTOR );
        uriMatcherForMENTORs.addURI( AUTHORITY, MENTOR_BASE_PATH + "/#", MENTOR_ID );
    }

    public SQLiteDatabase database;

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public boolean onCreate() {
        My_SQLiteOpenHelper helper = new My_SQLiteOpenHelper( getContext() );
        //getWritableDatabase() creates db and makes it writable
        database = helper.getWritableDatabase();
        return true;
    }

    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (uriMatcherForMENTORs.match( uri ) == MENTOR_ID) {
            selection = My_SQLiteOpenHelper.MENTOR_ID + "=" + uri.getLastPathSegment();
        }
        return database.query( My_SQLiteOpenHelper.MENTOR_TABLE, My_SQLiteOpenHelper.ALL_MENTOR_COLUMNS,
                selection, null, null, null,
                My_SQLiteOpenHelper.MENTOR_ID + " DESC" );
    }

    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert( My_SQLiteOpenHelper.MENTOR_TABLE, null, values );
        return Uri.parse( MENTOR_BASE_PATH + "/" + id );
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete( My_SQLiteOpenHelper.MENTOR_TABLE, selection, selectionArgs );
    }
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update( My_SQLiteOpenHelper.MENTOR_TABLE, values, selection, selectionArgs );
    }

}

