package com.neeraja.android.easyfit;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.neeraja.android.easyfit.data.EasyFitnessContract;
import com.neeraja.android.easyfit.data.UserDetails;
import com.neeraja.android.easyfit.sync.EasyFitSyncAdapter;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.util.Calendar;

/**
 * Created by neeraja on 3/11/2016.
 */
public class Utilities {


    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    static public @EasyFitSyncAdapter.LocationStatus
    int getLocationStatus(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getInt(c.getString(R.string.pref_location_status_key), EasyFitSyncAdapter
                .LOCATION_STATUS_UNKNOWN);
    }

    /**
     * Resets the location status.  (Sets it to SunshineSyncAdapter.LOCATION_STATUS_UNKNOWN)
     * @param c Context used to get the SharedPreferences
     */
    static public void resetLocationStatus(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_location_status_key), EasyFitSyncAdapter.LOCATION_STATUS_UNKNOWN);
        spe.apply();
    }
    static public String getWeekName(int week_day_num) {
        String week_day;
        switch (week_day_num) {
            case 4:
                week_day = "Sunday";
                break;
            case 5:
                week_day = "Monday";
                break;
            case 6:
                week_day = "Tuesday";
                break;
            case 7:
                week_day = "wednesday";
                break;
            case 1:
                week_day = "Thursday";
                break;
            case 2:
                week_day = "Friday";
                break;
            case 3:
                week_day = "Saturday";
                break;

            default:
                week_day = "Sunday";
        }
        return week_day;
    }

    static public int today_flowerimage(int flag) {
        int flowerImage;
        switch (flag) {
            case 0:
                flowerImage = R.drawable.r0;
                break;
            case 1:
                flowerImage = R.drawable.r1;
                break;
            case 2:
                flowerImage = R.drawable.r2;
                break;
            case 3:
                flowerImage = R.drawable.r3;
                break;
            case 4:
                flowerImage = R.drawable.r4;
                break;
            case 5:
                flowerImage = R.drawable.r5;
                break;
            case 6:
                flowerImage = R.drawable.r6;
                break;
            case 7:
                flowerImage = R.drawable.r7;
                break;
            // case 8: flowerImage = R.drawable.r7;break;


            default:
                flowerImage = R.drawable.r7;
        }
        return flowerImage;
    }


   /* to get the month name*/

    static public String getMonthName(int month) {
        String monthName;
        switch (month) {
            case 1:
                monthName = "January";
                break;
            case 2:
                monthName = "Febraruy";
                break;
            case 3:
                monthName = "March";
                break;
            case 4:
                monthName = "April";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "June";
                break;
            case 7:
                monthName = "July";
                break;
            case 8:
                monthName = "August";
                break;
            case 9:
                monthName = "September";
                break;
            case 10:
                monthName = "October";
                break;
            case 11:
                monthName = "November";
                break;
            case 12:
                monthName = "December";
                break;
            default:
                monthName = "Month";
        }
        return monthName;
    }

    static public int getMonthinNumber(String month) {
        int monthNum;
        switch (month) {
            case "January":
                monthNum = 1;
                break;
            case "Febraruy":
                monthNum = 2;
                break;
            case "March":
                monthNum = 3;
                break;
            case "April":
                monthNum = 4;
                break;
            case "May":
                monthNum = 5;
                break;
            case "June":
                monthNum = 6;
                break;
            case "July":
                monthNum = 7;
                break;
            case "August":
                monthNum = 8;
                break;
            case "September":
                monthNum = 9;
                break;
            case "October":
                monthNum = 10;
                break;
            case "November":
                monthNum = 11;
                break;
            case "December":
                monthNum = 12;
                break;
            default:
                monthNum = 0;
        }
        return monthNum;
    }


    static public long addUserAccountInfo(Context c, UserDetails userObject, String userAuthId) {
        long userDeatilEnteredId;

        /*// First, check if the location with this city name exists in the db
        Cursor userAccountInfoCursor = c.getContentResolver().query(
                EasyFitnessContract.UserDetailEntry.CONTENT_URI,
                new String[]{EasyFitnessContract.UserDetailEntry._ID},
                EasyFitnessContract.UserDetailEntry.COLUMN_USERDEATIL_AUTHENTIFICATION_ID + " = ?",
                new String[]{userAuthId},
                null);
*/

        // Now that the content provider is set up, inserting rows of data is pretty simple.
        // First create a ContentValues object to hold the data you want to insert.
        ContentValues userAccountInfoValues = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        userAccountInfoValues.put(EasyFitnessContract.UserDetailEntry.COLUMN_USERDEATIL_AUTHENTIFICATION_ID,
                userAuthId);
        userAccountInfoValues.put(EasyFitnessContract.UserDetailEntry.COLUMN_USER_NAME,
                userObject.getFullName());
        userAccountInfoValues.put(EasyFitnessContract.UserDetailEntry.COLUMN_USER_EMAIL,
                userObject.getEmail());
        userAccountInfoValues.put(EasyFitnessContract.UserDetailEntry.COLUMN_USER_AGE,
                userObject.getAge());
        userAccountInfoValues.put(EasyFitnessContract.UserDetailEntry.COLUMN_USER_WEIGHT,
                userObject.getWeight());
        userAccountInfoValues.put(EasyFitnessContract.UserDetailEntry.COLUMN_USER_GOALWEIGHT,
                userObject.getGoalWeight());
        userAccountInfoValues.put(EasyFitnessContract.UserDetailEntry
                .COLUMN_USER_CREATED, System.currentTimeMillis());

        // Finally, insert location data into the database.
        Uri insertedUri = c.getContentResolver().insert(
                EasyFitnessContract.UserDetailEntry.CONTENT_URI,
                userAccountInfoValues
        );

        // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
        userDeatilEnteredId = ContentUris.parseId(insertedUri);


        //userAccountInfoCursor.close();
        System.out.println("USER DEATILS SUCCESSFULL ENTERED IN local DB" + userObject.getAge());
        // Wait, that worked?  Yes!
        return userDeatilEnteredId;
    }

    // update user info in the local db with profile image

    static public int updateUserAccountInfowithProfileImage(Context c, String userAuthId, String
            imageName, String image) throws
            SQLiteException {

        // Now that the content provider is set up, inserting rows of data is pretty simple.
        // First create a ContentValues object to hold the data you want to insert.
        ContentValues userAccountInfoValues = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        userAccountInfoValues.put(EasyFitnessContract.UserDetailEntry.KEY_NAME,
                imageName);
        userAccountInfoValues.put(EasyFitnessContract.UserDetailEntry.KEY_IMAGE,
                image);

        int updateImageUri = c.getContentResolver().update(
                EasyFitnessContract.UserDetailEntry.CONTENT_URI,
                userAccountInfoValues,
                EasyFitnessContract.UserDetailEntry.COLUMN_USERDEATIL_AUTHENTIFICATION_ID + " = ?",
                new String[]{userAuthId});
        System.out.println("PROFILE IMAGE SUCCESSFULL UPDATED IN local DB");
        // Wait, that worked?  Yes!
        return updateImageUri;
    }


    static public long addUserRecordedWorkout(Context c, String userauthId, String workout_desc, int
            workout_duration, int year, int month, int date, String day, String push_id) {
        long locationId;
        /*// First, check if the location with this city name exists in the db
        Cursor userRecoredeworkoutCursor = c.getContentResolver().query(
                EasyFitnessContract.UserWorkOutRecord.CONTENT_URI,
                new String[]{EasyFitnessContract.UserWorkOutRecord._ID},
                null,
                null,
                null);*/

        /*if (userRecoredeworkoutCursor.moveToFirst()) {
            int userRecoredeworkoutIdIndex = userRecoredeworkoutCursor.getColumnIndex(EasyFitnessContract
                    .UserWorkOutRecord._ID);
            locationId = userRecoredeworkoutCursor.getLong(userRecoredeworkoutIdIndex);
        }*/
        //else {
        // Now that the content provider is set up, inserting rows of data is pretty simple.
        // First create a ContentValues object to hold the data you want to insert.


        Date record_workout_fulldate = new Date(year, month, date);
        ContentValues userRecoredeworkoutValues = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.

        userRecoredeworkoutValues.put(EasyFitnessContract.UserWorkOutRecord
                .COLUMN_USERDEATIL_AUTHENTIFICATION_ID, userauthId);
        userRecoredeworkoutValues.put(EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_DESCRIPTION,
                workout_desc);
        userRecoredeworkoutValues.put(EasyFitnessContract.UserWorkOutRecord
                .COLUMN_WORKOUT_DURATION, workout_duration);
        userRecoredeworkoutValues.put(EasyFitnessContract.UserWorkOutRecord
                .COLUMN_WORKOUT_RECORDED_DATE_YEAR, year);
        userRecoredeworkoutValues.put(EasyFitnessContract.UserWorkOutRecord
                .COLUMN_WORKOUT_RECORDED_DATE_MONTH, month);
        userRecoredeworkoutValues.put(EasyFitnessContract.UserWorkOutRecord
                .COLUMN_WORKOUT_RECORDED_DATE_DATE, date);
        userRecoredeworkoutValues.put(EasyFitnessContract.UserWorkOutRecord
                .COLUMN_WORKOUT_RECORDED_DATE_DAY, day);
        userRecoredeworkoutValues.put(EasyFitnessContract.UserWorkOutRecord
                .COLUMN_PUSH_ID, push_id);
        userRecoredeworkoutValues.put(EasyFitnessContract.UserWorkOutRecord
                .COLUMN_FULL_DATE, String.valueOf(record_workout_fulldate));


        // Finally, insert location data into the database.
        Uri insertedUri = c.getContentResolver().insert(
                EasyFitnessContract.UserWorkOutRecord.CONTENT_URI,
                userRecoredeworkoutValues
        );

        // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
        locationId = ContentUris.parseId(insertedUri);
        // }

        // userRecoredeworkoutCursor.close();
        // Wait, that worked?  Yes!
        return locationId;
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String mDay_string(int mDay) {
        String _day_string = "";


        switch (mDay) {
            case 1:
                _day_string = "Sunday";
                break;
            case 2:
                _day_string = "Monday";
                break;
            case 3:
                _day_string = "Tuesday";
                break;
            case 4:
                _day_string = "Wednesday";
                break;
            case 5:
                _day_string = "Thursday";
                break;
            case 6:
                _day_string = "Friday";
                break;
            case 7:
                _day_string = "Saturday";
                break;
            default:
                _day_string = "Saturday";
                break;

        }
        return _day_string;
    }

    public static int[] mCurrentDate_int() {
        int mYear;
        int mMonth;
        int mDate;
        int mDay;
        final Calendar current_date = Calendar.getInstance();
        mYear = current_date.get(Calendar.YEAR);
        mMonth = current_date.get(Calendar.MONTH);
        mDate = current_date.get(Calendar.DAY_OF_MONTH);
        mDay = current_date.get(Calendar.DAY_OF_WEEK);
        int[] currentDate = {mYear, mMonth, mDate, mDay};
        return currentDate;
    }


    public Intent createShareForecastIntent(String ShareText) {

        String EASY_FIT_HASHTAG = "#EASY FIT";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + EASY_FIT_HASHTAG);
        return shareIntent;
    }

    public static boolean checkConnectivity(Context context) {

        boolean isOnline;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
           /* if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .isConnectedOrConnecting()
                    || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .isConnectedOrConnecting())*/
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting())
            isOnline = true;
        else
            isOnline = false;
        return isOnline;
    }
}
