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

public class AssessmentProvider extends ContentProvider {

    /****** Assessments *****/

    //globally unique String that ids content provider to Android Framework
    public static final String AUTHORITY = "com.iladydeveloper.unitracker.assessmentprovider";
    //represents entire dataset
    private static final String ASSESSMENT_BASE_PATH = "Assessments";
    public static final Uri ASSESSMENT_CONTENT_URI =
            Uri.parse( "content://" + AUTHORITY + "/" + ASSESSMENT_BASE_PATH );
    public static final String CONTENT_ITEM_TYPE_ASSESSMENT = "Assessment";
    //Constants to identify the requested operation. Parses the URI and tells
    //you which operation has been requested
    //Terms
    private static final int ASSESSMENT = 1; // Means "give me the data"
    private static final int ASSESSMENT_ID = 2; // Deals with only a single record


    // URI matcher executes first time anything is called form this class
    //Terms
    private static final UriMatcher uriMatcherForAssessments = new UriMatcher( UriMatcher.NO_MATCH );
    static {

        uriMatcherForAssessments.addURI( AUTHORITY, ASSESSMENT_BASE_PATH, ASSESSMENT );
        uriMatcherForAssessments.addURI( AUTHORITY, ASSESSMENT_BASE_PATH + "/#", ASSESSMENT_ID );
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
        if (uriMatcherForAssessments.match( uri ) == ASSESSMENT_ID) {
            selection = My_SQLiteOpenHelper.ASSESSMENT_ID + "=" + uri.getLastPathSegment();
        }
        return database.query( My_SQLiteOpenHelper.ASSESSMENTS_TABLE, My_SQLiteOpenHelper.ALL_ASSESSMENT_COLUMNS,
                selection, null, null, null,
                My_SQLiteOpenHelper.ASSESSMENT_ID + " DESC" );
    }

    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert( My_SQLiteOpenHelper.ASSESSMENTS_TABLE, null, values );
        return Uri.parse( ASSESSMENT_BASE_PATH + "/" + id );
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete( My_SQLiteOpenHelper.ASSESSMENTS_TABLE, selection, selectionArgs );
    }
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update( My_SQLiteOpenHelper.ASSESSMENTS_TABLE, values, selection, selectionArgs );
    }

}

