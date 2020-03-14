package com.thereseparks.familyintouch.Model.DB;
import android.content.ComponentName;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTableLockedException;

import androidx.annotation.Nullable;

/** Defines db structure and creates database in persistent storage **/
public class My_SQLiteOpenHelper extends SQLiteOpenHelper {
    //Variables for Database

    private static final String DATABASE_NAME = "familyintouch.db";
    //increment by one when deploying app and for new versions after that.
    private static final int DATABASE_VERSION = 1;

    //Tables
/***************RELATION_STATUS INFORMATION***************/
    /** Relation Status table and columns **/
    public static final String RELATION_STATUS_TABLE = "Relation_Status";
    //Columns
    public static final String RELATION_ID = "_id";//PK
    public static final String RELATION_TITLE = "title";
    // This will differentiate between relation types: Immediate, Extended, In_Law, and Other(1,2,3 or 4)
    public static final String RELATION_NUMBER = "number";

    /** publicly accessible string of all Relation Status columns **/
    public static final String[] ALL_RELATION_STATUS_COLUMNS = {
            RELATION_ID, RELATION_TITLE, RELATION_NUMBER
    };

    /** SQL to create the RELATION_STATUS table **/
    private static final String CREATE_TABLE_RELATION_STATUS =
            "CREATE TABLE " + RELATION_STATUS_TABLE + " (" +
                    RELATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RELATION_TITLE + " TEXT, " +
                    RELATION_NUMBER + " TEXT " + ")";


    /***************BIRTHDAY INFORMATION***************/
    /** BIRTHDAY table and columns **/
    public static final String BIRTHDAY_TABLE = "Birthday";
    //Columns
    public static final String BIRTHDAY_ID = "_id";//PK
    public static final String BIRTH_DAY = "birthday";

    /** publicly accessible string of all BIRTHDAY columns **/
    public static final String[] ALL_BIRTHDAY_COLUMNS = {
            BIRTHDAY_ID, BIRTH_DAY
    };

    /** SQL to create the BIRTHDAY table **/
    private static final String CREATE_BIRTHDAY_TABLE =
            "CREATE TABLE " + BIRTHDAY_TABLE + " (" +
                    BIRTHDAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BIRTH_DAY + " DATE " + ")";



    /***************ADDRESS INFORMATION***************/
    /** ADDRESS table and columns **/
    public static final String ADDRESS_TABLE = "Address";
    //Columns
    public static final String ADDRESS_ID = "_id";//PK
    public static final String ADDRESS_LINE_1 = "address_line_1";
    public static final String ADDRESS_LINE_2 = "address_line_2";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String POSTAL_CODE = "postal_code";
    public static final String COUNTRY = "country";

    /** publicly accessible string of all ADDRESS columns **/
    public static final String[] ALL_ADDRESS_COLUMNS = {
            ADDRESS_ID, ADDRESS_LINE_1, ADDRESS_LINE_2, CITY, STATE, POSTAL_CODE, COUNTRY
    };

    /** SQL to create the ADDRESS table **/
    private static final String CREATE_ADDRESS_TABLE =
            "CREATE TABLE " + ADDRESS_TABLE + " (" +
                    ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ADDRESS_LINE_1 + " TEXT, " +
                    ADDRESS_LINE_2 + " TEXT, " +
                    CITY + " TEXT, " +
                    STATE + " TEXT, " +
                    POSTAL_CODE + " TEXT, " +
                    COUNTRY + " TEXT " + ")";


    /***************FAMILY MEMBER INFORMATION ***************/
    /** Family_Member table and columns **/
    public static final String FAMILY_MEMBER_TABLE = "Family_Member";
    //Columns
    public static final String FAMILY_MEMBER_ID = "_id";//PK
    public static final String FIRST_NAME = "first";
    public static final String LAST_NAME = "last" ;
    public static final String FAM_MEMBER_RELATION_ID = "relation_id"; //FK this is the relation_Number
    public static final String PHONE_1 = "phone_1";
    public static final String PHONE_2 = "phone_2";
    public static final String EMAIL= "email";
    public static final String FAM_MEMBER_BIRTHDAY_ID = "birthday_id"; //FK
    public static final String FAM_MEMBER_ADDRESS_ID = "address_id"; //FK

