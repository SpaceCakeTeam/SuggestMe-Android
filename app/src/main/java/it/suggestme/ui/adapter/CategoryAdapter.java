package it.suggestme.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;

import it.suggestme.R;
import it.suggestme.model.Category;

public class CategoryAdapter extends ArrayAdapter<Category> {

    public CategoryAdapter(Context context, int resource, List<Category> items) {
        super(context, resource, items);
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        View theView = convertView;
        if(theView == null) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            theView = li.inflate(R.layout.list_selected_row, null);
        }
        Category theCat = getItem(position);
        return theView;
    }
}
