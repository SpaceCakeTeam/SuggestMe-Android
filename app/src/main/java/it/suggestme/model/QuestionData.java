package it.suggestme.model;

public class QuestionData {

    private int catid;
    private int subcatid;
    private String text;
    private Boolean anon;

    public QuestionData(int catid, int subcatid, String text, Boolean anon) {
        this.catid = catid;
        this.subcatid = subcatid;
        this.text = text;
        this.anon = anon;
    }

    public int getCatId() {
        return catid;
    }

    public int getSubCatId() {
        return subcatid;
    }

    public String getText() {
        return text;
    }

    public Boolean getAnon() {
        return anon;
    }
}
