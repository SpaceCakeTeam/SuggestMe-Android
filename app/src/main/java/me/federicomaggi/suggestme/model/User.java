package me.federicomaggi.suggestme.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import me.federicomaggi.suggestme.services.CommunicationHandler;

/**
 * Created by federicomaggi on 20/05/15.
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
            // TODO -- Search in DB
            // updateUserInformationFromSocial

            // none found
            userInstance = new User(true,Gender.u,"","",0,"");
            getMyIDFromServer(userInstance);
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

        Log.d("GETTING_FROM_ID", "AD");
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

    public void updateUserInformationFromSocial( String name, String surname, int birth_date, String email, Gender gender ) {


    }

}
