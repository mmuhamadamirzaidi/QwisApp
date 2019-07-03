package com.mmuhamadamirzaidi.qwisapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressWarnings("unchecked")
public class SignInActivity extends AppCompatActivity {

    Button SignInButton; //Use for sign up & sign in options
    EditText SignInEmail, SignInPassword; //Use for getting user inputs to sign in

    Button ForgetPasswordButton, SignUpAccountButton; //Use for redirect users to desire page

    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private Boolean emailAddressChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        loadingBar = new ProgressDialog(this);

        //Sign In
        SignInEmail = (EditText) findViewById(R.id.signin_email);
        SignInPassword = (EditText) findViewById(R.id.signin_password);
        SignInButton = (Button) findViewById(R.id.signin_button);

        //Others
        ForgetPasswordButton = (Button) findViewById(R.id.forgot_password_button);
        SignUpAccountButton = (Button) findViewById(R.id.signup_account_button);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInAccount();
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

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            SendUserToIntroActivity();
        }
    }

    private void SignInAccount() {
        String email = SignInEmail.getText().toString().trim();
        String password = SignInPassword.getText().toString().trim();

        Boolean valid = true;

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please fill in your email address!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern ))
        {
            Toast.makeText(this, "Please use a valid email address!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (valid)
        {
            loadingBar.setTitle("Sign In Account");
            loadingBar.setMessage("Please wait while you are redirecting to your account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                VerifyEmailAddress();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(SignInActivity.this, "Error occurred: "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void VerifyEmailAddress()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        emailAddressChecker = user.isEmailVerified();

        if (emailAddressChecker)
        {
            SendUserToIntroActivity();
        }
        else{
            Toast.makeText(this, "Please verify your email address first!", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
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
