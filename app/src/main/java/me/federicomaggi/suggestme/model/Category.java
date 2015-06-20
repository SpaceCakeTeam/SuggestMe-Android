package me.federicomaggi.suggestme.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.federicomaggi.suggestme.services.CommunicationHandler;

/**
 * Created by federicomaggi on 20/05/15.
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

}
