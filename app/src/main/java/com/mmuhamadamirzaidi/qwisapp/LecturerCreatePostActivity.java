package com.mmuhamadamirzaidi.qwisapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class LecturerCreatePostActivity extends AppCompatActivity {

    private EditText titlepost, descpost;
    private ProgressDialog loadingBar;

    Button btnSaveTask;
    DatabaseReference reference;

    Integer postNum = new Random().nextInt();
    String keypost = Integer.toString(postNum);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_create_post);

        loadingBar = new ProgressDialog(this);

        titlepost = (EditText) findViewById(R.id.titlepost);
        descpost = (EditText) findViewById(R.id.descpost);

        btnSaveTask = findViewById(R.id.btnSaveTask);

        reference = FirebaseDatabase.getInstance().getReference().child("Post").child("Post" + postNum);

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMemoInformation();
            }
        });
    }

    private void SaveMemoInformation()
    {

        String titlePost = titlepost.getText().toString().trim();
        String descPost = descpost.getText().toString().trim();

        if (TextUtils.isEmpty(titlePost))
        {
            Toast.makeText(this, "Please fill in post title!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(descPost))
        {
            Toast.makeText(this, "Please fill in post description!", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Saving Post");
            loadingBar.setMessage("Please wait while post is creating...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            HashMap userMap = new HashMap();
            userMap.put("titlepost", titlePost);
            userMap.put("descpost", descPost);
            userMap.put("keypost", keypost);

            reference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        SendToLecturerHomeFragment();
                        Toast.makeText(LecturerCreatePostActivity.this, "Post created successfully!", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(LecturerCreatePostActivity.this, "Error occurred: "+message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void SendToLecturerHomeFragment() {
        Intent mainIntent = new Intent(LecturerCreatePostActivity.this, LecturerHomeActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
