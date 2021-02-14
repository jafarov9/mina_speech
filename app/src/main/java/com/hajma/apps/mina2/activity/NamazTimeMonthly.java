package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.NamazTimeMontlyWeeklyAdapter;
import com.hajma.apps.mina2.retrofit.model.NamazTimeWeeklyModel;
import com.hajma.apps.mina2.utils.LocaleHelper;
import com.hajma.apps.mina2.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class NamazTimeMonthly extends AppCompatActivity {

    private RecyclerView rvNamazTimeWeeklyMontly;
    private int type;
    private JSONObject result;
    private String resultString;
    private Util util = new Util();
    private ArrayList<NamazTimeWeeklyModel> list;
    private NamazTimeMontlyWeeklyAdapter adapter;
    private LottieAnimationView lottiBackNamazMontly;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namaz_time_monthly);

        resultString = getIntent().getStringExtra("result");
        type = getIntent().getIntExtra("type", 0);

        try {
            if(resultString != null) {
                result = new JSONObject(resultString);
            }else {
                result = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //init views
        rvNamazTimeWeeklyMontly = findViewById(R.id.rvNamazTimeWeeklyMontly);
        lottiBackNamazMontly = findViewById(R.id.lottiBackNamazMontly);
        lottiBackNamazMontly.setOnClickListener(v -> onBackPressed());


        try {
            setupRecyclerViewData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupRecyclerViewData() throws JSONException {

        list = new ArrayList<>();
        adapter = new NamazTimeMontlyWeeklyAdapter(this, list);
        rvNamazTimeWeeklyMontly.setLayoutManager(new LinearLayoutManager(this));
        rvNamazTimeWeeklyMontly.setAdapter(adapter);
        rvNamazTimeWeeklyMontly.setHasFixedSize(true);

        setDataFromResultJson();
    }

    private void setDataFromResultJson() throws JSONException {

        JSONArray data = null;

        if (result == null) {
            return;
        } else {

            if(type == C.NAMAZ_TYPE_MONTHLY) {
                data = result.getJSONArray("monthly");
            } else if(type == C.NAMAZ_TYPE_WEEKLY){
                data = result.getJSONArray("weekly");
            }

            for(int i = 0; i < data.length();i++) {
                JSONObject j = data.getJSONObject(i);

                NamazTimeWeeklyModel model = new NamazTimeWeeklyModel(
                    j.getString("date"),
                        j.getString("subh"),
                        j.getString("zohr"),
                        j.getString("esr"),
                        j.getString("sham"),
                        j.getString("xiften")
                );

                list.add(model);
            }

            //notify adapter for data changes
            adapter.notifyDataSetChanged();

        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}