    /** publicly accessible string of all FAMILY MEMBER columns **/
    public static final String[] ALL_FAMILY_MEMBER_COLUMNS = {
            FAMILY_MEMBER_ID, FIRST_NAME, LAST_NAME, FAM_MEMBER_RELATION_ID, PHONE_1, PHONE_2,
            EMAIL, FAM_MEMBER_BIRTHDAY_ID, FAM_MEMBER_ADDRESS_ID
    };
    /** SQL to create the Family_Member table **/
    private static final String CREATE_FAMILY_MEMBER_TABLE =
            "CREATE TABLE " + FAMILY_MEMBER_TABLE + " (" +
                    FAMILY_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FIRST_NAME + " TEXT, " +
                    LAST_NAME + " TEXT, " +
                    FAM_MEMBER_RELATION_ID + " INTEGER, " + //FK
                    PHONE_1 + " TEXT, " +
                    PHONE_2 + " TEXT,  " +
                    EMAIL + " TEXT, " +
                    FAM_MEMBER_BIRTHDAY_ID + " INTEGER, " + //FK
                    FAM_MEMBER_ADDRESS_ID + " TEXT,  " +    //FK
                    " FOREIGN KEY(relation_id) REFERENCES Relation_Status(_id), " +
                    " FOREIGN KEY(birthday_id) REFERENCES Birthday(_id), " +
                    " FOREIGN KEY(address_id) REFERENCES Address(_id)" + ")";

    /*************** NOTES INFORMATION ***************/
    /** NOTES table and columns **/

    public static final String NOTES_TABLE = "Notes";
    //Columns
    public static final String NOTES_ID = "_id";//PK
    public static final String NOTES_TITLE = "title";
    public static final String NOTES_TEXT = "text";
    public static final String NOTES_FAM_MEMBER_ID = "family_member_id"; //FK


    /** publicly accessible string of all NOTES columns **/
    public static final String[] ALL_NOTES_COLUMNS = {
            NOTES_ID, NOTES_TITLE, NOTES_TEXT, NOTES_FAM_MEMBER_ID
    };
    /** SQL to create the NOTES table **/
    private static final String CREATE_TABLE_NOTES =
            "CREATE TABLE " + NOTES_TABLE + " (" +
                    NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTES_TITLE + " TEXT, " +
                    NOTES_TEXT + " TEXT, " +
                    NOTES_FAM_MEMBER_ID + " INTEGER, " +
                    "FOREIGN KEY(family_member_id) REFERENCES Family_Member(_id)" + ")";




    // Constructor
    public My_SQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

   /** The system will call onCreate the first time access to one of the tables is required.
    In this method implementation you should call the execSQL method on the SQLiteDatabase
    and pass an SQL statement for creating your table(s). Here is an example.*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");// turn on foriegn keys
        db.execSQL(CREATE_TABLE_RELATION_STATUS);
        db.execSQL(CREATE_BIRTHDAY_TABLE);
        db.execSQL(CREATE_ADDRESS_TABLE);
        db.execSQL(CREATE_FAMILY_MEMBER_TABLE);
        db.execSQL(CREATE_TABLE_NOTES);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + RELATION_STATUS_TABLE);
        db.execSQL( "DROP TABLE IF EXISTS " + BIRTHDAY_TABLE );
        db.execSQL( "DROP TABLE IF EXISTS " + ADDRESS_TABLE );
        db.execSQL( "DROP TABLE IF EXISTS " + FAMILY_MEMBER_TABLE );
        db.execSQL( "DROP TABLE IF EXISTS " + NOTES_TABLE);
        onCreate(db);
    }
}
