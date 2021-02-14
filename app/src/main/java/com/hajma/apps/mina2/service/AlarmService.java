package com.hajma.apps.mina2.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.fragment.WakeUp;

public class AlarmService extends Service {


    public static String FIRING_ALARM_NOTIFICATION_CHAN = "Mina";
    public static int FIRING_ALARM_NOTIFICATION_ID = 1233;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("cdcd", "Onstartcommand called");


        if (intent.getAction().equals(C.STOPFOREGROUND_ACTION)) {

            Log.e("cdcd", "Onstartcommand called with stop service");

            stopForeground(true);
            stopSelf();
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("cdcd", "Oncreate called");

        if (Build.VERSION.SDK_INT >= 26) {
            // your start service code
            Intent wake = new Intent(this, WakeUp.class);
            //wake.putExtra(C.SALUTATION,intent.getExtras().getString(C.SALUTATION));
            wake.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            final NotificationManager manager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                    manager.getNotificationChannel(FIRING_ALARM_NOTIFICATION_CHAN) == null) {
                // Create a notification channel on first use.
                NotificationChannel chan = new NotificationChannel(
                        FIRING_ALARM_NOTIFICATION_CHAN,
                        "Alarm",
                        NotificationManager.IMPORTANCE_HIGH);
                chan.setSound(null, null);  // Service manages its own sound.
                manager.createNotificationChannel(chan);
            }
            final Notification notification =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            new Notification.Builder(this, FIRING_ALARM_NOTIFICATION_CHAN) :
                            new Notification.Builder(this))
                            .setContentTitle("Mina alarm")
                            //.setContentText(labels.isEmpty() ? getString(R.string.dismiss) : labels)
                            .setSmallIcon(R.drawable.ic_action_alarm)
                            // NOTE: This takes the place of the window attribute
                            // FLAG_SHOW_WHEN_LOCKED in the activity itself for newer APIs.
                            .setFullScreenIntent(PendingIntent.getActivity(this, 0, wake, 0), true)
                            .setCategory(Notification.CATEGORY_ALARM)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                            .setOngoing(true)
                            .setLights(Color.WHITE, 1000, 1000)
                            .build();
            notification.flags |= Notification.FLAG_INSISTENT;


            startForeground(FIRING_ALARM_NOTIFICATION_ID, notification);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
