package me.federicomaggi.suggestme.model;

/**
 * Created by federicomaggi on 20/05/15.
 */
public class Suggest {

    private int id = -1;
    private String text;

    /**
     *
     * @param text the text of the question
     */
    private Suggest( int id, String text ) {
        this.text = text;
        this.id = id;
    }




}
