package com.hajma.apps.mina2.responses.space;

import android.content.Context;
import android.content.Intent;

import com.hajma.apps.mina2.activity.BrainActivity;
import com.hajma.apps.mina2.activity.DistanceFromActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Space {

    private static String key;

    public static String space(Context context, JSONObject jsonObject, JSONObject result) {

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

                startActivityFromKey(context, result);

                return key;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private static void startActivityFromKey(Context context, JSONObject result) {

        switch (key) {
            case "space":

                Intent intent = new Intent(context, DistanceFromActivity.class);
                intent.putExtra("result", result.toString());
                context.startActivity(intent);
                break;
        }

    }


}
