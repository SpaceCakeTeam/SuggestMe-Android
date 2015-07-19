package it.suggestme.ui.tutorialpages;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import it.suggestme.R;
import it.suggestme.controller.CommunicationHandler;
import it.suggestme.controller.Helpers;
import it.suggestme.ui.SceltaCategorie;

public class TutorialPageFragment extends Fragment {

    private Helpers helpers = Helpers.shared();

    private int mTutorialPage = 0;

    private final int WELCOMEPAGE = 0;
    private final int ANONPAGE = 1;
    private final int CHATPAGE = 2;
    private final int WAITPAGE = 3;
    private final int LOGINPAGE = 4;

    public TutorialPageFragment setTutorialPage(int position) {
        this.mTutorialPage = position;
        return new TutorialPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutToLoad = R.layout.fragment_tutorial__page_1;
        switch (mTutorialPage) {
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
                break;
        }

        View rootView = inflater.inflate(layoutToLoad, container, false);

        if(mTutorialPage == LOGINPAGE) {
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
                            login();
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

    private void login() {
        helpers.communicationHandler.registrationRequest(new CommunicationHandler.RequestCallback() {
            @Override
            public void callback(Boolean success) {
                if (success) {
                    startActivity(new Intent(getActivity(), SceltaCategorie.class));
                }
            }
        });
    }
}
