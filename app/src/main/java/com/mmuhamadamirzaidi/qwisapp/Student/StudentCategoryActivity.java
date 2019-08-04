package com.mmuhamadamirzaidi.qwisapp.Student;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmuhamadamirzaidi.qwisapp.Common.Common;
import com.mmuhamadamirzaidi.qwisapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.qwisapp.Model.Category;
import com.mmuhamadamirzaidi.qwisapp.R;
import com.mmuhamadamirzaidi.qwisapp.StartGameActivity;
import com.mmuhamadamirzaidi.qwisapp.ViewHolder.CategoryViewHolder;
import com.squareup.picasso.Picasso;

public class StudentCategoryActivity extends AppCompatActivity {

    RecyclerView listCategory;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_category);

        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category");

        listCategory = (RecyclerView) findViewById(R.id.listCategory);
//        listCategory.setHasFixedSize(true); //Need to remove if using Firebase Recycler Adapter
        layoutManager = new LinearLayoutManager(this);
        listCategory.setLayoutManager(layoutManager);

        loadCategories();
    }

    private void loadCategories() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.item_student_category, CategoryViewHolder.class, categories) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int position) {

                viewHolder.Name.setText(model.getName());
                viewHolder.Description.setText(model.getDescription());

                final String getName = model.getName();
                final String getDescription = model.getDescription();

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(getActivity(), String.format("%s | %s", adapter.getRef(position).getKey(), model.getName()), Toast.LENGTH_SHORT).show();

                        Intent startGame = new Intent(StudentCategoryActivity.this, StartGameActivity.class);
                        Common.categoryId = adapter.getRef(position).getKey();
                        Common.categoryName = model.getName();

                        startGame.putExtra("Name", getName);
                        startGame.putExtra("Description", getDescription);

                        startActivity(startGame);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        listCategory.setAdapter(adapter);
    }
}
