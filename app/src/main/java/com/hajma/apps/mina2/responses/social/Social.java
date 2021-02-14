package com.hajma.apps.mina2.responses.social;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.hajma.apps.mina2.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Social {


    private static String key;

    public static String social(Context context, JSONObject jsonObject, JSONObject result) {

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

                playSoundFromKey(context, result);

                return key;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private static void playSoundFromKey(Context context, JSONObject result) {

        try {
            String soundUrl = result.getString("sound");
            //String url = URLDecoder.decode(soundUrl, "UTF-8");

            socialSound(soundUrl, new MediaPlayer(), context);

            Log.e("kkk", soundUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //play social sound
    public static void socialSound(String url, MediaPlayer mediaPlayer, Context context){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                if (!mp.isPlaying()) {
                    mp.start();
                } else {
                    mp.pause();
                }
                //audioReady = true;

            });

        } catch (IllegalArgumentException e) {
            Toast.makeText(context.getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(context.getApplicationContext(), "You might not set the URI ", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(context.getApplicationContext(), "You might not set the ", Toast.LENGTH_LONG).show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
