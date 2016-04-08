package com.neeraja.android.easyfit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.neeraja.android.easyfit.data.UserDetails;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserAccountInfo extends BaseActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "UserAccountInfo";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_REQUEST_CODE = 100;
    @Bind(R.id.user_name)
    EditText _name;
    @Bind(R.id.user_weight)
    EditText _weight;
    @Bind(R.id.user_goal_weight)
    EditText _goalWeight;
    @Bind(R.id.user_email)
    EditText _emailText;


    @Bind(R.id.user_age)
    EditText _age;
    @Bind(R.id.btn_save)
    Button _saveButton;
    @Bind(R.id.imageView_profile)
    ImageView imageView;
    @Bind(R.id.location_pickerimage)
    ImageView current_location_imagepicker;
    @Bind(R.id.user_current_location)
    EditText _address;
    @Bind(R.id.btn_pick)
    Button pickImageButton;
    Boolean isPickImageButtonClicked = false;
    //Image picker
    private final int PICK_IMAGE_REQUEST = 1;

    Bitmap selectedImage;
    /* A reference to the Firebase */
    private Firebase mFirebaseRef;
    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;
    String authId;
    String email ="";
    String name ="";
    String age="";
    String weight ="";
    String goalWeight ="";
    Intent photoPickerIntent;
    byte[] byteArray;
    private static final int PLACE_PICKER_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_info);
        ButterKnife.bind(this);
        // Initialize Firebase with the application context
        Firebase.setAndroidContext(this);


        // Session Manager
        SessionManagement session = new SessionManagement(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserFirebaseAuthId();
        // name
        authId = user.get(SessionManagement.KEY_NAME);
        // email
        email = user.get(SessionManagement.KEY_EMAIL);
        _emailText.setText(email);
        if (savedInstanceState != null) {

           // _emailText.setText(savedInstanceState.getString("USER_EMAIL"));
            _name.setText(savedInstanceState.getString("USER_NAME"));
            _age.setText(savedInstanceState.getString("USER_AGE"));
            _weight.setText(savedInstanceState.getString("USER_WEIGHT"));
            _goalWeight.setText(savedInstanceState.getString("USER_GOALWEIGHT"));
            isPickImageButtonClicked = savedInstanceState.getBoolean("USER_PROFILE_PIC_CHECKED");
            if(savedInstanceState.getByteArray("USER_PROFILE_PICTURE") != null) {
                selectedImage = Utilities.getImage(savedInstanceState.getByteArray("USER_PROFILE_PICTURE"));
            }

            if(selectedImage !=null){
                ImageView imageView = (ImageView) findViewById(R.id.imageView_profile);
                if(imageView !=null){
                    imageView.setImageBitmap(selectedImage);
                }
            }
            if(isPickImageButtonClicked == true){
                pickImageButton = (Button) findViewById(R.id.btn_pick);
                pickImageButton.setText(" Change Photo");
                pickImageButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        isPickImageButtonClicked = true;
                        photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }
                });
            }





        }

        if (savedInstanceState == null) {
            pickImageButton = (Button) findViewById(R.id.btn_pick);
            pickImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    isPickImageButtonClicked = true;
                    photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            });

        }
        Intent intent = getIntent();
        if(intent.getParcelableExtra("USER_PARCEL_OBJECT")!=null) {
            UserDetails user_object = intent.getParcelableExtra("USER_PARCEL_OBJECT");
            name = user_object.getFullName();
            email = user_object.getEmail();
            int age_int = user_object.getAge();
            int weight_int = user_object.getWeight();
            int goalWeight_int = user_object.getGoalWeight();
            age = String.valueOf(age_int);
            weight = String.valueOf(weight_int);
            goalWeight = String.valueOf(goalWeight_int);

            _name.setText(name);
            _emailText.setText(email);
            _age.setText(age);
            _weight.setText(weight);
            _goalWeight.setText(goalWeight);

        }

        System.out.println("Intent EXTRA" + name+email+age+weight);


        /* Create the Firebase ref that is used for all authentication with Firebase */
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        _saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               boolean isOnline = Utilities.checkConnectivity(getBaseContext());
                if (isOnline) {
                    signup();


                }else {


                    final AlertDialog alertDialog = new AlertDialog.Builder(UserAccountInfo.this,
                            R.style
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

            }
        });
        //place picker for current place

        mGoogleApiClient = new GoogleApiClient.Builder(UserAccountInfo.this)
                .enableAutoManage(this, 0, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(UserAccountInfo.this )
                .addOnConnectionFailedListener( UserAccountInfo.this )
                .build();


        current_location_imagepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPlacePicker();
            }
        });
    }
    @Override
    public void onConnected( Bundle bundle ) {

    }

    @Override
    public void onConnectionSuspended( int i ) {

    }


    private void callPlaceDetectionApi() throws SecurityException {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i(TAG, String.format("Place '%s' with " +
                                    "likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }
                likelyPlaces.release();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPlaceDetectionApi();
                }
                break;
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
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
       /* Toast.makeText(getBaseContext(), "new account failed", Toast.LENGTH_LONG).show();*/
        _saveButton.setEnabled(true);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    public void createUserdeatils_Firebase(){

        name = _name.getText().toString();
        email= _emailText.getText().toString();
        age = _age.getText().toString();
        weight= _weight.getText().toString();
        goalWeight=_goalWeight.getText().toString();
        int nage= Integer.parseInt(age);
        int nweight = Integer.parseInt(weight);
        int ngoalWeight = Integer.parseInt(goalWeight);




        UserDetails userObject = new UserDetails(name,email,nage,nweight,ngoalWeight);

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
        long userInfo_stored = Utilities.addUserAccountInfo(getBaseContext(), userObject, authId);
        System.out.println("successfully STORED USER INFO : ***" + userInfo_stored);

        if(selectedImage != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            selectedImage.recycle();
            byteArray = stream.toByteArray();
        }
            /*String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
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
                .show();*/
        //finish();
        onSuccess();

    }



    public boolean validate() {
        boolean valid = true;


        name = _name.getText().toString();
        email= _emailText.getText().toString();
        age = _age.getText().toString();
        weight= _weight.getText().toString();




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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap_profile_pic = MediaStore.Images.Media.getBitmap(getContentResolver(),
                        uri);
               /* // Log.d(TAG, String.valueOf(bitmap));
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), bitmap);
                circularBitmapDrawable.setCircular(true);*/

                // ImageView imageView = (ImageView) findViewById(R.id.imageView_profile);

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
                selectedImage = bitmap_profile_pic;


            } catch (IOException e) {
                e.printStackTrace();
            }
            if (selectedImage != null) {
                imageView.setImageBitmap(selectedImage);
                pickImageButton.setText("change photo");
            }
        }

        //place picker

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(data, UserAccountInfo.this);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
           // String attributions = (String) place.getAttributions();
           /* if (attributions == null) {
                attributions = "";
            }*/
            String addressString = String.valueOf(new StringBuilder().append(name).append(" ")
                    .append(address));
            System.out.println("ADDRESS : " + addressString);
            _address.setText(addressString);
            /*mName.setText(name);
            mAddress.setText(address);
            mAttributions.setText(Html.fromHtml(attributions));*/

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
    @Override
    protected void onResume() {
        super.onResume();

        if(selectedImage !=null){
            ImageView imageView = (ImageView) findViewById(R.id.imageView_profile);
            if(imageView !=null){
                imageView.setImageBitmap(selectedImage);
            }
        }
        if(isPickImageButtonClicked == true){
            pickImageButton = (Button) findViewById(R.id.btn_pick);
            pickImageButton.setText(" Change Photo");
            pickImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    isPickImageButtonClicked = true;
                    photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            });
        }
        if(isPickImageButtonClicked == false){
            pickImageButton = (Button) findViewById(R.id.btn_pick);
            pickImageButton.setText(" Add Photo");
            pickImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    isPickImageButtonClicked = true;
                    photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            });
        }
// Get the extra text from the Intent
        System.out.println("Intent EXTRA" + name + email + age + weight + goalWeight);

        _saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                signup();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {

            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
    private void displayPlacePicker() {
        if( mGoogleApiClient == null || !mGoogleApiClient.isConnected() )
            return;

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult( builder.build( getApplicationContext() ), PLACE_PICKER_REQUEST );
        } catch ( GooglePlayServicesRepairableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesRepairableException thrown" );
        } catch ( GooglePlayServicesNotAvailableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown" );
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        imageView.setFocusable(true);
        outState.putString("USER_EMAIL", _emailText.getText().toString());
        outState.putString("USER_NAME", _name.getText().toString());
        outState.putString("USER_AGE", _age.getText().toString());
        outState.putString("USER_WEIGHT", _weight.getText().toString());
        outState.putString("USER_GOALWEIGHT", _goalWeight.getText().toString());
        outState.putBoolean("USER_PROFILE_PIC_CHECKED", isPickImageButtonClicked);
        if(selectedImage != null){
            outState.putByteArray("USER_PROFILE_PICTURE",Utilities.getBytes(selectedImage));
       }

        super.onSaveInstanceState(outState);
    }


}