package it.suggestme.controller.services;

import android.content.Intent;

/**
 * Created by federicomaggi on 27/08/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class InstanceIDListenerService extends com.google.android.gms.iid.InstanceIDListenerService {

    private static final String TAG = "InstanceIDLS";

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
