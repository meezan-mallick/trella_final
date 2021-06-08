package Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.blogapplication.BlogModel;
import com.android.blogapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.List;


public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogModel> blog_list;
    Context ctx;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String blog_img_uri = blog_list.get(position).getBlog_image();
        String uid = blog_list.get(position).getBlog_image();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String profile_img_uri = "profiles/"+blog_list.get(position).getUser_id()+"/profile.jpg";

        holder.setTime(blog_list.get(position).getTime());
        holder.setDescription(blog_list.get(position).getBlog_content());
        holder.setTitle(blog_list.get(position).getBlog_title());
        holder.setUserName(currentUser.getDisplayName());

        //Storage for storing images
        StorageReference mStorageRef,profileRef;
        //intialising the StorageRefrence object
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference blogref =mStorageRef.child(blog_img_uri);
        blogref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(Picasso.get().load(uri).toString()!=null) {
                    Picasso.get().load(uri).into(holder.blog_image);
                }
            }
        });
        profileRef = mStorageRef.child(profile_img_uri);
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(Picasso.get().load(uri).toString()!=null) {
                    Picasso.get().load(uri).into(holder.profile_img);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView blog_description,blog_time,blog_title,username;
        private ImageView blog_image,profile_img;
        private View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            blog_image = mView.findViewById(R.id.blog_image);
            profile_img = mView.findViewById(R.id.image_profile);
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
            blog_title = mView.findViewById(R.id.blog_title);
            blog_title.setText(title);
        }
        public void setUserName(String uname){
            username = mView.findViewById(R.id.uname);
            username.setText(uname);
        }

    }
}
