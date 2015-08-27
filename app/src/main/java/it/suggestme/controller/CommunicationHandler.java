package it.suggestme.controller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.suggestme.R;

import it.suggestme.controller.interfaces.ServiceRequest;
import it.suggestme.controller.interfaces.RequestCallback;
import it.suggestme.controller.services.ServiceCallback;
import it.suggestme.controller.services.DrawableServiceRequest;

import it.suggestme.model.Category;
import it.suggestme.model.Question;
import it.suggestme.model.QuestionData;
import it.suggestme.model.Suggest;

public class CommunicationHandler {

    public CommunicationHandler() {}

    public void registrationRequest( final RequestCallback requestCallback, JSONObject userData ) {
        Helpers.shared().setSpinner();
        new ServiceRequest(Helpers.getString(R.string.registration_uri), userData, new ServiceCallback() {
            @Override
            public void callback(JSONObject response) {
                Helpers.shared().removeSpinner();

                if (response.optString("status").equalsIgnoreCase("ok")) {
                    JSONObject responseData = response.optJSONObject("data");
                    Log.i(Helpers.getString(R.string.loginfo), responseData.toString());

                    Helpers.shared().getAppUser().setId(responseData.optInt("userid"));
                    categoryRequest(new RequestCallback() {
                        @Override
                        public void callback(Boolean success) {
                            if (success) {
                                Helpers.shared().saveObj(Helpers.USERLBL, Helpers.shared().getAppUser().parse());
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
        new DrawableServiceRequest(url, new ServiceCallback() {
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
