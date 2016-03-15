package com.example.android.easyfitness;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.easyfitness.sync.SunshineSyncAdapter;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private int mYear;
    private int mMonth;
    private int mDate;
    boolean mToday_workout_completed = false;

    static final int DATE_DIALOG_ID = 0;
    String pickedDate;


    ImageView mFlowerImage;
    // Session Manager Class
    SessionManagement session;
    DrawerLayout drawer;NavigationView navigationView;
    boolean loginFlag = true;String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SunshineSyncAdapter.initializeSyncAdapter(this);





       Intent intent = new Intent(MainActivity.this, Login.class);
       startActivity(intent);

        mFlowerImage = (ImageView) findViewById(R.id.flower_imageview);

       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.android.easyfitness", PackageManager.GET_SIGNATURES); //Your
            //    package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }*/



        Glide.with(this)
                .load(R.drawable.f0)
                .fitCenter()
                .into(mFlowerImage);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


// get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDate = c.get(Calendar.DAY_OF_MONTH);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDate);
        }
        return null;
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
                                    .append(Utilities.getMonthName(mMonth+1)).append("-")
                                    .append(mDate).append("-")
                                    .append(mYear).append(" "));

                    System.out.println("Date from Date Picker Widget : " + pickedDate);
                    Intent intent = new Intent(MainActivity.this, WorkoutEntry.class);
                    intent.putExtra("PICKED_DATE", pickedDate);
                    startActivity(intent);
                }
            };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            if(session.checkLogin())
                item.setVisible(false);

            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            Toast.makeText(getApplicationContext(), "login", Toast.LENGTH_LONG)
                    .show();

        } else if (id == R.id.nav_myaccount) {
            if(session.checkLogin()){
            Intent intent = new Intent(MainActivity.this, UserAccountInfo.class);
            startActivity(intent);
            }

        } else if (id == R.id.nav_workoutEntry) {

            if(session.checkLogin()) {

                showDialog(DATE_DIALOG_ID);

            }

        } else if (id == R.id.nav_calendar) {
          if(session.checkLogin()){
                Intent intent = new Intent(MainActivity.this, CalenderView.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_workout_history) {
            if(session.checkLogin()) {
                Intent intent = new Intent(MainActivity.this, WorkoutHistory.class);
                startActivity(intent);
            }

        }else if (id == R.id.nav_logout) {

                session.logoutUser();

                item.setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);

            Toast.makeText(MainActivity.this, "Please LOG IN !", Toast.LENGTH_LONG)
                    .show();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void onResume(){
        super.onResume();
        // Session class instance
        session = new SessionManagement(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserFirebaseAuthId();
        HashMap<String, Integer> flag = session.getFlag_Session();
        // name
        String userAuthId = user.get(SessionManagement.KEY_NAME);
        int flower_flag = flag.get(SessionManagement.FLAG);
        // email
        email = user.get(SessionManagement.KEY_EMAIL);
        navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);

        //Flower Image Display check




            Glide.with(this)
                    .load(Utilities.today_flowerimage(flower_flag))
                    .fitCenter()
                    .into(mFlowerImage);


    }


}
