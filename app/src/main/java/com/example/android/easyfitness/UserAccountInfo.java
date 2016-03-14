package com.example.android.easyfitness;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.easyfitness.data.UserDetails;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserAccountInfo extends AppCompatActivity {
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





        Button pickImage = (Button) findViewById(R.id.btn_pick);
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
                createUserdeatils_Firebase();
            }
        });

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
        userRef.push().setValue(userObject, new Firebase.CompletionListener() {

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
        System.out.println("successfully STORED USER INFO : ***"+userInfo_stored);

if(selectedImage != null) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
    selectedImage.recycle();
    byte[] byteArray = stream.toByteArray();
    String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
    userRef.child("Images").child(authId).push().setValue(imageFile, new Firebase
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
}

        long userinfo_stored =  Utilities.addUserAccountInfo(getBaseContext(),userObject,authId);
        Toast.makeText(getBaseContext(), "Data saved successfully  : " + userinfo_stored, Toast.LENGTH_LONG)
                .show();
        finish();

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

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                //Glide.with(this).load(circularBitmapDrawable).centerCrop().into(imageView);

                imageView.setImageBitmap(bitmap);
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
