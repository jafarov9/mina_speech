package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.utils.LocaleHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DistanceFromActivity extends AppCompatActivity {

    private ImageButton iButtonBackDistanceFrom;
    private ImageView imgSpaceMan;
    private ImageView imgDistanceTo;
    private Animation animUp;
    private JSONObject result;
    private String resultString;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_from);

        mediaPlayer = new MediaPlayer();

        resultString = getIntent().getStringExtra("result");

        try {
            if(resultString != null) {
                result = new JSONObject(resultString);
            }else {
                result = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //init variables
        imgSpaceMan = findViewById(R.id.imgSpaceMan);
        imgDistanceTo = findViewById(R.id.imgDistanceTo);

        iButtonBackDistanceFrom = findViewById(R.id.iButtonBackDistanceFrom);
        iButtonBackDistanceFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        animUp = AnimationUtils.loadAnimation(this, R.anim.up);

        setDataFromJson();

    }

    private void setDataFromJson() {
        if(result == null) {
            return;
        }

        //Load distance image
        try {
            Picasso.get()
                    .load(result.getString("image"))
                    .resize(300, 300)
                    .into(imgDistanceTo);

            String url = result.getString("sound");
            playDistanceSound(url, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void playDistanceSound(String url, Context context) {

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                if (!mp.isPlaying()) {
                    mp.start();

                    imgSpaceMan.startAnimation(animUp);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}
