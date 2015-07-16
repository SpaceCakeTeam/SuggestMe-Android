package me.federicomaggi.suggestme.fragment;

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

import org.json.JSONException;

import java.util.ArrayList;

import me.federicomaggi.suggestme.R;
import me.federicomaggi.suggestme.adapter.ReplyListAdapter;
import me.federicomaggi.suggestme.model.Category;
import me.federicomaggi.suggestme.model.Question;
import me.federicomaggi.suggestme.model.ReplyListItem;
import me.federicomaggi.suggestme.model.SubCategory;
import me.federicomaggi.suggestme.model.Suggest;

/**
 * Created by federicomaggi on 20/05/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class LeMieDomandeFragment extends Fragment{

    private OnFragmentInteractionListener mListener;
    private ListView mQuestionList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LeMieDomandeFragment.
     */
    public static LeMieDomandeFragment newInstance() {
        return new LeMieDomandeFragment();
    }

    public LeMieDomandeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_le_mie_domande, container, false);

        mQuestionList = (ListView) rootView.findViewById(R.id.question_list);


        // Retrieve questions from Database
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
            Toast.makeText(getActivity(),getString(R.string.genericerror),Toast.LENGTH_LONG).show();
        }

        mQuestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ReplyListItem item = (ReplyListItem) mQuestionList.getItemAtPosition(i);

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
