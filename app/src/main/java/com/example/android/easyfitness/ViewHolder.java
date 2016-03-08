package com.example.android.easyfitness;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ViewHolder
{
    public TextView workoutDescText;
    public Switch workoutOptionSwitch;
    public ImageView workoutoptionImageview;


    public ViewHolder(View view)
    {

        workoutDescText = (TextView) view.findViewById(R.id.list_item_workout_textview);
        workoutOptionSwitch = (Switch)view.findViewById(R.id.workout_switch_btn);
        workoutoptionImageview = (ImageView) view.findViewById(R.id.list_item_icon);

    }
}
