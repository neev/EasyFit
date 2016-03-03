package com.example.android.easyfitness.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class EasyfitnessSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static EasyfitnessSyncAdapter sEasyfitnessSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("EasyfitnessSyncService", "onCreate - EasyfitnessSyncService");
        synchronized (sSyncAdapterLock) {
            if (sEasyfitnessSyncAdapter == null) {
                sEasyfitnessSyncAdapter = new EasyfitnessSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sEasyfitnessSyncAdapter.getSyncAdapterBinder();
    }
}