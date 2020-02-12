package com.example.te_scheduler_c196;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.view.View;

import java.util.Calendar;

public class App extends Application {
    public static final String CHANNEL_1_ID = "course_start";
    public static final String CHANNEL_2_ID = "course_end";
    public static final String CHANNEL_3_ID = "ass_goal";


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();

    }



    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel course_start = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Course Start Date",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            course_start.setDescription("Course start date channel");

            NotificationChannel course_end = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Course End Date",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            course_end.setDescription("Course end date channel");

            NotificationChannel ass_goal = new NotificationChannel(
                    CHANNEL_3_ID,
                    "Assessment Goal Date",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            ass_goal.setDescription("Assessment goal date channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(course_start);
            manager.createNotificationChannel(course_end);
            manager.createNotificationChannel(ass_goal);

        }
    }
}
