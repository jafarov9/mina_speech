package com.hajma.apps.mina2.responses.news;

import android.content.Context;
import android.content.Intent;

import com.hajma.apps.mina2.activity.NewsAudioPlayActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class News {

    private static String key;

    public static String news(Context context, JSONObject jsonObject, JSONObject result, String myKey) {



        try {
            if(!(jsonObject.isNull("child"))) {

                key = jsonObject.getString("key");

                switch (key) {
                    case "sddsd" :
                        return "sdsdsd";

                    default: return null;
                }

            } else {

                key = jsonObject.getString("key");

                startActivityFromKey(context, result, myKey);

                return key;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private static void startActivityFromKey(Context context, JSONObject result, String myKey) {

        switch (key) {
            case "qalanews" :

                Intent intent = new Intent(context, NewsAudioPlayActivity.class);
                intent.putExtra("result", result.toString());
                intent.putExtra("myKey", myKey);
                context.startActivity(intent);

                break;
        }

    }

}
