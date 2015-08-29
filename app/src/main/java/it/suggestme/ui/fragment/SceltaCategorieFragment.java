package it.suggestme.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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


        mSocialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, ChatFragment.newInstance(ChatFragment.SOCIAL)).commit();
            }
        });

        mGoodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, ChatFragment.newInstance(ChatFragment.GOODS)).commit();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
