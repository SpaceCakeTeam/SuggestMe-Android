package it.suggestme.model;

import org.json.JSONException;
import org.json.JSONObject;

public class UserData {

    private String name;
    private String surname;
    private int birthdate;
    private Gender gender;
    private String email;

    public UserData(String name, String surname, int birthdate, Gender gender, String email) {
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.gender = gender;
        this.email = email;
    }

    public enum Gender{m,f,u}

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getBirthdate() {
        return birthdate;
    }

    public Gender getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public JSONObject parse() {
        try {
            return new JSONObject()
                    .put("name",name)
                    .put("surname",surname)
                    .put("birth_date",birthdate)
                    .put("gender",gender)
                    .put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
