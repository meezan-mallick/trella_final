package com.android.blogapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    ArrayList<String> category_name;
    ArrayList<String> category_image;
    Context context;
    LayoutInflater inflater;

    public CategoryAdapter(Context context,ArrayList<String> category_name, ArrayList<String> category_image) {
        this.category_name = category_name;
        this.category_image = category_image;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View v = inflater.inflate(R.layout.row_category,parent,false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.cat_name.setText(category_name.get(position));
        Picasso.get().load(category_image.get(position)).into(holder.cat_img);
    }

    @Override
    public int getItemCount() {
        return category_name.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView cat_name;
        ImageView cat_img;
        public CategoryViewHolder(@NonNull final View itemView) {
            super(itemView);
            cat_img = itemView.findViewById(R.id.cat_img);
            cat_name = itemView.findViewById(R.id.cat_name);

        }


    }
}
