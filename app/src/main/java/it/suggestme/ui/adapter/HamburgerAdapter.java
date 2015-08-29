package it.suggestme.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.suggestme.R;
import it.suggestme.model.HamburgerItem;

public class HamburgerAdapter extends ArrayAdapter<HamburgerItem> {

    private Context context;

    public HamburgerAdapter(Context context, int resource, List<HamburgerItem> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View theView = convertView;
        if(theView == null) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            theView = li.inflate(R.layout.item_hamburger, null);
        }

        HamburgerItem pos = getItem(position);
        if(pos != null) {
            ImageView imview = (ImageView) theView.findViewById(R.id.ham_element_icon);
            TextView hamtext = (TextView)  theView.findViewById(R.id.ham_element_text);
            hamtext.setText(pos.getTitle());

            imview.setBackground(ContextCompat.getDrawable(getContext(), pos.getIcon()));
            imview.setContentDescription(pos.getAccessibility());
        }
        return theView;
    }
}
