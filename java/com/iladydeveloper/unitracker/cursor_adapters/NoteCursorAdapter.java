package com.iladydeveloper.unitracker.cursor_adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.iladydeveloper.unitracker.R;

public class NoteCursorAdapter extends CursorAdapter {

    public NoteCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super( context, c );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from( context ).inflate( R.layout.item_notes, parent, false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView noteTitle = (TextView) view.findViewById( R.id.noteTitle );

        String note = cursor.getString( cursor.getColumnIndexOrThrow( "title" ) );

        noteTitle.setText( note );


    }
}