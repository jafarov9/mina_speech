package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.MySoundListAdapter;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.retrofit.model.AuthorApiModel;
import com.hajma.apps.mina2.retrofit.model.CategoryApiModel;
import com.hajma.apps.mina2.retrofit.model.DetailedBookApiModel;
import com.hajma.apps.mina2.retrofit.model.SoundListApiModel;
import com.hajma.apps.mina2.utils.LocaleHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDetailedAudioBookActivity extends AppCompatActivity {

    private ImageView imgDtBookMyAudio;
    private SeekBar seekBarMyAudio;
    private MediaPlayer mediaPlayer;
    private ImageButton iButtonPlayMyAudio;
    private ImageButton iButtonFastRewindMyAudio;
    private ImageButton iButtonFastForwardMyAudio;
    private ImageButton iButtonShuffleMyAudio;
    private ImageButton iButtonReplayMyAudio;
    private RecyclerView rvMyAudioBookSounds;
    private MinaInterface minaInterface;
    private int bookId;
    private int langID = 1;
    private String token;
    private SharedPreferences sharedPreferences;
    private DetailedBookApiModel dtBook;
    private ArrayList<SoundListApiModel> soundList;
    private MySoundListAdapter adapter;
    private Handler handler = new Handler();
    private boolean isPaused;
    private LottieAnimationView lottiBackMyDetailedBook;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detailed_audio_book);


        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        minaInterface = ApiUtils.getMinaInterface();

        if(getIntent() != null) {
            bookId = getIntent().getIntExtra("bookId", 0);
        }


        //init views
        lottiBackMyDetailedBook = findViewById(R.id.lottiBackMyDetailedBook);
        imgDtBookMyAudio = findViewById(R.id.imgDtBookMyAudio);
        seekBarMyAudio = findViewById(R.id.seekBarMyAudio);
        iButtonPlayMyAudio = findViewById(R.id.iButtonPlayMyAudio);
        iButtonFastRewindMyAudio = findViewById(R.id.iButtonFastRewindMyAudio);
        iButtonFastForwardMyAudio = findViewById(R.id.iButtonFastForwardMyAudio);
        iButtonShuffleMyAudio = findViewById(R.id.iButtonShuffleMyAudio);
        iButtonReplayMyAudio = findViewById(R.id.iButtonReplayMyAudio);
        rvMyAudioBookSounds = findViewById(R.id.rvMyAudioBookSounds);

        lottiBackMyDetailedBook.setOnClickListener(v -> {
            onBackPressed();
        });

        setupRecyclerView();


        //load my book method
        if(token != null && bookId != 0) {
            loadMyAudioBook(langID, bookId);
        }


        mediaPlayer = new MediaPlayer();

        iButtonPlayMyAudio.setEnabled(false);

        seekBarMyAudio.setMax(100);

        seekBarMyAudio.setOnTouchListener(new View.OnTouchListener() {
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
                seekBarMyAudio.setProgress(0);
                iButtonPlayMyAudio.setBackgroundResource(R.drawable.ic_play);
                //mediaPlayer.reset();
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBarMyAudio.setSecondaryProgress(percent);
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                iButtonPlayMyAudio.setEnabled(true);
                startMediaPlayer();
            }
        });


        iButtonPlayMyAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mediaPlayer.isPlaying()) {
                    isPaused = true;
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    iButtonPlayMyAudio.setBackgroundResource(R.drawable.ic_play);
                }else {
                    startMediaPlayer();
                }
            }
        });

    }

    public void startMediaPlayer() {
        mediaPlayer.start();
        iButtonPlayMyAudio.setBackgroundResource(R.drawable.ic_pause);
        updateSeekBar();
    }

    private void setupRecyclerView() {
        soundList = new ArrayList<>();

        adapter = new MySoundListAdapter(soundList, this);
        rvMyAudioBookSounds.setAdapter(adapter);
        rvMyAudioBookSounds.setLayoutManager(new LinearLayoutManager(this));

    }

    private void loadMyAudioBook(int langID, int bookId) {

        String langIDBody = String.valueOf(langID);
        String bookIDBody = String.valueOf(bookId);

        minaInterface.getBookDetailed(langIDBody, bookIDBody, "Bearer "+token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()) {
                    try {
                        String s = response.body().string();

                        JSONObject data = new JSONObject(s).getJSONObject("success").getJSONObject("data");
                        dtBook = new DetailedBookApiModel();
                        dtBook.setId(data.getInt("id"));
                        dtBook.setImage(data.getString("image"));
                        dtBook.setName(data.getString("name"));
                        dtBook.setContent(data.getString("content"));
                        dtBook.setEpub(data.getString("epub"));
                        dtBook.setPage_count(data.getInt("page_count"));
                        dtBook.setHas_sound(data.getBoolean("has_sound"));
                        dtBook.setPrice(data.getString("price"));
                        dtBook.setSeller(data.getString("seller"));
                        dtBook.setYear(data.getInt("year"));
                        dtBook.setBook_language(data.getString("book_language"));




                        JSONArray sounds = data.getJSONArray("sounds");

                        if(sounds.length() > 1) {
                            for(int i = 0;i < sounds.length();i++) {

                                JSONObject j = sounds.getJSONObject(i);

                                SoundListApiModel sound = new SoundListApiModel();

                                sound.setId(j.getInt("id"));
                                sound.setTitle(j.getString("title"));
                                sound.setSound(j.getString("sound"));

                                soundList.add(sound);
                            }

                            adapter.notifyDataSetChanged();
                        }

                        loadViews();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });




    }

    private void loadViews() {

        if(dtBook != null) {
            Picasso.get()
                    .load(dtBook.getImage()
                            .replace("http:", "https:"))
                    .into(imgDtBookMyAudio);


            if(soundList.size() > 0) {

                Log.e("erer", "Url: "+soundList.get(0).getSound());


                prepareMediaPlayer(soundList.get(0).getSound());

            }

        }
    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };

    private void updateSeekBar() {
        if(mediaPlayer.isPlaying()) {
            seekBarMyAudio.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }


    public void prepareMediaPlayer(String sound) {

        seekBarMyAudio.setProgress(0);
        iButtonPlayMyAudio.setBackgroundResource(R.drawable.ic_play);
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(sound);
            mediaPlayer.prepareAsync();

        }catch(Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.reset();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
