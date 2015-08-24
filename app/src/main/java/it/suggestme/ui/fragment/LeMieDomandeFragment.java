package it.suggestme.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

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
    private ArrayList<Question> mQuestion;
    private ArrayList<ReplyListItem> mReplied;

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

        View rootView = inflater.inflate(R.layout.fragment_le_mie_domande, container, false);
        mQuestionList = (ListView) rootView.findViewById(R.id.question_list);

        mQuestion = Helpers.shared().getQuestions();
        mReplied  = new ArrayList<>();

        Boolean replied;
        String categoryName;
        String subCategoryName;

        final JSONArray unrepliedQuestion = new JSONArray();

        for( Question aQuest : mQuestion ) {

            replied = aQuest.getSuggest() != null;

            if( !replied ) {
                Log.i(Helpers.getString(R.string.loginfo), "Question: ".concat(((Integer) aQuest.getId()).toString()).concat(" REPLIED: ").concat(replied.toString()));

                unrepliedQuestion.put(aQuest.getId());
            }
            categoryName = Helpers.shared().getCategoryFromID(aQuest.getQuestionData().getCatId()).getName();
            subCategoryName = Helpers.shared().getSubcategoryFromID(
                    aQuest.getQuestionData().getCatId(),
                    aQuest.getQuestionData().getSubCatId() )
                    .getName();

            mReplied.add(new ReplyListItem(
                    aQuest,
                    categoryName,
                    subCategoryName,
                    replied));
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


        if (Helpers.shared().getQuestions() != null && Helpers.shared().getQuestions().size() > 0) {
            Helpers.shared().communicationHandler.getSuggestsRequest(unrepliedQuestion, new CommunicationHandler.RequestCallback() {
                @Override
                public void callback(Boolean success) {

                    if( !success )
                        return;

                    mQuestion = Helpers.shared().getQuestions();

                    for( int i = 0; i < unrepliedQuestion.length(); i++ ){

                        for( ReplyListItem rli : mReplied ) {
                            if( rli.getHasBeenReplied() )
                                continue;

                            try {
                                if( rli.getQuestion().getId() == (int)unrepliedQuestion.get(i) ){
                                    rli.updateReplied();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
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