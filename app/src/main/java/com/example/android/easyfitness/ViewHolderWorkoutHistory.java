package com.example.android.easyfitness;

import android.view.View;
import android.widget.TextView;

/**
 * Created by neeraja on 3/13/2016.
 */
public class ViewHolderWorkoutHistory {

    public TextView recorded_date;
    public TextView recorded_workout_desc;
    public TextView recorded_workout_duration;
 //   public FloatingActionButton fab;

    public ViewHolderWorkoutHistory(View view)
    {
       recorded_workout_desc = (TextView) view.findViewById(R.id.recorded_desc_textview);
        recorded_workout_duration = (TextView) view.findViewById(R.id.recorded_duration_textView);
        recorded_date = (TextView) view.findViewById(R.id.recorded_date_textView);
//        fab = (FloatingActionButton) view.findViewById(R.id.fab_share);
    }
}
