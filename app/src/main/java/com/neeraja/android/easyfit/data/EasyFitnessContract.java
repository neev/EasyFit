
package com.neeraja.android.easyfit.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 */
public class EasyFitnessContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.android.easyfitness";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.com.example.neeraja.fourseasons.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.com.example.neeraja.fourseasons.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.

    public static final String PATH_USERDETAIL = "userdetailpath";
    public static final String PATH_WORKOUT = "workoutpath";
    public static final String PATH_WORKOUTRECORD = "workoutrecordpath";


    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the location table */
    public static final class UserDetailEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERDETAIL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERDETAIL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERDETAIL;

        // Table name
        public static final String TABLE_NAME = "userdetail";
        public static final String _ID = "_id";
        public static final String COLUMN_USERDEATIL_AUTHENTIFICATION_ID = "userdeatil_authid";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_USER_WEIGHT = "user_weight";
        public static final String COLUMN_USER_EMAIL = "user_email";
        public static final String COLUMN_USER_GOALWEIGHT = "user_goal_weight";
        public static final String COLUMN_USER_CREATED = "user_create_date";
        public static final String COLUMN_USER_UPDATED_DATE = "user_updated_date";
        public static final String COLUMN_USER_AGE = "enter_age";
        public static final String KEY_NAME = "image_name";
        public static final String KEY_IMAGE = "image_data";

        public static Uri buildUserDetailUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
            public static Uri buildUserDetailWithCreatedDate(
                    String userSetting, long startDate) {
                long normalizedDate = normalizeDate(startDate);
                return CONTENT_URI.buildUpon().appendPath(userSetting)
                        .appendQueryParameter(COLUMN_USER_CREATED, Long.toString(normalizedDate)).build();
            }

            public static Uri buildUserDetailswithAuthId(String locationSetting) {
                return CONTENT_URI.buildUpon().appendPath(locationSetting)
                       .build();
            }



            public static String getUserAuthIdFromUri(Uri uri) {
                return uri.getPathSegments().get(1);
            }


            public static long getUserCreatedDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
            }

            /*public static long getUserCreatedDateFromUri(Uri uri) {
                String dateString = uri.getQueryParameter(COLUMN_USER_CREATED_DATE);
                if (null != dateString && dateString.length() > 0)
                    return Long.parseLong(dateString);
                else
                    return 0;
            }*/
        }


    /* Inner class that defines the table contents of the location table */
    public static final class WorkOutOptions implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORKOUT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT;

        // Table name
        public static final String TABLE_NAME = "workout";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
        public static final String _ID = "_id";
        public static final String COLUMN_WORKOUT_ID = "workout_id";

        public static final String COLUMN_WORKOUT_DESCRIPTION = "workout_desc";

        public static Uri buildWorkoutUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWorkoutDescriptionDisplay() {
            return CONTENT_URI.buildUpon().build();

        }

        public static int getWorkoutIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }

    /* Inner class that defines the table contents of the location table */
    public static final class UserWorkOutRecord implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORKOUTRECORD).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUTRECORD;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUTRECORD;

        // Table name
        public static final String TABLE_NAME = "userworkoutrecord";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
        public static final String _ID = "_id";
        public static final String COLUMN_USERDEATIL_AUTHENTIFICATION_ID = "userdeatil_authid";
        public static final String COLUMN_WORKOUT_DESCRIPTION = "workout_desc";
        public static final String COLUMN_WORKOUT_DURATION = "workout_duration";
        public static final String COLUMN_WORKOUT_RECORDED_DATE_YEAR = "workout_recorded_year";
        public static final String COLUMN_WORKOUT_RECORDED_DATE_MONTH = "workout_recorded_month";
        public static final String COLUMN_WORKOUT_RECORDED_DATE_DATE = "workout_recorded_date";
        public static final String COLUMN_WORKOUT_RECORDED_DATE_DAY = "workout_recorded_day";
        public static final String COLUMN_FLAG = "flag";
        public static final String COLUMN_WEEKLY_FLAG = "weekly_flag";
        public static final String COLUMN_PUSH_ID = "push_id";
        public static final String COLUMN_FULL_DATE = "full_date";

        public static Uri buildWorkoutUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildWorkoutRecordWithUserAuthIdandDate(String authId, int year,int
                month,int date) {
            return CONTENT_URI.buildUpon().appendPath(authId)
                    .appendPath(Integer.toString(year))
                    .appendPath(Integer.toString(month))
                    .appendPath(Integer.toString(date))
                            .build();
        }
        public static Uri buildWorkoutRecordWithAuthId(String authId) {
            return CONTENT_URI.buildUpon().appendPath(authId).build();

        }
        public static Uri buildWorkoutRecordWithUserAuthIdandMonth(String authId, int
                year,int month) {
            return CONTENT_URI.buildUpon().appendPath(authId)
                    .appendPath(Integer.toString(year))
                    .appendPath(Integer.toString(month))
                    .build();
        }
       public static Uri buildWorkoutRecordWithUserAuthIdandThisWeek(String authId,String
                startdate, String enddate) {
            return CONTENT_URI.buildUpon().appendPath(authId)
                    .appendPath(startdate)
                    .appendPath(enddate)
                    .build();
        }

        public static String getUserAuthIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static int getworkoutRecordYearFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(2));
        }

        public static int getworkoutRecordMonthFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(3));
        }
        public static int getworkoutRecordDateFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(4));
        }

        public static String getworkoutRecordStartDateFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getworkoutRecordEndDateFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }

    }
}
