package com.example.android.easyfitness;

import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.easyfitness.data.EasyFitnessContract;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by neeraja on 3/6/2016.
 */
public class WorkoutListAdapter extends CursorAdapter
{

    ViewHolder mHolder;
    ViewGroup container;
    private String WORKOUTOPTIONS_HASHTAG = "#Workout_Options";
    SwitchButtonListener _switchButtonListerner;

   String selected_desc;
    boolean swichbtn_flag = false;
    CustomTimePickerDialog timePickerDialog;
    TextView workout_dur_editText;
    Button workoutTimeDur;
    String logged_workoutDuration;
     String logged_workoutDecs ;
    int selected_row;
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


        return mItem;
    }



    @Override
    public void bindView(final View view, final Context context, final Cursor cursor)
    {


         mHolder = (ViewHolder) view.getTag();

        String option1 =cursor.getString(cursor.getColumnIndex(EasyFitnessContract.WorkOutEntry.COLUMN_WORKOUT_DESCRIPTION));
        mHolder.workoutoptionImageview.setImageResource(R.drawable.run);
        mHolder.workoutDescText.setText(option1);



        final int rowPosition = cursor.getPosition();
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
                        selected_row = cursor.getPosition();
                        _switchButtonListerner.onSwitchButtonClickListner(cursor.getPosition(),
                                logged_workoutDecs);

                        swichbtn_flag = true;
                        durationView(context,view,cursor);
                    }

                } else {
                    //Do something when switch is off/unchecked
                    swichbtn_flag = false;
                    container.removeViewAt(0);
                }
            }
        });
        mHolder.workoutoptionImageview.setImageResource(R.drawable.run);


    }


    public void durationView( final Context context,View view, final Cursor cursor){
        container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.workout_duration, null);

    final ViewHolder myviewHolder;
        myviewHolder = (ViewHolder) view.getTag();
        timePickerDialog = new CustomTimePickerDialog(context,
                timeSetListener,
                Calendar.getInstance().get(Calendar.HOUR),
                CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        workout_dur_editText = (TextView) v.findViewById(R.id.workout_duration_textview);
        workoutTimeDur = (Button) v.findViewById(R.id.workout_duration_button);
        Button workoutLogit = (Button) v.findViewById(R.id.workoutLogitbtn);

        container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));
        workoutTimeDur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add the time picker here

                timePickerDialog.setTitle("Set hours and minutes");
                timePickerDialog.show();
            }
        });
        workoutLogit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cursor.moveToPosition(selected_row);
               myviewHolder.loggedStatusText.setText("Logged for Today");
                myviewHolder.loggedStatusText.setVisibility(View.VISIBLE);
                myviewHolder.workoutOptionSwitch.setVisibility(View.GONE);
                container.removeViewAt(0);
                //container.setVisibility(View.GONE);
                logit();

            }
        });



    }
    public static class CustomTimePickerDialog extends TimePickerDialog {

        public static final int TIME_PICKER_INTERVAL=15;
        private boolean mIgnoreEvent=false;

        public CustomTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
            super(context, callBack, hourOfDay, minute, is24HourView);
        }

        @Override
        public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
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
            workout_dur_editText.setText(logged_workoutDuration);
            workout_dur_editText.setVisibility(View.VISIBLE);
            workoutTimeDur.setVisibility(View.GONE);
        }
    };

    /// log it function

    public void logit(){

        Date current_date =  new Date(System.currentTimeMillis());

        System.out.println("Logged time : " + logged_workoutDuration+
                            "Logged description : " + logged_workoutDecs+
                            "Logged date : " + current_date+
        "selected Row :*** " + selected_row);

    }

}
