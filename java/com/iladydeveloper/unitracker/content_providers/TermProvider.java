package com.iladydeveloper.unitracker.content_providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.iladydeveloper.unitracker.cursor_adapters.TermCursorAdapter;
import com.iladydeveloper.unitracker.database.My_SQLiteOpenHelper;

public class TermProvider extends ContentProvider {

    //globally unique String that ids content provider to Android Framework
    public static final String AUTHORITY = "com.iladydeveloper.unitracker.termprovider";

    /****** TERMS *****/
    //represents entire dataset
    private static final String TERM_BASE_PATH = "Terms";
    public static final Uri TERM_CONTENT_URI =
            Uri.parse( "content://" + AUTHORITY + "/" + TERM_BASE_PATH );
    public static final String CONTENT_ITEM_TYPE_TERM = "Term";
    //Constants to identify the requested operation. Parses the URI and tells
    //you which operation has been requested
    //Terms
    private static final int TERM = 1; // Means "give me the data"
    private static final int TERMS_ID = 2; // Deals with only a single record
    // URI matcher executes first time anything is called form this class
    //Terms
    private static final UriMatcher uriMatcherForTerms = new UriMatcher( UriMatcher.NO_MATCH );

    static {
        uriMatcherForTerms.addURI( AUTHORITY, TERM_BASE_PATH, TERM );
        uriMatcherForTerms.addURI( AUTHORITY, TERM_BASE_PATH + "/#", TERMS_ID );
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

    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (uriMatcherForTerms.match( uri ) == TERMS_ID) {
            selection = My_SQLiteOpenHelper.TERM_ID + "=" + uri.getLastPathSegment();
        }
        return database.query( My_SQLiteOpenHelper.TERMS_TABLE, My_SQLiteOpenHelper.ALL_TERM_COLUMNS,
                selection, null, null, null,
                My_SQLiteOpenHelper.TERM_START + " DESC" );
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert( My_SQLiteOpenHelper.TERMS_TABLE, null, values );
        return Uri.parse( TERM_BASE_PATH + "/" + id );
    }


    @Nullable
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        return database.delete( My_SQLiteOpenHelper.TERMS_TABLE, selection, selectionArgs );

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        return database.update( My_SQLiteOpenHelper.TERMS_TABLE, values, selection, selectionArgs );

    }

//    public static TermCursorAdapter cursorToTerm(Cursor cursor) {
//        TermCursorAdapter term = new TermCursorAdapter();
//        term.getCursor( cursor.getString( 0 ) );
//        term.setTermTitle( cursor.getString( 1 ) );
//        term.setStartDate( cursor.getString( 2 ) );
//        term.setEndDate( cursor.getString( 3 ) );
//        return term;
//    }
}
