package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.retrofit.model.DuaListApiModel;
import com.hajma.apps.mina2.retrofit.model.TodayNamazTimeApiModel;
import com.hajma.apps.mina2.utils.LocaleHelper;
import com.hajma.apps.mina2.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class NamazTimeDay extends AppCompatActivity {

    private TextView txtPrayerName;
    private TextView txtPrayerDay;
    private TextView txtCurrentNamazTime;
    private TextView txtCurrentNamazName;
    private TextView txtSubhTime, txtZohrTime,
            txtAsrTime, txtShamTime, txtHiftanTime;
    private JSONObject result;
    private String resultString;
    private Util util = new Util();
    private LottieAnimationView lottiBackNamazTimeDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namaz_time_day);

        lottiBackNamazTimeDay = findViewById(R.id.lottiBackNamazTimeDay);
        lottiBackNamazTimeDay.setOnClickListener(v -> onBackPressed());

        resultString = getIntent().getStringExtra("result");

        try {
            if(resultString != null) {
                result = new JSONObject(resultString);
            }else {
                result = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        //init variables
        txtCurrentNamazName = findViewById(R.id.txtCurrentNamazName);
        txtPrayerDay = findViewById(R.id.txtPrayerDay);
        txtPrayerName = findViewById(R.id.txtPrayerName);
        txtCurrentNamazTime = findViewById(R.id.txtCurrentNamazTime);
        txtSubhTime = findViewById(R.id.txtSubhTime);
        txtZohrTime = findViewById(R.id.txtZohrTime);
        txtAsrTime = findViewById(R.id.txtAsrTime);
        txtShamTime = findViewById(R.id.txtShamTime);
        txtHiftanTime = findViewById(R.id.txtHiftanTime);



        setDataFromResultJson();
    }

    private void setDataFromResultJson() {

        if (result == null) {
            return;
        }else {
            try {
                JSONObject todayTime = result.getJSONObject("today_time");
                JSONObject dua = result.getJSONObject("dua");
                String soundUrl = result.getString("sound");
                String currentNamaz = result.getString("namaz");
                String currentNamazTime = result.getString("time");

                //load today namaz details from json
                TodayNamazTimeApiModel todayTimeModel = new TodayNamazTimeApiModel(
                        todayTime.getString("date"),
                        todayTime.getString("hicri"),
                        todayTime.getString("imsak"),
                        todayTime.getString("subh"),
                        todayTime.getString("zohr"),
                        todayTime.getString("esr"),
                        todayTime.getString("sham"),
                        todayTime.getString("xiften"),
                        todayTime.getString("midnight"),
                        todayTime.getString("sunrise"),
                        todayTime.getString("sunset")
                );

                //load dua details from json
                DuaListApiModel duaModel = new DuaListApiModel();
                duaModel.setDay(dua.getInt("day"));
                duaModel.setContent(dua.getString("content"));
                duaModel.setTitle(dua.getString("title"));

                loadViews(duaModel, todayTimeModel, currentNamaz, currentNamazTime);
                playSound(soundUrl);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadViews(DuaListApiModel duaModel, TodayNamazTimeApiModel timeModel, String currentNamaz, String currentNamazTime) {

        txtPrayerName.setText(duaModel.getTitle());
        txtPrayerDay.setText(duaModel.getContent());

        txtCurrentNamazName.setText(currentNamaz.toUpperCase());
        txtCurrentNamazTime.setText(currentNamazTime);
        txtSubhTime.setText(timeModel.getSubh());
        txtZohrTime.setText(timeModel.getZohr());
        txtAsrTime.setText(timeModel.getEsr());
        txtShamTime.setText(timeModel.getSham());
        txtHiftanTime.setText(timeModel.getXiften());

    }

    private void playSound(String soundUrl) {

        //String url = URLDecoder.decode(soundUrl, "UTF-8");

        util.socialSound(soundUrl, new MediaPlayer(), this);

        Log.e("kkk", soundUrl);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}
