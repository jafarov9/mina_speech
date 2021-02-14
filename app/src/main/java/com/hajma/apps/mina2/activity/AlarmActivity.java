package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.eventbus.DataEvent;
import com.hajma.apps.mina2.fragment.AlarmRepeatFragment;
import com.hajma.apps.mina2.fragment.FragmentAlarm;
import com.hajma.apps.mina2.fragment.FragmentSettings;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    private LottieAnimationView lottiBackAlarm;
    private SharedPreferences sharedPreferences;
    private String token;
    private FragmentManager fm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        fm = getSupportFragmentManager();

        lottiBackAlarm = findViewById(R.id.lottiBackAlarm);
        lottiBackAlarm.setOnClickListener(v -> {
            Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        if(token == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        fm.beginTransaction()
                .add(R.id.alarm_container,
                        new FragmentAlarm(), "alarmFragment")
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .addToBackStack("alarmFragment")
                .commit();


    }

    @Override
    public void onBackPressed() {
        AlarmRepeatFragment alarmRepeatFragment = (AlarmRepeatFragment) fm.findFragmentByTag("alarmRepeatFragment");

        if(alarmRepeatFragment != null) {

            ArrayList<String> temp = alarmRepeatFragment.getWeekDays();

            if(temp.size() > 0) {
                EventBus.getDefault().postSticky(new DataEvent.ChangeAlarmRepeat(1, temp));
            }

            fm.popBackStack();

        } else if(fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        }else {
            finish();
        }
    }
}
