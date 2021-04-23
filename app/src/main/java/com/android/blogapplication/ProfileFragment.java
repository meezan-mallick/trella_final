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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    TextView welcome,welcome_txt;
    Button sign_out;
    ImageView profile_img;
    Uri img_uri;
    private  static final int PICK_IMG = 1;
    //firebase object
    private FirebaseAuth mAuth;
    //firebase firestore object
    private FirebaseFirestore fstore;
    //firebase User
    private FirebaseUser currentUser;
    String userID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_profile,container,false);

        welcome = v.findViewById(R.id.welcome);
        welcome_txt=v.findViewById(R.id.welcome_txt);
        sign_out = v.findViewById(R.id.sign_out);
        profile_img = v.findViewById(R.id.profile_img);
        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();
        //intialising the firebase firestore object
        fstore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();
        if(currentUser.getPhotoUrl() != null){
            Glide.with(container).load(currentUser.getPhotoUrl().toString()).into(profile_img);
        }
        if(currentUser.getDisplayName()!=null){
            welcome.setText(currentUser.getDisplayName());
        }
        if(currentUser.getEmail()!=null){
            welcome_txt.setText(currentUser.getEmail());
        }
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Select Profile"),PICK_IMG);
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
//                mAuth.getInstance().signOut();
                Intent i=new Intent(getActivity().getApplicationContext(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });


        return v;
    }

    //pick image from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== PICK_IMG && requestCode == RESULT_OK){
            img_uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),img_uri);
                profile_img.setImageBitmap(bitmap);
                Toast.makeText(getActivity().getApplicationContext(), "imageUploaded", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {


                Toast.makeText(getActivity().getApplicationContext(), "Error in Image Upload", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
