package it.suggestme.ui.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.suggestme.R;
import it.suggestme.controller.Helpers;

public class AboutFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    public AboutFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        ((TextView) rootView.findViewById(R.id.about_textview)).setTypeface(Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), Helpers.getString(R.string.fontpath) + Helpers.getString(R.string.fontname)));

        return rootView;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}