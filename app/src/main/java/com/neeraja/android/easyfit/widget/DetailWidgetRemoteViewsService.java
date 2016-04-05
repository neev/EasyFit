package com.neeraja.android.easyfit.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.neeraja.android.easyfit.MainActivity;
import com.neeraja.android.easyfit.R;
import com.neeraja.android.easyfit.SessionManagement;
import com.neeraja.android.easyfit.Utilities;
import com.neeraja.android.easyfit.data.EasyFitnessContract;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

/**
 * RemoteViewsService controlling the data being shown in the scrollable weather detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = DetailWidgetRemoteViewsService.class.getSimpleName();
    private static final String[] WORKOUT_RECORD_COLUMNS = {
            EasyFitnessContract.UserWorkOutRecord._ID +" " + " as "+" "+
                    EasyFitnessContract.UserWorkOutRecord._ID,
            EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_DURATION ,
            EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_DESCRIPTION,
            EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_YEAR,
            EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_MONTH,
            EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_DATE,
            EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_DAY
    };
    // these indices must match the projection
    public static final int COL_DESC = 2;
    public static final int COL_DURATION = 1;
    public static final int COL_YEAR = 3;
    public static final int COL_MONTH = 4;
    public static final int COL_DATE = 5;
    public static final int COL_DAY = 6;

    int numberOfWorkouts=0;
    int mYear;
    int mMonth;
    int mDate;
    int mDay;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do


            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();


                final Calendar currentDate = Calendar.getInstance();
                mYear = currentDate.get(Calendar.YEAR);
                mMonth = currentDate.get(Calendar.MONTH);
                mDate = currentDate.get(Calendar.DATE);
                mDay = currentDate.get(Calendar.DAY_OF_WEEK);
                int days_back = 0;
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

                Date startDate = new Date((mYear-1900),(mMonth),days_back);
               // Date endDate = new Date((mYear-1900),(mMonth),mDate);
                // Session Manager
                SessionManagement session = new SessionManagement(getBaseContext());
                // get user data from session
                HashMap<String, String> user = session.getUserFirebaseAuthId();
                // name
                String authId = user.get(SessionManagement.KEY_NAME);
                Uri weatherForLocationUri = EasyFitnessContract.UserWorkOutRecord
                        .buildWorkoutRecordWithUserAuthIdandThisWeek
                                (authId, String.valueOf(startDate), String
                                        .valueOf(startDate));
                data = getContentResolver().query(weatherForLocationUri,
                        WORKOUT_RECORD_COLUMNS,
                        null,
                        null,
                        "workout_recorded_year desc, " +
                                "workout_recorded_month desc, workout_recorded_date desc");
                numberOfWorkouts = data.getCount();



                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_list_item);

                String description = data.getString(COL_DESC);
                String duration = data.getString(COL_DURATION);

                String _year = data.getString(COL_YEAR);
                String _month = data.getString(COL_MONTH);
                String _date = data.getString(COL_DATE);
                String _day = data.getString(COL_DAY);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    setRemoteContentDescription(views, description);
                }

                views.setTextViewText(R.id.widget_workout_desc, description);
                views.setTextViewText(R.id.widget_workout_duration, duration + " min");
                views.setTextViewText(R.id.widget_month_date, Utilities.getMonthName(Integer
                        .parseInt(_month)) + " " + _date);
                if(Integer.parseInt(_date)== mDate) {
                    views.setTextViewTextSize(R.id.widget_month_date,1,20);
                    views.setTextColor(R.id.widget_month_date,getResources().getColor(R.color
                            .pink));
                    views.setTextViewTextSize(R.id.widget_workout_desc,1,20);
                    views.setTextColor(R.id.widget_workout_desc,getResources().getColor(R.color
                            .pink));
                    views.setTextViewTextSize(R.id.widget_workout_duration,1,20);
                    views.setTextColor(R.id.widget_workout_duration,getResources().getColor(R.color
                            .pink));
                }

                final Intent fillInIntent = new Intent(getBaseContext(),MainActivity.class);


                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, fillInIntent, 0);
                views.setOnClickPendingIntent(R.id.widget, pendingIntent);

                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget_rainbow_image, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                RemoteViews rv =  new RemoteViews(getPackageName(), R.layout.widget_list_item);

                return rv;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                //if (data.moveToPosition(position))
                   // return data.getLong(INDEX_WEATHER_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
