package it.suggestme.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import io.fabric.sdk.android.Fabric;
import it.suggestme.R;

import it.suggestme.controller.interfaces.HelperCallback;
import it.suggestme.controller.services.RegistrationIntentService;
import it.suggestme.model.Category;
import it.suggestme.model.Question;

import it.suggestme.model.SubCategory;
import it.suggestme.model.User;
import it.suggestme.model.UserData;
import it.suggestme.ui.fragment.NavigationDrawerFragment;

/**
 * Created by federicomaggi on 27/08/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class Helpers {

    private static final String FILENAME = "preferencesfile";
    private static JSONObject alerts;

    public  static final int ANON     = 0;
    public  static final int FACEBOOK = 1;
    public  static final int TWITTER  = 2;
    private static       int LOGGEDWITH;
    private static final String LOGGEDWTHLBL = "loggedwith";

    public static final String USERLBL            = "user";
    public static final String INSTANCEIDSAVEDLBL = "instanceIDsavedOnServer";
    public static final String TOKENLBL           = "token";
    public static final String DEVICEOSLBL        = "device";
    public static final String DEVICEOSNAME       = "android";
    public static final String FROMNOTIFICATION   = "intentFromNotification";

    private static Helpers mInstance;
    private static Context mAppContext;

    private Drawable mProfilePic;
    private NavigationDrawerFragment mNavDrawer;
    public  CommunicationHandler communicationHandler;
    private User mAppUser;
    private ArrayList<Category> categories;
    private ArrayList<Question> questions;

    // Social
    private CallbackManager mCallbackManager;
    private TwitterAuthClient mTwitterAuthClient;

    // Google GCM Push
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final int    PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private InstanceID mInstanceID;
    private String     mGCMToken;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    private Helpers() {
        try {
            alerts = new JSONObject() //TODO
                    .put( "0" , new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("-1", new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("-2" , new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("-3" , new JSONObject().put("title", "").put("message", "").put("cancel", ""))
                    .put("-10", new JSONObject().put("title",getString(R.string.error)).put("message",getString(R.string.profilePic404)).put("cancel",getString(R.string.ok)))
                    .put("-11", new JSONObject().put("title",getString(R.string.error)).put("message",getString(R.string.noGplayServices)).put("cancel",getString(R.string.ok)))
                    .put("1", new JSONObject().put("title", "").put("message", "").put("cancel", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LOGGEDWITH = -1;
        communicationHandler = new CommunicationHandler();
    }

    public static Helpers shared() {
        if (mInstance == null)
            mInstance = new Helpers();
        return mInstance;
    }

    public void setLoggedWith(int what){
        saveInt(LOGGEDWTHLBL,what);
        LOGGEDWITH = what;
    }

    public int getLoggedWith(){
        if( LOGGEDWITH == -1)
            LOGGEDWITH = getSavedInt(LOGGEDWTHLBL);
        return LOGGEDWITH;
    }

    public void initFBSdk(){
        if(mCallbackManager != null)
           return;

        FacebookSdk.sdkInitialize(Helpers.shared().getAppContext());
        mCallbackManager = CallbackManager.Factory.create();
    }

    public void initFabric(){
        if(mTwitterAuthClient != null)
            return;

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_api_key), getString(R.string.twitter_secret));
        Fabric.with(mAppContext, new Twitter(authConfig));
        mTwitterAuthClient = new TwitterAuthClient();
    }

    public boolean testGooglePlayServices(Activity activity){

        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mAppContext);
        if( result != ConnectionResult.SUCCESS ) {
            if( GooglePlayServicesUtil.isUserRecoverableError(result) ) {
                GooglePlayServicesUtil.getErrorDialog(result, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    public void registerToGCM( Activity activity ) {

    }

    public void setInstanceID() {
        mInstanceID = InstanceID.getInstance(getAppContext());
    }

    public InstanceID getInstanceID() {
        return mInstanceID;
    }

    public JSONObject getPushTokenRequestData() throws JSONException {

        return new JSONObject()
                .put(TOKENLBL,getGCMTOken())
                .put(DEVICEOSLBL,DEVICEOSNAME);
    }

    public void setGCMToken() throws IOException {
        mGCMToken = getInstanceID().getToken(getString(R.string.google_gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
    }

    public String getGCMTOken(){
        return mGCMToken;
    }

    public BroadcastReceiver getBroadcastReceiver(){
        return mRegistrationBroadcastReceiver;
    }

    public void setBroadcastReceiver( BroadcastReceiver br ){
        mRegistrationBroadcastReceiver = br;
    }

    public static void setAppContext(Context currCtx) {
        mAppContext = currCtx;
    }

    public Context getAppContext() {
        return mAppContext;
    }

    private static int getDisplayHeight() {
        WindowManager wm = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    private static int getDisplayWidth() {
        WindowManager wm = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static String getString(int resId) {
        return mAppContext.getString(resId);
    }

    public static boolean connected() {
        ConnectivityManager cm = (ConnectivityManager) mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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

        return new AlertDialog.Builder(mAppContext).setTitle(alert.optString("title")).setMessage(alert.optString("message")).show();
    }

    public void setSpinner() {
        //TODO
    }

    public void removeSpinner() {
        //TODO
    }

    public boolean keyExist(String key) {
        SharedPreferences sp = mAppContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    public void saveObj(String key, JSONObject obj) {
        SharedPreferences sp = mAppContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, obj.toString());
        editor.apply();
    }

    public JSONObject getSavedObj(String key) {
        SharedPreferences sp = mAppContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        if(!keyExist(key))
            return null;
        try {
            return (JSONObject) new JSONTokener(sp.getString(key,null)).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(getString(R.string.loginfo),"Error loading saved object "+e.toString());
            return null;
        }
    }

    public void saveString(String key, String text) {
        SharedPreferences sp = mAppContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, text);
        editor.apply();
    }

    public String getSavedString(String key) {
        SharedPreferences sp = mAppContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        if(!keyExist(key))
            return null;
        return sp.getString(key, null);
    }

    public void saveInt(String key, int val) {
        SharedPreferences sp = mAppContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, val);
        editor.apply();
    }

    public int getSavedInt(String key) {
        SharedPreferences sp = mAppContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        if(!keyExist(key))
            return -1;
        return sp.getInt(key, -1);
    }

    public void removePreference(String key) {
        SharedPreferences sp = mAppContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public User getAppUser() {
        return mAppUser;
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

            Log.e(getString(R.string.loginfo), "CATEGORY FROM ID IS NULL. Given CAT ID: "
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
        Log.e(getString(R.string.logerror),"SUBCATEGORY NOT FOUND ID. Given CAT ID: "
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
        if (keyExist(USERLBL)) {
            mAppUser = Parser.generateUser(getSavedObj(USERLBL));
            categories = Parser.generateCategories(getSavedObj("categories"));
            if (keyExist("questions"))
                questions = Parser.generateQuestions(getSavedObj("questions"));
            else questions = new ArrayList<>();
            return true;
        } else {
            mAppUser = new User(-1, true, new UserData("","",0, UserData.Gender.u,""));
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

    public void performFacebookLogin(Fragment fragment, final HelperCallback hpCallback) {

        LoginManager.getInstance().registerCallback(Helpers.shared().getFBCallbackMng(),
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    Profile userProfile = Profile.getCurrentProfile();

                    GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

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
                                        Date theDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ITALIAN).parse(birthday);
                                        lepoch = theDate.getTime() / 1000;
                                        epoch = (int)lepoch;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    mAppUser = new User( mAppUser !=null? mAppUser.getId():-1,
                                        false,
                                        new UserData(
                                                Profile.getCurrentProfile().getFirstName(),
                                                Profile.getCurrentProfile().getLastName(),
                                                epoch,
                                                gender,
                                                response.getJSONObject().getString("email")
                                        ));

                                    setLoggedWith(FACEBOOK);
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
                    hpCallback.callback(false);
                }

                @Override
                public void onError(FacebookException exception) {
                    hpCallback.callback(false);
                }
            });

        ArrayList<String> fbperm = new ArrayList<String>();
        fbperm.add("public_profile");
        fbperm.add("email");
        fbperm.add("user_birthday");


        LoginManager.getInstance().logInWithReadPermissions(fragment, fbperm);
    }

    public TwitterAuthClient getTwitterClient(){
        return this.mTwitterAuthClient;
    }

    public void performTwitterLogin(final Fragment fragment, final HelperCallback hpCallback) {
        mTwitterAuthClient.authorize(fragment.getActivity(), new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                TwitterSession session = Twitter.getSessionManager().getActiveSession();

                String uname = session.getUserName();
                String lname = "(Twitter)";

                mAppUser = new User( mAppUser!=null?mAppUser.getId():-1,
                        false,
                        new UserData(
                                uname,
                                lname,
                                0,
                                UserData.Gender.u,
                                ""
                        ));
                setLoggedWith(TWITTER);
                hpCallback.callback(true);
            }

            @Override
            public void failure(TwitterException e) {
                hpCallback.callback(false);
            }
        } );
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
