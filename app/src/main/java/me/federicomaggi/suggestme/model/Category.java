package me.federicomaggi.suggestme.model;

/**
 * Created by federicomaggi on 20/05/15.
 */
public class Category {

    int id;
    String name;
    SubCategory subcategories[];

    public Category(int id, String name, SubCategory subcategories[]) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
    }



}
