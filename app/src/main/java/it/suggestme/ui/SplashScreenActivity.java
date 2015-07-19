package it.suggestme.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import it.suggestme.R;
import it.suggestme.controller.Helpers;

public class SplashScreenActivity extends Activity {

    private static final int SPLASH_TIMER = 500;

    private Helpers helpers = Helpers.shared();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(helpers.setDataUser()) {
                    SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, SceltaCategorie.class));
                } else {
                    SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, TutorialActivity.class));
                }
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_TIMER);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
