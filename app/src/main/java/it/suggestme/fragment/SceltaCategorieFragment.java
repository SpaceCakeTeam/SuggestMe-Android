package it.suggestme.fragment;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import it.suggestme.R;

public class SceltaCategorieFragment extends Fragment {

    public static SceltaCategorieFragment newInstance() {
        return new SceltaCategorieFragment();
    }

    public SceltaCategorieFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scelta_categorie, container, false);

        ImageButton mSocialButton = (ImageButton) rootView.findViewById(R.id.social_imgbtn);
        ImageButton mGoodsButton  = (ImageButton) rootView.findViewById(R.id.goods_imgbn);

        mSocialButton.setOnTouchListener(new View.OnTouchListener() {
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
                        getFragmentManager().beginTransaction().replace(R.id.container, ChatFragment.newInstance(ChatFragment.SOCIAL)).commit();
                        view = (ImageButton) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        return true;
                    default:
                        return false;
                }
            }
        });

        mGoodsButton.setOnTouchListener(new View.OnTouchListener() {
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
                        getFragmentManager().beginTransaction().replace(R.id.container, ChatFragment.newInstance(ChatFragment.GOODS)).commit();
                        view = (ImageButton) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        return true;
                    default:
                        return false;
                }
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
