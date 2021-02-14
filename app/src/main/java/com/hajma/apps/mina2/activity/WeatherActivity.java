package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.WeatherAdapter;
import com.hajma.apps.mina2.retrofit.model.OtherWeatherDetailsApiModel;
import com.hajma.apps.mina2.retrofit.model.WeatherApiModel;
import com.hajma.apps.mina2.retrofit.model.WeatherListModel;
import com.hajma.apps.mina2.utils.LocaleHelper;
import com.hajma.apps.mina2.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    private LottieAnimationView lottiBackWeather;
    private TextView txtWeatherLocate;
    private TextView txtCurrentWeatherTemp;
    private ImageView imgCurrentWeather;
    private TextView txtWeatherInfo;
    private RecyclerView rvFourWeathers;
    private RecyclerView rvNextTenDaysWeather;
    private JSONObject result;
    private String resultString;
    private ArrayList<WeatherListModel> hourlyWeatherList;
    private WeatherAdapter hourlyAdapter;
    private WeatherAdapter dailyAdapter;
    private ArrayList<WeatherListModel> dailyWeatherList;
    private Util util = new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        resultString = getIntent().getStringExtra("response");

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
        lottiBackWeather = findViewById(R.id.lottiBackWeather);

        lottiBackWeather.setOnClickListener(v -> onBackPressed());


        txtWeatherLocate = findViewById(R.id.txtWeatherLocate);
        txtCurrentWeatherTemp = findViewById(R.id.txtCurrentWeatherTemp);
        imgCurrentWeather = findViewById(R.id.imgCurrentWeather);
        txtWeatherInfo = findViewById(R.id.txtWeatherInfo);
        rvFourWeathers = findViewById(R.id.rvFourWeathers);
        rvNextTenDaysWeather = findViewById(R.id.rvNextTenDaysWeather);


        //setupRecyclerViews
        setupRecyclerViews();

        try {
            setDataFromResultJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setDataFromResultJson() throws JSONException {

        if (result == null) {
            return;
        } else {

            //Current weather json object
            JSONObject currWeatherJsonObject = result.getJSONObject("currentWeather");

            //Hourly weather json object
            JSONObject weatherHourly = result.getJSONObject("weatherHourly");


            //Daily weather json object
            JSONObject weatherDaily = result.getJSONObject("weatherDaily");

            //load current weather details
            loadCurrentWeatherDataFromJson(currWeatherJsonObject);

            //load hourly weather details
            loadHourlyWeatherDataFromJSon(weatherHourly);

            //load daily weather details
            loadDailyWeatherDataFromJson(weatherDaily);


            //play weather sound
            new Handler().postDelayed(() -> {
                String url = null;
                try {
                    url = result.getString("sound");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                playSound(url);
            }, 500);

        }
    }

    private void loadDailyWeatherDataFromJson(JSONObject weatherDaily) throws JSONException {

        JSONArray list = weatherDaily.getJSONArray("list");

        for(int i = 0; i < list.length();i++) {
            JSONObject j = list.getJSONObject(i);

            WeatherListModel dailyWeather = new WeatherListModel();
            dailyWeather.setDateLong(j.getLong("dt"));
            dailyWeather.setIcon(j.getJSONArray("weather").getJSONObject(0).getString("icon"));
            dailyWeather.setTemp(j.getJSONObject("temp").getDouble("day") - 273.15);


            dailyWeatherList.add(dailyWeather);
        }

        //notify adapter for data changes
        dailyAdapter.notifyDataSetChanged();



    }

    private void loadHourlyWeatherDataFromJSon(JSONObject weatherHourly) throws JSONException {

        JSONArray list = weatherHourly.getJSONArray("list");

        for(int i = 0;i < list.length();i++) {
            JSONObject j = list.getJSONObject(i);

            WeatherListModel hourlyWeather = new WeatherListModel(
                    j.getString("dt_txt"),
                    j.getJSONArray("weather").getJSONObject(0).getString("icon"),
                    j.getJSONObject("main").getDouble("temp")  - 273.15
            );

            hourlyWeather.setDateLong(j.getLong("dt"));

            hourlyWeatherList.add(hourlyWeather);

        }

        //notify hourly weather adapter for recyclerview
        hourlyAdapter.notifyDataSetChanged();

    }

    private void loadCurrentWeatherDataFromJson(JSONObject currWeatherJsonObject) throws JSONException {

        JSONArray weather = currWeatherJsonObject.getJSONArray("weather");
        JSONObject main = currWeatherJsonObject.getJSONObject("main");
        WeatherApiModel currentWeatherModel = null;
        for(int i = 0; i < weather.length();i++) {

            JSONObject j = weather.getJSONObject(i);
            currentWeatherModel = new WeatherApiModel(
                    j.getInt("id"),
                    j.getString("main"),
                    j.getString("description"),
                    j.getString("icon")
            );

        }


        OtherWeatherDetailsApiModel weatherDetails = new OtherWeatherDetailsApiModel();
        weatherDetails.setName(currWeatherJsonObject.getString("name"));
        weatherDetails.setTemp((int)(main.getDouble("temp") - 273.15));
        weatherDetails.setTemp_max((int)(main.getDouble("temp_max") - 273.15));
        loadCurrentWeatherViews(currentWeatherModel, weatherDetails);

    }

    @SuppressLint("SetTextI18n")
    private void loadCurrentWeatherViews(WeatherApiModel currentWeather, OtherWeatherDetailsApiModel weatherDetails) {

        txtWeatherLocate.setText(weatherDetails.getName());
        txtCurrentWeatherTemp.setText((int) weatherDetails.getTemp()+ "\u2103");

        String imageRecource = currentWeather.getIcon();
        weatherImage(imageRecource, this, imgCurrentWeather);

        String wInfoDay = "Today, ";
        String wInfoType = currentWeather.getMain();
        String wInfoTemp = weatherDetails.getTemp() + "\u2103";
        String wInfoMaxTemp = weatherDetails.getTemp_max() + "\u2103";
        String wInfoFull = wInfoDay + wInfoType + ". " + "It's "
                + wInfoTemp + " " + "and high will be " + wInfoMaxTemp;

        txtWeatherInfo.setText(wInfoFull);

    }

    private void setupRecyclerViews() {

        //setup hourly weather recyclerview
        hourlyWeatherList = new ArrayList<>();
        hourlyAdapter = new WeatherAdapter(this, hourlyWeatherList);
        hourlyAdapter.setHourlyWeather(true);
        rvFourWeathers.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvFourWeathers.setAdapter(hourlyAdapter);

        //setup daily weather recyclerview
        dailyWeatherList = new ArrayList<>();
        dailyAdapter = new WeatherAdapter(this, dailyWeatherList);
        dailyAdapter.setHourlyWeather(false);
        rvNextTenDaysWeather.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvNextTenDaysWeather.setAdapter(dailyAdapter);


    }

    //set weather image
    public void weatherImage(String data, Context context, ImageView imageView){
        Glide.with(context)
                .load("file:///android_asset/" + data +".png")
                .into(imageView);
    }

    //play sound url
    private void playSound(String soundUrl) {

        try {

            String url = URLDecoder.decode(soundUrl, "UTF-8");

            util.socialSound(url, new MediaPlayer(), this);

            Log.e("kkk", url);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}
