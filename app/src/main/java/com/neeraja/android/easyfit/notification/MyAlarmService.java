package com.neeraja.android.easyfit.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.neeraja.android.easyfit.MainActivity;
import com.neeraja.android.easyfit.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by neeraja on 4/5/2016.
 */
public class MyAlarmService extends Service {

    private NotificationManager mManager;
    private static final int WEATHER_NOTIFICATION_ID = 3004;
    Boolean todayWorkout;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();


    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        Calendar timefornotification = Calendar.getInstance(Locale.getDefault());
        timefornotification.set(Calendar.HOUR_OF_DAY, 9);
        timefornotification.set(Calendar.MINUTE, 00);
        timefornotification.set(Calendar.SECOND, 00);

        Long difference = calendar.getTimeInMillis() - timefornotification.getTimeInMillis();


        if (difference < 0) {
            notificationTimer.postDelayed(notificationCaller, -(difference));
        } else {
            notificationTimer.postDelayed(notificationCaller, difference);
        }
        Calendar nighttimefornotification = Calendar.getInstance(Locale.getDefault());
        nighttimefornotification.set(Calendar.HOUR_OF_DAY, 11);
        nighttimefornotification.set(Calendar.MINUTE, 00);
        nighttimefornotification.set(Calendar.SECOND, 00);

        Long nightdifference = calendar.getTimeInMillis() - nighttimefornotification.getTimeInMillis();


        if (nightdifference < 0) {
            notificationTimer.postDelayed(notificationCaller, -(difference));
        } else {
            notificationTimer.postDelayed(notificationCaller, difference);
        }





}


    public void sendNotifications(){
        notifyWorkoutStatus();
        notificationTimer.removeCallbacksAndMessages(null);
        notificationTimer.postDelayed(notificationCaller, 86400000);

    }



    Handler notificationTimer = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            if (msg.what == 0) {
                sendNotifications();
            }
            super.handleMessage(msg);
        }
    };
    Runnable notificationCaller = new Runnable() {

        @Override
        public void run() {

            Message msg = notificationTimer.obtainMessage();
            msg.what = 0;
            notificationTimer.sendMessage(msg);
        }
    };




    public void notifyWorkoutStatus() {
        Context context = getApplicationContext();
        /*final Calendar currentDate = Calendar.getInstance();
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
        }
*/
            Resources resources = context.getResources();


            String title = context.getString(R.string.app_name);

            // Define the text of the forecast.
            String contentText = "Please LOGiN your Workout today";
            // NotificationCompatBuilder is a very convenient way to build backward-compatible
            // notifications.  Just throw in some data.
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setColor(resources.getColor(R.color.caldroid_light_red))
                            .setSmallIcon(R.drawable.run)
                            .setDefaults(Notification.DEFAULT_SOUND)
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
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
            mNotificationManager.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());




        //cursor.close();
    }
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
