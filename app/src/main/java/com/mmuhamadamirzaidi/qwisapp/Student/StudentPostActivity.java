package com.mmuhamadamirzaidi.qwisapp.Student;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.qwisapp.Adapters.PostAdapter;
import com.mmuhamadamirzaidi.qwisapp.Adapters.StudentPostAdapter;
import com.mmuhamadamirzaidi.qwisapp.Model.Post;
import com.mmuhamadamirzaidi.qwisapp.R;

import java.util.ArrayList;

public class StudentPostActivity extends AppCompatActivity {

    RecyclerView listPost;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Post> list;
    StudentPostAdapter studentPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_post);

        // working with data
        listPost = findViewById(R.id.listPost);
        listPost.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Post>();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Post");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // set code to retrieve data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Post p = dataSnapshot1.getValue(Post.class);
                    list.add(p);
                }
                studentPostAdapter = new StudentPostAdapter (StudentPostActivity.this, list);
                listPost.setAdapter(studentPostAdapter);
                studentPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
