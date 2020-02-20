package com.iladydeveloper.unitracker.cursor_adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.iladydeveloper.unitracker.R;

public class TermCursorAdapter extends CursorAdapter {

    public TermCursorAdapter(Context context, Cursor c, boolean autoRequery){
        super( context, c );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate( R.layout.item_terms, parent,false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView termTitle = (TextView) view.findViewById( R.id.termTitle);
        TextView termStart = (TextView) view.findViewById( R.id.termStart);
        TextView termEnd = (TextView) view.findViewById( R.id.termEnd);

        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String start = cursor.getString(cursor.getColumnIndexOrThrow("start"));
        String end = cursor.getString(cursor.getColumnIndexOrThrow("end"));

        termTitle.setText(title);
        termStart.setText(start);
        termEnd.setText(end);

    }
}

