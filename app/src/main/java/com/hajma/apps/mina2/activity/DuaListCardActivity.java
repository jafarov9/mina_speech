package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.DuaListAdapter;
import com.hajma.apps.mina2.retrofit.model.DuaListApiModel;
import com.hajma.apps.mina2.utils.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DuaListCardActivity extends AppCompatActivity {

    private LottieAnimationView lottiBackDuaList;
    private RecyclerView rvDuaListCard;
    private DuaListAdapter adapter;
    private ArrayList<DuaListApiModel> duaList;
    private JSONObject result;
    private String resultString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dua_list_card);

        rvDuaListCard = findViewById(R.id.rvDuaListCard);

        resultString = getIntent().getStringExtra("result");

        try {

            if(resultString != null) {
                result = new JSONObject(resultString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lottiBackDuaList = findViewById(R.id.lottiBackDuaList);
        lottiBackDuaList.setOnClickListener(v -> onBackPressed());

        setupRecyclerView();

    }

    private void setupRecyclerView() {

        rvDuaListCard.setLayoutManager(new GridLayoutManager(this, 2));

        duaList = new ArrayList<>();

        adapter = new DuaListAdapter(this, duaList);
        rvDuaListCard.setAdapter(adapter);

        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {

        if(result != null) {

            try {
                JSONArray dualar = result.getJSONArray("dualar");

                for(int i = 0; i < dualar.length(); i++) {
                    JSONObject dua = dualar.getJSONObject(i);

                    int id = dua.getInt("id");
                    int day = 0;
                    if(!dua.isNull("day")) {
                        day = dua.getInt("day");
                    }else {
                        day = -1;
                    }

                    String title = dua.getString("title");
                    String content = dua.getString("content");

                    DuaListApiModel model = new DuaListApiModel(
                        id, day, title, content
                    );

                    duaList.add(model);
                }

                adapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
