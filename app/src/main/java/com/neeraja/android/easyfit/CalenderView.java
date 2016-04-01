package com.neeraja.android.easyfit;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.neeraja.android.easyfit.data.EasyFitnessContract;
import com.neeraja.android.easyfit.data.EasyfitnessDbHelper;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@SuppressLint("SimpleDateFormat")
public class CalenderView extends BaseActivity {
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    public Calendar cal = Calendar.getInstance();
    String workout_desc;
    String workout_dur;
    int workout_recorded_date;
    Boolean workoutRecorded =false;
    Cursor cursor;SQLiteDatabase db;

    private void setCustomResourceForDates() {


        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        Date blueDate = cal.getTime();

        if (caldroidFragment != null) {
            ColorDrawable lightblue = new ColorDrawable(getResources().getColor(R.color.pink));
            caldroidFragment.setBackgroundDrawableForDate(lightblue, blueDate);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_view);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        //caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
        caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            // Session Manager
            SessionManagement session = new SessionManagement(this);
            // get user data from session
            HashMap<String, String> user = session.getUserFirebaseAuthId();
            // name
            String  authId = user.get(SessionManagement.KEY_NAME);
            int i = 0;
            ArrayList month_date = new ArrayList();
            ArrayList date = new ArrayList();
            db = (new EasyfitnessDbHelper(this)).getReadableDatabase();
            cursor = db.rawQuery("SELECT _id,workout_desc,workout_duration," +
                            "workout_recorded_month," +
                            "workout_recorded_date" +
                            " FROM UserWorkOutRecord WHERE userdeatil_authid" +
                            " = ?",
                    new String[]{authId});

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                workout_desc = cursor.getString(cursor.getColumnIndexOrThrow(EasyFitnessContract
                        .UserWorkOutRecord.COLUMN_WORKOUT_DESCRIPTION));
                workout_dur = cursor.getString(cursor.getColumnIndexOrThrow(EasyFitnessContract
                        .UserWorkOutRecord.COLUMN_WORKOUT_DURATION));
                workout_recorded_date = cursor.getInt(cursor.getColumnIndexOrThrow
                        (EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_DATE));
                int	workout_recorded_month = cursor.getInt(cursor.getColumnIndexOrThrow
                        (EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_MONTH));
System.out.println("cursor:"+workout_recorded_date + workout_desc);
// To set the extraData:
                 month_date.add(workout_recorded_month);
                    date.add(workout_recorded_date);
                i++;
                cursor.moveToNext();
            }
            db.close();

            HashMap<String, Object> extraData = (HashMap<String, Object>) caldroidFragment.getExtraData();
            extraData.put("month_KEY1", month_date);
            extraData.put("date_KEY2", date);


// Refresh view
            caldroidFragment.refreshView();
            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;

            }

            @Override
            public void onLongClickDate(Date date, View view) {
               /* Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    /*Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();*/
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);


    }



        // Customize the calendar
    public void calendar_method(){

        final TextView textView = (TextView) findViewById(R.id.textview);
                    textView.setText("");

                    // Reset calendar
                    caldroidFragment.clearDisableDates();
                    caldroidFragment.clearSelectedDates();
                    caldroidFragment.setMinDate(null);
                    caldroidFragment.setMaxDate(null);
                    caldroidFragment.setShowNavigationArrows(true);
                    caldroidFragment.setEnableSwipe(true);
                    caldroidFragment.refreshView();
                    undo = false;



                // Min date is last 7 days
                cal.add(Calendar.DATE, -7);
                Date minDate = cal.getTime();

                // Max date is next 7 days
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 14);
                Date maxDate = cal.getTime();

                // Set selected dates
                // From Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 2);
                Date fromDate = cal.getTime();

                // To Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 3);
                Date toDate = cal.getTime();

                // Set disabled dates
                ArrayList<Date> disabledDates = new ArrayList<Date>();
                for (int i = 5; i < 8; i++) {
                    cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, i);
                    disabledDates.add(cal.getTime());
                }

                // Customize
                caldroidFragment.setMinDate(minDate);
                caldroidFragment.setMaxDate(maxDate);
                caldroidFragment.setDisableDates(disabledDates);
                caldroidFragment.setSelectedDates(fromDate, toDate);
                caldroidFragment.setShowNavigationArrows(false);
                caldroidFragment.setEnableSwipe(false);

                caldroidFragment.refreshView();

                // Move to date
                // cal = Calendar.getInstance();
                // cal.add(Calendar.MONTH, 12);
                // caldroidFragment.moveToDate(cal.getTime());



                textView.setText("workout day and duration");
            }





    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        if (caldroidFragment != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        }
    }

}
