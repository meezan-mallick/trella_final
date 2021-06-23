package com.android.blogapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapter.BlogRecyclerAdapter;

public class FeedFragment extends Fragment {

    private RecyclerView blog_post_view;
    private List<BlogModel> blog_list;
    private FirebaseFirestore fstore;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private CollectionReference collectionRef;
    public static final String TAG = "TAG";

    //firebase object
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_my_blogs, container, false);

        blog_list = new ArrayList<>();
        blog_post_view = v.findViewById(R.id.blog_post_view);
        blogRecyclerAdapter = new BlogRecyclerAdapter(blog_list);

        blog_post_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_post_view.setAdapter(blogRecyclerAdapter);
        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();
        //initialize FirebaseFirestore object
        fstore = FirebaseFirestore.getInstance();
        blogRecyclerAdapter.notifyDataSetChanged();

        fstore.collection("blogs").
                whereEqualTo("publish",true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                        for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                            if(doc.getType() == DocumentChange.Type.ADDED){
                                BlogModel blogModel = doc.getDocument().toObject(BlogModel.class);
                                blog_list.add(blogModel);
                                blogRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });



        return v;

    }
}
