package com.onthegodevelopers.onthego;

import android.os.PatternMatcher;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;

public class SignUp extends AppCompatActivity {
    //Button oSignUpButton;
    EditText userName,userEmailID,userPassword,userMobileNo,userCPassword;
    String user_name,user_email,user_pass,user_cpass,user_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userName =(EditText) findViewById(R.id.nameOfUser);
        userEmailID =(EditText) findViewById(R.id.userEmailID);
        userPassword =(EditText) findViewById(R.id.userPassword);
        userMobileNo =(EditText) findViewById(R.id.userMobileNumber);
        userCPassword=(EditText) findViewById(R.id.userCPassword);
    }

    public void onSignup(View view) {
        initialize();
        if (!validate()){
            Toast.makeText(this,"Sign Up Failed",Toast.LENGTH_SHORT).show();
        }
        else{
            onSignupSuccess();
        }
    }

    public Boolean validate(){
        boolean valid=true;
        if (user_name.isEmpty() || user_name.length() >32){
            userName.setError("Please enter a valid Name");
            valid =false;
        }
        if (user_email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
            userEmailID.setError("Please enter a valid Email ID");
            valid =false;
        }
        if (user_pass.isEmpty() || user_pass.length() >32){
            userPassword.setError("Please enter a valid Password");
            valid =false;
        }
//        Log.i("INFO",user_cpass);
//        Log.i("INFO",user_pass);
//        if (user_cpass.isEmpty() || user_cpass.length() >32 || user_pass != user_cpass){
//            userCPassword.setError("Password are mismatched");
//            valid =false;
//        }
        if (user_mobile.isEmpty() || user_mobile.length() != 10 ){
            userMobileNo.setError("Please enter a valid Mobile number");
            valid =false;
        }
        return  valid;
    }
    public void onSignupSuccess(){
        String type = "SignUp";
        BackgroundWorker backgroundWorker =new BackgroundWorker(this);
        backgroundWorker.execute(type,user_name,user_pass,user_mobile,user_email);
    }
    public void initialize(){
        user_name =userName.getText().toString();
        user_pass =userPassword.getText().toString().trim();
        user_email =userEmailID.getText().toString();
        user_mobile =userMobileNo.getText().toString();
        user_cpass = userCPassword.getText().toString().trim();
    }
}

