package it.suggestme.model;

import java.util.ArrayList;

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
}
