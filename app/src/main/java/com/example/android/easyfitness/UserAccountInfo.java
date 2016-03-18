package com.example.android.easyfitness;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;
import com.example.android.easyfitness.data.UserDetails;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserAccountInfo extends BaseActivity  {
    private static final String TAG = "UserAccountInfo";
    private static final int REQUEST_SIGNUP = 0;
    @Bind(R.id.user_name)
    EditText _name;
    @Bind(R.id.user_weight)
    EditText _weight;
    @Bind(R.id.user_email)
    EditText _emailText;
    @Bind(R.id.user_age)
    EditText _age;
    @Bind(R.id.btn_save)
    Button _saveButton;
    Button pickImage;
    //Image picker
    private final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    Bitmap selectedImage;
    /* A reference to the Firebase */
    private Firebase mFirebaseRef;
    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;
    String authId;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_info);
        ButterKnife.bind(this);
        // Initialize Firebase with the application context
        Firebase.setAndroidContext(this);

         pickImage = (Button) findViewById(R.id.btn_pick);
        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"),PICK_IMAGE_REQUEST);
            }
        });





        // Session Manager
       SessionManagement session = new SessionManagement(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserFirebaseAuthId();
        // name
         authId = user.get(SessionManagement.KEY_NAME);

        // email
         email = user.get(SessionManagement.KEY_EMAIL);


        _emailText.setText(email);

        /* Create the Firebase ref that is used for all authentication with Firebase */
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        _saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                signup();
            }
        });

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onFailed();
            return;
        }

        _saveButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(UserAccountInfo.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        createUserdeatils_Firebase();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        try {
                            if ((progressDialog != null) && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } catch (final IllegalArgumentException e) {
                            // Handle or log or ignore
                        } catch (final Exception e) {
                            // Handle or log or ignore
                        } finally {
                            // progressDialog = null;
                        }
                        // onLoginFailed();

                        // On complete call either onLoginSuccess or onLoginFailed
                        onSuccess();


                    }
                }, 3000);
    }

    public void onSuccess() {
        _saveButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(UserAccountInfo.this, Profile.class);
        startActivity(intent);
        finish();
    }
    public void onFailed() {
        Toast.makeText(getBaseContext(), "new account failed", Toast.LENGTH_LONG).show();
        _saveButton.setEnabled(true);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    public void createUserdeatils_Firebase(){

        String name = _name.getText().toString();
        String email= _emailText.getText().toString();
        String age = _age.getText().toString();
        String weight= _weight.getText().toString();
        int nage= Integer.parseInt(age);
        int nweight = Integer.parseInt(weight);




        UserDetails userObject = new UserDetails(name,email,nage,nweight);

        Firebase userRef = mFirebaseRef.child("users").child(authId);
        userRef.setValue(userObject, new Firebase.CompletionListener() {

            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Data could not be saved. " + firebaseError.getMessage());
                    Log.i(TAG, firebaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                }
            }
        });
            //storing user info in sqlite
        long userInfo_stored = Utilities.addUserAccountInfo(getBaseContext(),userObject,authId);
        System.out.println("successfully STORED USER INFO : ***" + userInfo_stored);

if(selectedImage != null) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
    selectedImage.recycle();
    byte[] byteArray = stream.toByteArray();
    String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
    userRef.child("Images").child(authId).setValue(imageFile, new Firebase
            .CompletionListener() {

        @Override
        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
            if (firebaseError != null) {
                System.out.println("User Image could not be saved. " + firebaseError.getMessage
                        ());
                Log.i(TAG, firebaseError.getMessage());
            } else {
                System.out.println("User Image saved successfully.");
            }
        }
    });

    int updatedProfileImage = Utilities.updateUserAccountInfowithProfileImage(getBaseContext(),
            authId,"Profile Image",imageFile);

}

        Toast.makeText(getBaseContext(), "Image Data saved successfully  : " + userInfo_stored,
                Toast.LENGTH_LONG)
                .show();
        //finish();
        onSuccess();

    }


    public boolean validate() {
        boolean valid = true;


        String name = _name.getText().toString();
        String email= _emailText.getText().toString();
        String age = _age.getText().toString();
        String weight= _weight.getText().toString();




        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (name.isEmpty() || name.length() < 4 || name.length() > 30) {
            _name.setError("Please enter your name");
            valid = false;
        } else {
            _name.setError(null);
        }

        if (age.isEmpty() || age.length() < 1|| age.length() > 4) {
            _age.setError("Please enter your Age");
            valid = false;
        } else {
            _age.setError(null);
        }

        if (weight.isEmpty() ||  weight.length() <1|| weight.length() > 4) {
            _weight.setError("Please enter your Weight in lbs");
            valid = false;
        } else {
            _weight.setError(null);
        }

        return valid;
    }

    public class GlideConfiguration implements GlideModule {

        @Override
        public void applyOptions(Context context, GlideBuilder builder) {
            // Apply options to the builder here.
            builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        }

        @Override
        public void registerComponents(Context context, Glide glide) {
            // register ModelLoaders here.
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
               /* // Log.d(TAG, String.valueOf(bitmap));
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), bitmap);
                circularBitmapDrawable.setCircular(true);*/

                ImageView imageView = (ImageView) findViewById(R.id.imageView_profile);

               /* Glide.with(this).load(bitmap).centerCrop().into(imageView);

                Glide.with(this)
                        .load("yourUrl")
                        .asBitmap()
                        .into(new BitmapImageViewTarget(yourImageView)) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> anim) {
                        super.onResourceReady(bitmap, anim);
                        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                // Here's your generated palette
                                Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                                int color = palette.getDarkVibrantColor(swatch.getTitleTextColor());
                            }
                        });*/
                imageView.setImageBitmap(bitmap);
                pickImage.setText("change photo");
                selectedImage = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

}
