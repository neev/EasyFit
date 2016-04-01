package com.neeraja.android.easyfit.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by neeraja on 10/22/2015.
 */
public class EasyFitAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private EasyFitAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new EasyFitAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
