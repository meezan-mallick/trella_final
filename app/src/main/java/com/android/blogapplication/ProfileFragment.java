package com.android.blogapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    TextView welcome;
    Button sign_out;
    ImageView profile_img;
    //firebase object
    private FirebaseAuth mAuth;
    //firebase User
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_profile,container,false);

        welcome = v.findViewById(R.id.welcome_txt);
        sign_out = v.findViewById(R.id.sign_out);
        profile_img = v.findViewById(R.id.profile_img);
        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser.getPhotoUrl() != null){
            Glide.with(container).load(currentUser.getPhotoUrl().toString()).into(profile_img);

        }
        if(currentUser.getDisplayName()!=null){
            welcome.setText(currentUser.getEmail());
        }




        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getInstance().signOut();
//                Intent i=new Intent(getContext(),LoginActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
            }
        });


        return v;
    }

}
