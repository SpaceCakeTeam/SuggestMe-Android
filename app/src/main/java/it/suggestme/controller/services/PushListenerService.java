package it.suggestme.controller.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONArray;

import it.suggestme.R;
import it.suggestme.controller.Helpers;
import it.suggestme.controller.interfaces.RequestCallback;
import it.suggestme.model.Question;
import it.suggestme.ui.SceltaCategorie;

/**
 * Created by federicomaggi on 28/08/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class PushListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    public PushListenerService(){

    }

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        Helpers.setAppContext(this);
        Helpers.shared().setDataUser();

        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */
        JSONArray unrepliedQuestion = new JSONArray();
        boolean replied;

        for( Question aQuest : Helpers.shared().getQuestions() ) {

            replied = aQuest.getSuggest() != null;

            if( !replied ) {
                unrepliedQuestion.put(aQuest.getId());
            }
        }

        if (Helpers.shared().getQuestions() != null && Helpers.shared().getQuestions().size() > 0) {
            Helpers.shared().communicationHandler.getSuggestsRequest(unrepliedQuestion, new RequestCallback() {
                @Override
                public void callback(Boolean success) {

                    if (!success)
                        return;

                    // Do nothing
                }
            });
        }
        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, SceltaCategorie.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Helpers.FROMNOTIFICATION,true);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(Helpers.getString(R.string.push_notification_message))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        try{
            notificationBuilder
                    .setLargeIcon(Bitmap.createBitmap(((BitmapDrawable)getDrawable(R.drawable.ic_launcher_suggestme)).getBitmap()));
        }catch (NullPointerException e) {
            Log.e(Helpers.getString(R.string.logerror),"Can't get Large Icon Bitmap");
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
