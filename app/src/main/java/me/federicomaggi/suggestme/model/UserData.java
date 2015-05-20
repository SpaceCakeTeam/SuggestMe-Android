package me.federicomaggi.suggestme.model;

/**
 * Created by federicomaggi on 20/05/15.
 */
public class UserData {

    public enum Gender{
        m,
        f,
        u
    }

    String name;
    String surname;
    int birth_date;
    Gender gender;
    String email;

    public UserData( String name, String surname, int birth_date, Gender gender, String email ){
        this.name = name;
        this.surname = surname;
        this.birth_date = birth_date;
        this.gender = gender;
        this.email = email;
    }







}
