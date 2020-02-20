package com.iladydeveloper.unitracker.cursor_adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.iladydeveloper.unitracker.R;
//public static final String MENTOR_NAME_1 = "mentor_1";
//public static final String MENTOR_PHONE_1 = "mentor_phone_1";
//public static final String MENTOR_EMAIL_1 = "mentor_email_1";
public class MentorCursorAdapter extends CursorAdapter {


    public MentorCursorAdapter(Context context, Cursor c, boolean autoRequery){
        super( context, c );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate( R.layout.item_mentors, parent,false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView mentorTitle = (TextView) view.findViewById( R.id.mentor_title);
        TextView mentorPhone = (TextView) view.findViewById( R.id.mentor_phone);
        TextView mentorEmail = (TextView) view.findViewById( R.id.mentor_email);

        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
        String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

        mentorTitle.setText(title);
        mentorPhone.setText(phone);
        mentorEmail.setText(email);

    }
}
