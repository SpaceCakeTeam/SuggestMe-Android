package it.suggestme.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.suggestme.controller.CommunicationHandler;

public class Suggest {

    private int id;
    private String text;

    public Suggest(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public JSONObject parse() {
        try {
            return new JSONObject()
                    .put("id",id)
                    .put("text",text);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
/*
    public static ArrayList<Suggest> getSuggestsFromServer() throws JSONException {

        ArrayList<Integer> questionID = Question.retrieveMyQuestionsID();

        JSONArray jsquestionID = new JSONArray(questionID);

        CommunicationHandler comm = new CommunicationHandler();
        JSONObject thereply = comm.getSuggestsRequest(jsquestionID);
        JSONArray data;

        ArrayList<Suggest> myList = new ArrayList<>();

        if( thereply == null || !thereply.has("data") ) {
            Log.e("MODEL_SUGGEST", "NULL JSON");
            return null;
        }

        data = thereply.getJSONObject("data").getJSONArray("suggests");

        for( int i = 0; i < data.length(); i++ ) {
            JSONObject sugg = (JSONObject) data.get(i);
            myList.add(new Suggest(sugg.getInt("questionid"),sugg.getInt("suggestid"),sugg.getString("text")));
        }


        return myList;
    }
*/
}
