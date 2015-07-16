package me.federicomaggi.suggestme.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by federicomaggi on 20/05/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class SubCategory {

    private int id;
    private String name;

    private SubCategory(int id, String name){
        this.id = id;
        this.name = name;
    }

    public static ArrayList<SubCategory> getTheArrayListFromJsonArray( JSONArray theinput ) throws JSONException {

        JSONObject temp;
        ArrayList<SubCategory> mySubList = new ArrayList<>();

        for( int i = 0; i < theinput.length(); i++ ) {

            temp = (JSONObject) theinput.get(i);
            mySubList.add(new SubCategory(temp.getInt("subcategoryid"),temp.getString("subcategoryname")));
        }

        return mySubList;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }


    public static String getSubCategoryNameFromID( int subcatid, ArrayList<SubCategory> subcatList ) {

        if( subcatList == null )
            throw new NullPointerException();

        for( SubCategory aSubCat : subcatList ) {
            if( aSubCat.getId() == subcatid ) {
                return aSubCat.getName();
            }
        }
        return "";
    }


}
