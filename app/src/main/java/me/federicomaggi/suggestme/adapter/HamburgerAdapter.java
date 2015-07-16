package me.federicomaggi.suggestme.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.federicomaggi.suggestme.R;
import me.federicomaggi.suggestme.model.HamburgerItem;

/**
 * Created by federicomaggi on 17/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class HamburgerAdapter extends ArrayAdapter<HamburgerItem> {

    public HamburgerAdapter(Context context, int resource, List<HamburgerItem> items) {
        super(context, resource, items);
    }


    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {

        View theView = convertView;

        if( theView == null ) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            theView = li.inflate(R.layout.list_selected_row, null);
        }

        HamburgerItem item= getItem(position);

        if( item != null ) {

            ImageView imview = (ImageView) theView.findViewById(R.id.ham_element_icon);
            imview.setImageDrawable(item.getIcon());

            TextView hamtext = (TextView)  theView.findViewById(R.id.ham_element_text);
            hamtext.setText(item.getTitle());
        }

        return theView;
    }
}
