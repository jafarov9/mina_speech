package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.eventbus.DataEvent;

import org.greenrobot.eventbus.EventBus;

public class TutorialActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardTutorialWeather;
    private CardView cardTutorialCalendar;
    private CardView cardTutorialKaaba;
    private CardView cardTutorialPlace;
    private CardView cardTutorialDua;
    private CardView cardTutorialEvents;
    private CardView cardTutorialNews;
    private CardView cardTutorialIslamCalendar;
    private LottieAnimationView lottiBackTutorial;
    private CardView cardTutorialIslam;
    private CardView cardTutorialAperture;
    private CardView cardTutorialBook;
    private CardView cardTutorialBrain;
    private CardView cardTutorialSocial;
    private CardView cardTutorialUser;
    private CardView cardTutorialSettings;
    private CardView cardTutorialTasbih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        //init variables
        cardTutorialWeather = findViewById(R.id.cardTutorialWeather);
        cardTutorialCalendar = findViewById(R.id.cardTutorialCalendar);
        cardTutorialKaaba = findViewById(R.id.cardTutorialKaaba);
        cardTutorialPlace = findViewById(R.id.cardTutorialPlace);
        cardTutorialDua = findViewById(R.id.cardTutorialDua);
        cardTutorialEvents = findViewById(R.id.cardTutorialEvent);
        cardTutorialNews = findViewById(R.id.cardTutorialNews);
        cardTutorialIslamCalendar = findViewById(R.id.cardTutorialIslamCalendar);
        lottiBackTutorial = findViewById(R.id.lottiBackTutorial);
        cardTutorialIslam = findViewById(R.id.cardTutorialIslam);
        cardTutorialAperture = findViewById(R.id.cardTutorialAperture);
        cardTutorialBook = findViewById(R.id.cardTutorialBook);
        cardTutorialBrain = findViewById(R.id.cardTutorialBrain);
        cardTutorialSocial = findViewById(R.id.cardTutorialSocial);
        cardTutorialUser = findViewById(R.id.cardTutorialUser);
        cardTutorialSettings = findViewById(R.id.cardTutorialSettings);
        cardTutorialTasbih = findViewById(R.id.cardTutorialTasbih);
        lottiBackTutorial.setOnClickListener(v -> onBackPressed());

        //set on click listener cards
        cardTutorialWeather.setOnClickListener(this);
        cardTutorialCalendar.setOnClickListener(this);
        cardTutorialKaaba.setOnClickListener(this);
        cardTutorialPlace.setOnClickListener(this);
        cardTutorialDua.setOnClickListener(this);
        cardTutorialEvents.setOnClickListener(this);
        cardTutorialNews.setOnClickListener(this);
        cardTutorialIslamCalendar.setOnClickListener(this);
        cardTutorialIslam.setOnClickListener(this);
        cardTutorialAperture.setOnClickListener(this);
        cardTutorialBook.setOnClickListener(this);
        cardTutorialBrain.setOnClickListener(this);
        cardTutorialSocial.setOnClickListener(this);
        cardTutorialUser.setOnClickListener(this);
        cardTutorialSettings.setOnClickListener(this);
        cardTutorialTasbih.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cardTutorialWeather :

                Intent weather = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_weather_icon,
                        C.TUTORIAL_WEATHER_KEY,
                        1
                ));

                startActivity(weather);
                break;

            case R.id.cardTutorialIslam :

                Intent islam = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_islam_icon,
                        C.TUTORIAL_ISLAM_KEY,
                        1
                ));

                startActivity(islam);
                break;

            case R.id.cardTutorialCalendar :

                Intent calendar = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_calendar_icon,
                        C.TUTORIAL_CALENDAR_KEY,
                        1
                ));

                startActivity(calendar);
                break;

            case R.id.cardTutorialKaaba :

                Intent kaaba = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_kaaba_icon,
                        C.TUTORIAL_KAABA_KEY,
                        1
                ));

                startActivity(kaaba);
                break;

            case R.id.cardTutorialAperture :

                Intent aperture = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_aperture_icon,
                        C.TUTORIAL_APERTURE_KEY,
                        1
                ));

                startActivity(aperture);
                break;

            case R.id.cardTutorialPlace :

                Intent places = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_place_icon,
                        C.TUTORIAL_PLACES_KEY,
                        1
                ));

                startActivity(places);


                break;

            case R.id.cardTutorialDua :

                Intent dua = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_dua_icon,
                        C.TUTORIAL_DUA_KEY,
                        1
                ));
                startActivity(dua);

                break;

            case R.id.cardTutorialBook :

                Intent books = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_book_icon,
                        C.TUTORIAL_BOOKS_KEY,
                        1
                ));
                startActivity(books);

                break;

            case R.id.cardTutorialBrain :

                Intent brains = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_brain_icon,
                        C.TUTORIAL_BRAIN_KEY,
                        1
                ));
                startActivity(brains);

                break;

            case R.id.cardTutorialTasbih :

                Intent tasbih = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_tasbih_icon,
                        C.TUTORIAL_TASBIH_KEY,
                        1
                ));
                startActivity(tasbih);

                break;

            case R.id.cardTutorialSocial :

                Intent socials = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_social_icon,
                        C.TUTORIAL_SOCIAL_KEY,
                        1
                ));
                startActivity(socials);

                break;

            case R.id.cardTutorialUser :

                Intent user = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_user_icon,
                        C.TUTORIAL_USER_KEY,
                        1
                ));
                startActivity(user);

                break;

            case R.id.cardTutorialSettings :

                Intent settings = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_settings_icon,
                        C.TUTORIAL_SETTINGS_KEY,
                        1
                ));
                startActivity(settings);

                break;

            case R.id.cardTutorialEvent :

                Intent events = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_event_icon,
                        C.TUTORIAL_EVENTS_KEY,
                        1
                ));

                startActivity(events);


                break;

            case R.id.cardTutorialNews :

                Intent newsIntent = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_news_icon,
                        C.TUTORIAL_NEWS_KEY,
                        1
                ));
                startActivity(newsIntent);

                break;

            case R.id.cardTutorialIslamCalendar :

                Intent islamCalendar = new Intent(this, SubTutorialActivity.class);
                EventBus.getDefault().postSticky(new DataEvent.TutorialRequestData(
                        R.drawable.ic_islam_calendar_icon,
                        "namaz-time",
                        1
                ));
                startActivity(islamCalendar);
                break;

        }

    }
}
