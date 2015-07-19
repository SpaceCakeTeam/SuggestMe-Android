package it.suggestme.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import it.suggestme.model.Category;
import it.suggestme.model.Question;

import it.suggestme.model.User;
import it.suggestme.model.UserData;

public class Helpers {

    private static Helpers mInstance;

    public final static float screenHeight = getDisplay().y;
    public final static float screenWidth = getDisplay().x;
    public final static float navBarHeight = 44; //TODO
    public final static float statusBarHeight = 20; //TODO

    private static Context ctx;

    private User user;
    private ArrayList<Category> categories;
    private ArrayList<Question> questions;
    private Question currentQuestion;

    private static JSONObject alerts;

    private Helpers() {
        try {
            alerts = new JSONObject() //TODO
                    .put("0", new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("-1", new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("-2", new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("-3", new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("1", new JSONObject().put("title", "").put("message", "").put("cancel", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Helpers shared() {
        if (mInstance == null)
            mInstance = new Helpers();
        return mInstance;
    }

    public static void setCtx(Context currCtx) {
        ctx = currCtx;
    }

    public static Context getCtx() {
        return ctx;
    }

    private static Point getDisplay() {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
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
        return new AlertDialog.Builder(ctx).setTitle(alert.optString("title")).setMessage(alert.optString("message")).show();
    }

    public void setSpinner() {
        //TODO
    }

    public void removeSpinner() {
        //TODO
    }

    public boolean keyExist(String name ,String key) {
        SharedPreferences sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    public void saveObj(String name, String key, JSONObject obj) {
        SharedPreferences sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, obj.toString());
        editor.apply();
    }

    public JSONObject getSavedObj(String name, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        if(!keyExist(name, key))
            return null;
        try {
            return (JSONObject) new JSONTokener(sp.getString(key,null)).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveString(String name, String key, String text) {
        SharedPreferences sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, text);
        editor.apply();
    }

    public String getSavedString(String name, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        if(!keyExist(name, key))
            return null;
        return sp.getString(key, null);
    }

    public void removePreference(String name, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> newcategories) {
        categories = newcategories;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public boolean setDataUser(){
        if (keyExist("pf","user")) {
            user = Parser.generateUser(getSavedObj("pf", "user"));
            categories = Parser.generateCategories(getSavedObj("pf", "categories"));
            questions = Parser.generateQuestions(getSavedObj("pf", "questions"));
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

    public void getFacebookAccount() {
        //TODO
    }

    public void getTwitterAccount() {
        //TODO
    }
}
