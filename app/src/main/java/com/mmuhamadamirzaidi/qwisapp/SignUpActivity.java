package com.mmuhamadamirzaidi.qwisapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.qwisapp.Model.User;

public class SignUpActivity extends AppCompatActivity {
    Button SignUpButton; //Use for sign up & sign in options
    EditText SignUpUsername, SignUpEmail, SignUpPassword; //Use for getting user inputs to sign up

    Button SignInAccountButton; //Use for redirect users to desire page

    String Role = "Student";

    private ProgressDialog loadingBar;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loadingBar = new ProgressDialog(this);

        //Sign Up
        SignUpUsername = (EditText)findViewById(R.id.signup_username);
        SignUpEmail = (EditText) findViewById(R.id.signup_email);
        SignUpPassword = (EditText) findViewById(R.id.signup_password);
        SignUpButton = (Button) findViewById(R.id.signup_button);

        //Others
        SignInAccountButton = (Button) findViewById(R.id.signin_account_button);

        //Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpAccount();
            }
        });

        SignInAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToSignInActivity();
            }
        });

//        ForgetPasswordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SignUpActivity.this, ForgetPasswordActivity.class));
//            }
//        });
    }

    private void SignUpAccount() {
        final User user = new User(SignUpUsername.getText().toString().trim(),
        SignUpEmail.getText().toString().trim(),
        SignUpPassword.getText().toString().trim(), Role);

        Boolean valid = true;

        if (TextUtils.isEmpty(SignUpUsername.getText().toString().trim()))
        {
            Toast.makeText(this, "Please fill in your username!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (TextUtils.isEmpty(SignUpEmail.getText().toString().trim()))
        {
            Toast.makeText(this, "Please fill in your email address!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//        if (!email.matches(emailPattern ))
//        {
//            Toast.makeText(this, "Please use a valid email address!", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }
        if (TextUtils.isEmpty(SignUpPassword.getText().toString().trim()))
        {
            Toast.makeText(this, "Please fill in your password!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
//        String upperCaseChars = "(.*[A-Z].*)";
//        if (!password.matches(upperCaseChars ))
//        {
//            Toast.makeText(this, "Password should contain at least one upper case alphabet!", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }
//        String lowerCaseChars = "(.*[a-z].*)";
//        if (!password.matches(lowerCaseChars ))
//        {
//            Toast.makeText(this, "Password should contain at least one lower case alphabet!", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }
//        if (password.length() > 32 || password.length() < 8)
//        {
//            Toast.makeText(this, "Password should be more than 8 and less than 32 characters in length!", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }
//        String numbers = "(.*[0-9].*)";
//        if (!password.matches(numbers ))
//        {
//            Toast.makeText(this, "Password should contain at least one number!", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }
//        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
//        if (!password.matches(specialChars ))
//        {
//            Toast.makeText(this, "Password should contain at least one special character!", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }
        if(valid)
        {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait while your account is processing...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(user.getUsername()).exists()){
                        Toast.makeText(SignUpActivity.this, "User already exist!", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else{
                        users.child(user.getUsername()).setValue(user);
                        Toast.makeText(SignUpActivity.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
                        SendUserToSignInActivity();
                        loadingBar.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SignUpActivity.this, Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToSignInActivity() {
        Intent signinIntent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(signinIntent);
    }
}