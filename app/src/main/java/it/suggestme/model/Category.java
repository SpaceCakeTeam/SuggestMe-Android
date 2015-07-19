package it.suggestme.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.suggestme.controller.Parser;

public class Category {

    private int id;
    private String name;
    private ArrayList<SubCategory> subcategories;

    public Category(int id, String name, ArrayList<SubCategory> subcategories) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<SubCategory> getSubCategories() {
        return subcategories;
    }

    public JSONObject parse() {
        try {
            return new JSONObject()
                    .put("categoryid",id)
                    .put("category",name)
                    .put("subcategories", Parser.parseSubCategories(subcategories));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
