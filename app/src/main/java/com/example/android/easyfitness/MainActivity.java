package com.example.android.easyfitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.easyfitness.sync.SunshineSyncAdapter;

import java.util.HashMap;

public class MainActivity extends BaseActivity
{

    private static final String TAG = "MainActivity";

    int flower_flag = 0;

    ImageView mFlowerImage;
    // Session Manager Class
    SessionManagement session;
    DrawerLayout drawer;NavigationView navigationView;
    boolean loginFlag = true;String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SunshineSyncAdapter.initializeSyncAdapter(this);
        // Session class instance
        session = new SessionManagement(getApplicationContext());
    if(!session.checkLogin()) {
    Intent intent = new Intent(MainActivity.this, Login.class);
    startActivity(intent);
}
        mFlowerImage = (ImageView) findViewById(R.id.flower_imageview);

       // mFlowerImage.setImageResource(Utilities.today_flowerimage(flower_flag));

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



        Glide.with(MainActivity.this)
                .load(R.drawable.f0)
                .fitCenter()
                .into(mFlowerImage);
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



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

        // get user data from session
        HashMap<String, String> user = session.getUserFirebaseAuthId();
        HashMap<String, Integer> flag = session.getFlag_Session();
        // name
        String userAuthId = user.get(SessionManagement.KEY_NAME);
       flower_flag = flag.get(SessionManagement.FLAG);
        // email
        email = user.get(SessionManagement.KEY_EMAIL);

        //Flower Image Display check
//mFlowerImage.setImageResource(Utilities.today_flowerimage(flower_flag));
            Glide.with(MainActivity.this)
                    .load(Utilities.today_flowerimage(flower_flag))
                    .fitCenter()
                    .into(mFlowerImage);

    }


}
