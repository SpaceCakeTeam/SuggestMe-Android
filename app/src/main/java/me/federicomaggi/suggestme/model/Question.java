package me.federicomaggi.suggestme.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.federicomaggi.suggestme.services.CommunicationHandler;

/**
 * Created by federicomaggi on 20/05/15.
 */
public class Question {

    private int id = -1;
    private int categoryid;
    private int subcategoryid;
    private String text;
    private int timestamp;
    private Boolean anonflag;

    public Question( String text, int categoryid, int subcategoryid, Boolean anon ) {

        this.categoryid    = categoryid;
        this.subcategoryid = subcategoryid;
        this.text          = text;
        this.anonflag      = anon;
    }

    /**
     * Send question to server.
     * When a reply is received Question data are updated and stored in SQLite DB
     */
    public void commitQuestionToServer() throws JSONException {

        CommunicationHandler comm = CommunicationHandler.getCommunicationHandler();
        JSONObject questionData = comm.askQuestion(this);
        JSONObject replyData;

        if( questionData == null || !questionData.has("data") )
            return;

        replyData = questionData.getJSONObject("data");

        this.setQuestionID(replyData.getInt("questionid"));
        this.setTimestamp(replyData.getInt("timestamp"));

        storeQuestionInDatabase();
    }

    /**
     *
     * @return question content
     */
    public String getContent() {
        return this.text;
    }

    /**
     *
     * @return question category id
     */
    public int getCategory() {
        return this.categoryid;
    }

    /**
     *
     * @return question subcategory id
     */
    public int getSubcategoryid() {
        return this.subcategoryid;
    }

    /**
     *
     * @return anonymous flag
     */
    public Boolean getAnonflag() {
        return this.anonflag;
    }

    /**
     *
     * @param id set the id of the question returned from the server
     */
    private void setQuestionID( int id ) {
        this.id = id;
    }

    /**
     *
     * @param timestamp set the timestamp returned from the server
     */
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

    /**
     * Store question in SQLite Database.
     * This is fired when the server replies with QuestionID and Timestamp
     */
    private void storeQuestionInDatabase() {

        // TODO -- store the question data in local SQLite DB
        Log.d("QUESTION_COMMIT_DB","Hell yeah here we are");
    }

    /**
     * Retrieve an ArrayList containing QuestionID
     * stored in SQLite database
     *
     * @return ArrayList<Integer>
     */
    public static ArrayList<Integer> retrieveMyQuestions() {

        // TODO -- retrieve question data from local SQLite DB
        ArrayList<Integer> mylist = new ArrayList<Integer>();

        mylist.add(1);
        mylist.add(2);

        return mylist;
    }
}
