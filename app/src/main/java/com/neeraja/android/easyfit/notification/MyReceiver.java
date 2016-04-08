package com.neeraja.android.easyfit.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.neeraja.android.easyfit.sync.EasyFitSyncAdapter;

/**
 * Created by neeraja on 4/5/2016.
 */
public class MyReceiver extends BroadcastReceiver
{


@Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        //super.onReceive(context, intent);
        if (EasyFitSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            Intent service1 = new Intent(context, MyAlarmService.class);
            context.startService(service1);
        }
    }
}