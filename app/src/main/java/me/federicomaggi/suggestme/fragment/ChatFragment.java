package me.federicomaggi.suggestme.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import me.federicomaggi.suggestme.R;
import me.federicomaggi.suggestme.model.Category;
import me.federicomaggi.suggestme.model.Question;
import me.federicomaggi.suggestme.model.SubCategory;

/**
 * Created by federicomaggi on 20/05/15.
 */
public class ChatFragment extends Fragment {

    public static final String SOCIAL = "social";
    public static final String GOODS  = "goods";

    private static final String CATEGORY = "category_bundle_arg";

    private String category;

    private OnFragmentInteractionListener mListener;

    private ArrayList<Category> mCategoryList;

    private EditText    mQuestionEditor;
    private Spinner     mSpinner;
    private ImageButton mAnonButton;
    private TextView    mQuestionText;
    private TextView    mSuggestText;

    private String  questionBody;
    private int     categoryId;
    private int     subcategoryId;
    private Boolean anonflag;

    /**
     * Factory method to create a new ChatFragment
     * @param category The category to set proper background and show subcategories.
     * @return A new instance of fragment ChatFragment.
     */
    public static ChatFragment newInstance(String category) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.category = getArguments().getString(CATEGORY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        // Retrieve elements
        mSpinner        = (Spinner)     rootView.findViewById(R.id.subcategoy_spinner);
        mQuestionEditor = (EditText)    rootView.findViewById(R.id.input_question);
        mAnonButton     = (ImageButton) rootView.findViewById(R.id.anon_img);
        mQuestionText   = (TextView)    rootView.findViewById(R.id.question_cloud);

        // Initialise Anonymous flag
        setAnonFlag(true);

        // Set Background image
        if( this.category.equals(SOCIAL)  )
            rootView.setBackground(getResources().getDrawable(R.drawable.form_social_background));
        else
            rootView.setBackground(getResources().getDrawable(R.drawable.form_goods_background));

        // Download categories and subcategories
        try {
            mCategoryList = Category.getCategories();

            if( mCategoryList != null )
                setSpinnerValues(mCategoryList);
            else
                throw new IOException();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(rootView.getContext(),getString(R.string.genericerror),Toast.LENGTH_LONG).show();

        } catch (IOException e ) {
            Toast.makeText(rootView.getContext(),getString(R.string.nocategories),Toast.LENGTH_LONG).show();
        }


        // Listener for AnonButton
        mAnonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAnonFlag(!anonflag);
            }
        });


        // Listener for SendButton
        mQuestionEditor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mQuestionEditor.getRight() - mQuestionEditor.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        sendQuestion();
                        return true;
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    /**
     * Creates the ArrayAdapter to set Spinner values.
     * It retrieves the SubCategories matching with chosen Category
     *
     * @param categorylist List of categories downloaded from server
     */
    private void setSpinnerValues( ArrayList<Category> categorylist ) {

        ArrayList<SubCategory> subcats = new ArrayList<>();
        ArrayList<String> subcatsnames = new ArrayList<>();


        for( Category cat : categorylist ) {
            if( cat.getName().equals(this.category) ) {
                subcats = cat.getSubcategories();
                this.categoryId = cat.getId();
            }
        }

        for( SubCategory sub : subcats ) {
            subcatsnames.add(sub.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, subcatsnames );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    /**
     * Fired when the Send button is pressed
     * Retrieve all data and create a Question object
     * Commits the question to the server and store it in local SQLite DB
     */
    private void sendQuestion() {

        this.questionBody  = mQuestionEditor.getText().toString();

        if( this.questionBody.equals("") )
            return;

        this.subcategoryId = retrieveSelectedSubcategoryID();

        Question theQuestion = new Question(this.questionBody,
                                            this.categoryId,
                                            this.subcategoryId,
                                            this.anonflag);

        Log.d("ChatFragment", "Question data: " + theQuestion.toJSONString());

        try {

            theQuestion.commitQuestionToServer();
            mQuestionText.setVisibility(View.VISIBLE);
            mQuestionText.setText(this.questionBody);

            mQuestionEditor.setText("");
            mQuestionEditor.clearFocus();

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mQuestionEditor.getWindowToken(), 0);

            mQuestionEditor.setVisibility(View.INVISIBLE);

        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),getString(R.string.senderror),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Retrieves the SubCategory ID from the Spinner selected item
     *
     * @return the ID. In case the ID is not found returns -1
     */
    private int retrieveSelectedSubcategoryID() {

        for( Category cat : mCategoryList ) {
            if( cat.getName().equals(this.category) ) {
                for( SubCategory sub : cat.getSubcategories() ) {
                    if( sub.getName().equals(mSpinner.getSelectedItem().toString()) )
                        return sub.getId();
                }
            }
        }
        return -1;
    }

    /**
     * Sets local anonflag tu the value provided.
     * Also changes the ImageButton background
     *
     * @param set value to use as new anonflag
     */
    private void setAnonFlag( Boolean set ) {
        anonflag = set;
        if( !anonflag )
            mAnonButton.setBackground(getResources().getDrawable(R.drawable.ic_logged));
        else
            mAnonButton.setBackground(getResources().getDrawable(R.drawable.ic_anonymous));
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