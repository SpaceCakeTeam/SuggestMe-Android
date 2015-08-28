package it.suggestme.controller.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.suggestme.R;
import it.suggestme.controller.Helpers;
import it.suggestme.controller.interfaces.RequestCallback;

/**
 * Created by federicomaggi on 27/08/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"sm_push_channel"};

    public RegistrationIntentService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        try{
            Helpers.shared().setInstanceID();
            Helpers.shared().setGCMToken();

            JSONObject jsonToken = Helpers.shared().getPushTokenRequestData();

            Log.i(Helpers.getString(R.string.loginfo), "SENT TOKEN:: " + jsonToken.toString());

            Helpers.shared().communicationHandler.sendPushToken(jsonToken, new RequestCallback() {
                @Override
                public void callback(Boolean success) {
                    if (!success)
                        return;

                    Log.i(Helpers.getString(R.string.loginfo), "GCM REGISTRATION COMPLETED");
                    Intent registrationComplete = new Intent(Helpers.REGISTRATION_COMPLETE);
                    LocalBroadcastManager.getInstance(Helpers.shared().getAppContext()).sendBroadcast(registrationComplete);
                }
            });

            subscribeTopics(Helpers.shared().getGCMTOken());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(Helpers.getString(R.string.logerror)+"/"+TAG, "CAN'T HANDLE INTENT ERR: ["+e.toString()+"]");
        }
    }

    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

}
