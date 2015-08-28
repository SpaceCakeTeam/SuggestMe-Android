package it.suggestme.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.facebook.appevents.AppEventsLogger;

import it.suggestme.R;
import it.suggestme.controller.Helpers;

public class SplashScreenActivity extends Activity {


    private static final int SPLASH_TIMER = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Helpers.setAppContext(this);
        Helpers.shared();

        if( !Helpers.shared().testGooglePlayServices(this) ) {
            Log.e(Helpers.getString(R.string.logerror), "No valid Google Play Services APK found.");
            return;
        }

        Helpers.shared().initFBSdk();
        Helpers.shared().initFabric();
        Helpers.shared().getAppUser();

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


    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);

        if( !Helpers.shared().testGooglePlayServices(this) ) {
            Log.e(Helpers.getString(R.string.logerror), "No valid Google Play Services APK found.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Helpers.PLAY_SERVICES_RESOLUTION_REQUEST:
                if (resultCode == RESULT_CANCELED) {
                    Helpers.showAlert(-11);
                    finish();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
