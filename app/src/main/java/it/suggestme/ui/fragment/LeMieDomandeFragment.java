package it.suggestme.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;

import it.suggestme.R;
import it.suggestme.controller.CommunicationHandler;
import it.suggestme.controller.Helpers;
import it.suggestme.model.Question;
import it.suggestme.model.ReplyListItem;
import it.suggestme.ui.adapter.ReplyListAdapter;

public class LeMieDomandeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView mQuestionList;

    public static LeMieDomandeFragment newInstance() {
        return new LeMieDomandeFragment();
    }

    public LeMieDomandeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Helpers.shared().setDataUser();

        if (Helpers.shared().getQuestions() != null && Helpers.shared().getQuestions().size() > 0) {
            Helpers.shared().communicationHandler.getSuggestsRequest(new JSONArray(), new CommunicationHandler.RequestCallback() {
                @Override
                public void callback(Boolean success) {

                }
            });
        }

        View rootView = inflater.inflate(R.layout.fragment_le_mie_domande, container, false);
        mQuestionList = (ListView) rootView.findViewById(R.id.question_list);

        ArrayList<Question>      mQuestion = Helpers.shared().getQuestions();
        ArrayList<ReplyListItem> mReplied  = new ArrayList<>();

        Boolean replied;
        String categoryName;
        String subCategoryName;

        JSONArray unrepliedQuestion = new JSONArray();

        for( Question aQuest : mQuestion ) {

            replied = aQuest.getSuggest() != null;

            if( !replied )
                unrepliedQuestion.put(aQuest.getId());

            categoryName = Helpers.shared().getCategoryFromID(aQuest.getQuestionData().getCatId()).getName();
            subCategoryName = Helpers.shared().getSubcategoryFromID(
                    aQuest.getQuestionData().getCatId(),
                    aQuest.getQuestionData().getSubCatId() )
                    .getName();

            mReplied.add(new ReplyListItem(
                    aQuest,
                    aQuest.getSuggest()!=null   ?
                            aQuest.getSuggest() :
                            null,
                    categoryName,
                    subCategoryName,
                    replied ));
        }

        mQuestionList.setAdapter(new ReplyListAdapter(
                getActivity().getApplicationContext(),
                R.layout.le_mie_domande_list_item,
                mReplied ));

        mQuestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ReplyListItem rplitem = (ReplyListItem)mQuestionList.getItemAtPosition(i);

                ChatFragment thefragment = ChatFragment.newInstance(rplitem.getCategoryName());
                thefragment.setQuestionToShow(rplitem.getQuestion());

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, thefragment)
                        .commit();
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}