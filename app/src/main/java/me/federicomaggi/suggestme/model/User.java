package me.federicomaggi.suggestme.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import me.federicomaggi.suggestme.util.PreferencesManager;
import me.federicomaggi.suggestme.services.CommunicationHandler;

/**
 * Created by federicomaggi on 20/05/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class User {

    public enum Gender{ m,f,u }

    private int id = -1;
    private Boolean anonflag;
    private Gender gender;

    private String name;
    private String surname;
    private int birth_date;
    private String email;

    private static User userInstance = null;

    private User( Boolean anon, Gender gender,
                  String name, String surname,
                  int birthdate, String email ) {

        this.anonflag   = anon;
        this.gender     = gender;
        this.name       = name;
        this.surname    = surname;
        this.birth_date = birthdate;
        this.email      = email;
    }

    /**
     * At the start there shall be no user.
     *  First I'll checkout in DB for an existing user
     *  If there isn't then create the user
     *
     * @return the User instance
     */
    public static User getUserInstance() {

        if( userInstance == null )
        {
            // TODO -- Search in SharedPreferences
            if( PreferencesManager.getPreferencesManagerInstance().checkForKey("USERDATA","user") ) {
                Log.d("USER","Found user");


                try {
                    JSONObject userjson = PreferencesManager.getPreferencesManagerInstance()
                                                            .readJSONObjFromFile("USERDATA", "user");

                    String gender = userjson.getString("gender");

                    Gender g = getGenderFromString(gender);

                    userInstance = new User(userjson.getBoolean("anonflag"),
                                            g,
                                            userjson.getString("name"),
                                            userjson.getString("surname"),
                                            userjson.getInt("birthdate"),
                                            userjson.getString("email")
                                );
                    userInstance.setID(userjson.getInt("id"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.d("USER","User not found");
                userInstance = new User(true,Gender.u,"","",0,"");
                getMyIDFromServer(userInstance);

                PreferencesManager.getPreferencesManagerInstance()
                        .writeJSONObjOnFile("USERDATA", "user", userInstance.serializeInJSON());
            }
        }

        return userInstance;
    }

    public int getId() {
        return this.id;
    }

    public Boolean getAnonflag() {
        return this.anonflag;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getEmail() {
        return this.email;
    }

    public int getBirthdate() {
        return this.birth_date;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setID( int id ) {
        this.id = id;
    }

    private static void getMyIDFromServer( User who ) {

        CommunicationHandler comm = CommunicationHandler.getCommunicationHandler();
        JSONObject user = comm.registration();

        if( user == null || !user.has("data") ){
            Log.e("MODEL_USER", "Null JSON");
            return;
        }

        try {

            who.setID(user.getJSONObject("data").getInt("userid"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateUserInformationFromSocial( String name, String surname, int birthdate, String email, Gender gender ) {
        this.name = name;
        this.surname = surname;
        this.birth_date = birthdate;
        this.email = email;
        this.gender = gender;

        // TODO -- Send data to server
    }

    private JSONObject serializeInJSON() {
        JSONObject theUser = new JSONObject();

        try {

            theUser.put("id", this.id);
            theUser.put("anonflag", this.anonflag);
            theUser.put("name", this.name);
            theUser.put("surname", this.surname);
            theUser.put("birthdate", this.birth_date);
            theUser.put("gender", this.gender.toString());
            theUser.put("email",this.email);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return theUser;
    }

    private static Gender getGenderFromString( String genderstring ) {

        switch (genderstring.toLowerCase()){
            case "m":
                return Gender.m;
            case "f":
                return Gender.f;
            default:
                return Gender.u;
        }
    }
}
