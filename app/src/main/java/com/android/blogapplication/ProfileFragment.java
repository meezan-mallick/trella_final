package com.android.blogapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    TextView userName,userEmail,no_of_followers,no_of_posts,no_of_drafts;
    LinearLayout signOutView,categoryView;
    ImageView profile_img;
    Uri img_uri;
    private  static final int PICK_IMG = 1;
    //firebase object
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //firebase firestore object
    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();;
    //firebase User
    private FirebaseUser currentUser;
    //Storage for storing images
    private StorageReference mStorageRef;
    String userID;

    int drafts =0;
    int posts =0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_profile,container,false);
        setWigdets(v);

        no_of_drafts.setText("0");
        no_of_posts.setText("0");
        no_of_followers.setText("0");

        //select category button action
        categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),CategoryActivity.class);
                startActivity(i);

            }
        });

        //logout button action
        signOutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getInstance().signOut();
                Intent i=new Intent(getActivity(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        // ### user profile setup

        //intialising the StorageRefrence object
        mStorageRef = FirebaseStorage.getInstance().getReference();

        userID = mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();


        //Fetch Data from collection users using userID
        if(currentUser.getDisplayName()==null) {
            DocumentReference dr = fstore.collection("users").document(userID);
            dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    userName.setText(documentSnapshot.getString("userName"));
                    userEmail.setText(documentSnapshot.getString("email"));
                }
            });
        }

        if(currentUser.getPhotoUrl() != null){
            //Glide.with(container).load(currentUser.getPhotoUrl().toString()).into(profile_img);

            //caching images
            Picasso.get().load(currentUser.getPhotoUrl()).fetch(new Callback() {
                @Override
                public void onSuccess() {
                    Picasso.get().load(currentUser.getPhotoUrl()).into(profile_img);
                }
                @Override
                public void onError(Exception e) {
                }
            });
        }
        if(currentUser.getDisplayName()!=null){
            userName.setText(currentUser.getDisplayName());
        }
        else{
            //Fetch Data from collection users using userID
            DocumentReference dr = fstore.collection("users").document(userID);
            dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    userName.setText(documentSnapshot.getString("userName"));
                    userEmail.setText(documentSnapshot.getString("email"));
                }
            });
        }
        if(currentUser.getEmail()!=null){
            userEmail.setText(currentUser.getEmail());
        }

        // ### End of user profile setup

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
            public void onSuccess(final Uri uri) {
                if(Picasso.get().load(uri).toString()!=null) {
                    //Picasso.get().load(uri).into(profile_img);
                    //caching images
                    Picasso.get().load(uri).fetch(new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.get().load(uri).into(profile_img);
                        }
                        @Override
                        public void onError(Exception e) {
                        }
                    });
                }
            }
        });

        //pick image from gallery
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,1000);
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

    // setting all the user Statistic numbers (Follower, posts, drafts)
    public void setStatistics(){

        fstore.collection("blogs")
                .whereEqualTo("publish",true)
                .whereEqualTo("user_id",mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                posts++;
                                no_of_posts.setText(String.valueOf(posts));
                                //Toast.makeText(getActivity(), ""+document.getId(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //setting total on of drafts
        fstore.collection("blogs")
                .whereEqualTo("publish",false)
                .whereEqualTo("user_id",mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                drafts++;
                                no_of_drafts.setText(String.valueOf(drafts));
                                //Toast.makeText(getActivity(), ""+document.getId(), Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void setWigdets(View v){
        userName = v.findViewById(R.id.profileUsername);
        userEmail =v.findViewById(R.id.profileUserEmail);

        no_of_followers = v.findViewById(R.id.followers_no);
        no_of_posts =v.findViewById(R.id.posts_no);
        no_of_drafts =v.findViewById(R.id.drafts_no);


        profile_img = v.findViewById(R.id.pic_profile);
        setStatistics();

        signOutView = v.findViewById(R.id.signOutView);
        categoryView = v.findViewById(R.id.categoryView);


    }

}
