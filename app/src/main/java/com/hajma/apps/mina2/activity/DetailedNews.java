package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.utils.LocaleHelper;

public class DetailedNews extends AppCompatActivity {

    private ImageView sdsdsds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        sdsdsds = findViewById(R.id.imageView13);

        sdsdsds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(DetailedNews.this, NewsAudioPlayActivity.class));
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
