package it.suggestme.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import it.suggestme.R;
import it.suggestme.controller.Helpers;
import it.suggestme.controller.interfaces.RequestCallback;
import it.suggestme.controller.interfaces.HelperCallback;
import it.suggestme.ui.SceltaCategorie;

public class TutorialPageFragment extends Fragment {

    private int mTutorialPage = 0;

    private final int WELCOMEPAGE = 0;
    private final int ANONPAGE = 1;
    private final int CHATPAGE = 2;
    private final int WAITPAGE = 3;
    private final int LOGINPAGE = 4;

    private boolean firstTouchFired = false;

    public TutorialPageFragment setTutorialPage(int position) {
        this.mTutorialPage = position;
        return new TutorialPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        int layoutToLoad = R.layout.fragment_tutorial_page_1;
        switch (mTutorialPage) {
            case WELCOMEPAGE:
                layoutToLoad = R.layout.fragment_tutorial_page_1;
                break;
            case ANONPAGE:
                layoutToLoad = R.layout.fragment_tutorial_page_2;
                break;
            case CHATPAGE:
                layoutToLoad = R.layout.fragment_tutorial_page_3;
                break;
            case WAITPAGE:
                layoutToLoad = R.layout.fragment_tutorial_page_4;
                break;
            case LOGINPAGE:
                layoutToLoad = R.layout.fragment_tutorial_page_5;
                break;
            default:
                break;
        }

        View rootView = inflater.inflate(layoutToLoad, container, false);

        if(mTutorialPage == LOGINPAGE) {

            rootView.findViewById(R.id.login_social_tutorial_button).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getX() / v.getWidth() < event.getY() / v.getHeight()) {
                        if (firstTouchFired)
                            return false;
                        firstTouchFired = true;
                        Log.i(Helpers.getString(R.string.loginfo), "TWITTER");

                        Helpers.shared().performTwitterLogin(TutorialPageFragment.this, new HelperCallback() {
                            @Override
                            public void callback(Boolean success) {
                                firstTouchFired = false;
                                if (!success) {
                                    return;
                                }
                                login(Helpers.shared().getAppUser().parse());
                            }
                        });
                        return true;
                    } else {
                        if (firstTouchFired)
                            return false;
                        firstTouchFired = true;
                        Log.i(Helpers.getString(R.string.loginfo), "FACEBOOK");

                        Helpers.shared().performFacebookLogin(TutorialPageFragment.this, new HelperCallback() {
                            @Override
                            public void callback(Boolean success) {
                                firstTouchFired = false;
                                if (!success)
                                    return;

                                login(Helpers.shared().getAppUser().parse());
                            }
                        });
                        return true;
                    }
                }
            });

            rootView.findViewById(R.id.login_nonora_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helpers.shared().setLoggedWith(Helpers.ANON);
                    login(new JSONObject());
                }
            });


        }
        return rootView;
    }

    private void login( JSONObject userData ) {
        Helpers.shared().communicationHandler.registrationRequest(new RequestCallback() {
            @Override
            public void callback(Boolean success) {
                if (success) {
                    startActivity(new Intent(getActivity(), SceltaCategorie.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }
        }, userData);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Helpers.shared().getFBCallbackMng().onActivityResult(requestCode, resultCode, data);
    }

}
