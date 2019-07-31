package com.mmuhamadamirzaidi.qwisapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmuhamadamirzaidi.qwisapp.Common.Common;
import com.mmuhamadamirzaidi.qwisapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.qwisapp.Model.Category;
import com.mmuhamadamirzaidi.qwisapp.ViewHolder.CategoryViewHolder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    RecyclerView listCategory;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference categories;

    View myFragment;

    public static CategoryFragment newInstance() {
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_category, container, false);

        listCategory = (RecyclerView) myFragment.findViewById(R.id.listCategory);
//        listCategory.setHasFixedSize(true); //Need to remove if using Firebase Recycler Adapter
        layoutManager = new LinearLayoutManager(container.getContext());
        listCategory.setLayoutManager(layoutManager);

        loadCategories();

        return myFragment;
    }

    private void loadCategories() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.item_category, CategoryViewHolder.class, categories) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int position) {

                viewHolder.Name.setText(model.getName());
                viewHolder.Description.setText(model.getDescription());

                Picasso.get().load(model.getImage()).into(viewHolder.Image);
                Picasso.get().load(model.getIconImage()).into(viewHolder.IconImage);

                final String getName = model.getName();
                final String getDescription = model.getDescription();
//                final String getImage = model.getImage();
//                final String getIconImage = model.getIconImage();

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(getActivity(), String.format("%s | %s", adapter.getRef(position).getKey(), model.getName()), Toast.LENGTH_SHORT).show();

//

                        Intent startGame = new Intent(getActivity(), StartGameActivity.class);
                        Common.categoryId = adapter.getRef(position).getKey();

                        startGame.putExtra("Name", getName);
                        startGame.putExtra("Description", getDescription);

//                        startGame.putExtra("Image", getImage);
//                        startGame.putExtra("IconImage", getIconImage);

                        startActivity(startGame);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        listCategory.setAdapter(adapter);
    }
}
