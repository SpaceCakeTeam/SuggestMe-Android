package it.suggestme.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Question {

    private int id;
    private QuestionData questionData;
    private int date;
    private Suggest suggest;

    public Question(int id, QuestionData questionData, int date, Suggest suggest) {
        this.id = id;
        this.questionData = questionData;
        this.date = date;
        this.suggest = suggest;
    }

    public int getId() {
        return id;
    }

    public QuestionData getQuestionData() {
        return questionData;
    }

    public void setQuestionData(QuestionData questionData) {
        this.questionData = questionData;
    }

    public int getDate() {
        return date;
    }

    public Suggest getSuggest() {
        return suggest;
    }

    public void setSuggest(Suggest suggest) {
        this.suggest = suggest;
    }

    public JSONObject parse() {
        try {
            return new JSONObject()
                    .put("id",id)
                    .put("questiondata", questionData != null ? questionData.parse() : null)
                    .put("date", date)
                    .put("suggest",suggest!=null?suggest.parse():null);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
