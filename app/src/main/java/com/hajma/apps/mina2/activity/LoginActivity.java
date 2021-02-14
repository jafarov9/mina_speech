package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.fragment.LoginFragment;
import com.hajma.apps.mina2.utils.LocaleHelper;
import com.hajma.apps.mina2.utils.Util;

public class LoginActivity extends AppCompatActivity {

    private FragmentManager fm;
    private ImageButton iButtonBackLogin;
    private Util util = new Util();
    private LottieAnimationView lottiBackHomeFromLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fm = getSupportFragmentManager();

        fm.beginTransaction()
                .add(R.id.sign_fragment_container,
                new LoginFragment(), "loginFragment")
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .addToBackStack("loginFragment")
                .commit();



        //init views
        iButtonBackLogin = findViewById(R.id.iButtonBackLogin);
        iButtonBackLogin.setOnClickListener(v -> onBackPressed());

        lottiBackHomeFromLogin = findViewById(R.id.lottiBackHomeFromLogin);
        lottiBackHomeFromLogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }

    //handle on back pressed

    @Override
    public void onBackPressed() {

        Fragment verifyFragment = fm.findFragmentByTag("verifyFragment");

        if(verifyFragment != null) {
            String warningMsg = getResources().getString(R.string._if_you_back);
            util.openDialog(C.ALERT_TYPE_CANCEL_VERIFY, warningMsg, getSupportFragmentManager());

        } else if(fm.getBackStackEntryCount() > 1) {
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
