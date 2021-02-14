package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.PlaceEventsAdapter;
import com.hajma.apps.mina2.utils.LocaleHelper;

public class PlaceEventsActivity extends AppCompatActivity {

    private ImageButton iButtonBackPlaceEvents;
    private RecyclerView rvPlaceEvents;
    private PlaceEventsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_events);

        rvPlaceEvents = findViewById(R.id.rvPlaceEvents);
        iButtonBackPlaceEvents = findViewById(R.id.iButtonBackPlaceEvents);
        iButtonBackPlaceEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setupRecyclerView();
    }

    private void setupRecyclerView() {

        adapter = new PlaceEventsAdapter(this, null);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rvPlaceEvents.setLayoutManager(manager);
        rvPlaceEvents.setAdapter(adapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
