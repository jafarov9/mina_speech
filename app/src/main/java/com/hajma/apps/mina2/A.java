package com.hajma.apps.mina2;

import android.app.Application;

import com.hajma.apps.mina2.utils.LocaleHelper;

import java.util.Locale;

public class A extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String lang = LocaleHelper.getPersistedData(this, "az");
        LocaleHelper.setLocale(this, lang);

    }
}
