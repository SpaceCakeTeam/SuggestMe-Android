package it.suggestme.model;

public class Question {

    private int id;
    private QuestionData questionData;
    private int date;
    private Suggest suggest;

    public Question(int id, QuestionData questionData, int date, Suggest suggest) {
        this.id = id;
        this.questionData = questionData;
        this.date = date;
        this.suggest = suggest;
    }

    public int getId() {
        return id;
    }

    public QuestionData getQuestionData() {
        return questionData;
    }

    public int getDate() {
        return date;
    }

    public Suggest getSuggest() {
        return suggest;
    }
}
