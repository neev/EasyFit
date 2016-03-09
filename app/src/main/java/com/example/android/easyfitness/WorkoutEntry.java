package com.example.android.easyfitness;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.easyfitness.data.EasyFitnessContract;

public class WorkoutEntry extends FragmentActivity implements LoaderManager
        .LoaderCallbacks<Cursor>, SwitchButtonListener {
    public static final String LOG_TAG = WorkoutEntry.class.getSimpleName();
    public WorkoutListAdapter mAdapter;
    public static final int WORKOUT_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
            EasyFitnessContract.WorkOutEntry.COLUMN_WORKOUT_ID +" " + " as "+" "+EasyFitnessContract
                    .WorkOutEntry._ID,
            EasyFitnessContract.WorkOutEntry.COLUMN_WORKOUT_DESCRIPTION
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_entry);
        View emptyView = (View)findViewById(R.id.listview_forecast_empty);
        ListView workout_list = (ListView)findViewById(R.id.workout_list);
        mAdapter = new WorkoutListAdapter(this,null,0);
        mAdapter.setCustomButtonListner(WorkoutEntry.this);
        workout_list.setEmptyView(emptyView);
        workout_list.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(WORKOUT_LOADER, null, this);
        workout_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // ViewHolder selected = (ViewHolder) view.getTag();
                Toast.makeText(WorkoutEntry.this, "List ITEM CLICKED"+ position, Toast.LENGTH_SHORT)
                        .show();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {


        int locationSetting = 1;
        Uri DescriptionForWorkoutIdUri = EasyFitnessContract.WorkOutEntry
                .buildWorkoutDescriptionDisplay();


        return new CursorLoader(this,
                DescriptionForWorkoutIdUri,
                FORECAST_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        Toast.makeText(this, "onLoadFinished", Toast.LENGTH_SHORT).show();
        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            i++;
            cursor.moveToNext();
        }
        //Log.v(FetchScoreTask.LOG_TAG,"Loader query: " + String.valueOf(i));
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader){
    Toast.makeText(this, "onLoaderReset", Toast.LENGTH_SHORT).show();
        mAdapter.swapCursor(null);
    }

    @Override
    public void onSwitchButtonClickListner(int position,String desc) {
        Toast.makeText(WorkoutEntry.this, "Button click *** " + position+
                        "**********"+desc+"************"+mAdapter
                        .swichbtn_flag,
                Toast.LENGTH_SHORT).show();

        mAdapter.selected_desc = desc;
        mAdapter.notifyDataSetChanged();

    }
}
