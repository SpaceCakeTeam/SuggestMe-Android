package me.federicomaggi.suggestme.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import me.federicomaggi.suggestme.tutorialpages.TutorialPageFragment;

/**
 * Created by federicomaggi on 20/06/15.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 5;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        TutorialPageFragment mTutPage = new TutorialPageFragment();
        mTutPage.setTutorialPage(position);
        return mTutPage;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
