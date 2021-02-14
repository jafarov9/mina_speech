package com.hajma.apps.mina2.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.receiver.AlarmReceiver;
import com.hajma.apps.mina2.service.AlarmService;

public class WakeUp extends AppCompatActivity {

    AudioManager audioManager;
    AlarmReceiver alarmReceiver;
    private Button stopAlarm;
    private TextView tvStopLabel;
    private ImageView ivShakeAlarm;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        alarmReceiver = new AlarmReceiver();

        stopAlarm = findViewById(R.id.stop_alarm);
        tvStopLabel = findViewById(R.id.tv_stop_label);
        ivShakeAlarm = findViewById(R.id.iv_shake_alarm);



        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        shakeImage();

        stopAlarm.setOnClickListener(v -> {
            endTask();
            //System.exit(0);
        });
        //tvStopLabel.setText(getIntent().getExtras().getString(C.SALUTATION));
    }

    void shakeImage(){
        Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);
        ivShakeAlarm.setAnimation(shake);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void endTask(){
        alarmReceiver.stopRingtone();
        Intent alarmService = new Intent(this, AlarmService.class);
        //alarmService.setAction(C.STOPFOREGROUND_ACTION);
        stopService(alarmService);
        finishAndRemoveTask();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        endTask();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        super.onPause();
        endTask();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        endTask();
    }
}
