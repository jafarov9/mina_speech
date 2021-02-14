package com.hajma.apps.mina2.responses.mobile_function;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hajma.apps.mina2.activity.AlarmActivity;
import com.hajma.apps.mina2.activity.BrowserActivity;
import com.hajma.apps.mina2.activity.CalendarActivity;
import com.hajma.apps.mina2.activity.ClockActivity;
import com.hajma.apps.mina2.activity.CompassActivity;
import com.hajma.apps.mina2.activity.QiblaActivity;
import com.hajma.apps.mina2.activity.StopWatchActivity;
import com.hajma.apps.mina2.activity.TutorialActivity;
import com.hajma.apps.mina2.activity.WeatherActivity;
import com.hajma.apps.mina2.ar.ARActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class MobileFunction {

    private static String key;

    public static String mobileFunction(Context context, JSONObject jsonObject, JSONObject result) {

        try {
            if(!(jsonObject.isNull("child"))) {

                key = jsonObject.getString("key");
                JSONObject child = jsonObject.getJSONObject("child");

                switch (key) {
                    case "settings" :
                        return Settings.settings(context, child, result);

                    default: return null;
                }

            } else {

                key = jsonObject.getString("key");

                startActivityFromKey(context, result);

                return key;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private static void startActivityFromKey(Context context, JSONObject result) {

        if(key != null) {
            Log.e("vbvb", key);
        }

        switch (key) {
            case "calendar" :

                context.startActivity(new Intent(context, CalendarActivity.class));

                break;

            case "compas" :

                context.startActivity(new Intent(context, CompassActivity.class));

                break;

            case "timer" :

                context.startActivity(new Intent(context, StopWatchActivity.class));

                break;

            case "clock" :

                context.startActivity(new Intent(context, ClockActivity.class));

                break;


            case "weather" :

                Intent intent =  new Intent(context, WeatherActivity.class);
                intent.putExtra("response", result.toString());
                context.startActivity(intent);
                break;

            case "browser" :

                Intent browser = new Intent(context, BrowserActivity.class);
                browser.putExtra("response", result.toString());
                context.startActivity(browser);
                break;


            case "alarm" :
                context.startActivity(new Intent(context, AlarmActivity.class));
                break;


            case "near-objects" :

                Intent nearIntent = new Intent(context, ARActivity.class);
                nearIntent.putExtra("response", result.toString());
                context.startActivity(nearIntent);
                break;

            case "tutorial" :

                Intent tutorialIntent = new Intent(context, TutorialActivity.class);
                tutorialIntent.putExtra("response", result.toString());
                context.startActivity(tutorialIntent);
                break;

        }

    }


}
