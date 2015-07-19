package it.suggestme.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private int id;
    private Boolean anon;
    private UserData userData;

    public User(int id, Boolean anon, UserData userData) {
        this.id = id;
        this.anon = anon;
        this.userData = userData;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Boolean getAnon() {
        return anon;
    }

    public UserData getUserData() {
        return userData;
    }

    public JSONObject parse() {
        try {
            return new JSONObject()
                    .put("id",id)
                    .put("anon",anon)
                    .put("userdata",userData.parse());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
