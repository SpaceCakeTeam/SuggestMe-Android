package it.suggestme.ui;

import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import it.suggestme.R;
import it.suggestme.ui.fragment.AboutFragment;
import it.suggestme.ui.fragment.ChatFragment;
import it.suggestme.ui.fragment.LeMieDomandeFragment;
import it.suggestme.ui.fragment.LoginFragment;
import it.suggestme.ui.fragment.NavigationDrawerFragment;
import it.suggestme.ui.fragment.SceltaCategorieFragment;
import it.suggestme.model.User;
import it.suggestme.util.PreferencesManager;

public class SceltaCategorie extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
            LeMieDomandeFragment.OnFragmentInteractionListener,
            LoginFragment.OnFragmentInteractionListener,
            AboutFragment.OnFragmentInteractionListener,
            ChatFragment.OnFragmentInteractionListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scelta_categorie);

        PreferencesManager.getPreferencesManagerInstance().setContext(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.palette_white_transparent));
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        getSupportFragmentManager().beginTransaction().replace(R.id.container, SceltaCategorieFragment.newInstance()).commit();
        User.getUserInstance();
        restoreActionBar();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, LeMieDomandeFragment.newInstance())
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, SceltaCategorieFragment.newInstance())
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, LoginFragment.newInstance())
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
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
    public void onBackPressed(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, SceltaCategorieFragment.newInstance()).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
