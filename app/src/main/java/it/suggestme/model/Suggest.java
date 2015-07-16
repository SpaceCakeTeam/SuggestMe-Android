package it.suggestme.model;

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
}
