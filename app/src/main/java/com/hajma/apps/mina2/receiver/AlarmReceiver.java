package com.hajma.apps.mina2.receiver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;


import androidx.annotation.RequiresApi;

import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.fragment.WakeUp;
import com.hajma.apps.mina2.service.AlarmService;


public class AlarmReceiver extends BroadcastReceiver {

    public static String FIRING_ALARM_NOTIFICATION_CHAN = "Mina";
    public static int FIRING_ALARM_NOTIFICATION_ID = 1233;

    public AlarmReceiver(){
    }

    private static Ringtone ringtone;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent.getExtras().getString(C.ALARMMUSIC) != null){
            String[] data = intent.getExtras().getString(C.ALARMMUSIC).split("#");
            //Uri uri = Uri.parse(data[1]);

            Uri defaultRingtoneUri
                    = RingtoneManager
                    .getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM);

            ringtone =RingtoneManager.getRingtone(context,defaultRingtoneUri);
        }else {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            Log.i("sata", alarmUri.toString());
            ringtone = RingtoneManager.getRingtone(context, alarmUri);
        }
        ringtone.play();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isInteractive(); // check if screen is on
        if (!isScreenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
            wl.acquire(1000); //set your time in milliseconds
        }


        Intent alarmService = new Intent(context, AlarmService.class);
        alarmService.setAction(C.STARTFOREGROUND_ACTION);
        context.startForegroundService(alarmService);

        // Loop sound/vib/blink




        //this will send a notification message
        //ComponentName comp = new ComponentName(context.getPackageName(),
                //NotificationServices.class.getName());
       //startWakefulService(context, (intent.setComponent(comp)));
        //if(isOrderedBroadcast())
       //setResultCode(Activity.RESULT_OK);
    }

    public void stopRingtone(){
        //Ringtone r = RingtoneManager.getRingtone(context,uri);
        if(ringtone != null) {
            ringtone.stop();
        }

    }



}
