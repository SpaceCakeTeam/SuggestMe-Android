package me.federicomaggi.suggestme.services;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
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

/**
 * Created by federicomaggi on 08/06/15.
 */
public class HttpTask extends AsyncTask<JSONObject,Void,JSONObject> {

    private String REQUEST_URI = "";
    private JSONObject DATA = null;

    public HttpTask(String requestUri, JSONObject data){
        this.REQUEST_URI = requestUri;
        this.DATA = data;
    }

    @Override
    protected void onPreExecute(){
        REQUEST_URI = CommunicationHandler.BASEURL.concat(this.REQUEST_URI);
    }

    @Override
    protected JSONObject doInBackground(JSONObject... uri) {
        HttpClient httpclient = new DefaultHttpClient();

        String responseString = "";
        JSONObject response = null;
        try {
            Log.d("HTTPTASK", "REQUEST URI: ".concat(this.REQUEST_URI));

            HttpURLConnection connection = (HttpURLConnection) new URL(this.REQUEST_URI).openConnection();
            connection.setRequestMethod("POST");

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(this.DATA.toString());
            writer.flush();
            writer.close();
            os.close();

            BufferedReader bfr = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            responseString = bfr.readLine();

            response = (JSONObject) new JSONTokener(responseString).nextValue();
            Log.d("HTTPTASK","RESPONSE: " + response.toString());

        }catch (IOException e) {
            e.printStackTrace();

        }catch (JSONException e) {
            e.printStackTrace();

        }

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        //Do anything with response..

    }

}

