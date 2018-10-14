package com.onthegodevelopers.onthego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class resetPassword extends AppCompatActivity {
    Button oResetpassword;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String  verification_code;
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
        auth = FirebaseAuth.getInstance();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken){
                super.onCodeSent(s,forceResendingToken);
                verification_code=s;
                Toast.makeText(getApplicationContext(),"Code sent to the number",Toast.LENGTH_SHORT).show();
            }
        };
    }
    public void openOTPActivity(){
        String user_name= userNameEt.getText().toString();
        Log.i("INFO", user_name);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(user_name,60, TimeUnit.SECONDS,this,mCallback );
        Intent oIntent = new Intent(this, submit_otp.class);
        //Generate and send OTP here
        startActivity(oIntent);

    }
}
