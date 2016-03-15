package com.example.android.easyfitness;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created by neeraja on 3/2/2016.
 */

public class SessionManagement {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "EasyFitnessPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PICKED_DATE = "picked_date";
    public static final String FLAG = "flag";
    public static final String WEEKLY_FLAG = "weekly_flag";

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    /**
     * Create login session
     * */
    public void createLoginSession(String name, String email) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);


        // commit changes
        editor.commit();
    }

    public SessionManagement createPickedDateSession(String date){
        editor.putString(KEY_PICKED_DATE, date);
        editor.commit();
        return null;
    }

    public SessionManagement createFlagSession(int flag,int weekly_flag){
        editor.putInt(FLAG, flag);
        editor.putInt(WEEKLY_FLAG, weekly_flag);

        editor.commit();
        return null;
    }

    public HashMap<String, String> getUserFirebaseAuthId(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }
    public HashMap<String, String> getPickedDate(){
        HashMap<String, String> pickedDate = new HashMap<String, String>();
        // user name
        pickedDate.put(KEY_PICKED_DATE, pref.getString(KEY_PICKED_DATE, null));


        // return user
        return pickedDate;
    }
    public HashMap<String, Integer> getFlag_Session(){
        HashMap<String, Integer> flower_flag = new HashMap<>();
        // user name
        flower_flag.put(FLAG, pref.getInt(FLAG,0));
        flower_flag.put(FLAG, pref.getInt(WEEKLY_FLAG,0));


        // return user
        return flower_flag;
    }




    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public boolean checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
            return false;
        }
        return true;
    }

        /**
         * Clear session details
         * */
        public void logoutUser(){
            // Clearing all data from Shared Preferences
            editor.clear();
            editor.commit();

            /*// After logout redirect user to Loing Activity
            Intent i = new Intent(_context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);*/
            System.out.println("Successfully session logout ");
        }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }




}
