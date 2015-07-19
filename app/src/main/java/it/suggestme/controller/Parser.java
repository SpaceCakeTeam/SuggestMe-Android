package it.suggestme.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.suggestme.model.Category;
import it.suggestme.model.Question;
import it.suggestme.model.QuestionData;
import it.suggestme.model.SubCategory;
import it.suggestme.model.Suggest;
import it.suggestme.model.User;
import it.suggestme.model.UserData;

public class Parser {

    private static Parser mInstance;

    public Parser() {
        if (mInstance == null)
            mInstance = new Parser();
    }

    public static User generateUser(JSONObject obj) {
        return new User(obj.optInt("id"), obj.optBoolean("anon"), generateUserData(obj.optJSONObject("userdata")));
    }

    private static UserData generateUserData(JSONObject obj) {
        return new UserData(obj.optString("name"), obj.optString("surname"), obj.optInt("birthdate"), (UserData.Gender)obj.opt("gender"), obj.optString("email"));
    }

    public static ArrayList<Category> generateCategories(JSONObject obj) {
        ArrayList<Category> savedCategories = new ArrayList<>();
        JSONArray categories = obj.optJSONArray("categorieslist");
        for (int i=0;i<categories.length();i++) {
            JSONObject category = categories.optJSONObject(i);
            savedCategories.add(new Category(category.optInt("id"), category.optString("name"), generateSubCategories(category.optJSONArray("subcategories"))));
        }
        return savedCategories;
    }

    private static ArrayList<SubCategory> generateSubCategories(JSONArray subcategories) {
        ArrayList<SubCategory> savedSubCategories = new ArrayList<>();
        for (int i=0;i<subcategories.length();i++) {
            JSONObject subcategory = subcategories.optJSONObject(i);
            savedSubCategories.add(new SubCategory(subcategory.optInt("id"), subcategory.optString("name")));
        }
        return savedSubCategories;
    }

    public static ArrayList<Question> generateQuestions(JSONObject obj) {
        ArrayList<Question> savedQuestions = new ArrayList<>();
        JSONArray questions = obj.optJSONArray("questionslist");
        for (int i=0;i<questions.length();i++) {
            JSONObject question = questions.optJSONObject(i);
            savedQuestions.add(new Question(question.optInt("id"), generateQuestionData(question.optJSONObject("questiondata")), question.optInt("date"), generateSuggest(question.optJSONObject("suggest"))));
        }
        return savedQuestions;
    }

    private static QuestionData generateQuestionData(JSONObject obj) {
            return obj!=null?new QuestionData(obj.optInt("catid"), obj.optInt("subcatid"), obj.optString("text"), obj.optBoolean("anon")):null;
    }

    private static Suggest generateSuggest(JSONObject obj) {
            return obj!=null?new Suggest(obj.optInt("id"), obj.optString("text")):null;
    }

    public static JSONObject parseCategories(ArrayList<Category> categories) {
        JSONArray categoriesParsed = new JSONArray();
        for (Category category: categories)
            categoriesParsed.put(category.parse());
        try {
            return new JSONObject().put("categorieslist",categoriesParsed);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray parseSubCategories(ArrayList<SubCategory> subcategories) {
        JSONArray subcategoriesParsed = new JSONArray();
        for (SubCategory subcategory: subcategories)
            subcategoriesParsed.put(subcategory.parse());
        return subcategoriesParsed;
    }

    public static JSONObject parseQuestions(ArrayList<Question> questions) {
        JSONArray questionsParsed = new JSONArray();
        for (Question question: questions)
            questionsParsed.put(question.parse());
        try {
            return new JSONObject().put("questionslist",questionsParsed);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
