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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import it.suggestme.R;
import it.suggestme.controller.CommunicationHandler;
import it.suggestme.controller.Helpers;
import it.suggestme.model.Category;
import it.suggestme.model.Question;
import it.suggestme.model.SubCategory;
import it.suggestme.model.Suggest;
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
        if (Helpers.shared().getQuestions().size() > 0) {
            Helpers.shared().communicationHandler.getSuggestsRequest(new JSONArray(), new CommunicationHandler.RequestCallback() {
                @Override
                public void callback(Boolean success) {

                }
            });
        }
        View rootView = inflater.inflate(R.layout.fragment_le_mie_domande, container, false);
        mQuestionList = (ListView) rootView.findViewById(R.id.question_list);

        ArrayList<Question>      mQuestion = Question.retrieveMyQuestions();
        ArrayList<ReplyListItem> mReplied  = new ArrayList<>();

        try {
            ArrayList<Suggest>  remoteSuggests = Suggest.getSuggestsFromServer();
            ArrayList<Category> categories = Category.getCategories();

            Boolean replied;
            String categoryName;
            String subCategoryName;
            int suggestid;

            for( Question aQuest : mQuestion ) {

                replied = false;
                suggestid = -1;

                categoryName = Category.getCategoryFromID(aQuest.getCategory(), categories);
                subCategoryName = SubCategory.getSubCategoryNameFromID(aQuest.getSubcategoryid(),
                        Category.getSubCategoryFromID(aQuest.getCategory(), categories));

                assert remoteSuggests != null;
                for( Suggest aSuggest : remoteSuggests ) {

                    if ( aSuggest.getQuestionID() == aQuest.getID() ) {
                        replied = true;
                        suggestid = aSuggest.getId();
                    }
                }

                mReplied.add(new ReplyListItem( aQuest.getID(), suggestid, categoryName, subCategoryName, replied ));
            }

            mQuestionList.setAdapter(new ReplyListAdapter(
                    getActivity().getApplicationContext(),
                    R.layout.le_mie_domande_list_item,
                    mReplied.toArray(new ReplyListItem[mReplied.size()])
            ));

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),getString(R.string.genericerror), Toast.LENGTH_LONG).show();
        }

        mQuestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Question item = mQuestionList.getItemAtPosition(i);

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, ChatFragment.newInstance(item.getCategoryName(),item.getQuestionID(),item.getSuggestID(),false))
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