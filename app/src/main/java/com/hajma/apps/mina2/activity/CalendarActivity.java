package com.hajma.apps.mina2.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.CalendarView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView;
import com.hajma.apps.mina2.R;


import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private MaterialHijriCalendarView hijriCalendar;
    private CalendarView calendarView;
    private LottieAnimationView lottiBackCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        hijriCalendar = findViewById(R.id.hijriCalendar);
        lottiBackCalendar = findViewById(R.id.lottiBackCalendar);

        lottiBackCalendar.setOnClickListener(v -> onBackPressed());


        calendarView = findViewById(R.id.calendarView);



        hijriCalendar.setSelectedDate(new Date(calendarView.getDate()));

    }


    /*
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
     */
}
