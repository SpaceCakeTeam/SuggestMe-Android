package it.suggestme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {

    private static final int SPLASH_TIMER = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.tutorial_shared_prefs), Context.MODE_PRIVATE);
                if(sharedPref.contains(getResources().getString(R.string.tutorial_shared_prefs)) && sharedPref.getInt(getResources().getString(R.string.tutorial_shared_prefs), 0 ) == 1) {
                    final Intent mainIntent = new Intent(SplashScreenActivity.this, SceltaCategorie.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                } else {
                    final Intent mainIntent = new Intent(SplashScreenActivity.this, TutorialActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
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
