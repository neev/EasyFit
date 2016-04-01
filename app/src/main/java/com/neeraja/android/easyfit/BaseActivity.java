package com.neeraja.android.easyfit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.neeraja.android.easyfit.data.EasyfitnessDbHelper;

import java.util.Calendar;
import java.util.HashMap;


public class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public NavigationView navigationView;
    private DrawerLayout fullLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedNavItemId;
    SessionManagement session;


    private int mYear;
    private int mMonth;
    private int mDate;


    static final int DATE_DIALOG_ID = 0;
    String pickedDate;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }*/

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        /**
         * This is going to be our actual root layout.
         */
        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        /**
         * {@link FrameLayout} to inflate the child's view. We could also use a {@link ViewStub}
         */
        FrameLayout activityContainer = (FrameLayout) fullLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        /**
         * Note that we don't pass the child's layoutId to the parent,
         * instead we pass it our inflated layout.
         */
        super.setContentView(fullLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        navigationView = (NavigationView) findViewById(R.id.navigationView);



        if (useToolbar()) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            toolbar.setVisibility(View.GONE);
        }

        setUpNavView();
    }


    protected boolean useToolbar() {
        return true;
    }

    protected void setUpNavView() {
        navigationView.setNavigationItemSelectedListener(this);
        session=new SessionManagement(this);

        if (useDrawerToggle()) { // use the hamburger menu
            drawerToggle = new ActionBarDrawerToggle(this, fullLayout, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            fullLayout.setDrawerListener(drawerToggle);


            drawerToggle.syncState();
        } else if (useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources()
                    .getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        }
    }


    protected boolean useDrawerToggle() {
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        fullLayout.closeDrawer(GravityCompat.START);
        selectedNavItemId = menuItem.getItemId();

        return onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        switch (id) {
            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.nav_login: {
                if (!session.checkLogin()) {

                    navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);

                }

            }
            return true;

            case R.id.nav_myaccount: {
                if (session.checkLogin()) {
                    // Session Manager
                    SessionManagement session = new SessionManagement(getApplicationContext());
                    // get user data from session
                    HashMap<String, String> user = session.getUserFirebaseAuthId();
                    // name
                    String  authId = user.get(SessionManagement.KEY_NAME);
                    SQLiteDatabase db=(new EasyfitnessDbHelper(this)).getReadableDatabase();
                    Cursor cursor=db.rawQuery("SELECT user_name,user_email,user_weight,user_goal_weight," +
                                    "image_data" +
                                    " FROM userdetail WHERE userdeatil_authid" +
                                    " = ?",
                            new String[]{"" + authId});
                    int i = 0;
                    cursor.moveToFirst();
                   if(cursor.getCount()>0){
                       Intent intent = new Intent(this, Profile.class);
                       startActivity(intent);
                   }
                    else {
                       Intent intent = new Intent(this, UserAccountInfo.class);
                       startActivity(intent);
                   }
                    db.close();


                }

            }
            return true;

            case R.id.nav_calendar: {
                if (session.checkLogin()) {
                    Intent intent = new Intent(this, CalenderView.class);
                    startActivity(intent);
                }

            }
            return true;

            case R.id.nav_workout_history: {
                if (session.checkLogin()) {
                    Intent intent = new Intent(this, WorkoutHistory.class);
                    startActivity(intent);
                }

            }
            return true;

            case R.id.nav_workoutEntry: {

                if (session.checkLogin()) {

                    showDialog(DATE_DIALOG_ID);

                }

            }
            return true;

            case R.id.nav_logout: {
                if (session.checkLogin()) {

                    session.logoutUser();
                    navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);

                    Toast.makeText(this, "Please LOG IN !", Toast.LENGTH_LONG)
                            .show();
                }

            }
            return true;
            case R.id.nav_about: {

                if (session.checkLogin()) {
                    Intent intent = new Intent(this, About.class);
                    startActivity(intent);


                }

            }
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDate = c.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDate);
        }
        return null;
    }


    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (fullLayout.isDrawerOpen(GravityCompat.START)) {
            fullLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDate = dayOfMonth;

                    pickedDate =
                            String.valueOf(new StringBuilder()
                                    // Month is 0 based so add 1

                                    .append(mDate).append(" ")
                                    .append(Utilities.getMonthName(mMonth + 1)).append(" ")
                                    .append(mYear).append(" "));

                    System.out.println("Date from Date Picker Widget : " + pickedDate);
                    Intent intent = new Intent(BaseActivity.this, WorkoutEntry.class);
                    intent.putExtra("PICKED_DATE", pickedDate);
                    startActivity(intent);
                }
            };

    protected void onResume() {
        super.onResume();


            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);



    }
}




