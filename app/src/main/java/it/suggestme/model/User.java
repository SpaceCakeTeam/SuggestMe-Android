package it.suggestme.model;

public class User {

    private int id;
    private Boolean anon;
    private UserData userData;

    public User(int id, Boolean anon, UserData userData) {
        this.id = id;
        this.anon = anon;
        this.userData = userData;
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
}
