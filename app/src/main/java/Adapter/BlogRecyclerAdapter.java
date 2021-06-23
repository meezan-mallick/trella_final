package Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.android.blogapplication.SingleBlogDatactivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;


public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogModel> blog_list;
    public String uname;
    Context ctx;
    public BlogRecyclerAdapter(Context ctx,List<BlogModel> blog_list) {
        this.blog_list = blog_list;
        this.ctx = ctx;
        Picasso.get().setIndicatorsEnabled(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_blog_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        final BlogModel temp_data = blog_list.get(position);

        String blog_img_uri = blog_list.get(position).getBlog_image();
        String uid = blog_list.get(position).getBlog_image();
        //intialising the firebase firestore object
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String profile_img_uri = "profiles/"+blog_list.get(position).getUser_id()+"/profile.jpg";
        String user_id = blog_list.get(position).getUser_id();
        holder.setTime(blog_list.get(position).getTime());
        holder.setDescription(blog_list.get(position).getBlog_content());
        holder.setTitle(blog_list.get(position).getBlog_title());


        //Storage for storing images
        StorageReference mStorageRef,profileRef;
        //intialising the StorageRefrence object
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference blogref =mStorageRef.child(blog_img_uri);

        blogref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                if(Picasso.get().load(uri).toString()!=null) {
                    //Picasso.get().load(uri).into(holder.blog_image);
                    Picasso.get().load(uri).fetch(new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.get().load(uri).into(holder.blog_image);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }
        });

        profileRef = mStorageRef.child("profiles/"+user_id+"/profile.jpg");
        DocumentReference dr = fstore.collection("users").document(user_id);
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                String uname = documentSnapshot.getString("userName");
                holder.setUserName(uname);
            }
        });
//        profileRef = mStorageRef.child(profile_img_uri);

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                if(Picasso.get().load(uri).toString()!=null) {
                    //Picasso.get().load(uri).into(holder.profile_img);

                    //caching images
                    Picasso.get().load(uri).fetch(new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.get().load(uri).into(holder.profile_img);
                        }
                        @Override
                        public void onError(Exception e) {
                        }
                    });
                }
            }
        });



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, SingleBlogDatactivity.class);
                i.putExtra("blogimage",temp_data.getBlog_image());
                i.putExtra("username",uname);
                i.putExtra("content",temp_data.getBlog_content());
                i.putExtra("title",temp_data.getBlog_title());
                i.putExtra("time",temp_data.getTime());
                i.putExtra("uid",temp_data.getUser_id());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(i);
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
            profile_img = mView.findViewById(R.id.pic_profile);
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
        public void setUserName(String name){
            username = mView.findViewById(R.id.uname);
            username.setText(name);
        }

    }
}