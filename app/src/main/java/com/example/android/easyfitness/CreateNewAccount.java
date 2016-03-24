package com.example.android.easyfitness;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateNewAccount extends BaseActivity  {

    private static final String TAG = "CreateNewAccount";


    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;
    String name;
    String email;
    String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);
        ButterKnife.bind(this);
        // Initialize Firebase with the application context
        Firebase.setAndroidContext(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(CreateNewAccount.this, Login.class);
                startActivity(intent);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(CreateNewAccount.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


         email = _emailText.getText().toString();
         password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.
        createAccount(email,password);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(CreateNewAccount.this, Login.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "new account failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
        signup();
    }

    public boolean validate() {
        boolean valid = true;


         email = _emailText.getText().toString();
         password = _passwordText.getText().toString();



        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    public void createAccount(String email,String pwd){

        Firebase ref = new Firebase(getResources().getString(R.string.firebase_url));
        ref.createUser(email, pwd, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                Toast.makeText(getBaseContext(), " new account created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                Toast.makeText(getBaseContext(), "new account not created", Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {

        outState.putString("USER_EMAIL", email);
        outState.putString("USER_PASSWORD", password);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {


        email = savedInstanceState.getString("USER_EMAIL");
        password = savedInstanceState.getString("USER_PASSWORD");

        super.onRestoreInstanceState(savedInstanceState);
    }

}
