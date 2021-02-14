package com.hajma.apps.mina2.responses.book;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hajma.apps.mina2.activity.BooksActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class HajmaBooks {

    private static String key;

    public static String hajmabooks(Context context, JSONObject jsonObject, JSONObject result, String myKey) {

        try {



            if(!(jsonObject.isNull("child"))) {

                Log.e("vbvb", jsonObject.getString("child"));


                key = jsonObject.getString("key");

                switch (key) {
                    case "hajmabooks" :
                        return "sds";

                    default: return null;
                }

            }else {

                key = jsonObject.getString("key");

                startActivityFromKey(context, result, myKey);

                return jsonObject.getString("key");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void startActivityFromKey(Context context, JSONObject result, String myKey) {

        Intent intent = new Intent(context, BooksActivity.class);
        intent.putExtra("result", result.toString());
        intent.putExtra("key", myKey);
        context.startActivity(intent);

    }

}
