package com.mmuhamadamirzaidi.qwisapp.Lecturer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmuhamadamirzaidi.qwisapp.R;

import java.util.HashMap;

public class LecturerUpdatePostActivity extends AppCompatActivity {

    private EditText titlepost, descpost;

    Button btnSaveUpdate, btnDelete;

    private ProgressDialog loadingBar;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_update_post);

        loadingBar = new ProgressDialog(this);

        titlepost = (EditText) findViewById(R.id.titlepost);
        descpost = (EditText) findViewById(R.id.descpost);

//        alertMemoTitle = (TextView) findViewById(R.id.alertMemoTitle);

        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // get a value from previous page
        titlepost.setText(getIntent().getStringExtra("titlepost"));
        descpost.setText(getIntent().getStringExtra("descpost"));

        String keykeyPost = getIntent().getStringExtra("keypost");

        reference = FirebaseDatabase.getInstance().getReference().child("Post").child("Post" + keykeyPost);

        btnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePostInfo();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingBar.setTitle("Delete Post");
                loadingBar.setMessage("Please wait while post is deleting...");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();

                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            SendToLecturerHomeFragment();
                            Toast.makeText(LecturerUpdatePostActivity.this, "Post deleted successfully!", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                        }
                        else
                        {
                            String message = task.getException().getMessage();
                            Toast.makeText(LecturerUpdatePostActivity.this, "Error occurred: "+message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        });
    }

    private void UpdatePostInfo()
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

            loadingBar.setTitle("Update Post");
            loadingBar.setMessage("Please wait while post is updating...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            ValidatePostInfo(titlePost, descPost);
        }
    }

    private void ValidatePostInfo(String titlePost, String descPost) {

        HashMap userMap = new HashMap();
        userMap.put("titlepost", titlePost);
        userMap.put("descpost", descPost);

        reference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener()
        {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if (task.isSuccessful())
                {
                    SendToLecturerHomeFragment();
                    Toast.makeText(LecturerUpdatePostActivity.this, "Post updated successfully!", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(LecturerUpdatePostActivity.this, "Error occurred: "+message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void SendToLecturerHomeFragment() {
        Intent mainIntent = new Intent(LecturerUpdatePostActivity.this, LecturerHomeActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
