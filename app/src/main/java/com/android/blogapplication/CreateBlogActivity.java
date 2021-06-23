package com.android.blogapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
 
public class CreateBlogActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private  Spinner spinner;
    private DatabaseReference dbref;
    private FirebaseFirestore fstore;
    Button publish,draft;
    EditText blog_title,blog_content;
    ImageView upload_img;
    ValueEventListener listener;
    ArrayList<String> cat_list;
    ArrayAdapter<String> adapter;
    String userID;
    Boolean publish_draft;
    Uri img_uri;
    DateFormat df,df1;
    boolean select_cat;
    //firebase object
    private FirebaseAuth mAuth;
    //Storage for storing images
    private StorageReference mStorageRef;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);
        getWidgets();
        //Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Blog Post");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

//        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
//        String currentTime = df.format(Calendar.getInstance().getTime());

        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();
        //intialising the StorageRefrence object
        mStorageRef = FirebaseStorage.getInstance().getReference();

        dbref = FirebaseDatabase.getInstance().getReference("categories");
        fstore = FirebaseFirestore.getInstance();
        DocumentReference dr = fstore.collection("categories").document("cat1");

        //set blog image
        upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,1000);
            }
        });


        //spinner data [CATEGORY DROPDOWN]
        cat_list = new ArrayList<>();
        cat_list.add("Select Category");

        //fetching the categories from the firestore collection and storing into cat_list_array ;
        fstore.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                cat_list.add(document.getString("name"));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(CreateBlogActivity.this, "data not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cat_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    select_cat=false;
                }
                else {
                    select_cat=true;
//                    String cat_name = adapterView.getItemAtPosition(i).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        df = new SimpleDateFormat("dMMMyyyy_HH:mm");
        df1 = new SimpleDateFormat("d MMM, yyyy-HH:mm");
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(select_cat == false){
                    Toast.makeText(CreateBlogActivity.this, "Please Select Category", Toast.LENGTH_SHORT).show();
                }
                else{
                    publish_draft = true;
                    Toast.makeText(CreateBlogActivity.this, "btn publish", Toast.LENGTH_SHORT).show();
                    String pub_time = df.format(Calendar.getInstance().getTime());
                    Date t = Calendar.getInstance().getTime();
                    Toast.makeText(CreateBlogActivity.this, pub_time, Toast.LENGTH_SHORT).show();

                    //upload image to firestore
                    try{
                    uploadImagetoFireBase(img_uri,pub_time);
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(), "image upload error : "+e, Toast.LENGTH_SHORT).show();
                    }

                    //add blog to firestore
                    try {
                        addBlog(publish_draft, pub_time, t);
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(), "blog add error : "+e, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        draft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publish_draft = false;
                Date t = Calendar.getInstance().getTime();
                String draft_time = df.format(Calendar.getInstance().getTime());

                //upload image to firestore
                try{
                    uploadImagetoFireBase(img_uri,draft_time);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "image upload error : "+e, Toast.LENGTH_SHORT).show();
                }

                //add blog to firestore
                try{
                    addBlog(publish_draft,draft_time,t);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "blog add error : "+e, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void uploadImagetoFireBase(final Uri img_uri,String post_time) {
        //upload img to firebase storage
        final StorageReference fileRef = mStorageRef.child("blogsImages/"+mAuth.getCurrentUser().getUid()+"/"+post_time+"-blogImage.jpg");
        fileRef.putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(CreateBlogActivity.this, "image uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateBlogActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addBlog(Boolean publish_bol,String post_time,Date d){
        userID = mAuth.getCurrentUser().getUid();
        String selected_cat = spinner.getSelectedItem().toString();
        String img_path = "blogsImages/"+mAuth.getCurrentUser().getUid()+"/"+post_time+"-blogImage.jpg";

        // Add a new blog(document)
        Map<String, Object> blogData = new HashMap<>();
        blogData.put("blog_title",blog_title.getText().toString());
        blogData.put("blog_content",blog_content.getText().toString());
        blogData.put("blog_image",img_path);
        blogData.put("category",selected_cat);
        blogData.put("publish",publish_bol);
        blogData.put("user_id",userID);
        blogData.put("time",post_time);
        blogData.put("date",d);

        fstore.collection("blogs")
                .add(blogData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(CreateBlogActivity.this, "New Blog added Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed(); 
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(CreateBlogActivity.this, "Error : "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //open gallery on click
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode == Activity.RESULT_OK){
                img_uri = data.getData();
                upload_img.setImageURI(img_uri);
            }
        }
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void getWidgets() {
        spinner =  findViewById(R.id.spinner);
        publish = findViewById(R.id.publish);
        draft = findViewById(R.id.draft);
        blog_title = findViewById(R.id.blog_title);
        blog_content = findViewById(R.id.content_blog);
        upload_img = findViewById(R.id.upload_image);
    }
}