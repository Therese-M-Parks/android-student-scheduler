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

/***************** Relation_Status********************/
public class Relation_Status_Provider extends ContentProvider {
    //globally unique String that ids content provider to Android Framework
  //  public static final String AUTHORITY = "com.iladydeveloper.unitracker.assessmentprovider";
    public static final String AUTHORITY = "com.thereseparks.familyintouch.relation_status_provider";
    //represents entire data set
    private static final String RELATION_STATUS_BASE_PATH = "Relation_Status";
    public static final Uri RELATION_STATUS_CONTENT_URI =
            Uri.parse( "content://" + AUTHORITY + "/" + RELATION_STATUS_BASE_PATH );
    public static final String CONTENT_ITEM_TYPE_RELATION_STATUS = "Relation_Status";
    //Constants to identify the requested operation. Parses the URI and tells
    //you which operation has been requested
    //Relation_Status
    private static final int RELATION_STATUS = 1; // Means "give me the data"
    private static final int RELATION_STATUS_ID = 2; // Deals with only a single record

    // URI matcher executes first time anything is called form this class
    //Relation_Status
    private static final UriMatcher uriMatcherForRelation_Status = new UriMatcher( UriMatcher.NO_MATCH );
    static {

        uriMatcherForRelation_Status.addURI( AUTHORITY, RELATION_STATUS_BASE_PATH, RELATION_STATUS );
        uriMatcherForRelation_Status.addURI( AUTHORITY, RELATION_STATUS_BASE_PATH + "/#", RELATION_STATUS_ID );
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
        if (uriMatcherForRelation_Status.match( uri ) == RELATION_STATUS_ID) {
            selection = My_SQLiteOpenHelper.RELATION_ID + "=" + uri.getLastPathSegment();
        }
        return database.query( My_SQLiteOpenHelper.RELATION_STATUS_TABLE, My_SQLiteOpenHelper.ALL_RELATION_STATUS_COLUMNS,
                selection, null, null, null,
                My_SQLiteOpenHelper.RELATION_ID + " DESC" );
    }

    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert( My_SQLiteOpenHelper.RELATION_STATUS_TABLE, null, values );
        return Uri.parse( RELATION_STATUS_BASE_PATH + "/" + id );
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete( My_SQLiteOpenHelper.RELATION_STATUS_TABLE, selection, selectionArgs );
    }
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update( My_SQLiteOpenHelper.RELATION_STATUS_TABLE, values, selection, selectionArgs );
    }

}
