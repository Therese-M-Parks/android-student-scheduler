package com.iladydeveloper.unitracker.cursor_adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.iladydeveloper.unitracker.R;

import org.w3c.dom.Text;

public class CourseCursorAdapter extends CursorAdapter {

    public CourseCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super( context, c );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from( context ).inflate( R.layout.item_courses, parent, false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView courseTitle = (TextView) view.findViewById( R.id.course_title );
        TextView courseStart = (TextView) view.findViewById( R.id.course_start );
       // TextView courseEnd = (TextView) view.findViewById( R.id.course_end );
        TextView courseStatus = (TextView) view.findViewById( R.id.course_status );
        //TextView associatedTerm = (TextView) view.findViewById( R.id.tv_associated_term );


        String title = cursor.getString( cursor.getColumnIndexOrThrow( "title" ) );
        String start = cursor.getString( cursor.getColumnIndexOrThrow( "start" ) );
        //String end = cursor.getString( cursor.getColumnIndexOrThrow( "end" ) );
        String status = cursor.getString( cursor.getColumnIndexOrThrow( "status" ) );
       // String term = cursor.getString( cursor.getColumnIndexOrThrow( "ass_term_id" ) );

        courseTitle.setText( title );
        courseStart.setText( start );
        //courseEnd.setText( end );
        courseStatus.setText( status );
     //   associatedTerm.setText( term );


    }
}
