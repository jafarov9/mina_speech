package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.eventbus.DataEvent;
import com.hajma.apps.mina2.retrofit.model.DuaListApiModel;
import com.hajma.apps.mina2.utils.LocaleHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class TodayDuaActivity extends AppCompatActivity {

    private TextView txtTodayDuaTitle;
    private TextView txtTodayDuaSubTitle;
    private TextView txtTodayDuaContent;
    private LottieAnimationView lottiBackTodayDua;
    private DuaListApiModel dua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_dua);

        txtTodayDuaTitle = findViewById(R.id.txtTodayDuaTitle);
        txtTodayDuaSubTitle = findViewById(R.id.txtTodayDuaSubTitle);
        txtTodayDuaContent = findViewById(R.id.txtTodayDuaContent);
        lottiBackTodayDua = findViewById(R.id.lottiBackTodayDua);


        lottiBackTodayDua.setOnClickListener((v -> {
            Intent intent = new Intent(TodayDuaActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }));
    }

    @Subscribe(sticky = true)
    public void OnDuaModelClass(DataEvent.DuaModelClass event) {
        if(event.getResponse() == 1) {
            dua = new DuaListApiModel(
                    event.getId(),
                    event.getDay(),
                    event.getTitle(),
                    event.getContent()
            );

            txtTodayDuaTitle.setText(dua.getTitle());
            txtTodayDuaSubTitle.setText(dua.getContent().substring(0, 16));
            txtTodayDuaContent.setText(dua.getContent());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
