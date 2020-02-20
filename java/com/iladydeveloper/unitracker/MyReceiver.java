package com.iladydeveloper.unitracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {

    static int notificationID;

    //SOURCE https://www.youtube.com/watch?v=tTbd1Mfi-Sk
    // called before we start any activities right when app starts
    // good place to set up notification channels

    //constants
    public static final String channelID = "test";


    @Override
    public void onReceive(Context context, Intent intent) {
//        // TODO: This method is called when the BroadcastReceiver is receiving
        createNotificationChannel(context, channelID);

        Notification n = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon( R.drawable.ic_one )
                .setContentTitle( "Course/Assessment Reminder!" )
                .setContentText( "Course or Assessment Due. Check Your Schedule.").build();
        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, n);

        //Select all Start Course Times

        // an Intent broadcast.
      //  throw new UnsupportedOperationException( "Not yet implemented" );
    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification channel test";
            String description = "notification channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }





}