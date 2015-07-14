package it.suggestme.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.suggestme.R;

public class HamburgerAdapter extends ArrayAdapter<String> {

    public HamburgerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View theView = convertView;
        if(theView == null) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            theView = li.inflate(R.layout.list_selected_row, null);
        }

        String pos = getItem(position);
        if(pos != null) {
            ImageView imview = (ImageView) theView.findViewById(R.id.ham_element_icon);
            TextView hamtext = (TextView)  theView.findViewById(R.id.ham_element_text);
            hamtext.setText(pos);
        }
        return theView;
    }
}
