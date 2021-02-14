package com.hajma.apps.mina2.responses.religion;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hajma.apps.mina2.activity.CalendarActivity;
import com.hajma.apps.mina2.activity.ClockActivity;
import com.hajma.apps.mina2.activity.CompassActivity;
import com.hajma.apps.mina2.activity.DuaListCardActivity;
import com.hajma.apps.mina2.activity.QiblaActivity;
import com.hajma.apps.mina2.activity.StopWatchActivity;
import com.hajma.apps.mina2.activity.TodayDuaActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Dua {

    private static String key;

    public static String dua(Context context, JSONObject jsonObject, JSONObject result) {

        try {
            if(!(jsonObject.isNull("child"))) {

                Log.e("vbvb", jsonObject.getString("child"));


                key = jsonObject.getString("key");

                switch (key) {
                    case "namaz" :
                        return "sds";

                    default: return null;
                }

            }else {

                key = jsonObject.getString("key");

                startActivityFromKey(context, result);

                return jsonObject.getString("key");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void startActivityFromKey(Context context, JSONObject result) {

        switch (key) {
            case "todays-dua" : case "duas" :


                String sds = "Novruz";

                Intent intent = new Intent(context, DuaListCardActivity.class);
                intent.putExtra("result", result.toString());
                context.startActivity(intent);

                break;

        }

    }

}
