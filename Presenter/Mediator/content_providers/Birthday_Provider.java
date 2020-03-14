package com.thereseparks.familyintouch.Presenter.Mediator.content_providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.thereseparks.familyintouch.Model.DB.My_SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/***************** Birthday********************/
public class Birthday_Provider extends ContentProvider {
    //globally unique String that ids content provider to Android Framework
    //  public static final String AUTHORITY = "com.iladydeveloper.unitracker.assessmentprovider";
    public static final String AUTHORITY = "com.thereseparks.familyintouch.birthday_provider";
    //represents entire data set
    private static final String BIRTHDAY_BASE_PATH = "Birthday";
    public static final Uri BIRTHDAY_CONTENT_URI =
            Uri.parse( "content://" + AUTHORITY + "/" + BIRTHDAY_BASE_PATH );
    public static final String CONTENT_ITEM_TYPE_BIRTHDAY = "Birthday";
    //Constants to identify the requested operation. Parses the URI and tells
    //you which operation has been requested
    //Birthday
    private static final int BIRTHDAY= 1; // Means "give me the data"
    private static final int BIRTHDAY_ID = 2; // Deals with only a single record

    // URI matcher executes first time anything is called form this class
    // Birthday
    private static final UriMatcher uriMatcherForBirthday = new UriMatcher( UriMatcher.NO_MATCH );
    static {

        uriMatcherForBirthday.addURI( AUTHORITY, BIRTHDAY_BASE_PATH, BIRTHDAY );
        uriMatcherForBirthday.addURI( AUTHORITY, BIRTHDAY_BASE_PATH + "/#", BIRTHDAY_ID );
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
        if (uriMatcherForBirthday.match( uri ) == BIRTHDAY_ID) {
            selection = My_SQLiteOpenHelper.BIRTHDAY_ID + "=" + uri.getLastPathSegment();
        }
        return database.query( My_SQLiteOpenHelper.BIRTHDAY_TABLE, My_SQLiteOpenHelper.ALL_BIRTHDAY_COLUMNS,
                selection, null, null, null,
                My_SQLiteOpenHelper.BIRTHDAY_ID + " DESC" );
    }

    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert( My_SQLiteOpenHelper.BIRTHDAY_TABLE, null, values );
        return Uri.parse( BIRTHDAY_BASE_PATH + "/" + id );
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete( My_SQLiteOpenHelper.BIRTHDAY_TABLE, selection, selectionArgs );
    }
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update( My_SQLiteOpenHelper.BIRTHDAY_TABLE, values, selection, selectionArgs );
    }

}

