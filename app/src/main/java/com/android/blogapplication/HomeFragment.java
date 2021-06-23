package com.android.blogapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapter.BlogRecyclerAdapter;

public class HomeFragment extends Fragment {
    private RecyclerView blog_post_view;
    private List<BlogModel> blog_list;
    private FirebaseFirestore fstore;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private CollectionReference collectionRef;
    public static final String TAG = "TAG";
    //firebase object
    private FirebaseAuth mAuth;
    private DocumentSnapshot lastVisible;
    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();


        blog_list = new ArrayList<>();
        blog_post_view = v.findViewById(R.id.home_fragment);
        blogRecyclerAdapter = new BlogRecyclerAdapter(getContext(),blog_list);

        blog_post_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_post_view.setAdapter(blogRecyclerAdapter);
        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();
        //initialize FirebaseFirestore object
        fstore = FirebaseFirestore.getInstance();
        blogRecyclerAdapter.notifyDataSetChanged();

        blog_post_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Boolean isReachedBottom = recyclerView.canScrollVertically(1);
                if(isReachedBottom){
                    String lastdata = lastVisible.getString("blog_title");
//                    Toast.makeText(getContext(), "Reached : "+lastdata, Toast.LENGTH_SHORT).show();
                    try {
//                        loadMorePost();
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Query firstQry = fstore.collection("blogs")
//                .whereEqualTo("publish",true)
                .orderBy("time", Query.Direction.DESCENDING);
//                .startAfter(lastVisible)
//                .limit(5);


        firstQry.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if(!documentSnapshots.isEmpty()) {
                            lastVisible = documentSnapshots.getDocuments()
                                    .get(documentSnapshots.size() - 1);
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    BlogModel blogModel = doc.getDocument().toObject(BlogModel.class);
                                    blog_list.add(blogModel);
                                    blogRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        String count = String.valueOf(blog_list.size());
                        Toast.makeText(getContext(), count, Toast.LENGTH_SHORT).show();
                    }
                });

        return v;
    }

    public void loadMorePost(){
        Query secondQry = fstore.collection("blogs")
//                .whereEqualTo("publish",true)
                .orderBy("time", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(3);

                secondQry.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        lastVisible = documentSnapshots.getDocuments()
                                .get(documentSnapshots.size() -1);

                        for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                            if(doc.getType() == DocumentChange.Type.ADDED){
                                BlogModel blogModel = doc.getDocument().toObject(BlogModel.class);
                                blog_list.add(blogModel);
                                blogRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
