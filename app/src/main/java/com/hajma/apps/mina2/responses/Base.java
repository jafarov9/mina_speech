package com.hajma.apps.mina2.responses;

import android.content.Context;

import com.hajma.apps.mina2.responses.be_clever.BeClever;
import com.hajma.apps.mina2.responses.book.Book;
import com.hajma.apps.mina2.responses.mobile_function.MobileFunction;
import com.hajma.apps.mina2.responses.news.News;
import com.hajma.apps.mina2.responses.religion.Religion;
import com.hajma.apps.mina2.responses.social.Social;
import com.hajma.apps.mina2.responses.space.Space;

import org.json.JSONException;
import org.json.JSONObject;

public class Base {

    public static String base(Context context, JSONObject function, JSONObject result, String myKey) {

        try {
            String key = function.getString("key");
            JSONObject child;
            if(!function.isNull("child")) {
                child = function.getJSONObject("child");
            }else {
                child = function;
            }

            switch (key) {

                case "social" :
                    return Social.social(context, child, result);

                case "religion" :
                    return Religion.religion(context, child, result);

                case "mobile-function" :
                    return MobileFunction.mobileFunction(context, child, result);

                case "news":
                    return News.news(context, child, result, myKey);

                case "be-clever" :
                    return BeClever.beclever(context, child, result);

                case "book" :
                    return Book.book(context, child, result, myKey);

                case "space" :
                    return Space.space(context, child, result);

                default: return null;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

}
