package it.suggestme.controller;

import android.app.Service;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
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
import java.util.ArrayList;

import it.suggestme.R;
import it.suggestme.model.Category;
import it.suggestme.model.Question;
import it.suggestme.model.QuestionData;
import it.suggestme.model.Suggest;
import it.suggestme.model.User;

public class CommunicationHandler {

    private interface ServiceCallback {void callback(JSONObject obj);}
    public interface RequestCallback {void callback(Boolean success);}

    public CommunicationHandler() {}

    private class ServiceRequest extends AsyncTask<Void,Void,JSONObject> {

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
                User user = Helpers.shared().getUser();
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

    private class ExternalServiceRequest extends AsyncTask<Void,Void,JSONObject> {

        private String mRequestURL;
        private ServiceCallback mCallback;

        public ExternalServiceRequest(String url, ServiceCallback serviceCallback){
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

    public void registrationRequest( final RequestCallback requestCallback, JSONObject userData ) {
        Helpers.shared().setSpinner();
        new ServiceRequest(Helpers.getString(R.string.registration_uri), userData, new ServiceCallback() {
            @Override
            public void callback(JSONObject response) {
                Helpers.shared().removeSpinner();
                Log.i(Helpers.getString(R.string.loginfo), response.toString());

                if (response.optString("status").equalsIgnoreCase("ok")) {
                    JSONObject responseData = response.optJSONObject("data");
                    Log.i(Helpers.getString(R.string.loginfo), responseData.toString());

                    Helpers.shared().getUser().setId(responseData.optInt("userid"));
                    categoryRequest(new RequestCallback() {
                        @Override
                        public void callback(Boolean success) {
                            if (success) {
                                Helpers.shared().saveObj("user", Helpers.shared().getUser().parse());
                            }
                            requestCallback.callback(success);
                        }
                    });
                } else if (response.optString("status").equalsIgnoreCase("ko")) {
                    Helpers.showAlert(response.optInt("errno"));
                    requestCallback.callback(false);
                }
            }
        }).execute();
    }

    public void categoryRequest(final RequestCallback requestCallback) {
        new ServiceRequest(Helpers.getString(R.string.getcategories_uri), new JSONObject(), new ServiceCallback() {
            @Override
            public void callback(JSONObject response) {
                if (response.optString("status").equalsIgnoreCase("ok")) {
                    JSONObject responseData = response.optJSONObject("data");
                    Log.i(Helpers.getString(R.string.loginfo), responseData.toString());

                    JSONArray responseCategories = responseData.optJSONArray("categories");
                    try {
                        ArrayList<Category> categories = Parser.generateCategories(new JSONObject().put("categorieslist", responseCategories));
                        Helpers.shared().setCategories(categories);
                        Helpers.shared().saveObj("categories", Parser.parseCategories(Helpers.shared().getCategories()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestCallback.callback(true);
                } else if (response.optString("status").equalsIgnoreCase("ko")) {
                    Helpers.showAlert(response.optInt("errno"));
                    requestCallback.callback(false);
                }
            }
        }).execute();
    }

    public void askSuggestionRequest(final QuestionData questionData, final RequestCallback requestCallback) {
        Helpers.shared().setSpinner();
        new ServiceRequest(Helpers.getString(R.string.asksuggestion_uri), questionData.parse(), new ServiceCallback() {
            @Override
            public void callback(JSONObject response) {
                Helpers.shared().removeSpinner();
                if (response.optString("status").equalsIgnoreCase("ok")) {
                    JSONObject responseData = response.optJSONObject("data");
                    Log.i(Helpers.getString(R.string.loginfo), responseData.toString());

                    Question question = new Question(responseData.optInt("questionid"),questionData,responseData.optInt("timestamp"),null);
                    Helpers.shared().getQuestions().add(question);
                    Helpers.shared().saveObj("questions", Parser.parseQuestions(Helpers.shared().getQuestions()));
                    requestCallback.callback(true);
                } else if (response.optString("status").equalsIgnoreCase("ko")) {
                    Helpers.showAlert(response.optInt("errno"));
                    requestCallback.callback(false);
                }
            }
        }).execute();
    }

    public void getSuggestsRequest(JSONArray questionsId, final RequestCallback requestCallback) {
        new ServiceRequest(Helpers.getString(R.string.getsuggests_uri), questionsId, new ServiceCallback() {
            @Override
            public void callback(JSONObject response) {
                if (response.optString("status").equalsIgnoreCase("ok")) {
                    JSONObject responseData = response.optJSONObject("data");
                    Log.i(Helpers.getString(R.string.loginfo), responseData.toString());

                    JSONArray responseSuggests = responseData.optJSONArray("suggests");
                    for (Question question:Helpers.shared().getQuestions()) {
                        for (int i=0;i<responseSuggests.length();i++) {
                            JSONObject responseSuggest = responseSuggests.optJSONObject(i);
                            Suggest suggest = new Suggest(responseSuggest.optInt("suggestid"),responseSuggest.optString("text"));
                            if (question.getId() == responseSuggest.optInt("questionid")) {
                                question.setSuggest(suggest);
                                break;
                            }
                        }
                    }
                    Helpers.shared().saveObj("questions", Parser.parseQuestions(Helpers.shared().getQuestions()));
                    requestCallback.callback(true);
                } else if (response.optString("status").equalsIgnoreCase("ko")) {
                    Helpers.showAlert(response.optInt("errno"));
                    requestCallback.callback(false);
                }
            }
        }).execute();
    }

    public void getProfilePicture(String url, final RequestCallback requestCallback) {
        new ExternalServiceRequest(url, new ServiceCallback() {
            @Override
            public void callback(JSONObject obj) {
                try {
                    if (obj != null && obj.getString("status").toLowerCase() == "ok") {
                        requestCallback.callback(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    requestCallback.callback(false);
                }
                requestCallback.callback(false);
            }
        }).execute();
    }
}
