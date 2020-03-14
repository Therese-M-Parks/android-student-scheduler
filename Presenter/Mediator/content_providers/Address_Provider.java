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

/***************** Address********************/
public class Address_Provider extends ContentProvider {
    //globally unique String that ids content provider to Android Framework

    public static final String AUTHORITY = "com.thereseparks.familyintouch.address_provider";
    //represents entire data set
    private static final String ADDRESS_BASE_PATH = "Address";
    public static final Uri ADDRESS_CONTENT_URI =
            Uri.parse( "content://" + AUTHORITY + "/" + ADDRESS_BASE_PATH );
    public static final String CONTENT_ITEM_TYPE_Address = "Address";
    //Constants to identify the requested operation. Parses the URI and tells
    //you which operation has been requested
    //Address
    private static final int ADDRESS= 1; // Means "give me the data"
    private static final int ADDRESS_ID = 2; // Deals with only a single record

    // URI matcher executes first time anything is called form this class
    // Address
    private static final UriMatcher uriMatcherForAddress = new UriMatcher( UriMatcher.NO_MATCH );
    static {

        uriMatcherForAddress.addURI( AUTHORITY, ADDRESS_BASE_PATH, ADDRESS );
        uriMatcherForAddress.addURI( AUTHORITY, ADDRESS_BASE_PATH + "/#", ADDRESS_ID );
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
        if (uriMatcherForAddress.match( uri ) == ADDRESS_ID) {
            selection = My_SQLiteOpenHelper.ADDRESS_ID + "=" + uri.getLastPathSegment();
        }
        return database.query( My_SQLiteOpenHelper.ADDRESS_TABLE, My_SQLiteOpenHelper.ALL_ADDRESS_COLUMNS,
                selection, null, null, null,
                My_SQLiteOpenHelper.ADDRESS_ID + " DESC" );
    }

    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert( My_SQLiteOpenHelper.ADDRESS_TABLE, null, values );
        return Uri.parse( ADDRESS_BASE_PATH + "/" + id );
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete( My_SQLiteOpenHelper.ADDRESS_TABLE, selection, selectionArgs );
    }
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update( My_SQLiteOpenHelper.ADDRESS_TABLE, values, selection, selectionArgs );
    }

}


