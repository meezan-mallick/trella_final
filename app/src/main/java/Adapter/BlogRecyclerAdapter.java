package Adapter;

import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.blogapplication.BlogModel;
import com.android.blogapplication.BlogPost;
import com.android.blogapplication.R;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogModel> blog_list;

    public BlogRecyclerAdapter(List<BlogModel> blog_list) {
        this.blog_list = blog_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_blog_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String desc = blog_list.get(position).getBlog_content();
        holder.setTime(blog_list.get(position).getTime());
        holder.setDescription(blog_list.get(position).getBlog_content());
        holder.setTitle(blog_list.get(position).getBlog_title());
//        try {
//            TimeUnit.SECONDS.sleep(1);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//
//        }

    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView blog_description,blog_time,blog_title;
        private View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setDescription(String text){
             blog_description = mView.findViewById(R.id.blog_description);
             blog_description.setText(text);
        }
        public void setTime(String time){
            blog_time = mView.findViewById(R.id.blogpost_date);
            blog_time.setText(time);
        }
        public void setTitle(String title){
            blog_title = mView.findViewById(R.id.uname);
            blog_title.setText(title);
        }
    }
}
