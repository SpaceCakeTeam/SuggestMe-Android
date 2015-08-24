package it.suggestme.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubCategory {

    private int id;
    private String name;

    public SubCategory(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JSONObject parse() {
        try {
            return new JSONObject()
                    .put("subcategoryid",id)
                    .put("subcategoryname",name);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

  /*  public static String getSubCategoryNameFromID( int subcatid, ArrayList<SubCategory> subcatList ) {

        if( subcatList == null )
            throw new NullPointerException();

        for( SubCategory aSubCat : subcatList ) {
            if( aSubCat.getId() == subcatid ) {
                return aSubCat.getName();
            }
        }
        return "";
    }
    */
}
