package com.onthegodevelopers.onthego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;

public class SignUp extends AppCompatActivity {
    //Button oSignUpButton;
    EditText userName,userEmailID,userPassword,userMobileNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userName =(EditText) findViewById(R.id.nameOfUser);
        userEmailID =(EditText) findViewById(R.id.userEmailID);
        userPassword =(EditText) findViewById(R.id.userPassword);
        userMobileNo =(EditText) findViewById(R.id.userMobileNumber);
    }

    public void onSignup(View view) {
        String user_name =userName.getText().toString();
        String user_pass =userPassword.getText().toString();
        String user_email =userEmailID.getText().toString();
        String user_mobile =userMobileNo.getText().toString();
        String type = "SignUp";
        Log.i("INFO",user_name);
        Log.i("INFO",user_pass);
        Log.i("INFO",user_email);
        Log.i("INFO",user_mobile);
        BackgroundWorker backgroundWorker =new BackgroundWorker(this);
        backgroundWorker.execute(type,user_name,user_pass,user_mobile,user_email);
    }
}

