package com.android.blogapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    TextView welcome,welcome_txt;
    Button sign_out,cat;
    ImageView profile_img;
    Uri img_uri;
    private  static final int PICK_IMG = 1;
    //firebase object
    private FirebaseAuth mAuth;
    //firebase firestore object
    private FirebaseFirestore fstore;
    //firebase User
    private FirebaseUser currentUser;
    //Storage for storing images
    private StorageReference mStorageRef;
    String userID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_profile,container,false);

        welcome = v.findViewById(R.id.welcome);
        welcome_txt=v.findViewById(R.id.welcome_txt);
        sign_out = v.findViewById(R.id.sign_out);
        profile_img = v.findViewById(R.id.profile_img);
//test
        cat = v.findViewById(R.id.cat);
        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),CategoryActivity.class);

                startActivity(i);

            }
        });
        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();
        //intialising the firebase firestore object
        fstore = FirebaseFirestore.getInstance();
        //intialising the StorageRefrence object
        mStorageRef = FirebaseStorage.getInstance().getReference();

        userID = mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();
        //set profile image
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,1000);
            }
        });

        //get profile image - user not sign in with google
        StorageReference profileRef = mStorageRef.child("profiles/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(Picasso.get().load(uri).toString()!=null) {
                    Picasso.get().load(uri).into(profile_img);
                }
            }
        });

        if(currentUser.getPhotoUrl() != null){
            Glide.with(container).load(currentUser.getPhotoUrl().toString()).into(profile_img);
        }
        if(currentUser.getDisplayName()!=null){
            welcome.setText(currentUser.getDisplayName());
        }
        if(currentUser.getEmail()!=null){
            welcome_txt.setText(currentUser.getEmail());
        }
        //pick image from gallery
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,1000);
            }
        });

        //Fetch Data from collection users using userID
        if(currentUser.getDisplayName()==null) {
            DocumentReference dr = fstore.collection("users").document(userID);
            dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    welcome.setText(documentSnapshot.getString("userName"));
                    welcome_txt.setText(documentSnapshot.getString("email"));
                }
            });
        }
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getInstance().signOut();
                Intent i=new Intent(getActivity(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                
            }
        });
        return v;
    }

    //set image to profile from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode == Activity.RESULT_OK){
                img_uri = data.getData();
                profile_img.setImageURI(img_uri);
                uploadImagetoFireBase(img_uri);
            }
        }
    }
    private void uploadImagetoFireBase(final Uri img_uri) {
        //upload img to firebase storage
        final StorageReference fileRef = mStorageRef.child("profiles/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(img_uri).into(profile_img);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
