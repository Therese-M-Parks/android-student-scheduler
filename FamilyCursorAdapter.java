package com.thereseparks.familyintouch.Presenter.Mediator.content_providers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.thereseparks.familyintouch.Model.DB.My_SQLiteOpenHelper;
import com.thereseparks.familyintouch.R;

public class FamilyCursorAdapter extends CursorAdapter {
    Bitmap bitmap;

    public FamilyCursorAdapter(Context context, Cursor c, boolean autoRequery){
        super( context, c );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate( R.layout.item_all_family_members, parent,false );
     //  return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView placeholderImage = (ImageView) view.findViewById(R.id.button_image_icon);
        TextView firstName = (TextView) view.findViewById( R.id.family_member_firstName);
        TextView familyRelation = (TextView) view.findViewById( R.id.family_member_relation);
        TextView primaryPhone = (TextView) view.findViewById( R.id.primary_phone);

        byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow("key_family_image"));
      //  myImage = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        String first = cursor.getString(cursor.getColumnIndexOrThrow("first"));
        String relation = cursor.getString(cursor.getColumnIndexOrThrow("relation_title"));
        String phone_1 = cursor.getString(cursor.getColumnIndexOrThrow("phone_1"));

        //  imageView.setImageBitmap(myImage);
        placeholderImage.setImageBitmap(bitmap);
        firstName.setText(first);
        familyRelation.setText(relation);
        primaryPhone.setText(phone_1);

        byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(My_SQLiteOpenHelper.KEY_FAMILY_IMAGE));


    }
}
