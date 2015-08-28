package it.suggestme.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;

import it.suggestme.R;
import it.suggestme.controller.Helpers;
import it.suggestme.controller.services.RegistrationIntentService;
import it.suggestme.ui.fragment.AboutFragment;
import it.suggestme.ui.fragment.ChatFragment;
import it.suggestme.ui.fragment.LeMieDomandeFragment;
import it.suggestme.ui.fragment.LoginFragment;
import it.suggestme.ui.fragment.NavigationDrawerFragment;
import it.suggestme.ui.fragment.SceltaCategorieFragment;

/**
 * Created by federicomaggi on 27/08/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class SceltaCategorie extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
            LeMieDomandeFragment.OnFragmentInteractionListener,
            LoginFragment.OnFragmentInteractionListener,
            AboutFragment.OnFragmentInteractionListener,
            ChatFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scelta_categorie);

        Helpers.setAppContext(this);
        Helpers.shared().initFBSdk();
        Helpers.shared().initFabric();
        Helpers.shared().setDataUser();

        if( Helpers.shared().testGooglePlayServices(this) ) {

            Helpers.shared().setBroadcastReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (Helpers.shared().getSavedInt(Helpers.INSTANCEIDSAVEDLBL) == 1) {
                        Log.i(Helpers.getString(R.string.loginfo), "Twitter InstanceID saved");
                    } else {
                        Log.i(Helpers.getString(R.string.loginfo), "Twitter InstanceID NOT saved");
                    }
                }
            });

            if( Helpers.shared().getSavedInt(Helpers.INSTANCEIDSAVEDLBL) == 1 )
                startService(new Intent(this, RegistrationIntentService.class));

        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.palette_white_transparent));
        }

        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        if( getIntent().getBooleanExtra(Helpers.FROMNOTIFICATION,false) )
            getSupportFragmentManager().beginTransaction().replace(R.id.container, LeMieDomandeFragment.newInstance()).commit();
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.container, SceltaCategorieFragment.newInstance()).commit();

        restoreActionBar();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, LeMieDomandeFragment.newInstance())
                        .commit();
                break;
            case 1:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, SceltaCategorieFragment.newInstance())
                        .commit();
                break;
            case 2:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, LoginFragment.newInstance())
                        .commit();
                break;
            case 3:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, AboutFragment.newInstance())
                        .commit();
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if(actionBar == null)
            return;

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        ImageView mImageV = new ImageView(actionBar.getThemedContext());
        mImageV.setScaleType(ImageView.ScaleType.CENTER);
        mImageV.setImageResource(R.drawable.navbar_logo);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT,Gravity.END | Gravity.CENTER_VERTICAL);
        layoutParams.topMargin = 10;
        layoutParams.rightMargin = 10;

        mImageV.setLayoutParams(layoutParams);
        actionBar.setCustomView(mImageV);
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(
                        Helpers.shared().getBroadcastReceiver(),
                        new IntentFilter(Helpers.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(Helpers.shared().getBroadcastReceiver());

        AppEventsLogger.deactivateApp(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, SceltaCategorieFragment.newInstance()).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Helpers.shared().getTwitterClient().onActivityResult(requestCode, resultCode, data);
    }

}
