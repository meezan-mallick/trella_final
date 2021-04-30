package com.android.blogapplication;

import android.content.Context;
import android.graphics.Color;
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
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    ArrayList<String> category_name;
    ArrayList<String> category_image;
    ArrayList<String> selected_category;
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
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position) {
        holder.cat_name.setText(category_name.get(position));
        Picasso.get().load(category_image.get(position)).into(holder.cat_img);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private int mCounter = 1;
            @Override
            public void onClick(View view) {
                mCounter++;
                if(mCounter%2==0){

                    holder.itemView.setBackgroundColor(Color.LTGRAY);
                    Toast.makeText(context, "Item "+holder.cat_name.getText()+" selected", Toast.LENGTH_SHORT).show();
                }
                else{
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    Toast.makeText(context, "Item "+holder.cat_name.getText()+" deselected", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
