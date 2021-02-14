package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.SubTutorialAdapter;
import com.hajma.apps.mina2.eventbus.DataEvent;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.utils.LocaleHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubTutorialActivity extends AppCompatActivity {

    private ImageButton iButtonBackSubTutorial;
    private RecyclerView rvSubTutorial;
    private int drawableID;
    private MinaInterface minaInterface;
    private ArrayList<String> tutorialKeys;
    private SubTutorialAdapter subTutorialAdapter;
    private String function;
    private int langID;
    private LottieAnimationView lottiBackSubTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_tutorial);

        minaInterface = ApiUtils.getMinaInterface();

        String lang = LocaleHelper.getPersistedData(this, "az");
        if(lang.equals("az")) {
            langID = 1;

        }else if(lang.equals("ru")){
            langID = 4;

        }


        //init variables
        iButtonBackSubTutorial = findViewById(R.id.iButtonBackSubTutorial);
        rvSubTutorial = findViewById(R.id.rvSubTutorial);

        iButtonBackSubTutorial.setOnClickListener(v -> {onBackPressed();});

        lottiBackSubTutorial = findViewById(R.id.lottiBackSubTutorial);
        lottiBackSubTutorial.setOnClickListener(v -> {
            Intent intent = new Intent(SubTutorialActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }

    private void setupRecyclerView() {

        tutorialKeys = new ArrayList<>();
        subTutorialAdapter = new SubTutorialAdapter(this, tutorialKeys, drawableID);
        rvSubTutorial.setLayoutManager(new LinearLayoutManager(this));
        rvSubTutorial.setAdapter(subTutorialAdapter);

        loadRecylerViewData();
    }


    //get sub tutorials request
    private void loadRecylerViewData() {


        if(function != null) {



            minaInterface.getSubTutorials(function, langID).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {

                        try {


                            String s = response.body().string();
                            JSONObject success = new JSONObject(s).getJSONObject("success");

                            JSONArray tutorial = success.getJSONArray("tutorial");

                            for (int i = 0; i < tutorial.length();i++) {

                                JSONObject j = tutorial.getJSONObject(i);
                                String key = j.getString("key");
                                tutorialKeys.add(key);
                            }

                            //notify adapter data changes
                            subTutorialAdapter.notifyDataSetChanged();

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }else {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });




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

    @Subscribe(sticky = true)
    public void onTutorialRequestData(DataEvent.TutorialRequestData event) {
        if(event.getResponse() == 1) {
            function = event.getFunction();
            drawableID = event.getDrawableID();

            setupRecyclerView();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
