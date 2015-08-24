package it.suggestme.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.CollectionMapper;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import it.suggestme.R;
import it.suggestme.model.Category;
import it.suggestme.model.Question;

import it.suggestme.model.SubCategory;
import it.suggestme.model.User;
import it.suggestme.model.UserData;
import it.suggestme.ui.fragment.NavigationDrawerFragment;

public class Helpers {

    public interface HelperCallback{ void callback(Boolean success); }

    private static Helpers mInstance;

    private static Context ctx;

    private final static String filename = "preferencesfile";

    private User user;
    private ArrayList<Category> categories;
    private ArrayList<Question> questions;
    private Question currentQuestion;
    private CallbackManager mCallbackManager;
    private Drawable mProfilePic;
    private NavigationDrawerFragment mNavDrawer;

    public CommunicationHandler communicationHandler;
    private static JSONObject alerts;

    private Helpers() {
        try {
            alerts = new JSONObject() //TODO
                    .put( "0" , new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("-1", new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("-2" , new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("-3" , new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("-10", new JSONObject().put("title","Errore").put("message","Impossibile scaricare la foto profile").put("cancel","Ok"))
                    .put("1", new JSONObject().put("title", "").put("message", "").put("cancel", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        communicationHandler = new CommunicationHandler();
    }

    public void initFBSdk(){
        FacebookSdk.sdkInitialize(Helpers.shared().getCtx());
        mCallbackManager = CallbackManager.Factory.create();
    }
    public static Helpers shared() {
        if (mInstance == null)
            mInstance = new Helpers();
        return mInstance;
    }

    public void setCtx(Context currCtx) {
        ctx = currCtx;
    }

    public Context getCtx() {
        return ctx;
    }

    private static int getDisplayHeight() {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    private static int getDisplayWidth() {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static String getString(int resId) {
        return ctx.getString(resId);
    }

    public static boolean connected() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }

    public static String getTextLocalized(String text) {
        return text;
    }

    public static String getAppFont() {
        return "Helvetica Neue";
    }

    public static AlertDialog showAlert(int id) {
        JSONObject alert = alerts.optJSONObject(id + "");
        if( alert == null ) {
            try {
                alert = new JSONObject().put("title", "Impossibile collegarsi al server").put("message", "Ritenta più tardi").put("cancel", "Ok");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return new AlertDialog.Builder(ctx).setTitle(alert.optString("title")).setMessage(alert.optString("message")).show();
    }

    public void setSpinner() {
        //TODO
    }

    public void removeSpinner() {
        //TODO
    }

    public boolean keyExist(String key) {
        SharedPreferences sp = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    public void saveObj(String key, JSONObject obj) {
        SharedPreferences sp = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, obj.toString());
        editor.apply();
    }

    public JSONObject getSavedObj(String key) {
        SharedPreferences sp = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        if(!keyExist(key))
            return null;
        try {
            return (JSONObject) new JSONTokener(sp.getString(key,null)).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveString(String key, String text) {
        SharedPreferences sp = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, text);
        editor.apply();
    }

    public String getSavedString(String key) {
        SharedPreferences sp = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        if(!keyExist(key))
            return null;
        return sp.getString(key, null);
    }

    public void saveInt(String key, int val) {
        SharedPreferences sp = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, val);
        editor.apply();
    }

    public int getSavedInt(String key) {
        SharedPreferences sp = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        if(!keyExist(key))
            return -1;
        return sp.getInt(key, -1);
    }

    public void removePreference(String key) {
        SharedPreferences sp = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Category> getCategories() {

        if( categories == null )
            setCategories(Parser.generateCategories(getSavedObj("categories")));
        return categories;
    }

    public void setCategories(ArrayList<Category> newcategories) {
        categories = newcategories;
    }

    public Category getCategoryFromID( int catid ) {

        for( Category aCat : categories ) {
            if ( aCat.getId() == catid ) {
                return aCat;
            }
        }

        return null;
    }

    public ArrayList<SubCategory> getSubCategoriesFromCategoryID( int categoryId ) {

        for( Category aCat : categories ) {
            if( aCat.getId() == categoryId ) {
                return aCat.getSubCategories();
            }
        }
        return null;
    }

    public SubCategory getSubcategoryFromID( int catId, int subcatId ) {

        if( this.getCategoryFromID(catId) == null ){

            Log.i(getString(R.string.loginfo), "CATEGORY FROM ID IS NULL. Given CAT ID: "
                    .concat(((Integer) catId).toString())
                    .concat("Given SUBCAT ID: ")
                    .concat(((Integer) subcatId).toString()));

            return null;
        }

        for (SubCategory aSubCat : this.getCategoryFromID(catId).getSubCategories()) {
            if (aSubCat.getId() == subcatId) {
                return aSubCat;
            }
        }
        Log.i(getString(R.string.loginfo),"SUBCATEGORY NOT FOUND ID. Given CAT ID: "
                .concat(((Integer)catId).toString())
                .concat("Given SUBCAT ID: ")
                .concat(((Integer) subcatId).toString()));
        return null;
    }

    public ArrayList<Question> getQuestions() {

        if( questions == null )
            questions = Parser.generateQuestions(getSavedObj("questions"));

        return questions;
    }

    public boolean setDataUser(){
        if (keyExist("user")) {
            user = Parser.generateUser(getSavedObj("user"));
            categories = Parser.generateCategories(getSavedObj("categories"));
            if (keyExist("questions"))
                questions = Parser.generateQuestions(getSavedObj("questions"));
            else questions = new ArrayList<>();
            return true;
        } else {
            user = new User(-1, true, new UserData("","",0, UserData.Gender.u,""));
            return false;
        }
    }

    public boolean checkQuestionText(String text) {
        //TODO
        return true;
    }

    public CallbackManager getFBCallbackMng() {
        return this.mCallbackManager;
    }

    public void getFacebookAccount( Fragment fragment, final HelperCallback hpCallback ) {

        Log.i(getString(R.string.loginfo), "FB LOGIN REQUEST STARTING NOW");

        LoginManager.getInstance().registerCallback(Helpers.shared().getFBCallbackMng(),
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.i(getString(R.string.loginfo), "Success Login" + loginResult.toString());

                    Profile userProfile = Profile.getCurrentProfile();
                    Log.i(Helpers.getString(R.string.loginfo), "Name: " + userProfile.getName());

                    GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.i(Helpers.getString(R.string.loginfo), response.toString());

                                UserData.Gender gender;

                                try {

                                    switch (response.getJSONObject().getString("gender")){
                                        case "male":
                                            gender = UserData.Gender.m;
                                            break;
                                        case "female":
                                            gender = UserData.Gender.f;
                                            break;
                                        default:
                                            gender = UserData.Gender.u;
                                    }

                                    String birthday = response.getJSONObject().getString("birthday").replace("\\","");
                                    long lepoch;
                                    int epoch = 0;
                                    try {
                                        Date theDate = new SimpleDateFormat("MM/dd/yyyy").parse(birthday);
                                        lepoch = theDate.getTime() / 1000;
                                        epoch = (int)lepoch;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    user = new User( user!=null?user.getId():-1,
                                        false,
                                        new UserData(
                                                Profile.getCurrentProfile().getFirstName(),
                                                Profile.getCurrentProfile().getLastName(),
                                                epoch,
                                                gender,
                                                response.getJSONObject().getString("email")
                                        ));

                                    hpCallback.callback(true);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    hpCallback.callback(false);
                                }
                            }
                        });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();

                }

                @Override
                public void onCancel() {
                    // App code
                    Log.i(getString(R.string.loginfo), "FB CANCELLED");
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Log.i(getString(R.string.loginfo), "FB ERROR");

                }
            });

        ArrayList<String> fbperm = new ArrayList<String>();
        fbperm.add("public_profile");
        fbperm.add("email");
        fbperm.add("user_birthday");


        LoginManager.getInstance().logInWithReadPermissions(fragment, fbperm);
        Log.i(getString(R.string.loginfo), "REQUEST SENT");
    }

    public void getTwitterAccount() {
        //TODO
    }

    public void setProfilePic( Drawable pic ) {
        mProfilePic = pic;
    }

    public Drawable getProfilePic() {
        return mProfilePic;
    }

    public void setNavigationDrawer(NavigationDrawerFragment fragment) {
        this.mNavDrawer = fragment;
    }

    public NavigationDrawerFragment getNavigationDrawer(){
        return this.mNavDrawer;
    }
}

