package me.federicomaggi.suggestme.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.federicomaggi.suggestme.services.CommunicationHandler;

/**
 * Created by federicomaggi on 20/05/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class Suggest {

    private int id = -1;
    private int questionID = -1;
    private String text;

    /**
     *
     * @param text the text of the question
     */
    private Suggest( int questionID, int id, String text ) {
        this.questionID = questionID;
        this.text = text;
        this.id = id;
    }

    public static ArrayList<Suggest> getSuggestsFromServer() throws JSONException {

        CommunicationHandler comm = CommunicationHandler.getCommunicationHandler();
        JSONObject thereply = comm.getSuggests();
        JSONArray data;

        ArrayList<Suggest> myList = new ArrayList<>();

        if( thereply == null || !thereply.has("data") ) {
            Log.e("MODEL_SUGGEST","NULL JSON");
            return null;
        }

        data = thereply.getJSONObject("data").getJSONArray("suggests");

        for( int i = 0; i < data.length(); i++ ) {
            JSONObject sugg = (JSONObject) data.get(i);
            myList.add(new Suggest(sugg.getInt("questionID"),sugg.getInt("suggestid"),sugg.getString("text")));
        }


        return myList;
    }

    public int getId(){
        return this.id;
    }

    public int getQuestionID() {
        return this.questionID;
    }

}
