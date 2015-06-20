package me.federicomaggi.suggestme.services;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import me.federicomaggi.suggestme.model.Question;
import me.federicomaggi.suggestme.model.User;

/**
 * Created by federicomaggi on 08/06/15.
 */
public class CommunicationHandler {

    public static final String BASEURL = "http://server.federicomaggi.me/";

    private static final String REGISTRATION_URI    = "registration";
    private static final String GET_CATEGORIES_URI  = "getcategories";
    private static final String ASK_SUGESTION_URI   = "asksuggestion";
    private static final String GET_SUGGESTS_URI    = "getsuggests";

    private static final String SECRET       = "fd11016af3184be88299b007f9676231b69dd6fc";
    private static final String SECRET_LABEL = "secret";

    private static CommunicationHandler  handlerinstance = null;

    /**
     *  Empty Constructor
     */
    private CommunicationHandler(){

    }

    /**
     *  @return CommunicationHandler singleton instance
     */
    public static CommunicationHandler getCommunicationHandler(){
        if( handlerinstance == null )
            handlerinstance = new CommunicationHandler();

        return handlerinstance;
    }

    /**
     *  Registration Service
     *
     *  @return JSONObject containing the reply from server
     */
    public JSONObject registration(){

        JSONObject data = new JSONObject();
        JSONObject userdata = new JSONObject();
        JSONObject myreply = null;

        try {

            data.put("userid", User.getUserInstance().getId());
            data.put("anonflag", User.getUserInstance().getAnonflag());

            if( User.getUserInstance().getId() != -1 ) {
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

    /**
     *  Get Categories Service
     *
     *  @return JSONObject containing the reply from server
     */
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

    /**
     *  Ask Question service
     *
     *  @return JSONObject containing the reply from server
     */
    public JSONObject askQuestion(Question question) {

        JSONObject data     = new JSONObject();
        JSONObject userdata = new JSONObject();
        JSONObject myreply  = null;

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


    public JSONObject getSuggests() {

        JSONObject data     = new JSONObject();
        JSONObject myreply  = null;

        try {
            // Retrieve from DB an array of QUestion
            JSONArray questionid = new JSONArray(Question.retrieveMyQuestions()) ;

            data.put("userid", User.getUserInstance().getId());
            data.put("userdata",questionid);

            myreply = serviceRequest(GET_SUGGESTS_URI,data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return myreply;
    }

    /**
     *  Service request handler
     *
     *  @param requestUri partial request uri.
     *  @param requestData JSON data for the request.
     *
     *  @return JSONObject containing the reply from server.
     */
    private JSONObject serviceRequest( String requestUri, JSONObject requestData ){

        requestData = addSecret(requestData);

        try{
            JSONObject reply = new HttpTask(requestUri,requestData).execute(requestData).get();

            if( reply == null || !reply.getString("status").toLowerCase().equals("ok") ) {
                Log.e("COMM_HANDLE", "ERROR. " + reply.getString("status") + " REQUEST. Err.no: " + reply.getInt("errno"));
                return null;
            }
            return reply;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  Service request handler
     *
     *  @param withoutSecret JSON Object without secret field.
     *
     *  @return JSONObject containing the secret field.
     */
    private JSONObject addSecret(JSONObject withoutSecret){

        JSONObject withSecret = null;

        try{
            if( withoutSecret.has(SECRET_LABEL) )
                return withoutSecret;

            withSecret = withoutSecret.put(SECRET_LABEL,SECRET);
            return withSecret;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return withoutSecret;
    }


}
