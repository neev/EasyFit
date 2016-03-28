package com.example.android.easyfitness.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by neeraja on 10/22/2015.
 */
public class EasyFitSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static EasyFitSyncAdapter sEasyFitSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("EasyFitSyncService", "onCreate - EasyFitSyncService");
        synchronized (sSyncAdapterLock) {
            if (sEasyFitSyncAdapter == null) {
                sEasyFitSyncAdapter = new EasyFitSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sEasyFitSyncAdapter.getSyncAdapterBinder();
    }
}