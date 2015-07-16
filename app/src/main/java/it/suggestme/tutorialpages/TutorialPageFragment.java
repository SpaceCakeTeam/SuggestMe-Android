package it.suggestme.tutorialpages;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import it.suggestme.R;
import it.suggestme.SceltaCategorie;

<<<<<<< HEAD:app/src/main/java/me/federicomaggi/suggestme/tutorialpages/TutorialPageFragment.java
/**
 * Created by federicomaggi on 20/05/15.
 * © 2015 Federico Maggi. All rights reserved
 */
=======
>>>>>>> a35368c8c80cbc0d1bd4cf812b8f7ef61f4342e7:app/src/main/java/it/suggestme/tutorialpages/TutorialPageFragment.java
public class TutorialPageFragment extends Fragment {

    private int mTutorialPage = 0;

    private final int WELCOMEPAGE = 0;
    private final int ANONPAGE    = 1;
    private final int CHATPAGE    = 2;
    private final int WAITPAGE    = 3;
    private final int LOGINPAGE   = 4;

    public TutorialPageFragment setTutorialPage(int position) {
        this.mTutorialPage = position;
        return new TutorialPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutToLoad = R.layout.fragment_tutorial__page_1;
        switch (mTutorialPage){
            case WELCOMEPAGE:
                layoutToLoad = R.layout.fragment_tutorial__page_1;
                break;
            case ANONPAGE:
                layoutToLoad = R.layout.fragment_tutorial__page_2;
                break;
            case CHATPAGE:
                layoutToLoad = R.layout.fragment_tutorial__page_3;
                break;
            case WAITPAGE:
                layoutToLoad = R.layout.fragment_tutorial__page_4;
                break;
            case LOGINPAGE:
                layoutToLoad = R.layout.fragment_tutorial__page_5;
                break;
            default:
                Log.e(getResources().getString(R.string.tutorial_err_lbl), getResources().getString(R.string.tutorial_err_pagenotfound));
                break;
        }

        View rootView = inflater.inflate(layoutToLoad, container, false);

        if(mTutorialPage == LOGINPAGE){

            rootView.findViewById(R.id.nonora_imgbtn).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ImageButton view;
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            view = (ImageButton) v;
                            view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                            v.invalidate();
                            return true;
                        case MotionEvent.ACTION_UP:
                            Intent i = new Intent(getActivity(), SceltaCategorie.class);
                            startActivity(i);
                            view = (ImageButton) v;
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            return true;
                        default:
                            return false;
                    }
                }
            });

        }
        return rootView;
    }
}
