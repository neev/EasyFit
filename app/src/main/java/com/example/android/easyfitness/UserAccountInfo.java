package com.example.android.easyfitness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserAccountInfo extends AppCompatActivity {
    private static final String TAG = "Login";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_info);
        ButterKnife.bind(this);

        // Session Manager
       SessionManagement session = new SessionManagement(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        String name = user.get(SessionManagement.KEY_NAME);

        // email
        String email = user.get(SessionManagement.KEY_EMAIL);

        _name.setText(name);
        _emailText.setText(email);

    }
}
