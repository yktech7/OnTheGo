package com.onthegodevelopers.onthego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;

public class resetPassword extends AppCompatActivity {
    Button oResetpassword;
    FirebaseApp auth;

    EditText userNameEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        userNameEt =(EditText) findViewById(R.id.resetPasswordInput);
        oResetpassword = (Button) findViewById(R.id.resetPasswordButton);
        oResetpassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openOTPActivity();
            }
        });
        auth=FirebaseApp.getInstance();
    }
    public void openOTPActivity(){
        String user_name= userNameEt.getText().toString();
        Log.i("INFO", user_name);
        Intent oIntent = new Intent(this, submit_otp.class);
        //Generate and send OTP here
        startActivity(oIntent);

    }
}
