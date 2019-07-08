package com.mmuhamadamirzaidi.qwisapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.qwisapp.Common.Common;
import com.mmuhamadamirzaidi.qwisapp.Model.User;

public class SignInActivity extends AppCompatActivity {

    Button SignInButton; //Use for sign up & sign in options
    EditText SignInUsername, SignInPassword; //Use for getting user inputs to sign in

    Button SignUpAccountButton; //Use for redirect users to desire page

    private ProgressDialog loadingBar;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        loadingBar = new ProgressDialog(this);

        //Sign In
        SignInUsername = (EditText) findViewById(R.id.signin_username);
        SignInPassword = (EditText) findViewById(R.id.signin_password);
        SignInButton = (Button) findViewById(R.id.signin_button);

        //Others
        SignUpAccountButton = (Button) findViewById(R.id.signup_account_button);

        //Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInAccount(SignInUsername.getText().toString().trim(), SignInPassword.getText().toString().trim());
            }
        });

        SignUpAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToSignUpActivity();
            }
        });

//        ForgetPasswordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SignInActivity.this, ForgetPasswordActivity.class));
//            }
//        });
    }

    private void SignInAccount(final String user, final String password) {

        loadingBar.setTitle("Sign In Account");
        loadingBar.setMessage("Please wait while you are redirecting to your account...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists()) {
                    if (!user.isEmpty()) {
                        User signin = dataSnapshot.child(user).getValue(User.class);
                        if (signin.getPassword().equals(password)) {
                            Toast.makeText(SignInActivity.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                            Common.currentUser = signin;
                            SendUserToIntroActivity();
                            finish();
                            loadingBar.dismiss();
                        } else {
                            Toast.makeText(SignInActivity.this, "Password wrong!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, "Please fill in your username!", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToIntroActivity() {
        Intent introIntent = new Intent(SignInActivity.this, IntroductionsActivity.class);
        introIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(introIntent);
        finish();
    }

    private void SendUserToSignUpActivity() {
        Intent signupIntent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(signupIntent);
    }

}