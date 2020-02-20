package com.iladydeveloper.unitracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** Defines db structure and creates database in persistent storage **/
public class My_SQLiteOpenHelper extends SQLiteOpenHelper {

    //Variables for Database
    private static final String DATABASE_NAME = "unitracker.db";
    private static final int DATABASE_VERSION = 1; //increment by one when deploying app and for new version after that.
    //Variables for Terms table
    //Tables

    /***************TERM INFORMATION ***************/
    /** Terms table and columns **/
    public static final String TERMS_TABLE = "Terms";
    //Columns
    public static final String TERM_ID = "_id";//PK
    public static final String TERM_TITLE = "title";
    public static final String TERM_START = "start" ;
    public static final String TERM_END = "end";

    /** publicly accessible string of all term columns **/
    public static final String[] ALL_TERM_COLUMNS = {
            TERM_ID, TERM_TITLE, TERM_START, TERM_END
    };

    /** SQL to create the Terms table **/
    private static final String CREATE_TABLE_TERMS =
            "CREATE TABLE " + TERMS_TABLE + " (" +
                    TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_TITLE + " TEXT, " +
                    TERM_START + " DATE, " +
                    TERM_END + " DATE " + ")";


    /*************** COURSE INFORMATION ***************/

    /** Courses table and columns **/
    public static final String COURSES_TABLE = "Courses";
    //Columns
    public static final String COURSE_ID = "_id";//PK
    public static final String ASS_TERM_ID = "ass_term_id"; //FK
    public static final String COURSE_TITLE = "title";
    public static final String COURSE_START = "start" ;
    public static final String COURSE_END = "end";
    public static final String COURSE_STATUS = "status";
    public static final String OPTIONAL_NOTES = "optional_notes";


    /** publicly accessible string of all course columns **/
    public static final String[] ALL_COURSE_COLUMNS = {
            COURSE_ID, ASS_TERM_ID, COURSE_TITLE, COURSE_START, COURSE_END,
            COURSE_STATUS, OPTIONAL_NOTES
    };

    /** SQL to create the Courses table **/
    private static final String CREATE_TABLE_COURSES =
            "CREATE TABLE " + COURSES_TABLE + " (" +
                    COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASS_TERM_ID + " INTEGER, " +
                    COURSE_TITLE + " TEXT, " +
                    COURSE_START + " DATE, " +
                    COURSE_END + " DATE, " +
                    COURSE_STATUS + " TEXT, " +
                    OPTIONAL_NOTES + " TEXT, "  +
                    " FOREIGN KEY(ass_term_id) REFERENCES Terms(_id)" + ")";


    /*************** ASSESSMENT INFORMATION ***************/

    /** Assessments table and columns **/

    public static final String ASSESSMENTS_TABLE = "Assessments";
    //Columns
    public static final String ASSESSMENT_ID = "_id";//PK
    public static final String ASSESSMENT_TITLE = "title";
    public static final String ASSESSMENT_TYPE = "type";
    public static final String ASSOCIATED_COURSE_ID = "ass_course_id" ; //FK
    public static final String SCHEDULED_DATE = "scheduled_date";

    /** publicly accessible string of all ASSESSMENTS columns **/
    public static final String[] ALL_ASSESSMENT_COLUMNS = {
            ASSESSMENT_ID, ASSESSMENT_TITLE, ASSESSMENT_TYPE, ASSOCIATED_COURSE_ID, SCHEDULED_DATE
    };

    /** SQL to create the ASSESSMENTS table **/
    private static final String CREATE_TABLE_ASSESSMENTS =
            "CREATE TABLE " + ASSESSMENTS_TABLE + " (" +
                    ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESSMENT_TITLE + " TEXT, " +
                    ASSESSMENT_TYPE + " TEXT, " +
                    ASSOCIATED_COURSE_ID + " INTEGER, " +
                    SCHEDULED_DATE + " DATE, " +
                    "FOREIGN KEY(ass_course_id) REFERENCES Courses(_id)" + ")";


    /*************** MENTOR INFORMATION ***************/

    /** MENTOR table and columns **/
    public static final String MENTOR_TABLE = "Mentors";

    public static final String MENTOR_ID = "_id";
    public static final String MENTOR_TITLE = "title";
    public static final String MENTOR_PHONE = "phone";
    public static final String MENTOR_EMAIL = "email";
    public static final String ASSIGNED_COURSE_ID = "assigned_course_id";

    /** publicly accessible string of all MENTOR columns **/
    public static final String[] ALL_MENTOR_COLUMNS = {
            MENTOR_ID, MENTOR_TITLE, MENTOR_PHONE, MENTOR_EMAIL, ASSIGNED_COURSE_ID
    };
    /** SQL to create the MENTORS table **/
    private static final String CREATE_TABLE_MENTOR =
            "CREATE TABLE " + MENTOR_TABLE + " (" +
                    MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MENTOR_TITLE + " TEXT, " +
                    MENTOR_PHONE + " TEXT, " +
                    MENTOR_EMAIL + " TEXT, " +
                    ASSIGNED_COURSE_ID + " TEXT, " +
                    "FOREIGN KEY(assigned_course_id) REFERENCES Courses(_id)" + ")";


    /*************** NOTES INFORMATION ***************/
    /** NOTES table and columns **/

    public static final String NOTES_TABLE = "Notes";
    //Columns
    public static final String NOTES_ID = "_id";//PK
    public static final String NOTES_TITLE = "title";
    public static final String NOTES_TEXT = "text";
    public static final String ASS_COURSE_ID = "ass_course_id"; //FK


    /** publicly accessible string of all NOTES columns **/
    public static final String[] ALL_NOTES_COLUMNS = {
            NOTES_ID, NOTES_TITLE, NOTES_TEXT, ASS_COURSE_ID
    };
    /** SQL to create the NOTES table **/
    private static final String CREATE_TABLE_NOTES =
            "CREATE TABLE " + NOTES_TABLE + " (" +
                    NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTES_TITLE + " TEXT, " +
                    NOTES_TEXT + " TEXT, " +
                    ASS_COURSE_ID + " INTEGER, " +
                    "FOREIGN KEY(ass_course_id) REFERENCES Courses(_id)" + ")";


    public My_SQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }
    /*The system will call onCreate the first time access to one of the tables is required.
    In this method implementation you should call the execSQL method on the SQLiteDatabase
    and pass an SQL statement for creating your table(s). Here is an example.

    */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");// turn on foriegn keys
        db.execSQL(CREATE_TABLE_TERMS);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_ASSESSMENTS);
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL( CREATE_TABLE_MENTOR );

        //insert
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TERMS_TABLE);
        db.execSQL( "DROP TABLE IF EXISTS " + COURSES_TABLE );
        db.execSQL( "DROP TABLE IF EXISTS " + ASSESSMENTS_TABLE );
        db.execSQL( "DROP TABLE IF EXISTS " + NOTES_TABLE );
        db.execSQL( "DROP TABLE IF EXISTS "  + MENTOR_TABLE);
        onCreate(db);
    }



}

