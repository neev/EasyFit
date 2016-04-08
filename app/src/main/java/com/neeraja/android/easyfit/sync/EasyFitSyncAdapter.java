package com.neeraja.android.easyfit.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.neeraja.android.easyfit.MainActivity;
import com.neeraja.android.easyfit.R;
import com.neeraja.android.easyfit.SessionManagement;
import com.neeraja.android.easyfit.Utilities;
import com.neeraja.android.easyfit.data.EasyFitnessContract;
import com.neeraja.android.easyfit.data.WorkoutOptions;
import com.neeraja.android.easyfit.data.WorkoutRecord;
import com.neeraja.android.easyfit.notification.MyAlarmService;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;


/**
 * Created by neeraja on 10/22/2015.
 */
public class EasyFitSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String Log_tag = EasyFitSyncAdapter.class.getSimpleName();

    public static final String ACTION_DATA_UPDATED = "com.example.android.easyfitness.ACTION_DATA_UPDATED";

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final int WEATHER_NOTIFICATION_ID = 3004;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOCATION_STATUS_OK, LOCATION_STATUS_SERVER_DOWN, LOCATION_STATUS_SERVER_INVALID,  LOCATION_STATUS_UNKNOWN, LOCATION_STATUS_INVALID})
    public @interface LocationStatus {}
    public static final int LOCATION_STATUS_OK = 0;
    public static final int LOCATION_STATUS_SERVER_DOWN = 1;
    public static final int LOCATION_STATUS_SERVER_INVALID = 2;
    public static final int LOCATION_STATUS_UNKNOWN = 3;
    public static final int LOCATION_STATUS_INVALID = 4;
    // for user auth Id
    String authId;
    //for DB inserts
    Vector<ContentValues> workoutOptions_values = new Vector <ContentValues>();
    Vector<ContentValues> workoutRecords_values = new Vector <ContentValues>();
    Vector<ContentValues> userProfile_contentValues = new Vector <ContentValues>();

    Context mContext;

    public EasyFitSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        // Initialize Firebase with the application context
        Firebase.setAndroidContext(context);

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(Log_tag, "onPerformSync Called.");
        Log.v(Log_tag, "Starting sync");
        mContext = getContext();
        SessionManagement session = new SessionManagement(mContext);
        // get user data from session
        HashMap<String, String> user = session.getUserFirebaseAuthId();
        // name
        authId = user.get(SessionManagement.KEY_NAME);


        if(authId!=null) {


            //***************************************
            final Firebase ref = new Firebase(mContext.getResources().getString(R.string
                    .firebase_url)
                    +"/workout");
            //To read workout options from firebase
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
                        workoutOptions_values.add(workout_values);
                        System.out.println("values size : " + workoutOptions_values.size());
                    }


                    System.out.println("*****inserting values*****");
                    ContentValues[] cvArray = new ContentValues[workoutOptions_values.size()];
                    workoutOptions_values.toArray(cvArray);
                    int inserted_into_DB = mContext.getContentResolver().bulkInsert(
                            EasyFitnessContract.WorkOutOptions.CONTENT_URI, cvArray);
                    Log.v(Log_tag, "WORKOUT OPTIONS Succesfully Inserted : " + String.valueOf
                            (workoutOptions_values.size()) + "******* " + inserted_into_DB);


                    System.out.println("***** Out of for loop*****");
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }

            });

