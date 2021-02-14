package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.NewsAdapter;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.retrofit.model.NewsApiModel;
import com.hajma.apps.mina2.utils.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAudioPlayActivity extends AppCompatActivity {

    private ViewPager viewPagerNews;
    private NewsAdapter adapter;
    private ArrayList<NewsApiModel> newsList;
    private JSONObject result;
    private String resultString;
    private int maxPagePosition = 18;
    private MinaInterface minaInterface;
    private int page = 1;
    private String myKey;
    private SharedPreferences sharedPreferences;
    private boolean isLogin;
    private String token;
    private int langID;
    private LottieAnimationView lottiBackNewsAudioPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_audio_play);

        minaInterface = ApiUtils.getMinaInterface();

        lottiBackNewsAudioPlay = findViewById(R.id.lottiBackNewsAudioPlay);
        lottiBackNewsAudioPlay.setOnClickListener(v -> onBackPressed());

        String lang = LocaleHelper.getPersistedData(this, "az");
        if(lang.equals("az")) {
            langID = 1;

        }else if(lang.equals("ru")){
            langID = 4;

        }


        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        isLogin = sharedPreferences.getBoolean("isLogin", false);
        token = sharedPreferences.getString("token", "");


        resultString = getIntent().getStringExtra("result");
        myKey = getIntent().getStringExtra("myKey");
        try {
            if(resultString != null) {
                result = new JSONObject(resultString);
            }else {
                result = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewPagerNews = findViewById(R.id.viewPagerNews);

        viewPagerNews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(maxPagePosition == position) {
                    try {
                        loadOtherNews();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    maxPagePosition += 18;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager();

        try {
            loadDataFromJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadOtherNews() throws JSONException {

        page += 1;

        RequestBody languageId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(langID));
        RequestBody device_type = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(2));
        RequestBody device_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(1232323));
        RequestBody key = RequestBody.create(MediaType.parse("text/plain"), myKey);
        RequestBody sound = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(1232));
        RequestBody latBody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody lonBody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody pageBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(page));


        minaInterface.postAskRequest(
                languageId, device_type, device_id, key,
                sound, latBody, lonBody, pageBody, "Bearer "+token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()) {
                    try {
                        String s = response.body().string();
                        JSONArray newsArray
                                = new JSONObject(s)
                                .getJSONObject("success")
                                .getJSONObject("data")
                                .getJSONObject("result")
                                .getJSONArray("news");

                        for(int i = 0;i < newsArray.length();i++) {
                            JSONObject j = newsArray.getJSONObject(i);

                            NewsApiModel model = new NewsApiModel();
                            model.setCover(j.getString("cover"));
                            model.setTitle(j.getString("title"));
                            model.setCategory_name(j.getString("category_name"));
                            model.setContent(j.getString("content"));
                            model.setSound(j.getString("sound"));

                            newsList.add(model);
                        }

                        adapter.notifyDataSetChanged();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });



    }

    private void loadDataFromJson() throws JSONException {

        JSONArray news = result.getJSONArray("news");

        for(int i = 0;i < news.length(); i++) {
            JSONObject j = news.getJSONObject(i);

            NewsApiModel model = new NewsApiModel();
            model.setCover(j.getString("cover"));
            model.setTitle(j.getString("title"));
            model.setSound(j.getString("sound"));
            model.setCategory_name(j.getString("category_name"));
            model.setContent(j.getString("content"));

            newsList.add(model);
        }

        adapter.notifyDataSetChanged();
    }

    private void setupViewPager() {

        newsList = new ArrayList<>();
        adapter = new NewsAdapter(newsList, this);
        viewPagerNews.setAdapter(adapter);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
