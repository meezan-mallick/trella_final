package com.android.blogapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class SingleBlogDatactivity extends AppCompatActivity {
    ImageView blog_img,profile_img;
    TextView username,date_blogpost,title_blog,content_blog;
    StorageReference mStorageRef,profileRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_blog_datactivity);
        getWidgets();
        //Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Back");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String imagename = i.getStringExtra("blogimage");
        String uname = i.getStringExtra("username");
        String content = i.getStringExtra("content");
        String title = i.getStringExtra("title");
        String time = i.getStringExtra("time");
        String user_id = i.getStringExtra("uid");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference blogref = mStorageRef.child(imagename);
        blogref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(Picasso.get().load(uri).toString()!=null) {
                    Picasso.get().load(uri).into(blog_img);
                }
            }
        });
        profileRef = mStorageRef.child("profiles/"+user_id+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(Picasso.get().load(uri).toString()!=null) {
                    Picasso.get().load(uri).into(profile_img);
                }
            }
        });
        username.setText(uname);
        content_blog.setText(content);
        title_blog.setText(title);
        date_blogpost.setText(time);

    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void getWidgets() {
        username = findViewById(R.id.username_s);
        date_blogpost = findViewById(R.id.date_blogpost_s);
        title_blog = findViewById(R.id.title_blog_s);
        content_blog = findViewById(R.id.content_blog_s);
        blog_img = findViewById(R.id.blog_img_s);
        profile_img = findViewById(R.id.profile_img_s);

    }
}