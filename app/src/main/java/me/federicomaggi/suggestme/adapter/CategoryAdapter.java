package me.federicomaggi.suggestme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.federicomaggi.suggestme.R;
import me.federicomaggi.suggestme.model.Category;
import me.federicomaggi.suggestme.model.SubCategory;

/* ////////////////
///////////////////
///// UNUSED //////
///////////////////
//////////////// */


/**
 * Created by federicomaggi on 20/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class CategoryAdapter extends ArrayAdapter<Category> {

    public CategoryAdapter(Context context, int resource, List<Category> items) {
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

        Category theCat = getItem(position);

        return theView;
    }



}
