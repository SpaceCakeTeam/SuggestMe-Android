package me.federicomaggi.suggestme.model;

import android.graphics.drawable.Drawable;

/**
 * Created by federicomaggi on 24/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class HamburgerItem {

    private String   title;
    private Drawable icon;

    public HamburgerItem( String title, Drawable icon ){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return this.title;
    }

    public Drawable getIcon() {
        return this.icon;
    }
}
