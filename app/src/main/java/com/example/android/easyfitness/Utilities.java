package com.example.android.easyfitness;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.format.Time;

import com.example.android.easyfitness.data.EasyFitnessContract;
import com.example.android.easyfitness.data.UserDetails;

import java.text.SimpleDateFormat;

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
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


   /* to get the month name*/

    static public String getMonthName (int month){
        String monthName;
        switch (month) {
            case 1: monthName = "January";break;
            case 2: monthName = "Febraruy";break;
            case 3: monthName = "March";break;
            case 4: monthName = "April";break;
            case 5: monthName = "May";break;
            case 6: monthName = "June";break;
            case 7: monthName = "July";break;
            case 8: monthName = "August";break;
            case 9: monthName = "September";break;
            case 10: monthName = "October";break;
            case 11: monthName = "November";break;
            case 12: monthName = "December";break;
                default: monthName ="Month";
        }
        return monthName;
    }

    static public int getMonthinNumber (String month){
        int monthNum;
        switch (month) {
            case "January": monthNum = 1 ;break;
            case "Febraruy": monthNum = 2;break;
            case "March": monthNum = 3;break;
            case "April": monthNum = 4;break;
            case "May": monthNum = 5;break;
            case "June": monthNum = 6;break;
            case "July": monthNum =7 ;break;
            case "August": monthNum = 8;break;
            case "September": monthNum = 9;break;
            case "October": monthNum = 10;break;
            case "November": monthNum = 11;break;
            case "December": monthNum = 12 ;break;
            default: monthNum =0;
        }
        return monthNum;
    }

    static public String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        }
        else if ( julianDay == currentJulianDay -1)
        {
            return context.getString(R.string.yesterday);
        }
        else
        {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }

    static public long addUserAccountInfo(Context c,UserDetails userObject,String userAuthId) {
        long userDeatilEnteredId;

        // First, check if the location with this city name exists in the db
        Cursor userAccountInfoCursor = c.getContentResolver().query(
                EasyFitnessContract.UserDetailEntry.CONTENT_URI,
                new String[]{EasyFitnessContract.UserDetailEntry._ID},
                EasyFitnessContract.UserDetailEntry.COLUMN_USERDEATIL_AUTHENTIFICATION_ID + " = ?",
                new String[]{userAuthId},
                null);

        if (userAccountInfoCursor.moveToFirst()) {
            int userAccountInfoIdIndex = userAccountInfoCursor.getColumnIndex(EasyFitnessContract.UserDetailEntry._ID);
            userDeatilEnteredId = userAccountInfoCursor.getLong(userAccountInfoIdIndex);
        } else {
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
            userAccountInfoValues.put(EasyFitnessContract.UserDetailEntry
                    .COLUMN_USER_CREATED_DATE, System.currentTimeMillis());

            // Finally, insert location data into the database.
            Uri insertedUri = c.getContentResolver().insert(
                    EasyFitnessContract.UserDetailEntry.CONTENT_URI,
                    userAccountInfoValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            userDeatilEnteredId = ContentUris.parseId(insertedUri);
        }

        userAccountInfoCursor.close();
        System.out.println("USER DEATILS SUCCESSFULL ENTERED IN local DB");
        // Wait, that worked?  Yes!
        return userDeatilEnteredId;
    }

    static public long addUserRecordedWorkout(Context c, String userauthId, String workout_desc, int
            workout_duration,
                                int year,int month,int date,String day) {
        long locationId;

        // First, check if the location with this city name exists in the db
        Cursor userRecoredeworkoutCursor = c.getContentResolver().query(
                EasyFitnessContract.UserWorkOutRecord.CONTENT_URI,
                new String[]{EasyFitnessContract.UserWorkOutRecord._ID},
                null,
                null,
                null);

        if (userRecoredeworkoutCursor.moveToFirst()) {
            int userRecoredeworkoutIdIndex = userRecoredeworkoutCursor.getColumnIndex(EasyFitnessContract
                    .UserWorkOutRecord._ID);
            locationId = userRecoredeworkoutCursor.getLong(userRecoredeworkoutIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
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

            // Finally, insert location data into the database.
            Uri insertedUri = c.getContentResolver().insert(
                    EasyFitnessContract.UserWorkOutRecord.CONTENT_URI,
                    userRecoredeworkoutValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            locationId = ContentUris.parseId(insertedUri);
        }

        userRecoredeworkoutCursor.close();
        // Wait, that worked?  Yes!
        return locationId;
    }



}
