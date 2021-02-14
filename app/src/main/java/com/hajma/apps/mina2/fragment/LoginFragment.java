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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.activity.MainActivity;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.retrofit.model.Alarm;
import com.hajma.apps.mina2.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private TextView txtCreateNewAccount;
    private TextView txtForgotPassword;
    private EditText etLoginUsername;
    private EditText etLoginPassword;
    private Button btnLogin;
    private ProgressBar progressBarLoginLoading;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MinaInterface minaInterface;
    private String token;
    private String phone;
    private boolean verified;
    private Util util = new Util();
    private ArrayList<Alarm> myAlarmList = new ArrayList<>();
    private AlarmManager alarmManager;

    @SuppressLint("CommitPrefEdits")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =
                inflater.inflate(R.layout.fragment_login, container, false);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        minaInterface = ApiUtils.getMinaInterface();


        //init views
        txtForgotPassword = view.findViewById(R.id.txtForgotPassword);
        etLoginPassword = view.findViewById(R.id.etLoginPassword);
        etLoginUsername = view.findViewById(R.id.etLoginUsername);
        btnLogin = view.findViewById(R.id.btnLogin);
        progressBarLoginLoading = view.findViewById(R.id.progressBarLoginLoading);

        btnLogin.setOnClickListener(v -> {
            if (editTextControl()) {
                postLogin();
            }
        });

        txtCreateNewAccount = view.findViewById(R.id.txtCreateNewAccount);
        txtCreateNewAccount.setOnClickListener(v -> {

            Fragment registerFragment = new RegisterFragment();

            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                    .replace(R.id.sign_fragment_container, registerFragment, "registerFragment")
                    .addToBackStack("registerFragment")
                    .commit();

        });

        txtForgotPassword.setOnClickListener(v -> {
            Fragment resetPasswordMethodFragment = new ResetPasswordMethodFragment();
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                    .replace(R.id.sign_fragment_container, resetPasswordMethodFragment, "resetPasswordMethodFragment")
                    .addToBackStack("resetPasswordMethodFragment")
                    .commit();
        });

        return view;
    }

    private void postLogin() {


        btnLogin.setVisibility(View.INVISIBLE);
        progressBarLoginLoading.setVisibility(View.VISIBLE);
        progressBarLoginLoading.setIndeterminate(true);

        String email = etLoginUsername.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);

        minaInterface.postLogin(emailBody, passwordBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBarLoginLoading.setVisibility(View.GONE);
                progressBarLoginLoading.setIndeterminate(false);
                btnLogin.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {

                    String s = null;
                    try {
                        s = response.body().string();
                        JSONObject success =
                                new JSONObject(s)
                                        .getJSONObject("success");

                        JSONObject user = success.getJSONObject("user");

                        token = success.getString("token");
                        phone = user.getString("mobile");
                        verified = user.getBoolean("verified");

                        if (verified) {

                            editor.putString("token", token);
                            editor.putBoolean("isLogin", true);
                            editor.commit();

                            restartHome();
                        } else {

                            VerifyFragment verifyFragment = new VerifyFragment(phone, token, 1);

                            getActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.sign_fragment_container, verifyFragment, "verifyFragment")
                                    .commit();
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    String err = getResources().getString(R.string._you_are_not_registered);
                    util.openDialog(C.ALERT_TYPE_LOGIN_ERROR, err, getActivity().getSupportFragmentManager());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBarLoginLoading.setVisibility(View.GONE);
                progressBarLoginLoading.setIndeterminate(false);
                btnLogin.setVisibility(View.VISIBLE);

                String err = getActivity().getResources().getString(R.string.check_your_internet_connection);
                util.openDialog(C.ALERT_TYPE_LOGIN_ERROR, err, getActivity().getSupportFragmentManager());

            }
        });


    }

    private void restartHome() {

        //get and set all active alarms from user
        getAlarmsFromUser();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }


    private boolean editTextControl() {
        if (etLoginUsername.getText().toString().trim().isEmpty()) {
            etLoginUsername.setError(getActivity().getResources().getString(R.string._required_field));
            return false;
        }

        if (etLoginPassword.getText().toString().trim().isEmpty()) {
            etLoginPassword.setError(getActivity().getResources().getString(R.string._required_field));
            return false;
        }
        return true;
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

                            setAlarmsFromUser();
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

    private void setAlarmsFromUser() {

        if (myAlarmList.size() > 0) {

            for (int i = 0; i < myAlarmList.size(); i++) {
                Alarm a = myAlarmList.get(i);

                if (a.isStatus()) {

                    //set repeating alarm
                    if (a.getRepeat().size() > 0) {
                        util.setAlarmDays(getActivity(), a, alarmManager);
                    } else {
                        util.startAlarm(getActivity(), a, alarmManager);
                    }
                }
            }

        }

    }

}
