package it.suggestme.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.facebook.appevents.AppEventsLogger;

import it.suggestme.R;
import it.suggestme.controller.Helpers;
import it.suggestme.ui.adapter.ScreenSlidePagerAdapter;
import it.suggestme.ui.tutorialpages.ZoomOutPageTransformer;

public class TutorialActivity extends FragmentActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Helpers.setAppContext(this);
        Helpers.shared().initFBSdk();
        Helpers.shared().initFabric();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.palette_black));
        }

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Helpers.shared().getTwitterClient().onActivityResult(requestCode, resultCode, data);
    }

}
