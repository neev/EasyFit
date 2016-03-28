package com.example.android.easyfitness;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.easyfitness.data.EasyFitnessContract;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

public class WorkoutHistory extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
// Session Manager
        SessionManagement session = new SessionManagement(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserFirebaseAuthId();
        // name
        final String authId = user.get(SessionManagement.KEY_NAME);
        FetchRecordsFromFirebase mFetchRecordsfromFirebase = new
                FetchRecordsFromFirebase(getBaseContext());
        mFetchRecordsfromFirebase.execute(authId);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements LoaderManager
            .LoaderCallbacks<Cursor>{
        public static final int SCORES_LOADER = 0;
        public int _placeholderNumber = 0;
        public WorkoutHistoryListAdapter mAdapter;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        private static final String[] WORKOUT_RECORD_COLUMNS = {
                EasyFitnessContract.UserWorkOutRecord._ID +" " + " as "+" "+
                        EasyFitnessContract.UserWorkOutRecord._ID,
                EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_DURATION ,
                EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_DESCRIPTION,
                EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_YEAR,
                EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_MONTH,
                EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_DATE,
                EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_RECORDED_DATE_DAY
        };

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_workout_history, container, false);
            _placeholderNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            View emptyView = rootView.findViewById(R.id.listview_forecast_empty);

            ListView score_list = (ListView) rootView.findViewById(R.id.scores_list);


            mAdapter = new WorkoutHistoryListAdapter(getActivity(),null,0);
            score_list.setEmptyView(emptyView);
            score_list.setAdapter(mAdapter);
            getLoaderManager().initLoader(SCORES_LOADER, null, this);


            return rootView;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
        {
            int mYear;
            int mMonth;
            int mDate;
            int mDay;
            final Calendar currentDate = Calendar.getInstance();
            mYear = currentDate.get(Calendar.YEAR);
            mMonth = currentDate.get(Calendar.MONTH);
            mDate = currentDate.get(Calendar.DATE);
            mDay = currentDate.get(Calendar.DAY_OF_WEEK);

            // Session Manager
            SessionManagement session = new SessionManagement(getActivity().getBaseContext());
            // get user data from session
            HashMap<String, String> user = session.getUserFirebaseAuthId();
            // name
            String authId = user.get(SessionManagement.KEY_NAME);
            CursorLoader curLoader = null;

           switch(_placeholderNumber) {
               case 0: {
                   curLoader = new CursorLoader(getActivity(), EasyFitnessContract.UserWorkOutRecord
                           .buildWorkoutRecordWithAuthId(authId),
                           WORKOUT_RECORD_COLUMNS, null, null, "workout_recorded_year desc, " +
                           "workout_recorded_month desc, workout_recorded_date desc");

                   break;
               }
               case 1: {

                   curLoader = new CursorLoader(getActivity(), EasyFitnessContract
                           .UserWorkOutRecord.buildWorkoutRecordWithUserAuthIdandMonth(authId,
                                   mYear, (mMonth + 1)), WORKOUT_RECORD_COLUMNS, null, null,
                           "workout_recorded_year desc, " +
                                   "workout_recorded_month desc, workout_recorded_date desc");
                   break;
               }
               case 2: {

                   // WORKOUT RECORDS FOR THIS WEEK

                   // get the current date



                   int days_back = 0;
                    // current day of the week
                   System.out.println("DAY OF THE WEEK: "+ mDay);

                   //TO GET THE DATES OF THIS WEEK
                   switch (mDay) {
                       case 1:
                           days_back = mDate;
                           break;
                       case 2:
                           days_back = mDate - 1;
                           break;
                       case 3:
                           days_back = mDate - 2;
                           break;
                       case 4:
                           days_back = mDate - 3;
                           break;
                       case 5:
                           days_back = mDate - 4;
                           break;
                       case 6:
                           days_back = mDate - 5;
                           break;
                       case 7:
                           days_back = mDate - 6;
                           break;



                   }

                   Date startDate = new Date((mYear-1900),(mMonth),days_back);

                   System.out.println("Start Date : " + startDate.toString());

                   curLoader = new CursorLoader(getActivity(), EasyFitnessContract.UserWorkOutRecord
                           .buildWorkoutRecordWithUserAuthIdandThisWeek
                                   (authId,String.valueOf(startDate),String
                                           .valueOf(startDate)),
                           WORKOUT_RECORD_COLUMNS, null, null, "workout_recorded_year desc, " +
                           "workout_recorded_month desc, workout_recorded_date desc");
               }
               break;

           }
            return curLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
        {
           // Toast.makeText(getActivity(), "onLoadFinished", Toast.LENGTH_SHORT).show();

            cursor.moveToFirst();
          for(int i=0; i<cursor.getCount();i++)
            {

                cursor.moveToNext();
            }
            //Log.v(FetchScoreTask.LOG_TAG,"Loader query: " + String.valueOf(i));
            mAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader)
        {
           // Toast.makeText(getActivity(), "onLoaderReset", Toast.LENGTH_SHORT).show();
            mAdapter.swapCursor(null);
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title = "";
            switch (position) {
                case 0:
                    title = "ALL";
                    break;
                case 1:
                    title = "This Month";
                    break;
                case 2:
                    title = "This Week";
                    break;
            }
            return title;
        }
    }
}