package it.suggestme.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.suggestme.services.CommunicationHandler;

public class Suggest {

    private int id = -1;
    private int questionid = -1;
    private String text;

    private Suggest(int questionid, int id, String text) {
        this.questionid = questionid;
        this.text = text;
        this.id = id;
    }

    public static ArrayList<Suggest> getSuggestsFromServer() throws JSONException {
        CommunicationHandler comm = CommunicationHandler.getCommunicationHandler();
        JSONObject thereply = comm.getSuggests();
        JSONArray data;

        ArrayList<Suggest> myList = new ArrayList<>();

        if(thereply == null || !thereply.has("data")) {
            Log.e("MODEL_SUGGEST","NULL JSON");
            return null;
        }

        data = thereply.getJSONObject("data").getJSONArray("suggests");

        for(int i = 0; i < data.length(); i++) {
            JSONObject sugg = (JSONObject) data.get(i);
            myList.add(new Suggest(sugg.getInt("questionid"),sugg.getInt("suggestid"),sugg.getString("text")));
        }
        return myList;
    }
}
