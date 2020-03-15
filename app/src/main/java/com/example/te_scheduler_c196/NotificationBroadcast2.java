package com.example.te_scheduler_c196;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.te_scheduler_c196.Utility.MyNotification;

import java.util.ArrayList;

import static com.example.te_scheduler_c196.App.CHANNEL_3_ID;

public class NotificationBroadcast2 extends BroadcastReceiver {
    private static final String TAG = NotificationBroadcast.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Test: ", "Intent received in broadcast2");
        ArrayList<MyNotification> nList3 = new ArrayList<>();
        Bundle bundle3 = intent.getBundleExtra("BUNDLE3");

        if(bundle3!=null){
            nList3 = (ArrayList<MyNotification>) bundle3.getSerializable("N_LIST_3");
            if(nList3!=null){
                Log.i(TAG, "nList3 size: "+nList3.size());
            }
        }else{Log.i(TAG, "Bundle3 Null");}

        if (nList3!=null){
            for(MyNotification n : nList3){
                String nTitle = n.getNTitle();
                String nDescription = n.getNDescription();
                int id = n.getNId();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_3_ID)
                        .setSmallIcon(R.drawable.ic_assessment_icon)
                        .setContentTitle(nTitle)
                        .setContentText(nDescription)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id, builder.build());
            }
        }else{
            Log.i(TAG, "nList3 null");
        }
    }
}
