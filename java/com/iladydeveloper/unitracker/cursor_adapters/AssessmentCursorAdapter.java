package com.iladydeveloper.unitracker.cursor_adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.iladydeveloper.unitracker.R;

public class AssessmentCursorAdapter extends CursorAdapter {

    public AssessmentCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super( context, c );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from( context ).inflate( R.layout.item_assessments, parent, false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView assessmentTitle = (TextView) view.findViewById( R.id.assessment_title );
        TextView assessmentType = (TextView) view.findViewById( R.id.assessment_type);
        TextView assessmentDate = (TextView) view.findViewById( R.id.assessment_date );

        String title = cursor.getString( cursor.getColumnIndexOrThrow( "title" ) );
        String type = cursor.getString( cursor.getColumnIndexOrThrow( "type" ) );
        String date = cursor.getString( cursor.getColumnIndexOrThrow( "scheduled_date" ) );

        assessmentTitle.setText( title );
        assessmentType.setText( type );
        assessmentDate.setText( date );

    }
}
