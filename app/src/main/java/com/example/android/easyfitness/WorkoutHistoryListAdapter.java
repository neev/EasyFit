package com.example.android.easyfitness;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by neeraja on 3/13/2016.
 */
public class WorkoutHistoryListAdapter extends CursorAdapter {

    public static final int COL_DESC = 2;
    public static final int COL_DURATION = 1;
    public static final int COL_YEAR = 3;
    public static final int COL_MONTH = 4;
    public static final int COL_DATE = 5;
    public static final int COL_DAY = 6;

    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";
    public WorkoutHistoryListAdapter(Context context,Cursor cursor,int flags)
    {
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View mItem = LayoutInflater.from(context).inflate(R.layout.list_item_workout_history, parent, false);
        ViewHolderWorkoutHistory mHolder = new ViewHolderWorkoutHistory(mItem);
        mItem.setTag(mHolder);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolderWorkoutHistory mHolder = (ViewHolderWorkoutHistory) view.getTag();


        mHolder.recorded_workout_desc.setText(cursor.getString(COL_DESC));
        mHolder.recorded_workout_duration.setText(cursor.getString(COL_DURATION));
        String _year = cursor.getString(COL_YEAR);
        String _month = cursor.getString(COL_MONTH);
        String _date = cursor.getString(COL_DATE);
        String _day = cursor.getString(COL_DAY);
        StringBuilder _displayDate = (new StringBuilder()

                .append(Utilities.getMonthName(Integer.parseInt(_month)))
                .append(" ")
                .append(_date).append(", ")
                .append(_year).append(" "));

        mHolder.recorded_date.setText(_displayDate);
        System.out.println("HISTORY VALUES: "+cursor.getString(COL_DESC)+cursor.getString
                (COL_DURATION));



    }


}
