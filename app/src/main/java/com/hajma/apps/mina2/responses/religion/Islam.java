package com.hajma.apps.mina2.responses.religion;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hajma.apps.mina2.activity.QiblaActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Islam {

    private static String key;


    public static String islam(Context context, JSONObject jsonObject, JSONObject result) {


        try {
            if(!(jsonObject.isNull("child"))) {

                key = jsonObject.getString("key");

                JSONObject child = jsonObject.getJSONObject("child");

                switch (key) {
                    case "namaz" :
                        return Namaz.namaz(context, child, result);

                    case "dua" :
                        return Dua.dua(context, child, result);

                    case "qibla" :

                        Log.e("tyty", "Burdayam");
                        context.startActivity(new Intent(context, QiblaActivity.class));

                    default: return null;
                }

            } else {
                key = jsonObject.getString("key");
                startActivityFromKey(context);
                return key;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void startActivityFromKey(Context context) {

        switch (key) {
            case "qibla" :
                context.startActivity(new Intent(context, QiblaActivity.class));
        }
    }


}
