package it.suggestme.controller;

import android.os.AsyncTask;

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

    private Helpers helpers = Helpers.shared();
    private interface ServiceCallback {void callback(JSONObject obj);}
    private interface RequestCallback {void callback(Boolean success);}

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
                User user = helpers.getUser();
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

    public void registrationRequest(final RequestCallback requestCallback) {
        helpers.setSpinner();
        new ServiceRequest(Helpers.getString(R.string.registration_uri), new JSONObject(), new ServiceCallback() {
            @Override
            public void callback(JSONObject response) {
                helpers.removeSpinner();
                if (response.optString("status").equalsIgnoreCase("ok")) {
                    JSONObject responseData = response.optJSONObject("data");
                    helpers.getUser().setId(responseData.optInt("userid"));
                    categoryRequest(new RequestCallback() {
                        @Override
                        public void callback(Boolean success) {
                            if (success) {
                                helpers.saveObj("pf", "user", helpers.getUser().parse());
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
                    JSONArray responseCategories = responseData.optJSONArray("categories");
                    try {
                        ArrayList<Category> categories = Parser.generateCategories(new JSONObject().put("categorieslist", responseCategories));
                        helpers.setCategories(categories);
                        helpers.saveObj("pf","user",Parser.parseCategories(helpers.getCategories()));
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
        helpers.setSpinner();
        new ServiceRequest(Helpers.getString(R.string.asksuggestion_uri), questionData.parse(), new ServiceCallback() {
            @Override
            public void callback(JSONObject response) {
                helpers.removeSpinner();
                if (response.optString("status").equalsIgnoreCase("ok")) {
                    JSONObject responseData = response.optJSONObject("data");
                    Question question = new Question(responseData.optInt("questionid"),questionData,responseData.optInt("timestamp"),null);
                    helpers.getQuestions().add(question);
                    helpers.saveObj("pf", "user", Parser.parseQuestions(helpers.getQuestions()));
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
                    JSONArray responseSuggests = responseData.optJSONArray("suggests");
                    for (Question question:helpers.getQuestions()) {
                        for (int i=0;i<responseSuggests.length();i++) {
                            JSONObject responseSuggest = responseSuggests.optJSONObject(i);
                            Suggest suggest = new Suggest(responseSuggest.optInt("suggestid"),responseSuggest.optString("text"));
                            if (question.getId() == responseSuggest.optInt("questionid")) {
                                question.setSuggest(suggest);
                                break;
                            }
                        }
                    }
                    helpers.saveObj("pf","user",Parser.parseQuestions(helpers.getQuestions()));
                    requestCallback.callback(true);
                } else if (response.optString("status").equalsIgnoreCase("ko")) {
                    Helpers.showAlert(response.optInt("errno"));
                    requestCallback.callback(false);
                }
            }
        }).execute();
    }
}
