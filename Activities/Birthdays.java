package com.thereseparks.familyintouch.View.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.thereseparks.familyintouch.Model.DB.My_SQLiteOpenHelper;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.BirthdayCursorAdapter;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.FamilyCursorAdapter;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.FamilyMemberBirthdayProvider;
import com.thereseparks.familyintouch.Presenter.Mediator.content_providers.Family_Member_Provider;
import com.thereseparks.familyintouch.R;

public class Birthdays extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthdays);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cursor cursor = getContentResolver().query(FamilyMemberBirthdayProvider.FAMILY_MEMBER_BIRTHDAY_CONTENT_URI,
                My_SQLiteOpenHelper.ALL_FAMILY_MEMBER_COLUMNS,
                null, null, null, null);


        cursorAdapter = new BirthdayCursorAdapter( this, cursor, false );

        //reference to listview
        ListView list = (ListView)findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

        //sets a listener on the list item and tells the list item what to do when it is clicked on
        list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Here pull out the selected id and do the values.put thing maybe
                // then grab it from the other screen
                Intent intent = new Intent(Birthdays.this, QuickAccess.class);
                Uri uri = Uri.parse( FamilyMemberBirthdayProvider.FAMILY_MEMBER_BIRTHDAY_CONTENT_URI + "/" + id); //represents PK value
                intent.putExtra( FamilyMemberBirthdayProvider.CONTENT_ITEM_TYPE_FAMILY_MEMBER_BIRTHDAY, uri); // uri to intent as extra
                startActivityForResult(intent, EDITOR_REQUEST_CODE );
            }
        } );
        list.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        } );
    }// end onCreate()
    //when creating options menu, put save clickable word in menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_list_of_family_members, menu );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                openMainActivity();
                break;
            case R.id.action_add_family_member:
                openEditorFamilyMembers();
                break;
        }
        return true;
    }
    public void openEditorFamilyMembers(){
        Intent intent = new Intent(Birthdays.this, EditorFamilyMember.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(Birthdays.this, MoreChoices.class);
        startActivity(intent);
    }

}
