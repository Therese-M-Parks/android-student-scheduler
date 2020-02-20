package com.iladydeveloper.unitracker.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;

import com.iladydeveloper.unitracker.R;
import com.iladydeveloper.unitracker.database.My_SQLiteOpenHelper;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private CursorAdapter cursorAdapter;
    // declare variables for home screen
    private Button button_terms;
    private Button button_courses;
    private Button button_mentors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        My_SQLiteOpenHelper helper = new My_SQLiteOpenHelper(this);
        //getWritableDatabase() creates db and makes it writable
        SQLiteDatabase database = helper.getWritableDatabase();

        /**--------------- code for buttons on home screen -----------------**/

        // Create an explicit intent for an Activity in your app
        // we need alerts for start and end of each course as well as for exams.

        //code for list of terms
        button_terms = (Button) findViewById(R.id.button_terms);
        button_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openListOfTerms();
            }
        });

        //code for list of courses
        button_courses = (Button) findViewById(R.id.button_courses);
        button_courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openListOfCourses();
            }
        });
        //code for list of wgu mentors
        button_mentors = (Button) findViewById(R.id.button_mentors);
        button_mentors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openListOfMentors();
            }
        });


    }// end onCreate()

    /**--------------- methods for buttons on home screen ----------------**/

    // Click on the Terms button to go the list of terms)
    public void openListOfTerms(){
        Intent intent = new Intent(MainActivity.this, ListOfTermsActivity.class);
        startActivity(intent);
    }

    // Click on the Terms button to go to the list of courses
    public void openListOfCourses(){
        Intent intent = new Intent(MainActivity.this, ListOfCoursesActivity.class);
        startActivity(intent);
    }

    // Click on the Terms button to go to the list of courses
    public void openListOfMentors(){
        Intent intent = new Intent(MainActivity.this, ListOfMentors.class);
        startActivity(intent);
    }




}

