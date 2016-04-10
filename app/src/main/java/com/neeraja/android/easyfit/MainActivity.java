package com.neeraja.android.easyfit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.neeraja.android.easyfit.data.EasyfitnessDbHelper;
import com.neeraja.android.easyfit.sync.EasyFitSyncAdapter;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends BaseActivity
{

    private static final String TAG = "MainActivity";

    int flower_flag = 0;

    ImageView mFlowerImage;
    // Session Manager Class
    SessionManagement session;
    DrawerLayout drawer;NavigationView navigationView;
    boolean loginFlag = true;
    String email;
    //To check the internet conection

    boolean isOnline;
    CoordinatorLayout coordinatorLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Session class instance
        session = new SessionManagement(getApplicationContext());
        if(!session.checkLogin()) {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        }

        mFlowerImage = (ImageView) findViewById(R.id.flower_imageview);
         coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .baseActivity_coorid_layout);
        mFlowerImage.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                if(event.getActionMasked() == MotionEvent.ACTION_UP){

                                                    Snackbar snackbar = Snackbar
                                                        .make(coordinatorLayout, "Please click the menu or slide right  to view " +
                                                                "more!", Snackbar.LENGTH_LONG);

                                                    return true;
                                                }
                                                return false;
                                            }
                                        }
        );

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


                flower_flag = NumberofRECORDSthisWEEK();
        Glide.with(MainActivity.this)
                .load(Utilities.today_flowerimage(flower_flag))
                .fitCenter()
                .into(mFlowerImage);


        isOnline = Utilities.checkConnectivity(getBaseContext());
        if (isOnline) {
            EasyFitSyncAdapter.syncImmediately(this);


        }else {


            final AlertDialog alertDialog = new AlertDialog.Builder(this,R.style
                    .AppTheme_Dark_Dialog).create();

            alertDialog.setTitle("Network Not Connected...");
            alertDialog.setMessage("Please connect to a network and try again");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });
            alertDialog.setIcon(R.drawable.images_navicon);

            alertDialog.show();
        }
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }



   /* public class GlideConfiguration implements GlideModule {

        @Override
        public void applyOptions(Context context, GlideBuilder builder) {
            // Apply options to the builder here.
            builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        }

        @Override
        public void registerComponents(Context context, Glide glide) {
            // register ModelLoaders here.
        }
    }*/



    @Override
    public void onBackPressed() {

            super.onBackPressed();

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


    protected void onResume(){
        super.onResume();

     /*   isOnline = Utilities.checkConnectivity(getBaseContext());
        // get user data from session


        // name
        String userAuthId = user.get(SessionManagement.KEY_NAME);


        if (isOnline) {

            FetchRecordsFromFirebase mFetchRecordsfromFirebase = new FetchRecordsFromFirebase(this);
            mFetchRecordsfromFirebase.execute(userAuthId);

        }else {


            final AlertDialog alertDialog = new AlertDialog.Builder(this,R.style
                    .AppTheme_Dark_Dialog).create();

            alertDialog.setTitle("Network Not Connected...");
            alertDialog.setMessage("Please connect to a network and try again");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });
            alertDialog.setIcon(R.drawable.images_navicon);

            alertDialog.show();
        }*/
        isOnline = Utilities.checkConnectivity(getBaseContext());
        if (isOnline) {
            EasyFitSyncAdapter.syncImmediately(this);


        }else {


            final AlertDialog alertDialog = new AlertDialog.Builder(this,R.style
                    .AppTheme_Dark_Dialog).create();

            alertDialog.setTitle("Network Not Connected...");
            alertDialog.setMessage("Please connect to a network and try again");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });
            alertDialog.setIcon(R.drawable.images_navicon);

            alertDialog.show();
        }

        flower_flag = NumberofRECORDSthisWEEK();
        Glide.with(MainActivity.this)
                    .load(Utilities.today_flowerimage(flower_flag))
                    .fitCenter()
                    .into(mFlowerImage);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {

    outState.putInt("Flower_FLAG_SAVEInstanceState", flower_flag);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {


        flower_flag = savedInstanceState.getInt("Flower_FLAG_SAVEInstanceState");

        super.onRestoreInstanceState(savedInstanceState);
    }

    public int NumberofRECORDSthisWEEK(){
        Cursor cursor;
        SQLiteDatabase db;

        // Session Manager
        SessionManagement session = new SessionManagement(this);
        // get user data from session
        HashMap<String, String> user = session.getUserFirebaseAuthId();
        // name
        final String authId = user.get(SessionManagement.KEY_NAME);




       /* FetchRecordsFromFirebase mFetchRecordsfromFirebase = new
                FetchRecordsFromFirebase(getBaseContext());
        mFetchRecordsfromFirebase.execute(authId);*/

        EasyFitSyncAdapter.initializeSyncAdapter(this);
        //Get the start date of the current week

        int mYear;
        int mMonth;
        int mDate;
        int mDay;
        final Calendar currentDate = Calendar.getInstance();
        mYear = currentDate.get(Calendar.YEAR);
        mMonth = currentDate.get(Calendar.MONTH);
        mDate = currentDate.get(Calendar.DATE);
        mDay = currentDate.get(Calendar.DAY_OF_WEEK);

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

        int numberOfWorkouts = 0;

        if(authId != null) {

            Date startDate = new Date((mYear - 1900), (mMonth), days_back);

            db = (new EasyfitnessDbHelper(this)).getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM UserWorkOutRecord WHERE " +
                            "userdeatil_authid" +
                            " = ?  AND full_date  BETWEEN date(?)  AND date(? ,'+7 days')",
                    new String[]{authId, String.valueOf(startDate), String.valueOf(startDate)});
            //int i = 0;

            numberOfWorkouts = cursor.getCount();

            db.close();
        }
        return numberOfWorkouts;
    }



}
