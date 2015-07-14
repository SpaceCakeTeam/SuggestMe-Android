package it.suggestme.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.suggestme.services.CommunicationHandler;

public class Question {

    private int id = -1;
    private int categoryid;
    private int subcategoryid;
    private String text;
    private int timestamp;
    private Boolean anonflag;

    public Question(String text, int categoryid, int subcategoryid, Boolean anon) {
        this.categoryid = categoryid;
        this.subcategoryid = subcategoryid;
        this.text = text;
        this.anonflag = anon;
    }

    public void commitQuestionToServer() throws JSONException {
        CommunicationHandler comm = CommunicationHandler.getCommunicationHandler();
        JSONObject questionData = comm.askQuestion(this);
        JSONObject replyData;

        if(questionData == null || !questionData.has("data"))
            return;
        replyData = questionData.getJSONObject("data");
        this.setQuestionID(replyData.getInt("questionid"));
        this.setTimestamp(replyData.getInt("timestamp"));
        storeQuestionInDatabase();
    }

    public String getContent() {
        return this.text;
    }

    public int getCategory() {
        return this.categoryid;
    }

    public int getSubcategoryid() {
        return this.subcategoryid;
    }

    public Boolean getAnonflag() {
        return this.anonflag;
    }

    private void setQuestionID( int id ) {
        this.id = id;
    }

    private void setTimestamp( int timestamp ) {
        this.timestamp = timestamp;
    }

    public String toJSONString() {
        JSONObject question = new JSONObject();
        try {
            question.put("text",this.text);
            question.put("category",this.categoryid);
            question.put("subcategory",this.subcategoryid);
            question.put("anon",this.getAnonflag());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return question.toString();
    }

    private void storeQuestionInDatabase() {
        Log.d("QUESTION_COMMIT_DB","Hell yeah here we are");
    }

    public static ArrayList<Integer> retrieveMyQuestions() {
        ArrayList<Integer> mylist = new ArrayList<Integer>();
        mylist.add(1);
        mylist.add(2);
        return mylist;
    }
}
