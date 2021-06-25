package Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.blogapplication.BlogModel;
import com.android.blogapplication.R;
import com.android.blogapplication.UserModel;
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

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

    public List<UserModel> user_list;
    Context ctx;

    public UserRecyclerAdapter(List<UserModel> user_list, Context ctx) {
        this.user_list = user_list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users,parent,false);
        return new UserRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        //Storage for storing images
        StorageReference mStorageRef,profileRef;

        //intialising the StorageRefrence object
        mStorageRef = FirebaseStorage.getInstance().getReference();

        holder.setUserName(user_list.get(position).getUserName());

        holder.setUserEmail(user_list.get(position).getEmail());

        profileRef = mStorageRef.child("profiles/"+user_list.get(position).getUser_id()+"/profile.jpg");
//        Toast.makeText(ctx, "profiles/"+user_list.get(position).getUser_id()+"/profile.jpg", Toast.LENGTH_SHORT).show();
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                if(Picasso.get().load(uri).toString()!=null) {
                    //Picasso.get().load(uri).into(holder.profile_img);

                    try {
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
                    }catch (Exception e){
                        Toast.makeText(ctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return user_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView userName,userEmail;
        private ImageView profile_img;
        private View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            profile_img = mView.findViewById(R.id.raw_user_pic);
        }

        public void setUserName(String name){
            userName = mView.findViewById(R.id.row_userName);
            userName.setText(name);
        }
        public void setUserEmail(String email){
            userEmail = mView.findViewById(R.id.row_userEmail);
            userEmail.setText(email);
        }


    }


}

