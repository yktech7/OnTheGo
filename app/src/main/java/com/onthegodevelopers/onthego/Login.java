package com.onthegodevelopers.onthego;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private Button oForgotButton;
    private Button oSignUpButton;
    EditText UsernameEt,UserPasswordEt;
    String username,userpass;
    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        oForgotButton = (Button) findViewById(R.id.forgotPassword);
        oForgotButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openResetPasswordActivity();
            }
        });

        oSignUpButton = (Button) findViewById(R.id.signUpButton);
        oSignUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openSignUpActivity();
            }
        });

        UsernameEt = (EditText) findViewById(R.id.userEmailMobile);
        UserPasswordEt = (EditText) findViewById(R.id.userPassword);
    }

    public void onLoginButton(View view) {
        initialize();
        if (!validate()) {
            Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
        } else {
            onLogin();
        }
    }

    private void onLogin(){
         Log.d(TAG, "Login button clicked");
         String type = "login";
//         BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//         backgroundWorker.execute(type,username,userpass);
        UserConnect userConnect = new UserConnect(this);
        userConnect.execute(type,username,userpass);
    }

    private void initialize(){
        username = UsernameEt.getText().toString();
        userpass = UserPasswordEt.getText().toString();
    }

    public Boolean validate(){
        boolean valid=true;
        if (username.isEmpty()){
            UsernameEt.setError("Please enter valid Email ID or Mobile Number");
            valid =false;
        }
        if (userpass.isEmpty() || userpass.length() >32){
            UserPasswordEt.setError("Password cannot be empty, Please enter a valid password");
            valid =false;
        }
        return  valid;
    }

    public void openResetPasswordActivity(){
        Intent oIntent = new Intent(this, resetPassword.class);
        startActivity(oIntent);

    }
    public void openSignUpActivity(){
        Intent oSignUp = new Intent(this,SignUp.class);
        startActivity(oSignUp);
    }
}
