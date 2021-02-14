package com.hajma.apps.mina2.fragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.activity.MainActivity;
import com.hajma.apps.mina2.eventbus.DataEvent;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.retrofit.model.Alarm;
import com.hajma.apps.mina2.retrofit.model.ProfileApiModel;
import com.hajma.apps.mina2.utils.PicassoCache;
import com.hajma.apps.mina2.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSettings extends Fragment implements View.OnClickListener {

    private ImageView settingsProfileImage;
    private TextView txtProfilePanelName;
    private TextView txtProfilePanelDetails;
    private Button btnGeneralSettings;
    private Button btnReligionSettings;
    private Button btnTermsSettings;
    private Button btnOurAppsSettings;
    private Button btnContactUsSettings;
    private Button btnLogOutSettings;
    private MinaInterface minaInterface;
    private String token;
    private SharedPreferences sharedPreferences;
    private ProfileApiModel user;
    private SharedPreferences.Editor editor;
    private ArrayList<Alarm> myAlarmList = new ArrayList<>();
    private AlarmManager alarmManager;
    private Util util = new Util();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_settings, container, false);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        minaInterface = ApiUtils.getMinaInterface();
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", null);


        //init views
        settingsProfileImage = view.findViewById(R.id.settingsProfileImage);
        txtProfilePanelName = view.findViewById(R.id.txtProfilePanelName);
        txtProfilePanelDetails = view.findViewById(R.id.txtProfilePanelDetails);
        btnGeneralSettings = view.findViewById(R.id.btnGeneralSettings);
        btnReligionSettings = view.findViewById(R.id.btnReligionSettings);
        btnTermsSettings = view.findViewById(R.id.btnTermsSettings);
        btnOurAppsSettings = view.findViewById(R.id.btnOurAppsSettings);
        btnContactUsSettings = view.findViewById(R.id.btnContactUsSettings);
        btnLogOutSettings = view.findViewById(R.id.btnLogOutSettings);
        btnGeneralSettings.setEnabled(false);

        //set onclick listeners buttons
        btnGeneralSettings.setOnClickListener(this);
        btnReligionSettings.setOnClickListener(this);
        btnTermsSettings.setOnClickListener(this);
        btnOurAppsSettings.setOnClickListener(this);
        btnContactUsSettings.setOnClickListener(this);
        btnLogOutSettings.setOnClickListener(this);


        if(token != null) {
            loadSettingsDetails();
        }

        return view;
    }

    private void loadSettingsDetails() {

        minaInterface.postProfile("Bearer "+token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {

                    btnGeneralSettings.setEnabled(true);

                    Log.e("resss", "Response success");


                    try {
                        String s = response.body().string();
                        JSONObject success = new JSONObject(s).getJSONObject("success");

                        user = new ProfileApiModel(
                                success.getString("name"),
                                success.getString("email"),
                                success.getString("mobile"),
                                success.getString("username"),
                                success.getBoolean("verified"),
                                success.getString("profile"),
                                success.getInt("gender")
                        );

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    loadViews();
                } else {
                    Log.e("resss", "Response error : "+response.toString());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("resss", "Response fail : "+t.toString());
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void loadViews() {

        if(user != null) {

            if(!user.getProfile().equals("")) {
                PicassoCache.getPicassoInstance(getActivity())
                        .load(user.getProfile()
                                .replace("http:", "https:"))
                        .resize(80, 80)
                        .into(settingsProfileImage);
            }


            txtProfilePanelName.setText(user.getName());
            txtProfilePanelDetails.setText(user.getUsername() + ", " + user.getEmail());

        }

    }


    @Override
    public void onClick(View v) {

        Fragment f = null;

        switch (v.getId()) {
            case R.id.btnGeneralSettings :

                f = new GeneralSettingsFragment(user);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("genSettingsFragment")
                        .add(R.id.profile_container, f,"genSettingsFragment")
                        .commit();

                break;

            case R.id.btnReligionSettings :

                break;

            case R.id.btnContactUsSettings :

                break;

            case R.id.btnOurAppsSettings :

                break;

            case R.id.btnTermsSettings :

                break;

            case R.id.btnLogOutSettings :

                editor.remove("token");
                editor.remove("isLogin");
                editor.commit();

                restartHome();


                break;
        }
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

    @Subscribe
    public void OnProfileImageChange(DataEvent.ProfileImageChange event) {
        if(event.getResponse() == 1) {
            loadSettingsDetails();
        }
    }

    private void restartHome() {

        //get and cancel alarms when log out user
        getAlarmsFromUser();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void getAlarmsFromUser() {

        Log.e("ajaja", "Token: " + token);

        minaInterface.getAllAlarm("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Log.e("ajaja", "Get alarm response success");


                    try {
                        String s = response.body().string();

                        JSONArray data = new JSONObject(s)
                                .getJSONObject("success")
                                .getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject j = data.getJSONObject(i);
                            JSONArray repeat = j.getJSONArray("repeat");
                            Alarm a = new Alarm();


                            a.setId(j.getInt("id"));
                            a.setAlarm_mode(j.getString("alarm_mode"));
                            a.setLabel(j.getString("label"));
                            a.setRingtone(j.getString("ringtone"));
                            a.setSnooze_time(j.getString("snooze_time"));
                            a.setTime(j.getString("time"));
                            a.setStatus(j.getBoolean("status"));

                            List<String> repeatList = new ArrayList<>();

                            for (int k = 0; k < repeat.length(); k++) {
                                String tmp = repeat.getString(k);

                                repeatList.add(tmp);
                            }

                            a.setRepeat(repeatList);

                            if (a.isStatus()) {
                                myAlarmList.add(a);
                            }

                            cancelAllActiveAlarms();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    try {
                        String error = response.errorBody().string();
                        Log.e("ajaja", "Get alarm response error: " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ajaja", "Get alarm response fail");

            }
        });


    }

    private void cancelAllActiveAlarms() {

        if (myAlarmList.size() > 0) {

            for (int i = 0; i < myAlarmList.size(); i++) {
                Alarm a = myAlarmList.get(i);

                if (a.isStatus()) {

                    //cancelrepeating alarm
                    if (a.getRepeat().size() > 0) {
                        util.cancelRepeatingAlarm(getActivity(), a, alarmManager);
                    } else {
                        util.cancelAlarm(getActivity(), a, alarmManager);
                    }
                }
            }

        }

    }

}
