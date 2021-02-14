package com.hajma.apps.mina2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.CalendarEventsAdapter;
import com.hajma.apps.mina2.utils.LocaleHelper;

import java.util.Calendar;

public class EventFromDate extends AppCompatActivity {


    private CalendarView eventCalendarView;
    private CalendarEventsAdapter adapter;
    private RecyclerView rvCalendarEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_from_date);

        eventCalendarView = findViewById(R.id.eventCalendarView);
        rvCalendarEvents = findViewById(R.id.rvCalendarEvents);

        rvCalendarEvents.setLayoutManager(new LinearLayoutManager(this));


        eventCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {


                Calendar c = Calendar.getInstance();

                c.set(year, month, dayOfMonth);

                long changedDate = c.getTime().getTime();

                loadRecyclerViewData(changedDate);
            }
        });
    }

    private void loadRecyclerViewData(long changedDate) {

        Calendar c = Calendar.getInstance();

        c.set(2020, 3, 24);


        long apiDate = c.getTime().getTime();

        Log.e("cvb", "cur date: "+ changedDate);
        Log.e("cvb", "api date: "+ apiDate);


        if(changedDate == apiDate) {
            adapter = new CalendarEventsAdapter(this, null);
            rvCalendarEvents.setAdapter(adapter);
        }else {

        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
