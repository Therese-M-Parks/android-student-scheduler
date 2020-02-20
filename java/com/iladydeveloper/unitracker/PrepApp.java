package com.iladydeveloper.unitracker;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class PrepApp extends Application {
    // SOURCE https://www.youtube.com/watch?v=tTbd1Mfi-Sk
    // called before we start any activities right when app starts
    // good place to set up notification channels

    //constants
    public static final String CHANNEL_1_ID = "course start";
    public static final String CHANNEL_2_ID = "course end";
    public static final String CHANNEL_3_ID = "assessment reminder";



    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        //check Oreo or Higner
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //first channel for start of course
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Start Of Course",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Notifications for Course Start Dates" );
            //first channel for end of course
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "End Of Course",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("Notifications for Course End Dates" );
            //first channel for assessment Dates
            NotificationChannel channel3 = new NotificationChannel(
                    CHANNEL_3_ID,
                    "Scheduled Exam Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel3.setDescription("Notifications Scheduled Exam Dates" );

            NotificationManager manager  = getSystemService( NotificationManager.class );
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
        }

    }
}














