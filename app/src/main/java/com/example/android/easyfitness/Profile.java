package com.example.android.easyfitness;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.easyfitness.data.EasyFitnessContract;
import com.example.android.easyfitness.data.EasyfitnessDbHelper;

import java.util.HashMap;

public class Profile extends BaseActivity   {

    public static final String LOG_TAG = Profile.class.getSimpleName();
    private ImageView mProfileImage;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);*/
        String selectedImage = "string";
        String name = "";
        String email = "";
        int weight= 0;
        int age =0;
        int goal_weight =0;
        String created_date= "";
        // Session Manager
        SessionManagement session = new SessionManagement(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserFirebaseAuthId();
        // name
       String  authId = user.get(SessionManagement.KEY_NAME);

        // Find fields to populate in inflated template
        TextView text_name = (TextView) findViewById(R.id.tvNumber4);
        TextView text_email = (TextView) findViewById(R.id.tvNumber3);
        TextView text_weight = (TextView) findViewById(R.id.tvNumber1);
        TextView text_goal = (TextView) findViewById(R.id.tvNumber2);
        TextView text_age = (TextView) findViewById(R.id.tvNumberAge);
       bindProfileToolbar();

      // mProfileImage.setImageResource(R.drawable.google_thumb);


        SQLiteDatabase db=(new EasyfitnessDbHelper(this)).getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT _id,user_name,user_email,user_weight,user_goal_weight," +
                        "user_create_date," +
                        "enter_age," +
                        "image_data" +
                        " FROM userdetail WHERE userdeatil_authid" +
                        " = ?",
                new String[]{"" + authId});
        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            name = cursor.getString(cursor.getColumnIndexOrThrow(EasyFitnessContract
                    .UserDetailEntry.COLUMN_USER_NAME));
            email = cursor.getString(cursor.getColumnIndexOrThrow(EasyFitnessContract
                    .UserDetailEntry.COLUMN_USER_EMAIL));

            weight = cursor.getInt(cursor.getColumnIndexOrThrow(EasyFitnessContract
                    .UserDetailEntry.COLUMN_USER_WEIGHT));
            age = cursor.getInt(cursor.getColumnIndexOrThrow(EasyFitnessContract.UserDetailEntry
                    .COLUMN_USER_AGE));
            created_date = cursor.getString(cursor.getColumnIndexOrThrow(EasyFitnessContract
                    .UserDetailEntry.COLUMN_USER_CREATED));
           /* selectedImage = cursor.getString(cursor.getColumnIndexOrThrow(EasyFitnessContract
                    .UserDetailEntry.KEY_IMAGE));*/
            i++;
            cursor.moveToNext();
        }
        db.close();


   /* byte[] byteValue = selectedImage.getBytes();
    Bitmap imageProfilebitmap = Utilities.getImage(byteValue);*/



        // Populate fields with extracted properties
        text_name.setText(name);
        text_email.setText(email);
        text_age.setText(String.valueOf(age));
        text_weight.setText(String.valueOf(weight)+ " lbs");

       /* DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mProfileImage.setMinimumHeight(dm.heightPixels);
        mProfileImage.setMinimumWidth(dm.widthPixels);
        mProfileImage.setImageBitmap(imageProfilebitmap);*/



System.out.println("+++++++++++" + name+email+weight+goal_weight+"***"+selectedImage+ "////"+
        created_date);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Edit the Profile info", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(Profile.this, UserAccountInfo.class);
                startActivity(intent);

            }
        });


    }
    private void bindProfileToolbar() {
        mToolbar        = (Toolbar) findViewById(R.id.toolbarProfile);
        mProfileImage   = (ImageView) findViewById(R.id.profileimageview);

        mAppBarLayout   = (AppBarLayout) findViewById(R.id.app_barProfile);
    }

}

     