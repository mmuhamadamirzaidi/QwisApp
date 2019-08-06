package com.mmuhamadamirzaidi.qwisapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.qwisapp.Adapters.PostAdapter;
import com.mmuhamadamirzaidi.qwisapp.Model.Post;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LecturerPostFragment extends Fragment {

    RecyclerView listPost;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Post> list;
    PostAdapter postAdapter;

    FloatingActionButton PostFabCreate;

    View myFragment;

    public static LecturerPostFragment newInstance() {
        LecturerPostFragment lecturerPostFragment = new LecturerPostFragment();
        return lecturerPostFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Post");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_lecturer_post, container, false);

        //Floating Action Button
        PostFabCreate = (FloatingActionButton) myFragment.findViewById(R.id.post_fab_create);

        PostFabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(getActivity(), LecturerCreatePostActivity.class);
                startActivity(createIntent);
            }
        });


        // working with data
        listPost = (RecyclerView) myFragment.findViewById(R.id.listPost);
        listPost.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<Post>();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // set code to retrieve data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Post p = dataSnapshot1.getValue(Post.class);
                    list.add(p);
                }
                postAdapter = new PostAdapter(getActivity(), list);
                listPost.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        return myFragment;
    }

}
