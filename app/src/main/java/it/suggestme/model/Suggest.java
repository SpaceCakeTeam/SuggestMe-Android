package it.suggestme.model;

import org.json.JSONException;
import org.json.JSONObject;

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
}
