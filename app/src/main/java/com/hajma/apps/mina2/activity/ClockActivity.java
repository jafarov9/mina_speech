package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextClock;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.utils.LocaleHelper;

public class ClockActivity extends AppCompatActivity {

    private TextClock textClock;
    private LottieAnimationView lottiBackClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        textClock = findViewById(R.id.textClock);
        lottiBackClock = findViewById(R.id.lottiBackClock);
        lottiBackClock.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }


}
