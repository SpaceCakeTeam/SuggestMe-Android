package me.federicomaggi.suggestme.model;

/**
 * Created by federicomaggi on 20/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class ReplyListItem {

    private int questionID;
    private int suggestID;
    private String  categoryName;
    private String  subcategoryName;
    private Boolean hasBeenReplied;

    public ReplyListItem( int questionID, int suggestID, String categoryname, String subcategoryName, Boolean reply ) {

        this.questionID      = questionID;
        this.suggestID       = suggestID;
        this.categoryName    = categoryname;
        this.subcategoryName = subcategoryName;
        this.hasBeenReplied  = reply;
    }

    public int getQuestionID() {
        return this.questionID;
    }

    public int getSuggestID() {
        return this.suggestID;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public String getSubcategoryName() {
        return this.subcategoryName;
    }

    public Boolean getHasBeenReplied() {
        return this.hasBeenReplied;
    }

}
