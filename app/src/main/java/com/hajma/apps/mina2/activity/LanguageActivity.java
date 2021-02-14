package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.utils.LocaleHelper;

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAz;
    private Button btnRu;
    private Button btnEn;
    private Button btnTr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        btnAz = findViewById(R.id.btnAz);
        btnRu = findViewById(R.id.btnRu);
        btnEn = findViewById(R.id.btnEn);
        btnTr = findViewById(R.id.btnTr);

        btnAz.setOnClickListener(this);
        btnTr.setOnClickListener(this);
        btnRu.setOnClickListener(this);
        btnEn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAz :

                LocaleHelper.setLocale(this, "az");

                startActivity(new Intent(this, WhatIsMinaActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;

            case R.id.btnRu :

                LocaleHelper.setLocale(this, "ru");
                startActivity(new Intent(this, WhatIsMinaActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case R.id.btnEn :

            case R.id.btnTr :

                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
