package com.example.android.easyfitness.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.util.Log;

import com.example.android.easyfitness.R;
import com.example.android.easyfitness.data.EasyFitnessContract;
import com.example.android.easyfitness.data.UserDetails;
import com.example.android.easyfitness.data.WorkoutOptions;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Vector;

public class SunshineSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = SunshineSyncAdapter.class.getSimpleName();
    public static final String ACTION_DATA_UPDATED =
            "com.example.android.easyfitness.ACTION_DATA_UPDATED";
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;
    Vector<ContentValues> values = new Vector <ContentValues>(5);
    int flag =1;
    private static final String[] NOTIFY_WEATHER_PROJECTION = new String[] {
            EasyFitnessContract.UserDetailEntry.COLUMN_USERDEATIL_AUTHENTIFICATION_ID,
            EasyFitnessContract.UserDetailEntry.COLUMN_USER_NAME,
            //EasyFitnessContract.UserDetailEntry.COLUMN_WORKOUT_DESCRIPTION,
           // EasyFitnessContract.UserDetailEntry.COLUMN_USER_WORKOUT_DURATION
    };

    // these indices must match the projection
    private static final int INDEX_USERDEATIL_AUTHENTIFICATION_ID = 0;
    private static final int INDEX_USER_NAME = 1;
    private static final int INDEX_WORKOUT_DESCRIPTION = 2;
    private static final int INDEX_WORKOUT_DURATION = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOCATION_STATUS_OK, LOCATION_STATUS_SERVER_DOWN, LOCATION_STATUS_SERVER_INVALID,  LOCATION_STATUS_UNKNOWN, LOCATION_STATUS_INVALID})
    public @interface LocationStatus {}

    public static final int LOCATION_STATUS_OK = 0;
    public static final int LOCATION_STATUS_SERVER_DOWN = 1;
    public static final int LOCATION_STATUS_SERVER_INVALID = 2;
    public static final int LOCATION_STATUS_UNKNOWN = 3;
    public static final int LOCATION_STATUS_INVALID = 4;



    public SunshineSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        // Initialize Firebase with the application context
        Firebase.setAndroidContext(context);


    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

        // We no longer need just the location String, but also potentially the latitude and
        // longitude, in case we are syncing based on a new Place Picker API result.
        Context context = getContext();

        Log.d(LOG_TAG, "onPerformSync Called.");
        Log.v(LOG_TAG, "Starting sync");

        Firebase ref = new Firebase(context.getResources().getString(R.string.firebase_url)
                +"/workout");
        // Attach an listener to read the data at our posts reference
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are " + snapshot.getChildrenCount() + " workout options");
                for (DataSnapshot workoutSnapshot : snapshot.getChildren()) {
                    //BlogPost post = postSnapshot.getValue(BlogPost.class);
                    WorkoutOptions workoutObject = workoutSnapshot.getValue(WorkoutOptions.class);
                    System.out.println(workoutObject.getId() + " - " + workoutObject
                            .getWorkout());


                    ContentValues workout_values = new ContentValues();
                    workout_values.put(EasyFitnessContract.WorkOutOptions.COLUMN_WORKOUT_ID, workoutObject.getId());
                    workout_values.put(EasyFitnessContract.WorkOutOptions.COLUMN_WORKOUT_DESCRIPTION, workoutObject.getWorkout());
                    values.add(workout_values);
                    System.out.println("values size : " + values.size());
                }
                if (flag == 1) {
                    System.out.println("*****inserting values*****");
                    ContentValues[] cvArray = new ContentValues[values.size()];
                    values.toArray(cvArray);
                    flag = getContext().getContentResolver().bulkInsert(
                            EasyFitnessContract.WorkOutOptions.CONTENT_URI, cvArray);
                    Log.v(LOG_TAG, "WORKOUT OPTIONS Succesfully Inserted : " + String.valueOf
                            (values.size()) + "******* " + flag);
                }
                System.out.println("***** Out of for loop*****");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }

        });
// add to database

        System.out.println("*****SYNC ADAPTER END*****");


        return;
    }



    private void updateWidgets() {
        Context context = getContext();
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);

    }



    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);

    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SunshineSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Sets the location status into shared preference.  This function should not be called from
     * the UI thread because it uses commit to write to the shared preferences.
     * @param c Context to get the PreferenceManager from.
     * @param locationStatus The IntDef value to set
     */
    static private void setLocationStatus(Context c, @LocationStatus int locationStatus){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_location_status_key), locationStatus);
        spe.commit();
    }
}