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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.easyfitness.data.EasyFitnessContract;

import java.util.Calendar;

/**
 * Created by neeraja on 3/6/2016.
 */
public class WorkoutListAdapter extends CursorAdapter
{

    private String WORKOUTOPTIONS_HASHTAG = "#Workout_Options";
    SwitchButtonListener _switchButtonListerner;
    int switch_button_id = 0;
   String selected_desc;
    boolean swichbtn_flag = false;
    CustomTimePickerDialog timePickerDialog;
    //EditText workout_dur_editText;
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
                Calendar.getInstance().get(Calendar.HOUR),
                CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }



    @Override
    public void bindView(View view, final Context context, final Cursor cursor)
    {


        ViewHolder mHolder = (ViewHolder) view.getTag();

        String option1 =cursor.getString(cursor.getColumnIndex(EasyFitnessContract.WorkOutEntry.COLUMN_WORKOUT_DESCRIPTION));
        mHolder.workoutoptionImageview.setImageResource(R.drawable.run);
        mHolder.workoutDescText.setText(option1);

        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.workout_duration, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        //workout_dur_editText = (EditText) v.findViewById(R.id.editText_time);
        Button workoutTimeDur = (Button) v.findViewById(R.id.workout_duration_button);
        Button workoutLogit = (Button) v.findViewById(R.id.workoutLogitbtn);



        mHolder.workoutOptionSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Is the switch is on?
                boolean on = ((Switch) v).isChecked();
                if (on) {
                    //Do something when switch is on/checked
                    if(_switchButtonListerner !=null){
                        _switchButtonListerner.onSwitchButtonClickListner(cursor.getPosition(),
                                cursor.getString(1));
                    }
                    swichbtn_flag = true;
                } else {
                    //Do something when switch is off/unchecked
                    swichbtn_flag = false;
                }
            }
        });
        mHolder.workoutoptionImageview.setImageResource(R.drawable.run);




        if(cursor.getString(1)==selected_desc)
        {
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
                    Toast.makeText(context, "Logit Action", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {
            container.removeAllViews();
        }

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

    private CustomTimePickerDialog.OnTimeSetListener timeSetListener = new CustomTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //workout_dur_editText.setText(String.format("%02d", hourOfDay) + ":" +String.format
                 //   ("%02d", minute));
        }
    };



}
