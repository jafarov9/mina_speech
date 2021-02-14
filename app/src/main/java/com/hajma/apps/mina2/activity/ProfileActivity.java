package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.fragment.FragmentSettings;
import com.hajma.apps.mina2.fragment.LoginFragment;
import com.hajma.apps.mina2.utils.LocaleHelper;

public class ProfileActivity extends AppCompatActivity {

    private LottieAnimationView lottiBackProfile;
    private SharedPreferences sharedPreferences;
    private String token;
    private ImageButton iButtonBackProfile;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        fm = getSupportFragmentManager();

        iButtonBackProfile = findViewById(R.id.iButtonBackProfile);
        iButtonBackProfile.setOnClickListener(v -> onBackPressed());


        if(token == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        fm.beginTransaction()
                .add(R.id.profile_container,
                        new FragmentSettings(), "settingsFragment")
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .addToBackStack("settingsFragment")
                .commit();

        lottiBackProfile = findViewById(R.id.lottiBackProfile);
        lottiBackProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {

        if(fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        }else {
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}
