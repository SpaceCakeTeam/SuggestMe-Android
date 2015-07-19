package it.suggestme.model;

import org.json.JSONException;
import org.json.JSONObject;

public class SubCategory {

    private int id;
    private String name;

    public SubCategory(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JSONObject parse() {
        try {
            return new JSONObject()
                    .put("id",id)
                    .put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
