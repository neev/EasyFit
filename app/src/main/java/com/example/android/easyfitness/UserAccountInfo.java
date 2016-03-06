package com.example.android.easyfitness;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.easyfitness.data.UserDetails;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

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
        Toast.makeText(getBaseContext(), "Data saved successfully", Toast.LENGTH_LONG).show();
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
