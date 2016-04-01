package com.neeraja.android.easyfit;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.neeraja.android.easyfit.data.EasyFitnessContract;
import com.neeraja.android.easyfit.data.WorkoutRecord;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Calendar;
import java.util.HashMap;

;

/**
 * Created by neeraja on 3/6/2016.
 */
public class WorkoutListAdapter extends CursorAdapter
{

    private static final String TAG = "RecordWorkout";
    /* A reference to the Firebase */
    private Firebase mFirebaseRef;
    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;
    String authId;
    ViewHolder mHolder;
    ViewGroup container;
    private String WORKOUTOPTIONS_HASHTAG = "#Workout_Options";
    SwitchButtonListener _switchButtonListerner;
    int mMinutes;
    String selected_desc;
    String selected_date;
    boolean swichbtn_flag = false;
    CustomTimePickerDialog timePickerDialog;
    TextView workout_dur_editText;
    Button workoutTimeDur;
    String logged_workoutDuration;
    String logged_workoutDecs ;
    int selected_row;
    int screenorientaion_selected_row = -1;
    boolean switch_checked = false;
    int selected_position = -1;
    int timeSetStatus =0;
    public WorkoutListAdapter(Context context,Cursor cursor,int flags)
    {
        super(context,cursor,flags);
    }
    public void setCustomButtonListner(SwitchButtonListener listener) {
        this._switchButtonListerner = listener;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View mItem = LayoutInflater.from(context).inflate(R.layout.list_item_workoutoptions, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        timePickerDialog = new CustomTimePickerDialog(context,
                timeSetListener,
                0,
                CustomTimePickerDialog.getRoundedMinute(0 + CustomTimePickerDialog
                        .TIME_PICKER_INTERVAL), true);
        // Initialize Firebase with the application context
        Firebase.setAndroidContext(context);

        return mItem;
    }



    @Override
    public void bindView(final View view, final Context context, final Cursor cursor)
    {


        mHolder = (ViewHolder) view.getTag();
        final ViewHolder disabled_View =(ViewHolder) view.getTag();

        final String option1 =cursor.getString(cursor.getColumnIndex(EasyFitnessContract.WorkOutOptions.COLUMN_WORKOUT_DESCRIPTION));
        mHolder.workoutoptionImageview.setImageResource(R.drawable.run);
        mHolder.workoutDescText.setText(option1);

        final int rowPosition = cursor.getPosition();

        mHolder.workoutOptionSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // do something when check is selected
                            //Do something when switch is on/checked
                            if (_switchButtonListerner != null) {

                                cursor.moveToPosition(rowPosition);

                                logged_workoutDecs = cursor.getString(1);
                                selected_row =rowPosition;
                                _switchButtonListerner.onSwitchButtonClickListner(cursor.getPosition(),
                                        logged_workoutDecs);
                                timePickerDialog.setTitle("Set hours and minutes");
                                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        timeSetStatus = 2;
                                        cursor.moveToPosition(rowPosition);
                                        disabled_View.workoutOptionSwitch.setChecked(false);
                                        container.setVisibility(View.GONE);
                                        // Actions on clicks outside the dialog.
                                    }
                                });

