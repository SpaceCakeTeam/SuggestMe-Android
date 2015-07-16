package me.federicomaggi.suggestme.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.federicomaggi.suggestme.R;
import me.federicomaggi.suggestme.fragment.ChatFragment;
import me.federicomaggi.suggestme.model.ReplyListItem;

/**
 * Created by federicomaggi on 20/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class ReplyListAdapter extends ArrayAdapter<ReplyListItem> {

    public ReplyListAdapter(Context context, int resource, ReplyListItem[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {

        View theView = convertView;

        if( theView == null ) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            theView = li.inflate(R.layout.le_mie_domande_list_item, null);
        }

        final ReplyListItem item = getItem(position);

        if( item != null ) {

            final ImageView caticon = (ImageView) theView.findViewById(R.id.list_row_category_image);
            final TextView  catname = (TextView)  theView.findViewById(R.id.list_row_title);
            final ImageView replied = (ImageView) theView.findViewById(R.id.list_row_replied);


            if( item.getCategoryName().toLowerCase().equals(ChatFragment.SOCIAL) )
                caticon.setBackground(getContext().getResources().getDrawable(R.drawable.ic_questionlist_social));
            else
                caticon.setBackground(getContext().getResources().getDrawable(R.drawable.ic_questionlist_goods));

            catname.setText(item.getSubcategoryName());

            if( item.getHasBeenReplied() )
                replied.setBackground(getContext().getResources().getDrawable(R.drawable.ic_questionlist_replied));
            else
                replied.setBackground(getContext().getResources().getDrawable(R.drawable.ic_questionlist_pending_suggest));
        }

        return theView;
    }
}
