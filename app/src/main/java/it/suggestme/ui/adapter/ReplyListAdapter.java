package it.suggestme.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import it.suggestme.R;
import it.suggestme.controller.Helpers;
import it.suggestme.model.Category;
import it.suggestme.model.Question;
import it.suggestme.model.ReplyListItem;
import it.suggestme.ui.fragment.ChatFragment;

public class ReplyListAdapter extends ArrayAdapter<ReplyListItem> {

    public ReplyListAdapter(Context context, int resource, ArrayList<ReplyListItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {

        View theView = convertView;

        if(theView == null) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            theView = li.inflate(R.layout.le_mie_domande_list_item, null);
        }

        final Question item = getItem(position).getQuestion();

        if(item != null) {

            final ImageView caticon  = (ImageView) theView.findViewById(R.id.list_row_category_image);
            final TextView  sCatName = (TextView)  theView.findViewById(R.id.list_row_title);
            final ImageView replied  = (ImageView) theView.findViewById(R.id.list_row_replied);


            if( Helpers.shared().getCategoryFromID(item.getQuestionData().getCatId()).getName().toLowerCase().equals(ChatFragment.SOCIAL) )
                caticon.setBackground(getContext().getResources().getDrawable(R.drawable.ic_questionlist_social));
            else
                caticon.setBackground(getContext().getResources().getDrawable(R.drawable.ic_questionlist_goods));

            sCatName.setText(
                    Helpers.shared().getSubcategoryFromID(
                            item.getQuestionData().getCatId(),
                            item.getQuestionData().getSubCatId())
                            .getName());

            if(item.getSuggest()!=null)
                replied.setBackground(getContext().getResources().getDrawable(R.drawable.ic_questionlist_replied));
            else
                replied.setBackground(getContext().getResources().getDrawable(R.drawable.ic_questionlist_pending_suggest));
        }

        return theView;
    }
}
