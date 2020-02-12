package com.example.te_scheduler_c196;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.te_scheduler_c196.Utility.MyNotification;

import java.util.ArrayList;
import java.util.List;

import static com.example.te_scheduler_c196.App.CHANNEL_1_ID;
import static com.example.te_scheduler_c196.App.CHANNEL_2_ID;

public class NotificationBroadcast extends BroadcastReceiver {
    private static final String TAG = NotificationBroadcast.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<MyNotification> nList1 = new ArrayList<>();
        ArrayList<MyNotification> nList2 = new ArrayList<>();

        Log.i("Test: ", "Intent received in broadcast");

        Bundle bundle1 = intent.getBundleExtra("BUNDLE1");
        Bundle bundle2 = intent.getBundleExtra("BUNDLE2");
        if(bundle1!=null){
            nList1 = (ArrayList<MyNotification>) bundle1.getSerializable("N_LIST_1");
            if(nList1!=null){
                Log.i(TAG, "nList1 size: " +nList1.size());
            }
        }

        if(bundle2!=null){
            nList2 = (ArrayList<MyNotification>) bundle2.getSerializable("N_LIST_2");
            if(nList2!=null){
                Log.i(TAG, "nList2 size: "+nList2.size());
            }
        }

        if(nList1!=null){
            for(MyNotification n : nList1){
                String nTitle = n.getNTitle();
                String nDescription = n.getNDescription();
                int id = n.getNId();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_course_icon)
                        .setContentTitle(nTitle)
                        .setContentText(nDescription)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id, builder.build());
            }
        }else{
            Log.i(TAG, "nList1 null");
        }

        if (nList2!=null){
            for(MyNotification n : nList2){
                String nTitle = n.getNTitle();
                String nDescription = n.getNDescription();
                int id = n.getNId();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_2_ID)
                        .setSmallIcon(R.drawable.ic_course_icon)
                        .setContentTitle(nTitle)
                        .setContentText(nDescription)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id, builder.build());
            }
        }else{
            Log.i(TAG, "nList2 null");
        }


    }

}
