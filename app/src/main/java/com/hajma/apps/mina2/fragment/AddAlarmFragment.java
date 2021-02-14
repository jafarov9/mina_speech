package com.hajma.apps.mina2.fragment;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.eventbus.DataEvent;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.retrofit.model.Alarm;
import com.hajma.apps.mina2.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAlarmFragment extends Fragment {


    private LinearLayout ll_go_ringtone;
    private TextView tv_repeat;
    private TextView tv_ringtone;
    private LinearLayout ll_go_week;
    private ArrayList<String> repeatList = new ArrayList<>();
    private Util util;
    private AlarmManager alarmManager;
    private SingleDateAndTimePicker timePicker;
    private String alarmHours;
    private ImageButton imgAddAlarm;
    private MinaInterface minaInterface;
    private String token;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_alarm, container, false);

        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        token = sharedPreferences.getString("token", null);

        minaInterface = ApiUtils.getMinaInterface();
        util = new Util();

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        ll_go_ringtone = view.findViewById(R.id.ll_go_ringtone);
        ll_go_week = view.findViewById(R.id.ll_go_week);
        timePicker = view.findViewById(R.id.timePicker);
        tv_repeat = view.findViewById(R.id.tv_repeat);
        tv_ringtone = view.findViewById(R.id.tv_ringtone);
        imgAddAlarm = view.findViewById(R.id.imgAddAlarm);

        timePicker.setIsAmPm(false);
        timePicker.setStepMinutes(1);

        Date date1 = new Date();
        alarmHours = changeFormat(date1.toString());

        timePicker.addOnDateChangedListener((displayed, date) -> {
            //Log.i("data",changeFormat(date.toString()).substring(0,2));
            //Log.i("data",date.toString());
            alarmHours = changeFormat(date.toString());
        });

        ll_go_ringtone.setOnClickListener(v -> {
            Fragment alarmMusicFragment = new AlarmMusicFragment();

            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                    .replace(R.id.alarm_container, alarmMusicFragment, "alarmMusicFragment")
                    .addToBackStack("alarmMusicFragment")
                    .commit();
        });

        ll_go_week.setOnClickListener(v -> {
            Fragment alarmRepeatFragment = new AlarmRepeatFragment(repeatList);

            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                    .replace(R.id.alarm_container, alarmRepeatFragment, "alarmRepeatFragment")
                    .addToBackStack("alarmRepeatFragment")
                    .commit();
        });

        imgAddAlarm.setOnClickListener(v -> {

            String data = util.getSharedPreference(C.ALARMMUSIC, getActivity());

            Alarm alarm = new Alarm(0, "Mina", data, "default", alarmHours, "none", true, repeatList);
            startAlarmWithServer(alarm);


        });

        setRingtoneName();

        return view;
    }

    private void startAlarmWithServer(Alarm alarm) {

        minaInterface.setOneAlarm(alarm, "Bearer "+ token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    if(repeatList.size() > 0) {
                        util.setAlarmDays(getActivity(), alarm, alarmManager);
                    }else {
                        Log.e("cvbb", "Start alarm running");
                        util.startAlarm(getActivity(), alarm, alarmManager);
                    }

                    getActivity().onBackPressed();
                } else {
                    try {
                        String s = response.errorBody().string();
                        Log.e("fff", "Error: "+s);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("fff", "Error: "+t.getMessage());
            }
        });



    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(sticky = true)
    public void OnAlarmTextChange(DataEvent.ChangeAlarmMusicText event) {
        if(event.getResponse() == 1) {
            tv_ringtone.setText(event.getTitle());
        }
    }

    @Subscribe(sticky = true)
    public void OnAlarmRepeatChange(DataEvent.ChangeAlarmRepeat event) {
        if(event.getResponse() == 1) {
            repeatList = event.getRepeatList();
            StringBuilder sb = new StringBuilder();

            for(int i = 0;i < repeatList.size();i++) {
                if(i == repeatList.size() - 1) {
                    sb.append(repeatList.get(i)).append(".");
                }else {
                    sb.append(repeatList.get(i)).append(", ");
                }
            }

            tv_repeat.setText(sb);
        }

    }


    public String changeFormat(String time) {
        String format = "EEE MMM dd HH:mm:ss zzzz yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT+4"));
        Date d = null;
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern("HH:mm");
        return sdf.format(d);
    }

    private void setRingtoneName(){
        String music = util.getSharedPreference(C.ALARMMUSIC,getContext());
        if(music != null){
            String[] data = music.split("#");
            tv_ringtone.setText(data[0]);
            Log.i("data",music);
        }else {
            tv_ringtone.setText(getString(R.string.defaults));
            //Log.i("data",music);
        }
    }



}
