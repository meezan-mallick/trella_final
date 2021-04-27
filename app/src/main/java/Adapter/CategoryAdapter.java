package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.blogapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryView> {
    List<Integer> cat_img = new ArrayList<>();
    List<String> cat_title = new ArrayList<>();

    public CategoryAdapter(List<Integer> cat_img, List<String> cat_title) {
        this.cat_img = cat_img;
        this.cat_title = cat_title;
    }

    @NonNull
    @Override
    public CategoryView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category,parent,false);
        return new CategoryView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryView holder, int position) {
        holder.category_img.setImageResource(cat_img.get(position));
        holder.category_name.setText(cat_title.get(position));
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class CategoryView extends RecyclerView.ViewHolder{
        ImageView category_img;
        TextView category_name ;

        public CategoryView(@NonNull View itemView) {
            super(itemView);
            category_img = itemView.findViewById(R.id.cat_img);
            cat_img = itemView.findViewById(R.id.cat_name);

        }
    }


}
