package it.suggestme.ui.fragment;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;


import java.util.ArrayList;

import it.suggestme.R;
import it.suggestme.controller.interfaces.RequestCallback;
import it.suggestme.controller.Helpers;
import it.suggestme.model.HamburgerItem;
import it.suggestme.ui.adapter.HamburgerAdapter;

public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mMainDrawerListView;
    private View mFragmentContainerView;
    private View mRootView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Helpers.shared().setNavigationDrawer(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Helpers.shared().initFBSdk();
        Helpers.shared().initFabric();

        mRootView = inflater.inflate(R.layout.fragment_navigation_drawer,container);

        if( Helpers.shared().getLoggedWith() == Helpers.FACEBOOK )
            updateProfilePicFromFacebook();

        if( Helpers.shared().getLoggedWith() == Helpers.TWITTER )
            updateProfilePicFromTwitter();

        mMainDrawerListView = (ListView) mRootView.findViewById(R.id.HamburgerList_main);
        mMainDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        ArrayList<HamburgerItem> mainList = new ArrayList<>();
        mainList.add(new HamburgerItem(getString(R.string.ham_lemiedomande),R.drawable.ic_ham_ask,getString(R.string.ham_icon_list_accessibility)));
        mainList.add(new HamburgerItem(getString(R.string.ham_categorie),R.drawable.ic_ham_list,getString(R.string.ham_icon_ask_accessibility)));
        mainList.add(new HamburgerItem(getString(R.string.ham_login),R.drawable.ic_ham_login,getString(R.string.ham_icon_login_accessibility)));
        mainList.add(new HamburgerItem(getString(R.string.ham_about),R.drawable.ic_ham_about,getString(R.string.ham_icon_about_accessibility)));

        mMainDrawerListView.setAdapter(new HamburgerAdapter(
                getActionBar().getThemedContext(),
                R.layout.item_hamburger,
                mainList
        ));
        mMainDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        RelativeLayout hamHead = (RelativeLayout) mRootView.findViewById(R.id.ham_head_relativeLayout);
        hamHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return mRootView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setDrawerShadow(R.drawable.br_drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                getActivity().supportInvalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setHomeAsUpIndicator (R.drawable.ic_hamburger);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mMainDrawerListView != null) {
            mMainDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    public void updateProfilePicFromFacebook(){

        if( Helpers.shared().getAppUser().getUserData().getName() != null &&
                !Helpers.shared().getAppUser().getUserData().getName().equals("") &&
                Helpers.shared().getAppUser().getUserData().getName().length() > 0 ) {

            TextView mUserNameTextView = (TextView) mRootView.findViewById(R.id.ham_head_userName);
            final ImageView mAvatar = (ImageView) mRootView.findViewById(R.id.ham_head_avatar);

            mUserNameTextView.setText(Helpers.shared().getAppUser().getUserData().getName() + " " + Helpers.shared().getAppUser().getUserData().getSurname());

            Helpers.shared().communicationHandler.getProfilePicture(Profile.getCurrentProfile().getProfilePictureUri(60, 60).toString(),
                    new RequestCallback() {
                        @Override
                        public void callback(Boolean success) {
                            if (!success)
                                return;

                            mAvatar.setImageDrawable(Helpers.shared().getProfilePic());
                        }
                    });
        }
    }

    public void updateProfilePicFromTwitter(){

        if( Helpers.shared().getAppUser().getUserData().getName() != null &&
                !Helpers.shared().getAppUser().getUserData().getName().equals("") &&
                Helpers.shared().getAppUser().getUserData().getName().length() > 0 ) {

            TextView mUserNameTextView = (TextView) mRootView.findViewById(R.id.ham_head_userName);
            final ImageView mAvatar = (ImageView) mRootView.findViewById(R.id.ham_head_avatar);

            mUserNameTextView.setText("@" + Helpers.shared().getAppUser().getUserData().getName());

            Twitter.getApiClient().getAccountService().verifyCredentials(true, false, new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    User user = result.data;

                    Helpers.shared().communicationHandler.getProfilePicture(user.profileImageUrl,
                        new RequestCallback() {
                            @Override
                            public void callback(Boolean success) {
                                if (!success)
                                    return;

                                mAvatar.setImageDrawable(Helpers.shared().getProfilePic());
                            }
                        });
                }

                @Override
                public void failure(TwitterException e) {
                    // TODO
                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }
}