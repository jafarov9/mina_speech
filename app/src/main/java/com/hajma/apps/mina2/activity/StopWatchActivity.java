package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.utils.LocaleHelper;

public class StopWatchActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnStartStopWatch;
    private Button btnLapStopWatch;
    private ImageView imgStopWatchMile;
    private Animation roundingAnimation;
    private LottieAnimationView lottiBackStopWatch;
    private boolean isRunning;
    private Chronometer chronometer;
    private long pause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        roundingAnimation = AnimationUtils.loadAnimation(this, R.anim.roundingalone);


        //init variables
        chronometer = findViewById(R.id.chronometer);

        btnStartStopWatch = findViewById(R.id.btnStartStopWatch);
        btnLapStopWatch = findViewById(R.id.btnLapStopWatch);
        imgStopWatchMile = findViewById(R.id.imgStopWatchMile);
        lottiBackStopWatch = findViewById(R.id.lottiBackStopWatch);

        btnStartStopWatch.setOnClickListener(this);
        btnLapStopWatch.setOnClickListener(this);
        lottiBackStopWatch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartStopWatch :

                if(!isRunning) {
                    isRunning = true;
                    imgStopWatchMile.startAnimation(roundingAnimation);
                    btnStartStopWatch.setText("Finish");

                    chronometer.setFormat("%s");
                    chronometer.setBase(SystemClock.elapsedRealtime() - pause);
                    chronometer.start();

                }else {
                    isRunning = false;
                    imgStopWatchMile.clearAnimation();
                    btnStartStopWatch.setText("Start");

                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    pause = 0;
                }



                break;

            case R.id.btnLapStopWatch :

                imgStopWatchMile.postInvalidateOnAnimation();




                break;


            case R.id.lottiBackStopWatch :

                onBackPressed();

                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
