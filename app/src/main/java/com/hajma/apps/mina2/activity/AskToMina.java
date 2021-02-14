package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.utils.LocaleHelper;

public class AskToMina extends AppCompatActivity {

    private static final int REQUEST_CODE = 11;
    private Button btnNextAskToMina;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_to_mina);

        sharedPreferences = getSharedPreferences("system", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("isFirstRun", false);
        editor.apply();

        btnNextAskToMina = findViewById(R.id.btnNextAskToMina);

        btnNextAskToMina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AskToMina.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });


        requestAllPermissions();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    public void requestAllPermissions() {


        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) +
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                checkSelfPermission(Manifest.permission.CAMERA) +
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) +
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) +
                checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_CONTACTS
            }, REQUEST_CODE);
        }

    }


}
