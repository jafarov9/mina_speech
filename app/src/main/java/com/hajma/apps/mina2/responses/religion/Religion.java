package com.hajma.apps.mina2.responses.religion;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class Religion {

    public static String religion(Context context, JSONObject jsonObject, JSONObject result) {

        try {
            if(!(jsonObject.isNull("child"))) {

                String key = jsonObject.getString("key");

                switch (key) {
                    case "islam" :
                        return Islam.islam(context, jsonObject.getJSONObject("child"), result);

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
