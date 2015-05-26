package me.federicomaggi.suggestme;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageButton;


public class SceltaCategorie extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
                   LeMieDomandeFragment.OnFragmentInteractionListener,
                   LoginFragment.OnFragmentInteractionListener,
                   AboutFragment.OnFragmentInteractionListener,
                   ChatFragment.OnFragmentInteractionListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scelta_categorie);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SceltaCategorieFragment.newInstance())
                .commit();
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

/*
    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.ham_lemiedomande);
                break;
            case 1:
                mTitle = getString(R.string.ham_categorie);
                break;
            case 2:
                mTitle = getString(R.string.ham_login);
                break;
            case 3:
                mTitle = getString(R.string.ham_about);
                break;
        }
    }
*/
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        BitmapDrawable background = new BitmapDrawable (BitmapFactory.decodeResource(getResources(),
                R.drawable.navbar_logo));

        //actionBar.setTitle(mTitle);
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar_logo));
        //actionBar.setBackgroundDrawable(background);

        actionBar.setIcon(R.drawable.navbar_logo);
    }

    @Override
    public void onBackPressed(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SceltaCategorieFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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


            mSocialButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, ChatFragment.newInstance(ChatFragment.SOCIAL))
                            .commit();
                }
            });

            mGoodsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, ChatFragment.newInstance(ChatFragment.GOODS))
                            .commit();
                }
            });

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            //((SceltaCategorie) activity).onSectionAttached(
            //       getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
