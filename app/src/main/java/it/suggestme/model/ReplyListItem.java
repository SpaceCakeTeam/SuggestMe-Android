package it.suggestme.model;

/**
 * Created by federicomaggi on 23/08/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class ReplyListItem {

    private Question question;
    private Suggest suggest;
    private String  categoryName;
    private String  subcategoryName;
    private Boolean hasBeenReplied;

    public ReplyListItem( Question question, Suggest suggest, String categoryname, String subcategoryName, Boolean reply ) {

        this.question        = question;
        this.suggest         = suggest;
        this.categoryName    = categoryname;
        this.subcategoryName = subcategoryName;
        this.hasBeenReplied  = reply;
    }

    public Question getQuestion() {
        return this.question;
    }

    public Suggest getSuggest() {
        return this.suggest;
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