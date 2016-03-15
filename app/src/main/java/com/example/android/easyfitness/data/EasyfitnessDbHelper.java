/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.easyfitness.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.easyfitness.data.EasyFitnessContract.UserDetailEntry;
import com.example.android.easyfitness.data.EasyFitnessContract.WorkOutOptions;
import com.example.android.easyfitness.data.EasyFitnessContract.UserWorkOutRecord;

/**
 * Manages a local database for weather data.
 */
public class EasyfitnessDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "easyfitness.db";

    public EasyfitnessDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_WORKOUT_TABLE = "CREATE TABLE " + WorkOutOptions.TABLE_NAME + " (" +
                WorkOutOptions._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WorkOutOptions.COLUMN_WORKOUT_ID + " INTEGER NOT NULL," +
                WorkOutOptions.COLUMN_WORKOUT_DESCRIPTION + " TEXT NOT NULL );";




        final String SQL_CREATE_USERDETAIL_TABLE = "CREATE TABLE " + UserDetailEntry.TABLE_NAME +
                " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                UserDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                UserDetailEntry.COLUMN_USERDEATIL_AUTHENTIFICATION_ID + " INTEGER NOT NULL, " +
                UserDetailEntry.COLUMN_USER_NAME + " TEXT , " +
                UserDetailEntry.COLUMN_USER_EMAIL + " TEXT , " +
                UserDetailEntry.COLUMN_USER_AGE + " INTEGER ," +

                UserDetailEntry.COLUMN_USER_WEIGHT + " INTEGER , " +
                UserDetailEntry.COLUMN_USER_GOALWEIGHT + " INTEGER , " +

                UserDetailEntry.COLUMN_USER_CREATED_DATE + " DATE , " +
                UserDetailEntry.COLUMN_USER_UPDATED_DATE + " DATE , " +
                UserDetailEntry.KEY_NAME + " TEXT, " +
                UserDetailEntry.KEY_IMAGE + " TEXT );";


                /*// To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + UserDetailEntry.COLUMN_USER_CREATED_DATE + ", " +
                UserDetailEntry.COLUMN_USERDEATIL_AUTHENTIFICATION_ID + ") ON CONFLICT REPLACE);";*/


        final String SQL_CREATE_USERWORKOUT_RECORD_TABLE = "CREATE TABLE " + UserWorkOutRecord.TABLE_NAME +
                " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                UserWorkOutRecord._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                UserWorkOutRecord.COLUMN_USERDEATIL_AUTHENTIFICATION_ID + " INTEGER NOT NULL, " +
                UserWorkOutRecord.COLUMN_WORKOUT_DESCRIPTION + " TEXT , " +
                UserWorkOutRecord.COLUMN_WORKOUT_DURATION + " TEXT , " +
                UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_YEAR + " INTEGER ," +
                UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_MONTH + " INTEGER , " +
                UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_DATE + " INTEGER , " +
                UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_DAY + " TEXT, " +
                UserWorkOutRecord.COLUMN_FLAG + " INTEGER, " +
                UserWorkOutRecord.COLUMN_WEEKLY_FLAG + " INTEGER " +

                " );";

                /*// Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + UserWorkOutRecord.COLUMN_USERDEATIL_AUTHENTIFICATION_ID + ") REFERENCES " +
                UserDetailEntry.TABLE_NAME + " (" +UserDetailEntry
                .COLUMN_USERDEATIL_AUTHENTIFICATION_ID + "); ";*/

        sqLiteDatabase.execSQL(SQL_CREATE_WORKOUT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERDETAIL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERWORKOUT_RECORD_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WorkOutOptions.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserDetailEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserWorkOutRecord.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
