package com.hajma.apps.mina2.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chaos.view.PinView;
import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.activity.MainActivity;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyFragment extends Fragment {

    private String phone;
    private String token;
    private TextView txtUserPhoneNumber;
    private PinView firstPinView;
    private Button btnFInishRegister;
    private static final long START_TIME_IN_MILLIS = 60000;
    private TextView txtCountDown;
    private CountDownTimer countDownTimer;
    private boolean mTimerIsRunning;
    private long mTimeLeftInMilllis = START_TIME_IN_MILLIS;
    private TextView txtResendSMS;
    private MinaInterface minaInterface;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressBar progressBarVerifyLoading;
    private Util util = new Util();
    private int verifyType;


    public VerifyFragment(String phone, String token, int verifyType) {
        this.phone = phone;
        this.token = token;
        this.verifyType = verifyType;
    }

    @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_verify, container, false);


        minaInterface = ApiUtils.getMinaInterface();

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //init views
        progressBarVerifyLoading = view.findViewById(R.id.progressBarVerifyLoading);
        progressBarVerifyLoading.setIndeterminate(false);

        btnFInishRegister = view.findViewById(R.id.btnFInishRegister);
        txtUserPhoneNumber = view.findViewById(R.id.txtUserPhoneNumber);
        txtUserPhoneNumber.setText(""+phone);
        firstPinView = view.findViewById(R.id.firstPinView);
        txtResendSMS = view.findViewById(R.id.txtResendSMS);
        txtCountDown = view.findViewById(R.id.txtCountdownTimer);
        startTimerResendCode();

        txtResendSMS.setOnClickListener(v -> {
            resetTimer();
            startTimerResendCode();
            resendSMS();
        });

        btnFInishRegister.setOnClickListener(v -> {
            btnFInishRegister.setVisibility(View.INVISIBLE);
            progressBarVerifyLoading.setVisibility(View.VISIBLE);
            progressBarVerifyLoading.setIndeterminate(true);

            String code = Objects.requireNonNull(firstPinView.getText()).toString();
            checkVerifyCodeIsCorrect(code);

        });

        if(verifyType == 1) {
            resendSMS();
        }


        return view;
    }

    private void checkVerifyCodeIsCorrect(String code) {

        RequestBody codeBody = RequestBody.create(MediaType.parse("text/plain"), code);

        minaInterface.postVerify(codeBody, "Bearer "+token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBarVerifyLoading.setVisibility(View.GONE);
                progressBarVerifyLoading.setIndeterminate(false);
                btnFInishRegister.setVisibility(View.VISIBLE);

                if(response.isSuccessful()) {

                    try {
                        String s = response.body().string();

                        JSONObject jsonObject = new JSONObject(s);

                        token = jsonObject.getJSONObject("success").getString("token");

                            editor.putString("token", token);
                            editor.putBoolean("isLogin", true);
                            editor.commit();
                            restartHome();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        String errResponse = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errResponse).getJSONObject("error");
                        String errorMessage = jsonObject.getString("message");
                        util.openDialog(C.ALERT_TYPE_LOGIN_ERROR, errorMessage, getActivity().getSupportFragmentManager());

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBarVerifyLoading.setVisibility(View.GONE);
                progressBarVerifyLoading.setIndeterminate(false);
                btnFInishRegister.setVisibility(View.VISIBLE);


                String err = getResources().getString(R.string.check_your_internet_connection);
                util.openDialog(C.ALERT_TYPE_LOGIN_ERROR, err, getActivity().getSupportFragmentManager());

            }
        });


    }

    private void restartHome() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void resendSMS() {

        minaInterface.postResendSms("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    String s = getResources().getString(R.string._new_code_sent);
                    util.openDialog(C.ALERT_TYPE_NOT, s, getActivity().getSupportFragmentManager());
                }else {
                    String s = getResources().getString(R.string._new_code_sent);
                    util.openDialog(C.ALERT_TYPE_NOT, s, getActivity().getSupportFragmentManager());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = getResources().getString(R.string.check_your_internet_connection);
                util.openDialog(C.ALERT_TYPE_LOGIN_ERROR, s, getActivity().getSupportFragmentManager());
            }
        });



    }

    //resend code timer start
    public void startTimerResendCode() {
        countDownTimer = new CountDownTimer(mTimeLeftInMilllis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMilllis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerIsRunning = false;
                txtResendSMS.setVisibility(View.VISIBLE);
            }
        }.start();

        mTimerIsRunning = true;
        txtResendSMS.setVisibility(View.INVISIBLE);
    }

    //update timer text
    private void updateCountDownText() {

        int minutes = (int) (mTimeLeftInMilllis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMilllis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        txtCountDown.setText(timeLeftFormatted);
    }

    //reset timer
    private void resetTimer() {
        mTimeLeftInMilllis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }




}
