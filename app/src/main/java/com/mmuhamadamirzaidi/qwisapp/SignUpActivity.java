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

public class SignUpActivity extends AppCompatActivity {
    Button SignUpButton; //Use for sign up & sign in options
    EditText SignUpEmail, SignUpPassword, SignUpConfirmPassword; //Use for getting user inputs to sign up

    Button ForgetPasswordButton, SignInAccountButton; //Use for redirect users to desire page

    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loadingBar = new ProgressDialog(this);

        //Sign Up
        SignUpEmail = (EditText) findViewById(R.id.signup_email);
        SignUpPassword = (EditText) findViewById(R.id.signup_password);
        SignUpConfirmPassword = (EditText) findViewById(R.id.signup_confirm_password);
        SignUpButton = (Button) findViewById(R.id.signup_button);

        //Others
        ForgetPasswordButton = (Button) findViewById(R.id.forgot_password_button);
        SignInAccountButton = (Button) findViewById(R.id.signin_account_button);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

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

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            SendUserToMainActivity();
        }
    }

    private void SignUpAccount() {
        String email = SignUpEmail.getText().toString().trim();
        String password = SignUpPassword.getText().toString().trim();
        String confirmPassword = SignUpConfirmPassword.getText().toString().trim();

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
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please fill in your password!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars ))
        {
            Toast.makeText(this, "Password should contain at least one upper case alphabet!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars ))
        {
            Toast.makeText(this, "Password should contain at least one lower case alphabet!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (password.length() > 32 || password.length() < 8)
        {
            Toast.makeText(this, "Password should be more than 8 and less than 32 characters in length!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers ))
        {
            Toast.makeText(this, "Password should contain at least one number!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (!password.matches(specialChars ))
        {
            Toast.makeText(this, "Password should contain at least one special character!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(this, "Please fill in your confirm password!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (!confirmPassword.equals(password))
        {
            Toast.makeText(this, "Password do not match!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(valid)
        {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait while your account is processing...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                SendEmailVerification();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(SignUpActivity.this, "Error occurred: "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void SendEmailVerification(){
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null)
        {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(SignUpActivity.this, "Sign up successful. Please verify your email that has been sent!", Toast.LENGTH_SHORT).show();
                        SendUserToSignInActivity();
                        mAuth.signOut();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(SignUpActivity.this, "Error occurred: "+message, Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                }
            });
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToSignInActivity() {
        Intent signinIntent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(signinIntent);
    }
}