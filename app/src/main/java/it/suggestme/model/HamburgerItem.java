package it.suggestme.model;

/**
 * Created by federicomaggi on 24/08/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class HamburgerItem {

    private String title;
    private int icon;

    public HamburgerItem( String title, int icon ) {
        this.title = title;
        this.icon  = icon;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }
}