                                timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {

                                        // Actions on Cancel button click.
                                        cursor.moveToPosition(rowPosition);
                                        // Actions on Cancel button click.
                                        disabled_View.workoutOptionSwitch.setChecked(false);
                                        container.setVisibility(View.GONE);

                                    }
                                });

                                timePickerDialog.show();
                                swichbtn_flag = true;
                                durationView(context, view, cursor);


                            } else {
                                //do something when unchecked


                                swichbtn_flag = false;
                                //container.removeAllViews();
                                container.setVisibility(View.GONE);
                                selected_position = -1;
                            }
                            notifyDataSetChanged();
                        }
                    }
                }
        );

        mHolder.workoutOptionSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Is the switch is on?
                boolean on = ((Switch) v).isChecked();
                if (on) {
                    //Do something when switch is on/checked
                    if (_switchButtonListerner != null) {

                        cursor.moveToPosition(rowPosition);

                        logged_workoutDecs = cursor.getString(1);
                        selected_row = rowPosition;
                        _switchButtonListerner.onSwitchButtonClickListner(cursor.getPosition(),
                                logged_workoutDecs);
                        timePickerDialog.setTitle("Set hours and minutes");
                        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                timeSetStatus = 2;
                                cursor.moveToPosition(rowPosition);
                                disabled_View.workoutOptionSwitch.setChecked(false);
                                container.setVisibility(View.GONE);
                                // Actions on clicks outside the dialog.
                            }
                        });

                        timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                                // Actions on Cancel button click.
                                cursor.moveToPosition(rowPosition);
                                // Actions on Cancel button click.
                                disabled_View.workoutOptionSwitch.setChecked(false);
                                container.setVisibility(View.GONE);

                            }
                        });

                        timePickerDialog.show();
                        swichbtn_flag = true;
                        durationView(context, view, cursor);



                    }

                } else {
                    //Do something when switch is off/unchecked



                    swichbtn_flag = false;
                    //container.removeAllViews();
                    container.setVisibility(View.GONE);
                    selected_position = -1;
                }

                notifyDataSetChanged();
            }


        });



    }


    public void durationView( final Context context,View view, final Cursor cursor){


        container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.workout_duration, null);

        final ViewHolder myviewHolder;
        myviewHolder = (ViewHolder) view.getTag();


        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        workout_dur_editText = (TextView) v.findViewById(R.id.workout_duration_textview);
        workoutTimeDur = (Button) v.findViewById(R.id.workout_duration_button);
        //Button workoutLogit = (Button) v.findViewById(R.id.workoutLogitbtn);

        container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));

                workoutTimeDur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add the time picker here

                if (workout_dur_editText.getText() != "") {

                    cursor.moveToPosition(selected_row);
                    myviewHolder.loggedStatusText.setText("Logged Duration");
                    myviewHolder.loggedDurationTimeText.setText(logged_workoutDuration);
                    myviewHolder.loggedStatusText.setVisibility(View.VISIBLE);
                    myviewHolder.loggedDurationTimeText.setVisibility(View.VISIBLE);
                    myviewHolder.workoutOptionSwitch.setVisibility(View.GONE);

                    logit(context);

                    //container.removeAllViews();
                    container.setVisibility(View.GONE);


                }
            }
        });

        }



    public static class CustomTimePickerDialog extends TimePickerDialog {

        public static final int TIME_PICKER_INTERVAL=15;
        private boolean mIgnoreEvent = false;

        public CustomTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
            super(context, callBack, hourOfDay, minute, is24HourView);



        }


        @Override
        public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute){
                super.onTimeChanged(timePicker, hourOfDay, minute);
            if (!mIgnoreEvent){
                minute = getRoundedMinute(minute);
                mIgnoreEvent=true;
                timePicker.setCurrentMinute(minute);
                mIgnoreEvent=false;
            }
        }



        public static  int getRoundedMinute(int minute){
            if(minute % TIME_PICKER_INTERVAL != 0){
                int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
                minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
                if (minute == 60)  minute=0;
            }

            return minute;
        }
    }

    CustomTimePickerDialog.OnTimeSetListener timeSetListener = new CustomTimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            logged_workoutDuration = String.format("%02d", hourOfDay) + ":" + String.format
                    ("%02d", minute);
            mMinutes = (hourOfDay*60) + minute;
            workout_dur_editText.setText(logged_workoutDuration);
            workout_dur_editText.setVisibility(View.VISIBLE);
            workoutTimeDur.setVisibility(View.GONE);

            workoutTimeDur.callOnClick();
            timeSetStatus = 1;

        }
    };



    /// log it function

    public void logit(Context context){

        // Date current_date =  new Date(System.currentTimeMillis());

        // Session Manager
        SessionManagement session = new SessionManagement(context);
        // get user data from session
        HashMap<String, String> user = session.getUserFirebaseAuthId();
        // name
        authId = user.get(SessionManagement.KEY_NAME);
        HashMap<String, String> date = session.getPickedDate();
        // name
        selected_date = date.get(SessionManagement.KEY_PICKED_DATE);

        HashMap<String, Integer> flag = session.getFlag_Session();
        // name
        int _flag = flag.get(SessionManagement.FLAG);
        /* Create the Firebase ref that is used for all authentication with Firebase */
        mFirebaseRef = new Firebase(context.getResources().getString(R.string.firebase_url));
        System.out.println("Logged time : " + logged_workoutDuration +
                "Logged description : " + logged_workoutDecs +
                "selected Row :*** " + selected_row + "/n IN MINUTES" + mMinutes + selected_date);



        Calendar c = Calendar.getInstance();
        String[] parts = selected_date.split(" ");
        String _month = parts[1].trim();
        String _date = parts[0].trim();
        String _year = parts[2].trim();




        WorkoutRecord recordedWorkout = new WorkoutRecord(mMinutes,logged_workoutDecs);
        Firebase recordWorkoutRef = mFirebaseRef.child("recordedWorkoutList").child(authId).child
                (_year).child(_month).child(_date);
        Firebase recordedWorkout_pushId = recordWorkoutRef.push();
        recordedWorkout_pushId.setValue(recordedWorkout, new Firebase.CompletionListener() {

            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("WORKOUT could not be saved. " + firebaseError.getMessage());
                    Log.i(TAG, firebaseError.getMessage());
                } else {
                    System.out.println("WORKOUT saved successfully.");
                }
            }
        });

        String pushId = recordedWorkout_pushId.getKey();

        //storing user workout record in sqlite
        int y = Integer.parseInt(_year);
        int m = Utilities.getMonthinNumber(_month);
        int d = Integer.parseInt(_date);

        c.set(y,m,d);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        String _day = Utilities.getWeekName(day_of_week);




        // ADDING THE RECORDS TO THE TABLE IN LOCAL DB
       /* long workoutRecord_stored = Utilities.addUserRecordedWorkout(context,authId,
                logged_workoutDecs,mMinutes,y,m,d,_day,pushId);
        System.out.println("Successfully stored WORKOUT RECORD### : "+ workoutRecord_stored +
                "DAY OF WEEK : ##"+ _day + "FLAG -----" + _flag);*/



    }

}