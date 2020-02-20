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

public class CourseProvider extends ContentProvider {

    //globally unique String that ids content provider to Android Framework
    public static final String AUTHORITY = "com.iladydeveloper.unitracker.courseprovider";

    /****** Courses *****/
    //represents entire dataset
    private static final String COURSE_BASE_PATH = "Courses";
    public static final Uri COURSE_CONTENT_URI =
            Uri.parse( "content://" + AUTHORITY + "/" + COURSE_BASE_PATH );
    public static final String CONTENT_ITEM_TYPE_COURSE = "Course";
    //Constants to identify the requested operation. Parses the URI and tells
    //you which operation has been requested
    //COURSES
    private static final int COURSE = 1; // Means "give me the data"
    private static final int COURSES_ID = 2; // Deals with only a single record

    // URI matcher executes first time anything is called form this class
    //Terms
    private static final UriMatcher uriMatcherForCourses = new UriMatcher( UriMatcher.NO_MATCH );

    static {
        uriMatcherForCourses.addURI( AUTHORITY, COURSE_BASE_PATH, COURSE );
        uriMatcherForCourses.addURI( AUTHORITY, COURSE_BASE_PATH + "/#", COURSES_ID );
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
        if (uriMatcherForCourses.match( uri ) == COURSES_ID) {
            selection = My_SQLiteOpenHelper.COURSE_ID + "=" + uri.getLastPathSegment();
        }
        return database.query( My_SQLiteOpenHelper.COURSES_TABLE, My_SQLiteOpenHelper.ALL_COURSE_COLUMNS,
                selection, null, null, null,
                My_SQLiteOpenHelper.COURSE_START + " DESC" );
    }

    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert( My_SQLiteOpenHelper.COURSES_TABLE, null, values );
        return Uri.parse( COURSE_BASE_PATH + "/" + id );
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete( My_SQLiteOpenHelper.COURSES_TABLE, selection, selectionArgs );
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update( My_SQLiteOpenHelper.COURSES_TABLE, values, selection, selectionArgs );
    }

}

