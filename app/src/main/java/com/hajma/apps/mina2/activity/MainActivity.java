package com.hajma.apps.mina2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.airbnb.lottie.LottieAnimationView;
import com.dnkilic.waveform.WaveView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.SuggestionsAdapter;
import com.hajma.apps.mina2.responses.Base;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.retrofit.model.ContactsModel;
import com.hajma.apps.mina2.utils.LocaleHelper;
import com.hajma.apps.mina2.utils.StaggeredListDecoration;
import com.hajma.apps.mina2.utils.Util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int RECORD_AUDIO_PERMISSON_CODE = 1;
    private static final int REQUEST_CONTACTS = 33;
    private static final int REQUEST_LOCATION_GOOGLE = 111;
    private ImageButton iButtonTutorials;
    private LottieAnimationView lottieMic;
    private ImageButton iButtonBehavior;
    private long eventTime = 0;
    private long downTime = 0;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private MinaInterface minaInterface;
    private LinearLayout lnrSuggestions;
    private RecyclerView rvSuggestions;
    private SuggestionsAdapter suggestionsAdapter;
    private ArrayList<String> suggestKeyList;
    private boolean haveRecordPermission;
    private LocationManager locationManager;
    private String lat = "";
    private String lon = "";
    private String token;
    private boolean isLogin;
    private SharedPreferences sharedPreferences;
    private Util util = new Util();
    private ArrayList<String> slideList = new ArrayList<>();
    private Handler handler;
    private TextSwitcher textSwitcher;
    private int stringIndex = 0;
    private TextView textView;
    private boolean threadIsRunning = true;
    private int langID;
    private String extraLanguage;
    private AudioManager audioManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Vibrator vibrator;
    private WaveView siriWave;
    private LazyLoader mLazyLoader;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //get languge details to speeching and api -> languageId
        String lang = LocaleHelper.getPersistedData(this, "az");
        if(lang.equals("az")) {
            langID = 1;
            extraLanguage = "az-AZ";
        }else if(lang.equals("ru")){
            langID = 4;
            extraLanguage = "ru-RU";
        }


        // play welcome sound
        playWelcomeSound();



        //Location services init
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        //init vibrate service
        vibrator  = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //init audioManager service
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //init handler object
        handler = new Handler();

        //init minaInterface object, send request servers apiService
        minaInterface = ApiUtils.getMinaInterface();

        //init sharedPreferences
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);


        //get isLogin and token sharedPreferences
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        token = sharedPreferences.getString("token", "");


        //request permissions, upload contacts
        requestPermission();

        //findLocation();
        findLocationGoogleService();


        //init textSwitcher view
        textSwitcher = findViewById(R.id.txtAutoSlideSuggest);
        textSwitcher.setFactory(() -> {
            textView = new TextView(MainActivity.this);
            //textView.setTextColor(Color.BLACK);
            //textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextAppearance(R.style.fontForSlideText);

            return textView;
        });


        //setup dots loader animation view
        setupDotsLoader();

        //init suggests linearLayout panel
        lnrSuggestions = findViewById(R.id.lnrSuggestions);
        lnrSuggestions.setVisibility(View.GONE);
        //init suggestions recyclerview
        rvSuggestions = findViewById(R.id.rvSuggestions);

        //init siriWave speeching animation
        siriWave = findViewById(R.id.siriWave);


        //init button tutorials and behavior
        iButtonTutorials = findViewById(R.id.iButtonTutorials);
        iButtonBehavior = findViewById(R.id.iButtonBehavior);


        //onclick method behavior button
        iButtonBehavior.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TeachMeActivity.class)));

        //onclick method tutorials button
        iButtonTutorials.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TutorialActivity.class)));


        //init lottieMicrophone animation view
        lottieMic = findViewById(R.id.iButtonMicrophone);


        //init and setup speechRecognizer intent
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //Log.e("vbvb", "Locale: "+Locale.getDefault().getLanguage());



        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, extraLanguage);



        //set recognition listener for speechRecognizer
        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            boolean singleResult = true;


            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                //Log.e("lklk", "Buffer: "+buffer.toString());
            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                if(singleResult) {

                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


                    Log.e("lklk", results.toString());


                    if (matches != null) {
                        String result = matches.get(0);

                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();

                        sendApiRequest(result);
                    }
                    singleResult = false;
                }



                handler.postDelayed(() -> singleResult = true, 100);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }


        });


        //set onTouch listener for lottie microphone
        lottieMic.setOnTouchListener((v, event) -> {

            //check runtime permisson
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) ==
                        PackageManager.PERMISSION_DENIED) {
                    haveRecordPermission = false;
                    //permisson not granted, request it.
                    String[] permissons = {Manifest.permission.RECORD_AUDIO};
                    //show popup for runtime permission
                    requestPermissions(permissons, RECORD_AUDIO_PERMISSON_CODE);
                }else {
                    //permisson already granted
                    haveRecordPermission = true;
                }
            } else {
                //system os is less than marsmallow
                haveRecordPermission = true;
            }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        //cancel microphone animation and end siriWave animation
                        lottieMic.cancelAnimation();
                        siriWave.speechStarted();

                        //if haveRecord permission is true, start listening
                        if(haveRecordPermission) {
                            lottieMic.setPressed(true);
                            downTime = event.getDownTime();

                            streamMute(AudioManager.STREAM_NOTIFICATION);
                            streamMute(AudioManager.STREAM_ALARM);
                            streamMute(AudioManager.STREAM_MUSIC);
                            streamMute(AudioManager.STREAM_RING);
                            streamMute(AudioManager.STREAM_SYSTEM);


                            speechRecognizer.startListening(speechRecognizerIntent);

                        }

                        return true;

                    case MotionEvent.ACTION_UP:

                        //resume animations when action up
                        lottieMic.resumeAnimation();
                        siriWave.speechEnded();

                        lottieMic.setPressed(false);

                        eventTime = event.getEventTime();


                        //hold time is user keeping time for button
                        long holdTime = (eventTime - downTime);

                        //Log.e("jkjk", "Hold time: " + holdTime);

                        streamUnmute(AudioManager.STREAM_NOTIFICATION);
                        streamUnmute(AudioManager.STREAM_ALARM);
                        streamUnmute(AudioManager.STREAM_MUSIC);
                        streamUnmute(AudioManager.STREAM_RING);
                        streamUnmute(AudioManager.STREAM_SYSTEM);


                        //when hold time is great one second, speeching correctly
                        if(holdTime > 1000) {
                            Log.e("jkjk", "Buraxilib: " + eventTime);

                            //when user action up call vibrate method
                            vibrate();

                            //stop listening for speechRecognizer
                            speechRecognizer.stopListening();
                            return true;
                        }

                }


            return true;
        });

        //setup suggestion for request
        setupSuggestionRecyclerView();


        //when thread is running load slides for textSwitcher
        if(threadIsRunning) {
            loadSlideSuggestData(langID);
        }

        //after 300 milliseconds for oncreate method call start siriWaveAnimation
        handler.postDelayed(this::startSiriWaveAnimation, 300);
    }

    //setup dots loader progress bar
    private void setupDotsLoader() {

        mLazyLoader = findViewById(R.id.mLazyLoader);

        LazyLoader loader = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader.setAnimDuration(500);
        loader.setFirstDelayDuration(100);
        loader.setSecondDelayDuration(200);
        loader.setInterpolator(new LinearInterpolator());

        mLazyLoader.addView(loader);
        mLazyLoader.setVisibility(View.INVISIBLE);


    }

    //start siri animation view
    private void startSiriWaveAnimation() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        siriWave.initialize(dm);

    }



    //stop suggest text swither when activity not showing
    private void stopSuggestTextSwitcher() {
        threadIsRunning = false;
        handler.removeCallbacks(slideUpdater);
        stringIndex = 0;
        textSwitcher.setText("");
    }


    //change text switcher for slide suggests view
    private void changeTextSwitcher() {

        if(slideList.size() > 0) {
            if (stringIndex == slideList.size() - 1) {
                stringIndex = 0;
                textSwitcher.setText(slideList.get(stringIndex));
            } else {
                textSwitcher.setText(slideList.get(++stringIndex));
            }

            handler.postDelayed(slideUpdater, 2000);

        }
    }


    //runnable updater for suggest view slide
    private Runnable slideUpdater = new Runnable() {
        @Override
        public void run() {
            changeTextSwitcher();
        }
    };


    //suggests list setup
    private void setupSuggestionRecyclerView() {

        StaggeredGridLayoutManager sdsd = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);

        suggestKeyList = new ArrayList<>();
        suggestionsAdapter = new SuggestionsAdapter(suggestKeyList, this);

        rvSuggestions.addItemDecoration(new StaggeredListDecoration());
        rvSuggestions.setLayoutManager(sdsd);
        rvSuggestions.setAdapter(suggestionsAdapter);
    }


    //send a main ask api request
    public void sendApiRequest(final String result) {

        mLazyLoader.setVisibility(View.VISIBLE);

        Log.e("mbmb", result);

        RequestBody languageId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(langID));
        RequestBody device_type = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(2));
        RequestBody device_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(1232323));
        RequestBody key = RequestBody.create(MediaType.parse("text/plain"), result);
        RequestBody sound = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(1232));
        RequestBody latBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lat));
        RequestBody lonBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lon));
        RequestBody pageBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(1));

        Log.e("ererer", "Lat: "+lat + "Lon: "+lon);

        minaInterface.postAskRequest(languageId, device_type, device_id, key, sound, latBody, lonBody, pageBody, "Bearer "+token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                        mLazyLoader.setVisibility(View.INVISIBLE);


                        if(response.isSuccessful()) {
                            try {
                                //clear suggestions and invisible linear layout
                                lnrSuggestions.setVisibility(View.GONE);
                                suggestKeyList.clear();
                                suggestionsAdapter.notifyDataSetChanged();

                                assert response.body() != null;
                                String s = response.body().string();

                                Log.e("vbvb", "Response: " +s);

                                JSONObject data = new JSONObject(s)
                                        .getJSONObject("success")
                                        .getJSONObject("data");

                                JSONObject function = data.getJSONObject("function");

                                JSONObject resultJSON = null;

                                if(!data.isNull("result")) {
                                    resultJSON = data.getJSONObject("result");
                                }

                                //find response with parent static method
                                Base.base(MainActivity.this, function, resultJSON, result);


                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            try {
                                //clear last suggest keys list
                                suggestKeyList.clear();
                                suggestionsAdapter.notifyDataSetChanged();


                                assert response.errorBody() != null;
                                String s = response.errorBody().string();

                                Log.e("vbvb", "Response Error: " +s);


                                if(response.code() == 500) {
                                    //Server error
                                    Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_LONG).show();
                                }else {

                                    JSONObject error
                                            = new JSONObject(s)
                                            .getJSONObject("error");

                                    int errorID = error.getInt("id");
                                    JSONArray suggestions = error.getJSONArray("suggest");

                                    if (suggestions.length() > 0) {

                                        //load suggestions list first or again
                                        for (int i = 0; i < suggestions.length(); i++) {
                                            JSONObject suggest = suggestions.getJSONObject(i);
                                            suggestKeyList.add(suggest.getString("key"));
                                        }

                                        //notify adapter data changed
                                        suggestionsAdapter.notifyDataSetChanged();

                                        //visible suggestions LinearLayout
                                        lnrSuggestions.setVisibility(View.VISIBLE);
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, TeachMeActivity.class);
                                        intent.putExtra("errorID", errorID);
                                        startActivity(intent);
                                    }

                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        mLazyLoader.setVisibility(View.INVISIBLE);
                        Log.e("vbvb", "Response fail: " +t.toString());

                    }
                });


    }



    //find location method standard, never used
    /*private void findLocation() {

        Log.e("dfdfdf", "Location called");

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //check gps is enable or not
        assert locationManager != null;
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //write function to enable gps
            onGps();
        } else {
            // GPS is already on then
            getLocation();
        }
    }*/


    //get location latitude and longitude method
    /*private void getLocation() {

        //check permmissons again
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location locationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if(locationGPS != null) {
                double latitude = locationGPS.getLatitude();
                double longitude = locationGPS.getLongitude();

                lat = String.valueOf(latitude);
                lon = String.valueOf(longitude);

                Log.e("jhjh", "Lat: "+ lat + " Lon: "+lon);

            } else if(locationNetwork != null) {
                double latitude = locationNetwork.getLatitude();
                double longitude = locationNetwork.getLongitude();

                lat = String.valueOf(latitude);
                lon = String.valueOf(longitude);

                Log.e("jhjh", "Lat: "+ lat + " Lon: "+lon);

            } else if(locationPassive != null) {
                double latitude = locationPassive.getLatitude();
                double longitude = locationPassive.getLongitude();

                lat = String.valueOf(latitude);
                lon = String.valueOf(longitude);

                Log.e("jhjh", "Lat: "+ lat + " Lon: "+lon);

            }
        }

    }*/

    //enable gps method
    private void onGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage("Enable GPS")
                .setCancelable(false)
                .setPositiveButton("YES", (dialog, which) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("NO", (dialog, which) -> dialog.cancel());

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    //request permisson and if permisson granted, upload user contacts to server
    private void requestPermission() {


        boolean isContactsUploaded = sharedPreferences.getBoolean("isContactsUploaded", false);

        if(!isContactsUploaded) {

            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS);
            } else {
                ArrayList<ContactsModel.Contact> contacts;
                contacts = util.getAllContacts(MainActivity.this);
                String id = UUID.randomUUID().toString();
                util.uploadContact(MainActivity.this, id, contacts, token, sharedPreferences);

            }
        }
    }





    //Handle onNewIntent method to tutorial and notifications pending intents
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent != null && intent.getStringExtra("tutorialKey") != null) {
            String tutorialKey = intent.getStringExtra("tutorialKey");
            sendApiRequest(tutorialKey);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!threadIsRunning) {
            Log.e("qweee", "Burdayam");
            loadSlideSuggestData(langID);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSuggestTextSwitcher();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    //load slide suggests data
    private void loadSlideSuggestData(int langID) {

        slideList.clear();

        minaInterface.getSlideSuggests(String.valueOf(langID)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        String s = response.body().string();

                        JSONArray data
                                = new JSONObject(s)
                                .getJSONObject("success")
                                .getJSONArray("data");

                        for (int i = 0;i < data.length();i++) {

                            String temp = data.getString(i);

                            if(temp.length() <= 25) {
                                slideList.add(temp);
                            }
                        }

                        changeTextSwitcher();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    stopSuggestTextSwitcher();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                stopSuggestTextSwitcher();
            }
        });


    }


    //attach base context to language changes
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }


    //unmute all stream when user action up microphone
    private void streamUnmute(int stream) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (audioManager.isStreamMute(stream)) {
                audioManager.adjustStreamVolume(stream, AudioManager.ADJUST_UNMUTE, 0);
            }
        } else {
            audioManager.setStreamMute(stream, false);
        }
    }


    //mute all stream when user touch microphone
    private void streamMute(int stream) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!audioManager.isStreamMute(stream)) {
                audioManager.adjustStreamVolume(stream, AudioManager.ADJUST_MUTE, 0);
            }
        } else {
            audioManager.setStreamMute(stream, true);
        }
    }



    //Handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CONTACTS) {

            if(grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                //permission was granted
                ArrayList<ContactsModel.Contact> contacts;
                contacts = util.getAllContacts(MainActivity.this);
                String id = UUID.randomUUID().toString();
                util.uploadContact(MainActivity.this, id, contacts, token, sharedPreferences);

            } else {
                //permission was denied
            }


        }


        if(requestCode == REQUEST_LOCATION_GOOGLE) {

            if(grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                //permission was granted
                findLocationGoogleService();

            } else {
                //permission was denied
            }
        }




    }



    //find location google services
    public void findLocationGoogleService() {

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        assert locationManager != null;
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //write function to enable gps
            onGps();
        } else {
            // GPS is already on then

        }


        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_GOOGLE);
        } else {
            mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if(location != null) {
                        lat = String.valueOf(location.getLatitude());
                        lon = String.valueOf(location.getLongitude());

                    }
                });
        }




    }



    //Vibrate method when microphone action up
    private void vibrate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert vibrator != null;
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
        } else {
            assert vibrator != null;
            vibrator.vibrate(50);
        }
    }


    //play welcome sound
    private void playWelcomeSound(){
        if(langID == 1){
            util.errorPlayer(R.raw.welcome_az, new MediaPlayer(),this);
        }else if(langID == 4){
            util.errorPlayer(R.raw.welcome_ru, new MediaPlayer(),this);
        }
    }

}
