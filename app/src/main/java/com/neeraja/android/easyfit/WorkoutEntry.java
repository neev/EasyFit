package com.neeraja.android.easyfit;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.neeraja.android.easyfit.data.EasyFitnessContract;

public class WorkoutEntry extends BaseActivity  implements LoaderManager
        .LoaderCallbacks<Cursor>, SwitchButtonListener {

    int switch_btn_position;
    Boolean switch_btn_checked = false;
    private TextView mDateDisplay;
String pickedDate;


    public static final String LOG_TAG = WorkoutEntry.class.getSimpleName();
    public WorkoutListAdapter mAdapter;
    public static final int WORKOUT_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
            EasyFitnessContract.WorkOutOptions.COLUMN_WORKOUT_ID +" " + " as "+" "+ EasyFitnessContract.WorkOutOptions._ID,
            EasyFitnessContract.WorkOutOptions.COLUMN_WORKOUT_DESCRIPTION
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_entry);

        Intent intent = getIntent();


        mDateDisplay = (TextView) findViewById(R.id.pickedDatetext);
        pickedDate = intent.getExtras().getString("PICKED_DATE");
        SessionManagement sessionDate = new SessionManagement(getApplicationContext()).createPickedDateSession
                (pickedDate);
        mDateDisplay.setText(pickedDate);
        View emptyView = findViewById(R.id.listview_forecast_empty);
        ListView workout_list = (ListView)findViewById(R.id.workout_list);
        mAdapter = new WorkoutListAdapter(this,null,0);
        mAdapter.setCustomButtonListner(WorkoutEntry.this);
        workout_list.setEmptyView(emptyView);
        workout_list.setAdapter(mAdapter);
        /*workout_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                } );*/

        getSupportLoaderManager().initLoader(WORKOUT_LOADER, null, this);




    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {

        Uri DescriptionForWorkoutIdUri = EasyFitnessContract.WorkOutOptions
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
       /* Toast.makeText(this, "onLoadFinished", Toast.LENGTH_SHORT).show();*/
        int i=0;
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
       // Toast.makeText(this, "onLoaderReset", Toast.LENGTH_SHORT).show();
        mAdapter.swapCursor(null);
    }

    @Override
    public void onSwitchButtonClickListner(int position,String desc) {
        switch_btn_position = position;
        switch_btn_checked = true;
       /* Toast.makeText(WorkoutEntry.this, "Button click *** " + position+
                        "**********"+desc+"************"+mAdapter
                        .swichbtn_flag,
                Toast.LENGTH_SHORT).show();*/
        mAdapter.selected_desc = desc;
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(switch_btn_checked){
            mAdapter.screenorientaion_selected_row = switch_btn_position;
            mAdapter.switch_checked = switch_btn_checked;
            mAdapter.notifyDataSetChanged();
        }



    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {

        outState.putInt("SWITCHBTN_POSITION", switch_btn_position);
        outState.putBoolean("SWITCHBTN_CHECKED", switch_btn_checked);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {

        switch_btn_position = savedInstanceState.getInt("SWITCHBTN_POSITION");
        switch_btn_checked = savedInstanceState.getBoolean("SWITCHBTN_CHECKED");
        super.onRestoreInstanceState(savedInstanceState);
    }



}
