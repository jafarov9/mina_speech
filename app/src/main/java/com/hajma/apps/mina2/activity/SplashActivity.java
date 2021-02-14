package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.utils.LocaleHelper;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String lang = LocaleHelper.getPersistedData(this, "az");
        LocaleHelper.setLocale(this, lang);

        //init variables
        sharedPreferences = getSharedPreferences("system", MODE_PRIVATE);
        isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!isFirstRun) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();


                    }else {
                        startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                        finish();
                    }
                }
            }, 2000);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
