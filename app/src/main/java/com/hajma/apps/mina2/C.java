package com.hajma.apps.mina2;

public class C {

    public static final String BASE_URL = "https://mina.islamim.com/";

    public static final int NAMAZ_TYPE_MONTHLY = 1;
    public static final int NAMAZ_TYPE_WEEKLY = 2;


    //error type
    public static final int ALERT_TYPE_LOGIN_ERROR = 1;
    public static final int ALERT_TYPE_NOT = 2;
    public static final int ALERT_TYPE_PAYMENT_NOTIFY = 3;
    public static final int ALERT_TYPE_CANCEL_VERIFY = 4;

    //FORGOT TYPE
    public static final int FORGOT_TYPE_EMAIL = 1;
    public static final int FORGOT_TYPE_PHONE = 2;



    public static String URL_CONTACTS_UPLOAD
            = "https://mina.islamim.com/api/get-contact";


    public static String URL_DELETE_ALARM
            = "https://mina.islamim.com/api/delete-alarm/";

    public static String URL_CHANGE_ALARM_STATUS
            = "https://mina.islamim.com/api/set-alarm-status/";

    public static String ALARMMUSIC = "alarmMusic";
    public static String SALUTATION = "salutation";

    public static final String STOPFOREGROUND_ACTION = "startForegorund";
    public static final String STARTFOREGROUND_ACTION = "stopForeground";
    public static String DEFAULTS = "DEFAULTS";



    //Tutorial function keys
    public static final String TUTORIAL_DUA_KEY = "duas, todays dua";
    public static final String TUTORIAL_WEATHER_KEY = "weather, today weather";
    public static final String TUTORIAL_ISLAM_KEY = "namaz-time, weekly-namaz-time, monthly-namaz-time";
    public static final String TUTORIAL_NEWS_KEY = "qalanews";
    public static final String TUTORIAL_KAABA_KEY = "weekly-namaz-time, qibla";
    public static final String TUTORIAL_CALENDAR_KEY = "calendar, clock";
    public static final String TUTORIAL_APERTURE_KEY = "near-objects";
    public static final String TUTORIAL_PLACES_KEY = "near-objects";
    public static final String TUTORIAL_EVENTS_KEY = "near-objects";
    public static final String TUTORIAL_BOOKS_KEY = "my-books, all-books";
    public static final String TUTORIAL_BRAIN_KEY = "be-clever";
    public static final String TUTORIAL_SOCIAL_KEY = "speech";
    public static final String TUTORIAL_USER_KEY = "login";
    public static final String TUTORIAL_SETTINGS_KEY = "change-language";
    public static final String TUTORIAL_TASBIH_KEY = "duas, todays dua";



}
