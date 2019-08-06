package com.mmuhamadamirzaidi.qwisapp.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmuhamadamirzaidi.qwisapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.qwisapp.Model.User;
import com.mmuhamadamirzaidi.qwisapp.R;
import com.mmuhamadamirzaidi.qwisapp.ViewHolder.LecturerViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminLecturerFragment extends Fragment {


    View myFragment;

    RecyclerView listLecturers;

    FloatingActionButton LecturerFabCreate;

    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<User, LecturerViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference usersTable;

    public static AdminLecturerFragment newInstance() {
        AdminLecturerFragment lecturerFragment = new AdminLecturerFragment();
        return lecturerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        usersTable = database.getReference("Users");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_admin_lecturer, container, false);


        //Floating Action Button
        LecturerFabCreate = (FloatingActionButton)  myFragment.findViewById(R.id.lecturer_fab_create);

        LecturerFabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(getActivity(), AdminCreateLecturerActivity.class);
                startActivity(createIntent);
            }
        });

        //Init view
        listLecturers = (RecyclerView) myFragment.findViewById(R.id.listLecturers);
        layoutManager = new LinearLayoutManager(getActivity());
//        listRanking.setHasFixedSize(true); //Need to remove if using Firebase Recycler Adapter

        //OrderByChild method sort with ascending, so, need to reverse Recycler data
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        listLecturers.setLayoutManager(layoutManager);

        //Set adapter
        adapter = new FirebaseRecyclerAdapter<User, LecturerViewHolder>(User.class, R.layout.item_lecturers, LecturerViewHolder.class, usersTable.orderByChild("role").equalTo("Lecturer")) {
            @Override
            protected void populateViewHolder(LecturerViewHolder viewHolder, final User model, int position) {

                viewHolder.IconImage.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));
                viewHolder.Container.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_scale_animation));

                viewHolder.txt_name.setText(model.getUsername());
                viewHolder.txt_email.setText(String.valueOf(model.getEmail()));

                //Fix crash when click to item
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(), String.format("%s",  model.getUsername()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        listLecturers.setAdapter(adapter);

        return myFragment;
    }

}
