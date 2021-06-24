package com.android.blogapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import javax.annotation.Nullable;

import Adapter.BlogRecyclerAdapter;

public class DraftsFragment extends Fragment {
    private RecyclerView blog_post_view;
    private List<BlogModel> blog_list;
    private FirebaseFirestore fstore;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private CollectionReference collectionRef;
    public static final String TAG = "TAG";
    //firebase object
    private FirebaseAuth mAuth;
    public DraftsFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_drafts, container, false);

        blog_list = new ArrayList<>();
        blog_post_view = v.findViewById(R.id.blog_draft_view);
        blogRecyclerAdapter = new BlogRecyclerAdapter(getContext(),blog_list);

        blog_post_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_post_view.setAdapter(blogRecyclerAdapter);
        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();
        //initialize FirebaseFirestore object
        fstore = FirebaseFirestore.getInstance();
        blogRecyclerAdapter.notifyDataSetChanged();

        fstore.collection("blogs").
                whereEqualTo("publish",false)
                .whereEqualTo("user_id",mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(!queryDocumentSnapshots.isEmpty()) {

                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    BlogModel blogModel = doc.getDocument().toObject(BlogModel.class);
                                    blog_list.add(blogModel);
                                    blogRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
        return v;

    }
}
