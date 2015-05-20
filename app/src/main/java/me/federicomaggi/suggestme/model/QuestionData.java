package me.federicomaggi.suggestme.model;

/**
 * Created by federicomaggi on 20/05/15.
 */
public class QuestionData {

    private int catid;
    private int subcatid;
    private String text;
    private Boolean anon;

    public QuestionData(int catid, int subcatid, String text, Boolean anon){
        this.catid = catid;
        this.subcatid = subcatid;
        this.text = text;
        this.anon = anon;
    }

}
