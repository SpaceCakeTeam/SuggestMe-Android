package me.federicomaggi.suggestme;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import me.federicomaggi.suggestme.fragment.AboutFragment;
import me.federicomaggi.suggestme.fragment.ChatFragment;
import me.federicomaggi.suggestme.fragment.LeMieDomandeFragment;
import me.federicomaggi.suggestme.fragment.LoginFragment;
import me.federicomaggi.suggestme.fragment.NavigationDrawerFragment;
import me.federicomaggi.suggestme.model.User;
import me.federicomaggi.suggestme.util.PreferencesManager;


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

        // White status bar for Lollipop
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.palette_white));
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SceltaCategorieFragment.newInstance())
                .commit();

        User.getUserInstance();


        restoreActionBar();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position)
        {
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
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        ImageView mImageV = new ImageView(actionBar.getThemedContext());
        mImageV.setScaleType(ImageView.ScaleType.CENTER);
        mImageV.setImageResource(R.drawable.navbar_logo);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.END | Gravity.CENTER_VERTICAL
        );
        layoutParams.rightMargin = 10;

        mImageV.setLayoutParams(layoutParams);
        actionBar.setCustomView(mImageV);
    }

    @Override
    public void onBackPressed(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SceltaCategorieFragment.newInstance())
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SceltaCategorieFragment extends Fragment {

        public static SceltaCategorieFragment newInstance() {

            return new SceltaCategorieFragment();
        }

        public SceltaCategorieFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_scelta_categorie, container, false);

            ImageButton mSocialButton = (ImageButton) rootView.findViewById(R.id.social_imgbtn);
            ImageButton mGoodsButton  = (ImageButton) rootView.findViewById(R.id.goods_imgbn);


            mSocialButton.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event){

                    ImageButton view;

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            view = (ImageButton) v;
                            view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                            v.invalidate();
                            return true;

                        case MotionEvent.ACTION_UP:

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, ChatFragment.newInstance(ChatFragment.SOCIAL))
                                    .commit();

                            view = (ImageButton) v;
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            return true;

                        default:
                            return false;
                    }
                }
            });

            mGoodsButton.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event){

                    ImageButton view;

                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            view = (ImageButton) v;
                            view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                            v.invalidate();
                            return true;

                        case MotionEvent.ACTION_UP:
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, ChatFragment.newInstance(ChatFragment.GOODS))
                                    .commit();

                            view = (ImageButton) v;
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            return true;

                        default:
                            return false;
                    }
                }

            });




            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

        }
    }

}
