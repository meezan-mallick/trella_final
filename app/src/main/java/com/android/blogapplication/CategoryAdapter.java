package com.android.blogapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends FirestoreRecyclerAdapter<SingleCategory, CategoryAdapter.CategoryHolder> {

    View view;

    @Override
    protected void onBindViewHolder(CategoryHolder holder, int i, SingleCategory singleCategory) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewPriority.setText(String.valueOf(model.getPriority()));
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category,
                parent, false);
        return new CategoryHolder(v);
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewPriority;
        public CategoryHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }
    public CategoryAdapter(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }
    public void setDetails(Context context,String category_name, String category_image){
        TextView mcategory_name =  view.findViewById(R.id.cat_name);
        ImageView mcategory_image = view.findViewById(R.id.cat_img);

        mcategory_name.setText(category_name);
        Picasso.get().load(category_image).into(mcategory_image);
    }
}
