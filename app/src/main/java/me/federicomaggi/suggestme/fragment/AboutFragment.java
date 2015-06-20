package me.federicomaggi.suggestme.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.federicomaggi.suggestme.R;

/**
 * Created by federicomaggi on 20/06/15.
 */
public class AboutFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
        ImageView imageBG = (ImageView) container.findViewById(R.id.aboutBackgroundImageView);
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.about_background);
        Matrix matrix = new Matrix();
        matrix.postScale(container.getWidth()/2, container.getHeight()/2);
        imageBG.setImageBitmap(Bitmap.createBitmap(icon, 0,0,icon.getWidth(), icon.getHeight(), matrix, false));
        */
        return inflater.inflate(R.layout.fragment_about, container, false);
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
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
