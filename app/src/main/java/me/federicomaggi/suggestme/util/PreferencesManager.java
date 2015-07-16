package me.federicomaggi.suggestme.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by federicomaggi on 17/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class PreferencesManager {

    private static Context appContext = null;

    private static PreferencesManager singleton = null;

    private PreferencesManager() {
    }

    public static PreferencesManager getPreferencesManagerInstance() {
        if( singleton == null ) {
            singleton = new PreferencesManager();
        }

        return singleton;
    }

    public void setContext( Context newContext ) {
        appContext = newContext;
    }

    public boolean checkForKey( String sharedFile ,String key ) {
        SharedPreferences sp = appContext.getSharedPreferences(sharedFile, Context.MODE_PRIVATE);

        return sp.contains(key);
    }



    public void writeJSONObjOnFile( String filename, String key, JSONObject data ) {
        SharedPreferences sp = appContext.getSharedPreferences(filename, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,data.toString());
        editor.commit();
    }

    public JSONObject readJSONObjFromFile( String filename, String key ) throws JSONException {
        SharedPreferences sp = appContext.getSharedPreferences(filename, Context.MODE_PRIVATE);

        if( !this.checkForKey(filename,key) )
            return null;

        return (JSONObject) new JSONTokener(sp.getString(key,"err")).nextValue();


    }
}
