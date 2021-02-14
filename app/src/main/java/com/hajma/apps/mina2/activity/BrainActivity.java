package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.BeCleverImagesAdapter;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.utils.LocaleHelper;
import com.hajma.apps.mina2.utils.PicassoCache;
import com.hajma.apps.mina2.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class BrainActivity extends AppCompatActivity {

    private ImageView imgBrainCover;
    private ImageView imgArtWork;
    private TextView txtArtworkName;
    private TextView txtArtWorkTitle;
    private SeekBar seekBarBeClever;
    private ImageButton iButtonReplay, iButtonFastRewind,
                        iButtonPlay, iButtonFastForward,
                        iButtonShuffle;
    private TextView txtBeCleverContent;


    private LottieAnimationView lottieBackBrain;
    private JSONObject result;
    private String resultString;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean isPaused;
    private RecyclerView rvBrainOtherImages;
    private ArrayList<String> imgList;
    private BeCleverImagesAdapter adapter;
    private TextView txtCurrentTime;
    private TextView txtTotalTime;
    private MediaPlayer nameMediaPlayer;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain);

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

        txtCurrentTime = findViewById(R.id.txtCurrentTime);
        txtTotalTime = findViewById(R.id.txtTotalTime);
        rvBrainOtherImages = findViewById(R.id.rvBrainOtherImages);
        txtBeCleverContent = findViewById(R.id.txtBeCleverContent);
        imgBrainCover = findViewById(R.id.imgBrainCover);
        imgArtWork = findViewById(R.id.imgArtWork);
        txtArtworkName = findViewById(R.id.txtArtworkName);
        txtArtWorkTitle = findViewById(R.id.txtArtWorkTitle);
        seekBarBeClever = findViewById(R.id.seekBarBeClever);
        iButtonReplay = findViewById(R.id.iButtonReplay);
        iButtonFastRewind = findViewById(R.id.iButtonFastRewind);
        iButtonPlay = findViewById(R.id.iButtonPlay);
        iButtonFastForward = findViewById(R.id.iButtonFastForward);
        iButtonShuffle = findViewById(R.id.iButtonShuffle);
        lottieBackBrain = findViewById(R.id.lottiBackBrain);

        lottieBackBrain.setOnClickListener(v -> onBackPressed());

        mediaPlayer = new MediaPlayer();

        iButtonPlay.setEnabled(false);

        seekBarBeClever.setMax(100);

        seekBarBeClever.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                SeekBar seekBar = (SeekBar) v;

                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                return false;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekBarBeClever.setProgress(100);
                iButtonPlay.setBackgroundResource(R.drawable.ic_play);
                //txtCurrentTime.setText("0:00");

                //mediaPlayer.reset();
            }
        });




        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBarBeClever.setSecondaryProgress(percent);
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                txtTotalTime.setText(milliSecondsToTime(mediaPlayer.getDuration()));
                iButtonPlay.setEnabled(true);
                mediaPlayer.start();
                iButtonPlay.setBackgroundResource(R.drawable.ic_pause);
                updateSeekBar();
            }
        });

        iButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mediaPlayer.isPlaying()) {
                    isPaused = true;
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    iButtonPlay.setBackgroundResource(R.drawable.ic_play);
                }else {
                    mediaPlayer.start();
                    iButtonPlay.setBackgroundResource(R.drawable.ic_pause);
                    updateSeekBar();
                }
            }
        });

        setupRecyclerView();

        setDataFromJson();

    }

    private void setupRecyclerView() {

        imgList = new ArrayList<>();
        adapter = new BeCleverImagesAdapter(this, imgList);
        rvBrainOtherImages.setLayoutManager(new LinearLayoutManager(this));
        rvBrainOtherImages.setAdapter(adapter);
        rvBrainOtherImages.setHasFixedSize(true);
    }

    private void setDataFromJson() {

        if (result == null) {
            return;
        } else {
            //load image from url
            try {
                PicassoCache.getPicassoInstance(this)
                        .load(result.getString("image")
                                .replace("http:", "https:"))
                        .resize(600, 600)
                        .into(imgBrainCover);

                PicassoCache.getPicassoInstance(this)
                        .load(result.getString("image")
                                .replace("http:", "https:"))
                        .resize(120, 120)
                        .into(imgArtWork);

                txtArtworkName.setText(result.getString("title"));
                txtArtWorkTitle.setText(result.getString("subtitle"));
                txtBeCleverContent.setText(result.getString("text"));

                JSONArray other_images = result.getJSONArray("other_images");
                for (int i = 0; i < other_images.length();i++) {
                    JSONObject j = other_images.getJSONObject(i);



                    String image = j.getString("img");

                    Log.e("fgfg", image);

                    imgList.add(image);
                }

                adapter.notifyDataSetChanged();

                String url = result.getString("sound");

                nameMediaPlayer = new MediaPlayer();

                beCleverNamePlay(url, nameMediaPlayer, this);




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void prepareMediaPlayer(String sound) {

        if(sound.equals("")) {
            return;
        }

        seekBarBeClever.setProgress(0);
        iButtonPlay.setBackgroundResource(R.drawable.ic_play);
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(sound);
            mediaPlayer.prepareAsync();

        }catch(Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private Runnable updater = () -> {
        updateSeekBar();
        long currentDuration = mediaPlayer.getCurrentPosition();

        txtCurrentTime.setText(milliSecondsToTime(currentDuration));

    };

    private String milliSecondsToTime(long milliSeconds) {
        String timeString = "";
        String secondsString;

        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) /1000);

        if(hours > 0) {
            timeString = hours + ":";
        }

        if(seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        timeString = timeString + minutes + ":" + secondsString;

        return timeString;
    }

    private void updateSeekBar() {
        if(mediaPlayer.isPlaying()) {
            seekBarBeClever.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.reset();

        nameMediaPlayer.reset();

    }

    //play be clever first sound
    public void beCleverNamePlay(String url, MediaPlayer mediaPlayer, Context context){
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

            mediaPlayer.setOnCompletionListener(mp -> {
                try {
                    prepareMediaPlayer(result.getString("text_sound"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                try {
                    prepareMediaPlayer(result.getString("text_sound"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
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