//*************************************************************
            final Firebase ref_workout_records = new Firebase(mContext.getResources().getString(R.string
                    .firebase_url)
                    +"/recordedWorkoutList/"+authId);

            ref_workout_records.addListenerForSingleValueEvent
                    (new

                             ValueEventListener() {

                                 @Override
                                 public void onDataChange (DataSnapshot workout_records){

                                     ref_workout_records.addListenerForSingleValueEvent(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(DataSnapshot querySnapshot) {
                                             for (DataSnapshot mWorkoutRecordsYear : querySnapshot.getChildren()) {
                                                 //BlogPost post = postSnapshot.getValue(BlogPost.class);
                                                 for (DataSnapshot mWorkoutRecordsMonth : mWorkoutRecordsYear
                                                         .getChildren()) {
                                                     for (DataSnapshot mWorkoutRecordsDate : mWorkoutRecordsMonth
                                                             .getChildren()) {
                                                         for (DataSnapshot mWorkoutRecordsDetail :
                                                                 mWorkoutRecordsDate
                                                                         .getChildren()) {

                                                             WorkoutRecord workoutRecord = mWorkoutRecordsDetail
                                                                     .getValue(WorkoutRecord.class);
                                                             String year = mWorkoutRecordsYear.getKey().toString();
                                                             String month = mWorkoutRecordsMonth.getKey()
                                                                     .toString();
                                                             String date = mWorkoutRecordsDate.getKey().toString();

                                                             String pushID = mWorkoutRecordsDetail
                                                                     .getKey();

                                                             System.out.println(year + "-" + month + "-" + date);
                                                             System.out.println(workoutRecord.getWorkoutDesc() + " - " +
                                                                     workoutRecord
                                                                             .getWorkoutDuration());

                                                             Date record_workout_fulldate = new Date((Integer.parseInt
                                                                     (year)-1900), (Utilities.getMonthinNumber(month)-1),
                                                                     Integer.parseInt(date));
                                                             ContentValues userRecoredeworkoutValues = new ContentValues();
                                                             ContentValues record_values = new
                                                                     ContentValues();
                                                             record_values.put
                                                                     (EasyFitnessContract.UserWorkOutRecord.COLUMN_USERDEATIL_AUTHENTIFICATION_ID,
                                                                             authId);
                                                             record_values.put(EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_DESCRIPTION,
                                                                     workoutRecord.getWorkoutDesc());
                                                             record_values.put(EasyFitnessContract.UserWorkOutRecord
                                                                     .COLUMN_WORKOUT_DURATION, workoutRecord.getWorkoutDuration());
                                                             record_values.put(EasyFitnessContract.UserWorkOutRecord
                                                                             .COLUMN_WORKOUT_RECORDED_DATE_YEAR,
                                                                     year);
                                                             record_values.put(EasyFitnessContract.UserWorkOutRecord
                                                                             .COLUMN_WORKOUT_RECORDED_DATE_MONTH,
                                                                     Utilities.getMonthinNumber
                                                                             (month));
                                                             record_values.put(EasyFitnessContract.UserWorkOutRecord
                                                                             .COLUMN_WORKOUT_RECORDED_DATE_DATE,
                                                                     date);
                                                             record_values.put(EasyFitnessContract.UserWorkOutRecord
                                                                             .COLUMN_PUSH_ID,
                                                                     pushID);
                                                             record_values.put(EasyFitnessContract.UserWorkOutRecord
                                                                     .COLUMN_FULL_DATE, String.valueOf(record_workout_fulldate));
                                                             workoutRecords_values.add(record_values);
                                                             System.out.println("values size : " + workoutRecords_values.size());
                                                         }
                                                     }
                                                 }


                                                 System.out.println("*****inserting values*****");
                                                 ContentValues[] cvArrayWR = new
                                                         ContentValues[workoutRecords_values.size()];
                                                 workoutRecords_values.toArray(cvArrayWR);
                                                 int records_inserted_intoDB = mContext
                                                         .getContentResolver().bulkInsert(
                                                                 EasyFitnessContract.UserWorkOutRecord.CONTENT_URI, cvArrayWR);
                                                 Log.v(Log_tag, "WORKOUT RECORDS Succesfully " +
                                                         "Inserted : " + String.valueOf
                                                         (workoutRecords_values.size()) + "******* " + records_inserted_intoDB);


                                             }
                                         }

                                         public void onCancelled(FirebaseError error) {


                                         }
                                     });
                                 }

                                 @Override
                                 public void onCancelled (FirebaseError error){
                                 }
                             });
        }

        updateWidgets();
        Intent service = new Intent(getContext(), MyAlarmService.class);
        getContext().startService(service);
        //notifyWorkoutStatus();
        System.out.println("*****ASYNC TASK ENDED*****");

        return;
    }




    private void updateWidgets() {
        Context context = getContext();
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }
    private void notifyWeather() {
        Context context = getContext();
        final Calendar currentDate = Calendar.getInstance();
        int mYear = currentDate.get(Calendar.YEAR);
        int mMonth = currentDate.get(Calendar.MONTH);
        int mDate = currentDate.get(Calendar.DATE);
        Date startDate = new Date((mYear-1900),(mMonth),mDate);

        // Session Manager
        SessionManagement session = new SessionManagement(context);
        // get user data from session
        HashMap<String, String> user = session.getUserFirebaseAuthId();
        // name
        String authId = user.get(SessionManagement.KEY_NAME);
        Uri recordForThisWeekUri = EasyFitnessContract.UserWorkOutRecord
                .buildWorkoutRecordWithUserAuthIdandThisWeek
                        (authId, String.valueOf(startDate), String
                                .valueOf(startDate));
                Cursor cursor = context.getContentResolver().query(recordForThisWeekUri,
                        null,
                        null,
                        null,
                        "workout_recorded_year desc, " +
                                "workout_recorded_month desc, workout_recorded_date desc");
        // these indices must match the projection
        final int COL_DESC = 2;
        final int COL_DURATION = 1;
        final int COL_YEAR = 3;
        final int COL_MONTH = 4;
        final int COL_DATE = 5;
        final int COL_DAY = 6;
                if (cursor.moveToFirst()) {

                    String description = cursor.getString(COL_DESC);
                    String duration = cursor.getString(COL_DURATION);

                    String _year = cursor.getString(COL_YEAR);
                    String _month = cursor.getString(COL_MONTH);
                    String _date = cursor.getString(COL_DATE);
                    String _day = cursor.getString(COL_DAY);


                    Resources resources = context.getResources();


                    String title = context.getString(R.string.app_name);

                    // Define the text of the forecast.
                    String contentText = "Please log in your Workout today";
                    // NotificationCompatBuilder is a very convenient way to build backward-compatible
                    // notifications.  Just throw in some data.
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getContext())
                                    .setColor(resources.getColor(R.color.colorPrimaryDark))
                                    .setSmallIcon(R.drawable.run)

                                    .setContentTitle(title)
                                    .setContentText(contentText);

                    // Make something interesting happen when the user clicks on the notification.
                    // In this case, opening the app is sufficient.
                    Intent resultIntent = new Intent(context, MainActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
                    mNotificationManager.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());


                }
                cursor.close();
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
        EasyFitSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

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