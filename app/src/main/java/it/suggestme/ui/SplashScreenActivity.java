package it.suggestme.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import it.suggestme.R;
import it.suggestme.controller.Helpers;

public class SplashScreenActivity extends Activity {

    private static final int SPLASH_TIMER = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Helpers.shared().setCtx(this);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Helpers.shared().setDataUser()) {
                    SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, SceltaCategorie.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, TutorialActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        }, SPLASH_TIMER);
    }
}
