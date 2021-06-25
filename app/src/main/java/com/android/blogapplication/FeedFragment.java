package com.android.blogapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import Adapter.UserRecyclerAdapter;

public class FeedFragment extends Fragment {

    private RecyclerView users_recyclerView;
    private List<UserModel> user_list;
    private FirebaseFirestore fstore;
    private UserRecyclerAdapter UserRecyclerAdapter;
    private CollectionReference collectionRef;
    public static final String TAG = "TAG";

    //firebase object
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_my_blogs, container, false);

        try {

            user_list = new ArrayList<>();

            users_recyclerView = v.findViewById(R.id.myRecycler);
            UserRecyclerAdapter = new UserRecyclerAdapter(user_list, getContext());

            users_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            users_recyclerView.setAdapter(UserRecyclerAdapter);

        }
        catch (Exception e){
            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();
        //initialize FirebaseFirestore object
        fstore = FirebaseFirestore.getInstance();
        UserRecyclerAdapter.notifyDataSetChanged();

        fstore.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                        for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                            if(doc.getType() == DocumentChange.Type.ADDED){
                                UserModel uModel = doc.getDocument().toObject(UserModel.class);
                                user_list.add(uModel);
                                UserRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        return v;
    }
}