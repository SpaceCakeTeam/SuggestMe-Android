package it.suggestme.controller.services;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import it.suggestme.controller.Helpers;
import it.suggestme.controller.interfaces.ServiceCallback;

/**
 * Created by federicomaggi on 27/08/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class DrawableServiceRequest extends AsyncTask<Void,Void,JSONObject> {

    private String mRequestURL;
    private ServiceCallback mCallback;

    public DrawableServiceRequest(String url, ServiceCallback serviceCallback){
        mRequestURL = url;
        mCallback   = serviceCallback;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        JSONObject response = new JSONObject();
        try {
            Drawable thumb_d = Drawable.createFromStream(new URL(mRequestURL).openStream(), "src");
            Helpers.shared().setProfilePic(thumb_d);

            response.put("status","ok");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        mCallback.callback(result);
    }
}