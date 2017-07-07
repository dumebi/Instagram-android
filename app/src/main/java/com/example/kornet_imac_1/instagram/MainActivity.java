package com.example.kornet_imac_1.instagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText usernameField;
    EditText passwordField;
    TextView changSignupMode;
    LinearLayout layoutLinear;
    Button signupButton;

    Boolean signupModeActive;
    public void signupOrLogin(View view){
        if (signupModeActive == true) {
            ParseUser user = new ParseUser();
            user.setUsername(String.valueOf(usernameField.getText()));
            user.setPassword(String.valueOf(passwordField.getText()));

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("AppInfo - Signup", "successful");
                        showUserList();
                    }else{
                        Log.i("AppInfo - Signup", "failed");
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(": ")), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            ParseUser.logInInBackground(String.valueOf(usernameField.getText()), String.valueOf(passwordField.getText()), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Log.i("AppInfo - login", "successful");
                        showUserList();
                    }else{
                        Log.i("AppInfo - login", "failed");
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(": ")), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameField.getWindowToken(), 0);
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.changeSignUPMODE){
            if (signupModeActive == true){
                signupModeActive = false;
                changSignupMode.setText("Register");
                signupButton.setText("Login");
            }else{
                signupModeActive = true;
                changSignupMode.setText("Login");
                signupButton.setText("Register");
            }
        }
    }

    public void showUserList(){
        Intent userlist = new Intent(getApplicationContext(), UserList.class);
        startActivity(userlist);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myAppId")
                .clientKey("myMasterKey")
                .server("https://onlineparse.herokuapp.com/parse/")
                .build()
        );

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        if(ParseUser.getCurrentUser() != null){
            showUserList();
        }

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        layoutLinear = (LinearLayout)findViewById(R.id.layoutLinear);
        changSignupMode = (TextView) findViewById(R.id.changeSignUPMODE);
        signupModeActive = true;
        signupButton = (Button)findViewById(R.id.signin);

        changSignupMode.setOnClickListener(this);


        layoutLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
    }


}
