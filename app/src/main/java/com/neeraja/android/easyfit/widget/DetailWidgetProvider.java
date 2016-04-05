package com.neeraja.android.easyfit.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.neeraja.android.easyfit.MainActivity;
import com.neeraja.android.easyfit.R;
import com.neeraja.android.easyfit.SessionManagement;
import com.neeraja.android.easyfit.Utilities;
import com.neeraja.android.easyfit.data.EasyfitnessDbHelper;
import com.neeraja.android.easyfit.sync.EasyFitSyncAdapter;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Provider for a scrollable weather detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetProvider extends AppWidgetProvider {

    int numberOfWorkouts = 0;
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget_this_week);

            // Create an Intent to launch MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            //////////////////////
            // Session Manager
            SessionManagement session = new SessionManagement(context);
            // get user data from session
            HashMap<String, String> user = session.getUserFirebaseAuthId();
            // name
            String authId = user.get(SessionManagement.KEY_NAME);
            Cursor cursor;
            SQLiteDatabase db;

            // Session Manager




            int mYear;
            int mMonth;
            int mDate;
            int mDay;
            final Calendar currentDate = Calendar.getInstance();
            mYear = currentDate.get(Calendar.YEAR);
            mMonth = currentDate.get(Calendar.MONTH);
            mDate = currentDate.get(Calendar.DATE);
            mDay = currentDate.get(Calendar.DAY_OF_WEEK);

            int days_back = 0;
            // current day of the week
            System.out.println("DAY OF THE WEEK: "+ mDay);

            //TO GET THE DATES OF THIS WEEK
            switch (mDay) {
                case 1:
                    days_back = mDate;
                    break;
                case 2:
                    days_back = mDate - 1;
                    break;
                case 3:
                    days_back = mDate - 2;
                    break;
                case 4:
                    days_back = mDate - 3;
                    break;
                case 5:
                    days_back = mDate - 4;
                    break;
                case 6:
                    days_back = mDate - 5;
                    break;
                case 7:
                    days_back = mDate - 6;
                    break;



            }



            if(authId != null) {

                Date startDate = new Date((mYear - 1900), (mMonth), days_back);

                db = (new EasyfitnessDbHelper(context)).getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM UserWorkOutRecord WHERE " +
                                "userdeatil_authid" +
                                " = ?  AND full_date  BETWEEN date(?)  AND date(? ,'+7 days')",
                        new String[]{authId, String.valueOf(startDate), String.valueOf(startDate)});
                //int i = 0;

                numberOfWorkouts = cursor.getCount();

                db.close();
            }

            views.setImageViewResource(R.id.widget_rainbow_image, Utilities.today_flowerimage
                    (numberOfWorkouts));

            //////////////////////////////////////////////////////////////////
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Set up the collection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views);
            } else {
                setRemoteAdapterV11(context, views);
            }
            Intent clickIntentTemplate = new Intent(context, MainActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);
            views.setEmptyView(R.id.widget_list, R.id.widget_empty);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (EasyFitSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, DetailWidgetRemoteViewsService.class));

    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_list,
                new Intent(context, DetailWidgetRemoteViewsService.class));
        views.setImageViewResource(R.id.widget_rainbow_image, Utilities.today_flowerimage
                (numberOfWorkouts));
    }


}
