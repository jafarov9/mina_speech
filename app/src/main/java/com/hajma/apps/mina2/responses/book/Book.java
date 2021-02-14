package com.hajma.apps.mina2.responses.book;

import android.content.Context;

import com.hajma.apps.mina2.responses.religion.Dua;
import com.hajma.apps.mina2.responses.religion.Namaz;

import org.json.JSONException;
import org.json.JSONObject;

public class Book {

    public static String book(Context context, JSONObject jsonObject, JSONObject result, String myKey) {


        try {
            if(!(jsonObject.isNull("child"))) {

                String key = jsonObject.getString("key");

                JSONObject child = jsonObject.getJSONObject("child");

                switch (key) {
                    case "hajmabooks" :
                        return HajmaBooks.hajmabooks(context, child, result, myKey);

                    default: return null;
                }

            } else {
                return jsonObject.getString("key");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
