package it.suggestme.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import it.suggestme.R;
import it.suggestme.controller.interfaces.RequestCallback;
import it.suggestme.controller.interfaces.HelperCallback;
import it.suggestme.controller.Helpers;

public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private boolean firstTouchFired = false;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        if( Helpers.shared().getAppUser().getUserData().getName() != null &&
                !Helpers.shared().getAppUser().getUserData().getName().equals("") &&
                Helpers.shared().getAppUser().getUserData().getName().length() > 0 ) {
            rootView.findViewById(R.id.login_social_main_button).setVisibility(View.GONE);

            rootView.findViewById(R.id.login_done).setVisibility(View.VISIBLE);
        }

        rootView.findViewById(R.id.login_social_main_button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if( event.getX() / v.getWidth() < event.getY() / v.getHeight() ){
                    if( firstTouchFired )
                        return false;
                    firstTouchFired = true;

                    Log.i(Helpers.getString(R.string.loginfo), "TWITTER");
                    Helpers.shared().performTwitterLogin(LoginFragment.this, new HelperCallback() {
                        @Override
                        public void callback(Boolean success) {
                            firstTouchFired = false;

                            if (!success) {
                                Helpers.showAlert(-13);
                                return;
                            }

                            Helpers.shared().communicationHandler.registrationRequest(new RequestCallback() {

                                @Override
                                public void callback(Boolean success) {
                                    if (success) {
                                        rootView.findViewById(R.id.login_social_main_button).setVisibility(View.GONE);
                                        rootView.findViewById(R.id.login_done).setVisibility(View.VISIBLE);

                                        Helpers.shared().getNavigationDrawer().updateProfilePicFromTwitter();
                                    }
                                }
                            }, Helpers.shared().getAppUser().parse());
                        }
                    });
                    return true;
                } else {
                    if( firstTouchFired )
                        return false;
                    firstTouchFired = true;

                    Log.i(Helpers.getString(R.string.loginfo),"FACEBOOK");
                    Helpers.shared().performFacebookLogin(LoginFragment.this, new HelperCallback() {
                        @Override
                        public void callback(Boolean success) {
                            firstTouchFired = false;

                            if (!success) {
                                Helpers.showAlert(-12);
                                return;
                            }

                            Helpers.shared().communicationHandler.registrationRequest(new RequestCallback() {
                                @Override
                                public void callback(Boolean success) {
                                    if (success) {
                                        rootView.findViewById(R.id.login_social_main_button).setVisibility(View.GONE);
                                        rootView.findViewById(R.id.login_done).setVisibility(View.VISIBLE);

                                        Helpers.shared().getNavigationDrawer().updateProfilePicFromFacebook();
                                    }
                                }
                            }, Helpers.shared().getAppUser().parse());
                        }
                    });
                    return true;
                }
            }
        });
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Helpers.shared().getFBCallbackMng().onActivityResult(requestCode, resultCode, data);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
