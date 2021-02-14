package com.hajma.apps.mina2.responses.mobile_function;

import android.content.Context;
import android.content.Intent;

import com.hajma.apps.mina2.activity.MainActivity;
import com.hajma.apps.mina2.activity.ProfileActivity;
import com.hajma.apps.mina2.utils.LocaleHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class Settings {

    private static String key;

    public static String settings(Context context, JSONObject jsonObject, JSONObject result) {

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

            case "login" :
                context.startActivity(new Intent(context, ProfileActivity.class));
                break;

            case "change-language":

                String lang = LocaleHelper.getPersistedData(context, "az");

                if(lang.equals("az")) {
                    LocaleHelper.setLocale(context, "ru");
                }else {
                    LocaleHelper.setLocale(context, "az");
                }
                restartHome(context);
                break;
        }
    }

    private static void restartHome(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((MainActivity)context).finish();

    }


}
