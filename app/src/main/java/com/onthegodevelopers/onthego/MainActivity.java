package com.onthegodevelopers.onthego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button oForgotButton;
    private Button oSignUpButton;
    EditText UsernameEt,UserPasswordEt;


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
         String username = UsernameEt.getText().toString();
         String userpass = UserPasswordEt.getText().toString();
         String type = "login";
         BackgroundWorker backgroundWorker = new BackgroundWorker(this);
         backgroundWorker.execute(type,username,userpass);

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
