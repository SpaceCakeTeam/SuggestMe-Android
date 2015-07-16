package it.suggestme.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

<<<<<<< HEAD:app/src/main/java/me/federicomaggi/suggestme/util/PreferencesManager.java
/**
 * Created by federicomaggi on 17/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
=======
>>>>>>> a35368c8c80cbc0d1bd4cf812b8f7ef61f4342e7:app/src/main/java/it/suggestme/util/PreferencesManager.java
public class PreferencesManager {

    private static Context appContext = null;
    private static PreferencesManager singleton = null;
    private PreferencesManager() {}

    public static PreferencesManager getPreferencesManagerInstance() {
        if(singleton == null) {
            singleton = new PreferencesManager();
        }
        return singleton;
    }

    public void setContext( Context newContext ) {
        appContext = newContext;
    }

<<<<<<< HEAD:app/src/main/java/me/federicomaggi/suggestme/util/PreferencesManager.java
    public boolean checkForKey( String sharedFile ,String key ) {
        SharedPreferences sp = appContext.getSharedPreferences(sharedFile, Context.MODE_PRIVATE);

=======
    public boolean checkForKey(String sharedfile ,String key) {
        SharedPreferences sp = appContext.getSharedPreferences(sharedfile, Context.MODE_PRIVATE);
>>>>>>> a35368c8c80cbc0d1bd4cf812b8f7ef61f4342e7:app/src/main/java/it/suggestme/util/PreferencesManager.java
        return sp.contains(key);
    }

    public void writeJSONObjOnFile(String filename, String key, JSONObject data) {
        SharedPreferences sp = appContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,data.toString());
        editor.commit();
    }

    public JSONObject readJSONObjFromFile(String filename, String key) throws JSONException {
        SharedPreferences sp = appContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        if( !this.checkForKey(filename,key) )
            return null;
        return (JSONObject) new JSONTokener(sp.getString(key,"err")).nextValue();
    }
}
