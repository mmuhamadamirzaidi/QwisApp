package com.mmuhamadamirzaidi.qwisapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.qwisapp.Model.User;

public class SignUpActivity extends AppCompatActivity {
    Button SignUpButton; //Use for sign up & sign in options
    EditText SignUpEmail, SignUpUsername, SignUpPassword; //Use for getting user inputs to sign up

    Button ForgetPasswordButton, SignInAccountButton; //Use for redirect users to desire page

    FirebaseDatabase database; //Firebase authentication
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Sign Up
        SignUpEmail = (EditText) findViewById(R.id.signup_email);
        SignUpUsername = (EditText) findViewById(R.id.signup_username);
        SignUpPassword = (EditText) findViewById(R.id.signup_password);
        SignUpButton = (Button) findViewById(R.id.signup_button);

        //Others
        ForgetPasswordButton = (Button) findViewById(R.id.forgot_password_button);
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
    }

    private void SignUpAccount() {

        final User user = new User(SignUpEmail.getText().toString().trim(), SignUpUsername.getText().toString().trim(), SignUpPassword.getText().toString().trim());

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user.getUsername()).exists()) {
                    Toast.makeText(SignUpActivity.this, "Username not available!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                    SendUserToSignInActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToSignInActivity() {
        Intent signinIntent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(signinIntent);
    }
}
