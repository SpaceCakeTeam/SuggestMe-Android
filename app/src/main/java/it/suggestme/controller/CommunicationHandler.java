package it.suggestme.controller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import it.suggestme.model.Question;
import it.suggestme.model.User;

/**
 * Created by federicomaggi on 08/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class CommunicationHandler {

    // Avevi tolto queste final ma il resto del codice le usa ancora.. Le ho reinserite
    public static final String BASEURL = "http://server.federicomaggi.me/";

    private static final String REGISTRATION_URI    = "registration";
    private static final String GET_CATEGORIES_URI  = "getcategories";
    private static final String ASK_SUGESTION_URI   = "asksuggestion";
    private static final String GET_SUGGESTS_URI    = "getsuggests";

    private static final String SECRET       = "fd11016af3184be88299b007f9676231b69dd6fc";
    private static final String SECRET_LABEL = "secret";
    // *********************************************************************************

    private static CommunicationHandler mInstance = null;

    private CommunicationHandler() {
        if (mInstance == null)
        mInstance = new CommunicationHandler();
    }

    public JSONObject registration(){
        JSONObject data = new JSONObject();
        JSONObject userdata = new JSONObject();
        JSONObject myreply = null;
        try {
            data.put("userid", User.getUserInstance().getId());
            data.put("anonflag", User.getUserInstance().getAnon());
            if(User.getUserInstance().getId() != -1) {
                userdata.put("name",User.getUserInstance().getName());
                userdata.put("surname",User.getUserInstance().getSurname());
                userdata.put("birth_date",User.getUserInstance().getBirthdate());
                userdata.put("gender",User.getUserInstance().getGender());
                userdata.put("email",User.getUserInstance().getEmail());
                data.put("userdata",userdata);
            }
            myreply = serviceRequest(REGISTRATION_URI, data );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myreply;
    }

    public JSONObject getCategories() {
        JSONObject data = new JSONObject();
        JSONObject myreply = null;
        try {
            data.put("userid", User.getUserInstance().getId());
            data.put("userdata", "");
            myreply = serviceRequest(GET_CATEGORIES_URI,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myreply;
    }

    public JSONObject askQuestion(Question question) {
        JSONObject data = new JSONObject();
        JSONObject userdata = new JSONObject();
        JSONObject myreply = null;

        try {
            data.put("userid", User.getUserInstance().getId());
            userdata.put("categoryid", question.getCategory());
            userdata.put("subcategoryid", question.getSubcategoryid());
            userdata.put("text", question.getContent());
            userdata.put("anonflag", question.getAnonflag());
            data.put("userdata",userdata);
            myreply = serviceRequest(ASK_SUGESTION_URI,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myreply;
    }

    /**
     * Service Get Suggests
     * Used to retrieve suggests for Question locally stored
     *
     * @return JSONObject containing the reply from server
     */
    public JSONObject getSuggests() {
        JSONObject data = new JSONObject();
        JSONObject myreply = null;

        try {
            JSONArray questionid = new JSONArray(Question.retrieveMyQuestions()) ;
            data.put("userid", User.getUserInstance().getId());
            data.put("userdata",questionid);
            myreply = serviceRequest(GET_SUGGESTS_URI,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myreply;
    }

    private JSONObject serviceRequest(String requestUri, JSONObject requestData) {
        requestData = addSecret(requestData);
        try{
            JSONObject reply = new HttpTask(requestUri,requestData).execute(requestData).get();
            if(reply == null || !reply.getString("status").toLowerCase().equals("ok")) {
                Log.e("COMM_HANDLE", "ERROR. " + reply.getString("status") + " REQUEST. Err.no: " + reply.getInt("errno"));
                return null;
            }
            return reply;
        } catch (InterruptedException | ExecutionException | JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject addSecret(JSONObject withoutSecret){

        JSONObject withSecret;
        try{
            if(withoutSecret.has(SECRET_LABEL))
                return withoutSecret;
            withSecret = withoutSecret.put(SECRET_LABEL,SECRET);
            return withSecret;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return withoutSecret;
    }
}
