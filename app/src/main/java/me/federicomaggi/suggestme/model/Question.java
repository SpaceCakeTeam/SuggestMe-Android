package me.federicomaggi.suggestme.model;

/**
 * Created by federicomaggi on 20/05/15.
 */
public class Question {

    private int id;
    private QuestionData questiondata;
    private int date;
    private Suggest suggest;

    public Question(int id, QuestionData questiondata, int date, Suggest suggest){
        this.id = id;
        this.questiondata = questiondata;
        this.date = date;
        this.suggest = suggest;
    }


}
