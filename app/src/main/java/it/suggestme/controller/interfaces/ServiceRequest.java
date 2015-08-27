package it.suggestme.controller.interfaces;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import it.suggestme.R;
import it.suggestme.controller.Helpers;
import it.suggestme.controller.services.ServiceCallback;
import it.suggestme.model.User;

/**
 * Created by federicomaggi on 27/08/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class ServiceRequest extends AsyncTask<Void,Void,JSONObject> {

    private String requestUri;
    private Object requestData;
    private ServiceCallback serviceCallback;
    private JSONObject requestDataWithIdAndSecret;

    public ServiceRequest(String requestUri, Object requestData, ServiceCallback serviceCallback) {
        this.requestUri = requestUri;
        this.requestData = requestData;
        this.serviceCallback = serviceCallback;
    }

    @Override
    protected void onPreExecute(){
        try {
            User user = Helpers.shared().getAppUser();
            requestDataWithIdAndSecret = new JSONObject()
                    .put("userid", user.getId())
                    .put("secret", Helpers.getString(R.string.secret));

            if (requestUri.equalsIgnoreCase(Helpers.getString(R.string.registration_uri))) {
                requestDataWithIdAndSecret.put("anonflag", user.getAnon()).put("userdata", user.getUserData().parse());
            } else {
                requestDataWithIdAndSecret.put("userdata", requestData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONObject doInBackground(Void... v) {
        JSONObject response = new JSONObject();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(Helpers.getString(R.string.base_url)+requestUri).openConnection();
            connection.setRequestMethod("POST");
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(requestDataWithIdAndSecret.toString());
            writer.flush();
            writer.close();
            os.close();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String responseString = bfr.readLine();
            response = (JSONObject) new JSONTokener(responseString).nextValue();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        serviceCallback.callback(result);
    }
}
