package com.hajma.apps.mina2.responses.religion;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.activity.NamazTimeDay;
import com.hajma.apps.mina2.activity.NamazTimeMonthly;

import org.json.JSONException;
import org.json.JSONObject;

public class Namaz {

    private static String key;

    public static String namaz(Context context, JSONObject jsonObject, JSONObject result) {

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
            case "namaz-time" :

                Intent intent = new Intent(context, NamazTimeDay.class);
                intent.putExtra("result", result.toString());
                context.startActivity(intent);
                break;

            case "weekly-namaz-time":
                Intent weeklyNamazIntent = new Intent(context, NamazTimeMonthly.class);
                weeklyNamazIntent.putExtra("type", C.NAMAZ_TYPE_WEEKLY);
                weeklyNamazIntent.putExtra("result", result.toString());
                context.startActivity(weeklyNamazIntent);
                break;

            case "monthly-namaz-time":
                Intent monthlyNamazIntent = new Intent(context, NamazTimeMonthly.class);
                monthlyNamazIntent.putExtra("type", C.NAMAZ_TYPE_MONTHLY);
                monthlyNamazIntent.putExtra("result", result.toString());
                context.startActivity(monthlyNamazIntent);
                break;

        }

    }

}
