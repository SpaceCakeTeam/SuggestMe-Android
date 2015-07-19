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
import it.suggestme.model.SubCategory;
import it.suggestme.model.Suggest;
import it.suggestme.model.User;

public class CommunicationHandler {

    private Helpers helpers = Helpers.shared();

    public CommunicationHandler() {}

    class ServiceRequest extends AsyncTask<Void,Void,JSONObject> {

        private String requestUri;
        private JSONObject requestData;
        private ServiceCallback serviceCallback;
        private JSONObject requestDataWithIdAndSecret;

        public ServiceRequest(String requestUri, JSONObject requestData, ServiceCallback serviceCallback) {
            this.requestUri = requestUri;
            this.requestData = requestData;
            this.serviceCallback = serviceCallback;
        }

        @Override
        protected void onPreExecute(){
            try {
                User user = helpers.getUser();
                requestDataWithIdAndSecret = new JSONObject();
                if (requestUri.equalsIgnoreCase(Helpers.getString(R.string.registration_uri))) {
                    try {
                        requestDataWithIdAndSecret
                                .put("userid", user.getId())
                                .put("anonflag", user.getAnon())
                                .put("userdata", user.getUserData())
                                .put("userdata", requestData)
                                .put("secret", Helpers.getString(R.string.secret));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    requestDataWithIdAndSecret
                            .put("userid", user.getId())
                            .put("userdata", requestData)
                            .put("secret", Helpers.getString(R.string.secret));
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
            serviceCallback.run(); //pass result
        }
    }

    private class ServiceCallback implements Runnable {
        private final JSONObject response;

        public ServiceCallback(JSONObject response) {
            this.response = response;
        }

        public void run() {}
    }

    private class RequestCallback implements Runnable {
        private final Boolean response;

        public RequestCallback(Boolean response) {
            this.response = response;
        }

        @Override
        public void run() {}
    }

    public void registrationRequest(final RequestCallback requestCallback) {
        helpers.setSpinner();
        new ServiceRequest(Helpers.getString(R.string.registration_uri), new JSONObject(), new ServiceCallback() {
            @Override
            public void run(JSONObject response) {
                helpers.removeSpinner();
                if (response.optString("status").equalsIgnoreCase("ok")) {
                    JSONObject responseData = response.optJSONObject("data");
                    helpers.getUser().setId(responseData.optInt("userid"));
                    categoryRequest(new RequestCallback() {
                        @Override
                        public void run(Boolean response2) {
                            if (response2) {
                                helpers.saveObj("pf","user",helpers.getUser().parse());
                                requestCallback.run(); //true
                            }
                            requestCallback.run(); // false
                        }
                    });
                } else if (response.optString("status").equalsIgnoreCase("ko")) {
                    Helpers.showAlert(response.optInt("errno"));
                    requestCallback.run(); //false
                }
            }
        }).execute();
    }

    public void categoryRequest(final RequestCallback requestCallback) {
        new ServiceRequest(Helpers.getString(R.string.getcategories_uri), new JSONObject(), new ServiceCallback() {
            @Override
            public void run(JSONObject response) {
                if (response.optString("status").equalsIgnoreCase("ok")) {
                    JSONObject responseData = response.optJSONObject("data");
                    JSONArray responseCategories = responseData.optJSONArray("categories");
                    ArrayList<Category> categories = new ArrayList<Category>();
                    for (int i=0;i<responseCategories.length();i++) {
                        JSONObject responseCategory = responseCategories.optJSONObject(i);
                        ArrayList<SubCategory> subcategories = new ArrayList<SubCategory>();
                        for (int j=0;j<responseCategory.optJSONArray("subcategories").length();j++) {
                            JSONObject responseSubCategory = responseCategory.optJSONArray("subcategories").optJSONObject(j);
                            subcategories.add(new SubCategory(responseSubCategory.optInt("subcategoryid"), responseSubCategory.optString("subcategoryname")));
                        }
                        categories.add(new Category(responseCategory.optInt("categoryid"),responseCategory.optString("category"),subcategories));
                    }
                    helpers.setCategories(categories);
                    helpers.saveObj("pf","user",Parser.parseCategories(helpers.getCategories()));
                    requestCallback.run(); //true
                } else if (response.optString("status").equalsIgnoreCase("ko")) {
                    Helpers.showAlert(response.optInt("errno"));
                    requestCallback.run(); //false
                }
            }
        }).execute();
    }

    public void askSuggestionRequest(final QuestionData questionData, final RequestCallback requestCallback) {
        helpers.setSpinner();
        new ServiceRequest(Helpers.getString(R.string.asksuggestion_uri), questionData.parse(), new ServiceCallback() {
            @Override
            public void run(JSONObject response) {
                helpers.removeSpinner();
                if (response.optString("status").equalsIgnoreCase("ok")) {
                    JSONObject responseData = response.optJSONObject("data");
                    Question question = new Question(responseData.optInt("questionid"),questionData,responseData.optInt("timestamp"),null);
                    helpers.getQuestions().add(question);
                    helpers.saveObj("pf", "user", Parser.parseQuestions(helpers.getQuestions()));
                    requestCallback.run(); //true
                } else if (response.optString("status").equalsIgnoreCase("ko")) {
                    Helpers.showAlert(response.optInt("errno"));
                    requestCallback.run(); //false
                }
            }
        }).execute();
    }

    public void getSuggestsRequest(int[] questionsId, final RequestCallback requestCallback) {
        new ServiceRequest(Helpers.getString(R.string.getsuggests_uri), questionsId, new ServiceCallback() {
            @Override
            public void run(JSONObject response) {
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
                    requestCallback.run(); //true
                } else if (response.optString("status").equalsIgnoreCase("ko")) {
                    Helpers.showAlert(response.optInt("errno"));
                    requestCallback.run(); //false
                }
            }
        }).execute();
    }

    //TODO Snellire CommunicationHandler con i parser
}
