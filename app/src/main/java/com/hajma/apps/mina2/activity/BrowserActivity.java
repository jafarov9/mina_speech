package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.utils.LocaleHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class BrowserActivity extends AppCompatActivity {

    private LottieAnimationView lottiBackBrowser;
    private WebView browserVeiw;
    private JSONObject result;
    private String resultString;
    private ProgressBar pbBrowserLoading;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        if(getIntent() != null) {
            resultString = getIntent().getStringExtra("response");
            try {
                result = new JSONObject(resultString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            result = null;
        }

        //init views
        pbBrowserLoading = findViewById(R.id.pbBrowserLoading);
        lottiBackBrowser = findViewById(R.id.lottiBackBrowser);
        browserVeiw = findViewById(R.id.browserView);

        WebSettings webSettings = browserVeiw.getSettings();
        webSettings.setJavaScriptEnabled(true);

        lottiBackBrowser.setOnClickListener(v -> {
            Intent intent = new Intent(BrowserActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });


        try {
            setDataFromJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setDataFromJson() throws JSONException {

        if(result == null) {
            return;
        }else {
            browserVeiw.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    pbBrowserLoading.setVisibility(View.VISIBLE);
                    //setTitle("Loading...");
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    pbBrowserLoading.setVisibility(View.GONE);
                    //setTitle(view.getTitle());
                }
            });
            browserVeiw.loadUrl(result.getString("url"));
        }
    }

    @Override
    public void onBackPressed() {

        if(browserVeiw.canGoBack()) {
            browserVeiw.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}
