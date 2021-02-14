package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.utils.LocaleHelper;

public class WhatIsMinaActivity extends AppCompatActivity {

    private Button btnNextWhatIsMina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_is_mina);

        btnNextWhatIsMina = findViewById(R.id.btnNextWhatIsMina);

        btnNextWhatIsMina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WhatIsMinaActivity.this, AskToMina.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
