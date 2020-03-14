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

/***************** Family_Member********************/
   public class Family_Member_Provider extends ContentProvider {
    //globally unique String that ids content provider to Android Framework
    public static final String AUTHORITY = "com.thereseparks.familyintouch.family_member_provider";
    //represents entire data set
    private static final String FAMILY_MEMBER_BASE_PATH = "Family_Member";
    public static final Uri FAMILY_MEMBER_CONTENT_URI =
            Uri.parse( "content://" + AUTHORITY + "/" + FAMILY_MEMBER_BASE_PATH);
    public static final String CONTENT_ITEM_TYPE_FAMILY_MEMBER = "Family_Member";
    //Constants to identify the requested operation. Parses the URI and tells
    //you which operation has been requested
    //Family_Member
    private static final int FAMILY_MEMBER= 1; // Means "give me the data"
    private static final int FAMILY_MEMBER_ID = 2; // Deals with only a single record

    // URI matcher executes first time anything is called form this class
    // Family_Member
    private static final UriMatcher uriMatcherForFamilyMember = new UriMatcher( UriMatcher.NO_MATCH );
    static {

        uriMatcherForFamilyMember.addURI( AUTHORITY, FAMILY_MEMBER_BASE_PATH, FAMILY_MEMBER );
        uriMatcherForFamilyMember.addURI( AUTHORITY, FAMILY_MEMBER_BASE_PATH + "/#", FAMILY_MEMBER_ID );
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
        if (uriMatcherForFamilyMember.match( uri ) == FAMILY_MEMBER_ID) {
            selection = My_SQLiteOpenHelper.FAMILY_MEMBER_ID + "=" + uri.getLastPathSegment();
        }
        return database.query( My_SQLiteOpenHelper.FAMILY_MEMBER_TABLE, My_SQLiteOpenHelper.ALL_FAMILY_MEMBER_COLUMNS,
                selection, null, null, null,
                My_SQLiteOpenHelper.FAMILY_MEMBER_ID + " DESC" );
    }

    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert( My_SQLiteOpenHelper.FAMILY_MEMBER_TABLE, null, values );
        return Uri.parse( FAMILY_MEMBER_BASE_PATH + "/" + id );
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete( My_SQLiteOpenHelper.FAMILY_MEMBER_TABLE, selection, selectionArgs );
    }
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update( My_SQLiteOpenHelper.FAMILY_MEMBER_TABLE, values, selection, selectionArgs );
    }

}

