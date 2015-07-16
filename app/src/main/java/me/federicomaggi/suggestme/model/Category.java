package me.federicomaggi.suggestme.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.federicomaggi.suggestme.services.CommunicationHandler;

/**
 * Created by federicomaggi on 20/05/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class Category {

    int id;
    String name;
    ArrayList<SubCategory> subcategories;

    private Category(int id, String name, ArrayList<SubCategory> subcategories) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
    }

    public static ArrayList<Category> getCategories() throws JSONException {

        CommunicationHandler mycomm = CommunicationHandler.getCommunicationHandler();
        JSONObject mJson = mycomm.getCategories();
        ArrayList<Category> myList = new ArrayList<>();

        if( mJson == null ) {
            Log.e("MODEL_CATEGORY", "Null JSON");
            return null;
        }

        JSONArray catArray = mJson.getJSONObject("data").getJSONArray("categories");
        JSONObject temp;

        for( int i = 0; i < catArray.length(); i++  ) {

            temp = (JSONObject) catArray.get(i);

            myList.add(new Category( temp.getInt("categoryid"),
                    temp.getString("category"),
                    SubCategory.getTheArrayListFromJsonArray(temp.getJSONArray("subcategories")))
            );
        }

        return myList;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<SubCategory> getSubcategories() {
        return this.subcategories;
    }

    public static String getCategoryFromID( int catid, ArrayList<Category> catList ) {

        for( Category aCat : catList ) {
            if ( aCat.getId() == catid ) {

                return aCat.getName();
            }
        }

        return "";
    }

    public static ArrayList<SubCategory> getSubCategoryFromID( int catid, ArrayList<Category> catList ) {

        for( Category aCat : catList ) {
            if( aCat.getId() == catid ) {
                return aCat.getSubcategories();
            }
        }
        return null;
    }
}
